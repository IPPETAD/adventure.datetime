package ca.cmput301f13t03.adventure_datetime.view;

import ca.cmput301f13t03.adventure_datetime.R;
import ca.cmput301f13t03.adventure_datetime.model.StoryFragment;
import ca.cmput301f13t03.adventure_datetime.model.Interfaces.ICurrentFragmentListener;
import ca.cmput301f13t03.adventure_datetime.serviceLocator.Locator;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.view.WindowManager;

public class FullScreen_Author extends Activity implements ICurrentFragmentListener {

	private StoryFragment _fragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		// Fullscreen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.viewpager);
		
		setUpView();
	}
	@Override
	public void OnCurrentFragmentChange(StoryFragment newFragment) {
		setUpView();
	}
	@Override
	public void onResume() {
		Locator.getPresenter().Subscribe(this);
	}
	@Override
	public void onPause() {
		Locator.getPresenter().Unsubscribe(this);
	}
	
	private void setUpView() {
		if (_fragment == null) return;
	}
	
	
	

}
