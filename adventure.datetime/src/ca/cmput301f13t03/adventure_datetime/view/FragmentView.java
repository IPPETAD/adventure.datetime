package ca.cmput301f13t03.adventure_datetime.view;

import ca.cmput301f13t03.adventure_datetime.R;
import android.app.Activity;
import android.os.Bundle;

public class FragmentView extends Activity {
	private static final String TAG = "FragmentView";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_view);
	}

}
