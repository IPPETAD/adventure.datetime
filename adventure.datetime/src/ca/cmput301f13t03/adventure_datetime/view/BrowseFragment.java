package ca.cmput301f13t03.adventure_datetime.view;

import ca.cmput301f13t03.adventure_datetime.R;
import ca.cmput301f13t03.adventure_datetime.model.Story;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class BrowseFragment extends Fragment {

	protected ListView _listView;
	protected RowArrayAdapter _adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.list_view, container, false);
		
		return rootView;
	}
	
	@Override
	public void onResume() {
		
		super.onResume();
	}
	
	protected class RowArrayAdapter extends ArrayAdapter<Story> {
		
		private Context context;
		private int layoutResourceID;
		private Story[] values;
		
		public RowArrayAdapter(Context context, int layoutResourceID, Story[] values) {
			super(context, layoutResourceID, values);

			this.context = context;
			this.layoutResourceID = layoutResourceID;
			this.values = values;
		}
		
	}
}
