package ca.cmput301f13t03.adventure_datetime.view;

import ca.cmput301f13t03.adventure_datetime.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AuthorEdit_Preview extends Fragment {
	private static final String TAG = "AuthorEdit";
	
	@Override
	public View onCreateView(LayoutInflater inflater, 
			ViewGroup container, Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.listviewitem, container, false);
		return rootView;
	}

}
