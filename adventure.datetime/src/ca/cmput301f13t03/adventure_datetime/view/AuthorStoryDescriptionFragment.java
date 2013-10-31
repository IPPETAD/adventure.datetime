package ca.cmput301f13t03.adventure_datetime.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ca.cmput301f13t03.adventure_datetime.R;

public class AuthorStoryDescriptionFragment extends Fragment {
	public static final String ARG_OBJECT = "object";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.listviewitem, container, false);
		Bundle args = getArguments();

		//TextView content = (TextView) rootView.findViewById(R.id.content);
		//content.setText(Integer.toString(args.getInt(ARG_OBJECT)));

		return rootView;
	}
	
	
}
