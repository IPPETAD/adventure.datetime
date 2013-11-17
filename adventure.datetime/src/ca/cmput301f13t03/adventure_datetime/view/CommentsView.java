package ca.cmput301f13t03.adventure_datetime.view;

import ca.cmput301f13t03.adventure_datetime.R;
import ca.cmput301f13t03.adventure_datetime.model.Comment;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CommentsView extends Activity {
	
	private ListView _listView;
	private RowArrayAdapter _adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_view);
		
		_listView = (ListView) findViewById(R.id.list_view);
		
		setUpView();
	}
	
	private void setUpView() {
		if (_listView == null) return;
		
		// TODO: Use model data
		
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
			ImageView thumbnail = (ImageView) rowView.findViewById(R.id.thumbnail);
			TextView content = (TextView) rowView.findViewById(R.id.content);
			
			// TODO::JF use actual data
			
			//author.setText(item.getAuthor());
			//content.setText(item.getContent());
			author.setText("Jane Austen");
			content.setText("Yolo swag, hipsters be sick yo. #hatersgonnaskate");
			
			
			return rowView;
		}
		
	}

}
