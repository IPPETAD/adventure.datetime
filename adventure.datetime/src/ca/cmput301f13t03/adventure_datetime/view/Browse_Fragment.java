package ca.cmput301f13t03.adventure_datetime.view;

import java.util.Collection;

import ca.cmput301f13t03.adventure_datetime.R;
import ca.cmput301f13t03.adventure_datetime.model.Story;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Browse_Fragment extends Fragment {

	protected ListView _listView;
	protected RowArrayAdapter _adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.list_view, container, false);
		_listView = (ListView) rootView.findViewById(R.id.list_view);
		
		return rootView;
	}
	
	@Override
	public void onResume() {

		_listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// If bug here, use this first: ListView listView = (ListView) parent;
				
				// TODO::JF Unsubscribe from model
				
				// Get selected item
				Story item = (Story) _listView.getItemAtPosition(position);
				
				// Launch StoryDescription & send Story ID
				Intent intent = new Intent(getActivity(), StoryDescription.class);
				intent.putExtra(StoryDescription.ARG_STORYID, item.getId());
				startActivity(intent);
			}
		});
		
		super.onResume();
	}
	
	public void setStories(Collection<Story> stories) {

		Story[] array = (Story[]) stories.toArray();
		_adapter = new RowArrayAdapter(getActivity(), R.layout.listviewitem, array);
		_listView.setAdapter(_adapter);
		
	}
	
	protected class RowArrayAdapter extends ArrayAdapter<Story> {
		
		private Context context;
		private int layoutResourceID;
		private Story[] stories;
		
		public RowArrayAdapter(Context context, int layoutResourceID, Story[] stories) {
			super(context, layoutResourceID, stories);

			this.context = context;
			this.layoutResourceID = layoutResourceID;
			this.stories = stories;
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View rowView = inflater.inflate(R.layout.listviewitem, parent, false);

			ImageView thumbnail = (ImageView) rowView.findViewById(R.id.thumbnail);
			TextView title = (TextView) rowView.findViewById(R.id.title);
			TextView author = (TextView) rowView.findViewById(R.id.author);
			TextView time = (TextView) rowView.findViewById(R.id.datetime);
			ImageView status = (ImageView) rowView.findViewById(R.id.status_icon);

			Story story = stories[position];
			
			title.setText(story.getTitle());
			// TODO::JF set the thumbnail
			// TODO::JF author.setText(story.getAuthor());
			author.setText("Author: Jane Austen");
			// TODO::JF time.setText(story.getTimeStamp());
			time.setText("Last Played: 01/01/1812");
			
			// TODO::JF Bookmark icon once available
			// TODO::JF Completed icon once Bookmark shit available

			return rowView;
		}
		
	}
}
