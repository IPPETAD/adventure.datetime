package ca.cmput301f13t03.adventure_datetime.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import ca.cmput301f13t03.adventure_datetime.R;
import ca.cmput301f13t03.adventure_datetime.model.Story;
import ca.cmput301f13t03.adventure_datetime.serviceLocator.Locator;

import java.util.Collection;


/**
 * 
 * Fragment owned by BrowseView.
 * 
 * Three are used to show the cached, authored, and online
 * stories.
 * 
 * @author James Finlay
 *
 */
public class BrowseFragment extends Fragment {
	private static final String TAG = "BrowseFragment";
	public static final String SERVER = "icanhaz_server";
	public static final int SOURCE_CACHE = 0;
	public static final int SOURCE_AUTHOR = 1;
	public static final int SOURCE_ONLINE = 2;

	private Collection<Story> _stories;
	private ListView _listView;
	private RowArrayAdapter _adapter;
	private ProgressBar _bar;
	private int source;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.browse, container, false);
		_listView = (ListView) rootView.findViewById(R.id.list_view);
		_bar = (ProgressBar) rootView.findViewById(R.id.progressBar);
		
		setUpView();
		
		return rootView;
	}
	public void setStories(Collection<Story> stories, int source) {
		_stories = stories;
		this.source = source;
		setUpView();
	}
	
	private void setUpView() {
		if (_stories == null) return;
		if (_listView == null) return;
		
		/* Run on UI Thread for server stuff */
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Story[] array = _stories.toArray(new Story[_stories.size()]);
				_adapter = new RowArrayAdapter(getActivity(), R.layout.listviewitem, array);
				_listView.setAdapter(_adapter);
				_bar.setVisibility(View.GONE);
			}
		});
		
		
	}
	
	@Override
	public void onResume() {

		_listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// If bug here, use this first: ListView listView = (ListView) parent;
				
				// Get selected item
				Story item = (Story) _listView.getItemAtPosition(position);
				
				Locator.getAuthorController().selectStory(item.getId());
				
				Intent intent = new Intent(getActivity(), StoryDescription.class);
				intent.putExtra(StoryDescription.SERVER, source);
				startActivity(intent);
			}
		});
		
		super.onResume();
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
			author.setText("Author: " + story.getAuthor());
			time.setText("Last Modified: " + story.getFormattedTimestamp());
			
			//if (story.getThumbnail() != null)
				thumbnail.setImageBitmap(story.decodeThumbnail());
			
			// TODO::JF Bookmark icon once available
			// TODO::JF Completed icon once Bookmark shit available
			return rowView;
		}
		
	}
}
