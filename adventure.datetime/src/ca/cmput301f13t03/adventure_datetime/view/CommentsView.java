package ca.cmput301f13t03.adventure_datetime.view;

import ca.cmput301f13t03.adventure_datetime.R;
import ca.cmput301f13t03.adventure_datetime.model.Comment;
import ca.cmput301f13t03.adventure_datetime.model.Story;
import ca.cmput301f13t03.adventure_datetime.model.StoryFragment;
import ca.cmput301f13t03.adventure_datetime.model.Interfaces.ICurrentFragmentListener;
import ca.cmput301f13t03.adventure_datetime.model.Interfaces.ICurrentStoryListener;
import ca.cmput301f13t03.adventure_datetime.serviceLocator.Locator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CommentsView extends Activity implements ICurrentStoryListener,
												ICurrentFragmentListener {
	public static final String COMMENT_TYPE = "forStory";
	
	private ListView _listView;
	private Story _story;
	private StoryFragment _fragment;
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
		if (forStoryEh)
			Locator.getPresenter().Unsubscribe((ICurrentStoryListener)this);
		else
			Locator.getPresenter().Unsubscribe((ICurrentFragmentListener)this);
		super.onPause();
	}
	@Override
	public void OnCurrentStoryChange(Story story) {
		_story = story;
		setUpView();
	}
	@Override
	public void OnCurrentFragmentChange(StoryFragment fragment) {
		_fragment = fragment;
		setUpView();
	}
	private void setUpView() {
		if (_listView == null) return;
		if (_story == null && forStoryEh) return;
		if (_fragment == null && !forStoryEh) return;
		
		// TODO: Use model data
		// TODO: Send diff comments whether from story or fragment
		Comment comments[] = new Comment[5];
		_adapter = new RowArrayAdapter(this, R.layout.comment_single, comments);
		
		_listView.setAdapter(_adapter);
		
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
			ImageView avatar = (ImageView) rowView.findViewById(R.id.thumbnail);
			Button image = (Button) rowView.findViewById(R.id.image_button);
			TextView content = (TextView) rowView.findViewById(R.id.content);
			
			// TODO::JF use actual data
			
			//author.setText(item.getAuthor());
			//content.setText(item.getContent());
			if (forStoryEh)
				author.setText("Famous Story Critic");
			else
				author.setText("Famous Fragment Critic");
			content.setText("Yolo swag, hipsters be sick yo. #hatersgonnaskate\n"+
					"This is more text that we can just throw in to ensure that "+
					"everything lines up properly ALL THE WAY FUCKING DOWN!\nHol"+
					"y shit batman, what is that!?");
			
			
			return rowView;
		}
		
	}

}
