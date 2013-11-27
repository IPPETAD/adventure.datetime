package ca.cmput301f13t03.adventure_datetime.view;

import java.util.List;

import ca.cmput301f13t03.adventure_datetime.R;
import ca.cmput301f13t03.adventure_datetime.model.AccountService;
import ca.cmput301f13t03.adventure_datetime.model.Comment;
import ca.cmput301f13t03.adventure_datetime.model.Story;
import ca.cmput301f13t03.adventure_datetime.model.StoryFragment;
import ca.cmput301f13t03.adventure_datetime.model.Interfaces.ICommentsListener;
import ca.cmput301f13t03.adventure_datetime.model.Interfaces.ICurrentFragmentListener;
import ca.cmput301f13t03.adventure_datetime.model.Interfaces.ICurrentStoryListener;
import ca.cmput301f13t03.adventure_datetime.serviceLocator.Locator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CommentsView extends Activity implements ICurrentStoryListener,
									ICurrentFragmentListener, ICommentsListener {
	private static final String TAG = "CommentsView";
	public static final String COMMENT_TYPE = "forStory";
	
	private ListView _listView;
	private Story _story;
	private StoryFragment _fragment;
	private List<Comment> _comments;
	private RowArrayAdapter _adapter;
	private boolean forStoryEh;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_view);
		
		forStoryEh = getIntent().getBooleanExtra(COMMENT_TYPE, true);
		
		_listView = (ListView) findViewById(R.id.list_view);
		
		setUpView();
	}
	@Override
	protected void onResume() {
		if (forStoryEh)
			Locator.getPresenter().Subscribe((ICurrentStoryListener)this);
		else
			Locator.getPresenter().Subscribe((ICurrentFragmentListener)this);
		super.onResume();
	}
	@Override
	protected void onPause() {
		if (forStoryEh) {
			Locator.getPresenter().Unsubscribe((ICurrentStoryListener)this);
			Locator.getPresenter().Unsubscribe(_story.getId());
		} else {
			Locator.getPresenter().Unsubscribe((ICurrentFragmentListener)this);
			Locator.getPresenter().Unsubscribe(_fragment.getFragmentID());
		}
		
		super.onPause();
	}
	@Override
	public void OnCurrentStoryChange(Story story) {
		_story = story;
		Log.v(TAG, "subscribe story");
		Locator.getPresenter().Subscribe((ICommentsListener)this, _story.getId());
		setUpView();
	}
	@Override
	public void OnCurrentFragmentChange(StoryFragment fragment) {
		_fragment = fragment;
		Log.v(TAG, "subscribe fragment");
		Locator.getPresenter().Subscribe((ICommentsListener)this, _fragment.getFragmentID());
		setUpView();
	}
	@Override
	public void OnCommentsChange(List<Comment> newComments) {
		_comments = newComments;
		Log.v(TAG, "Comments received. Count: " + newComments.size());
		setUpView();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.comment_menu, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_new:
			final Dialog dia = new Dialog(CommentsView.this);
			dia.setTitle("New Comment");
			dia.setContentView(R.layout.comment_add);
			dia.setCancelable(true);
			
			/** Layout items **/
			Button media = (Button) dia.findViewById(R.id.media);
			final EditText txt = (EditText) dia.findViewById(R.id.content);
			Button okay = (Button) dia.findViewById(R.id.okay);
			Button cancel = (Button) dia.findViewById(R.id.cancel);
			
			okay.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					Comment c = new Comment();
					c.setAuthor(AccountService.getUserName(getContentResolver()));
					c.setContent(txt.getText().toString());
					if (forStoryEh)	c.setTargetId(_story.getId());
					else c.setTargetId(_fragment.getFragmentID());
					//TODO::JF Save attached pic
					
					Locator.getUserController().AddComment(c);
					
					dia.dismiss();
				}				
			});
			cancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dia.dismiss();
				}
			});
			
			//TODO::AF media from gallery / camera
			dia.show();
			
			break;
		}
		return true;
	}
	private void setUpView() {
		if (_listView == null) return;
		if (_comments == null) return;
		if (_story == null && forStoryEh) return;
		if (_fragment == null && !forStoryEh) return;

		// TODO: Send diff comments whether from story or fragment

		runOnUiThread(new Runnable() {
			public void run() {
				_adapter = new RowArrayAdapter(getApplicationContext(), 
						R.layout.comment_single, _comments.toArray(new Comment[_comments.size()]));
				_listView.setAdapter(_adapter);					
			}
		});
		
	}

	private class RowArrayAdapter extends ArrayAdapter<Comment> {
		
		private Context context;
		private Comment[] values;
		
		public RowArrayAdapter(Context context, int layoutResourceID, Comment values[]) {
			super(context, layoutResourceID, values);
			
			this.context = context;
			this.values = values;
		}
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			View rowView = inflater.inflate(R.layout.comment_single, parent, false);
			
			Comment item = values[position];
			
			/** Layout Items **/
			TextView author = (TextView) rowView.findViewById(R.id.author);
			TextView date = (TextView) rowView.findViewById(R.id.datetime);
			Button btnImage = (Button) rowView.findViewById(R.id.image_button);
			RelativeLayout layImage = (RelativeLayout) rowView.findViewById(R.id.wrapper);
			TextView content = (TextView) rowView.findViewById(R.id.content);
			
			// TODO::JF use actual data
			
			author.setText(item.getAuthor());
			content.setText(item.getContent());
			date.setText(item.getFormattedTimestamp());
			// TODO:: Use model image
			layImage.setVisibility(View.GONE);
			btnImage.setOnClickListener(new ShowOnClickListener().
					setUp(layImage, btnImage));
			
			
			return rowView;
		}
		
	}
	
	private class ShowOnClickListener implements OnClickListener {

		private RelativeLayout _layout;
		private Button _button;
		
		public OnClickListener setUp(RelativeLayout layout, Button button) {
			_layout = layout;
			_button = button;
			return this;
		}
		@Override
		public void onClick(View v) {
			_layout.setVisibility(View.VISIBLE);
			_button.setText("Hide Image");
			_button.setOnClickListener(new HideOnClickListener().
					setUp(_layout, _button));
		}
	}
	
	private class HideOnClickListener implements OnClickListener {

		private RelativeLayout _layout;
		private Button _button;
		
		public OnClickListener setUp(RelativeLayout layout, Button button) {
			_layout = layout;
			_button = button;
			return this;
		}
		@Override
		public void onClick(View v) {
			_layout.setVisibility(View.GONE);
			_button.setText("Show Image");
			_button.setOnClickListener(new ShowOnClickListener().
					setUp(_layout, _button));
		}
	}
}
