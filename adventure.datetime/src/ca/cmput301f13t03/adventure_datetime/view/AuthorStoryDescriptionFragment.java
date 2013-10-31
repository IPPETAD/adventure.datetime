package ca.cmput301f13t03.adventure_datetime.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import ca.cmput301f13t03.adventure_datetime.R;

public class AuthorStoryDescriptionFragment extends Fragment {
	private static final String TAG = "AuthorStoryDescriptionFragment";
	public static final String ARG_OBJECT = "object";

	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.listviewitem, container, false);
		Bundle args = getArguments();

		//TextView content = (TextView) rootView.findViewById(R.id.content);
		//content.setText(Integer.toString(args.getInt(ARG_OBJECT)));

		return rootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.authordesc, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_edit:
			break;
		case R.id.action_upload:
			break;
		case R.id.action_discard:
			break;
		default:
			Log.e(TAG, "onOptionsItemSelected -> Unknown MenuItem");
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
}
