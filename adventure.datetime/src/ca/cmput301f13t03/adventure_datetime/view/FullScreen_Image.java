/*
 *	Copyright (c) 2013 Andrew Fontaine, James Finlay, Jesse Tucker, Jacob Viau, and
 * 	Evan DeGraff
 *
 * 	Permission is hereby granted, free of charge, to any person obtaining a copy of
 * 	this software and associated documentation files (the "Software"), to deal in
 * 	the Software without restriction, including without limitation the rights to
 * 	use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * 	the Software, and to permit persons to whom the Software is furnished to do so,
 * 	subject to the following conditions:
 *
 * 	The above copyright notice and this permission notice shall be included in all
 * 	copies or substantial portions of the Software.
 *
 * 	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * 	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * 	FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * 	COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * 	IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * 	CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package ca.cmput301f13t03.adventure_datetime.view;

import java.util.ArrayList;
import java.util.List;

import ca.cmput301f13t03.adventure_datetime.R;
import ca.cmput301f13t03.adventure_datetime.model.StoryFragment;
import ca.cmput301f13t03.adventure_datetime.model.Interfaces.ICurrentFragmentListener;
import ca.cmput301f13t03.adventure_datetime.serviceLocator.Locator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * 
 * View accessed by clicking image in filmstrip
 * 
 * Show fullscreen version of image / play video
 * 
 * @author James Finlay
 *
 */
public class FullScreen_Image extends FragmentActivity implements ICurrentFragmentListener {
	private static final String TAG = "FragmentActivity";

	private StoryFragment _fragment;
	private ViewPager _viewPager;
	private StoryPagerAdapter _pageAdapter;

	@Override
	public void OnCurrentFragmentChange(StoryFragment newFragment) {
		_fragment = newFragment;
		setUpView();
	}

	private void setUpView() {
		if (_fragment == null) return;
		if (_pageAdapter == null) return;

		//_pageAdapter.setIllustrations(_fragment.getStoryMedia());
		ArrayList<String> list = new ArrayList<String>();
		for (int i=0; i<5; i++) list.add(""+i);
		_pageAdapter.setIllustrations(list);
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Fullscreen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.viewpager);


		_pageAdapter = new StoryPagerAdapter(getSupportFragmentManager());
		_viewPager = (ViewPager) findViewById(R.id.pager);
		_viewPager.setAdapter(_pageAdapter);

		setUpView();
	}

	@Override
	public void onResume() {
		Locator.getPresenter().Subscribe(this);
		super.onResume();
	}
	@Override
	public void onPause() {
		Locator.getPresenter().Unsubscribe(this);
		super.onPause();
	}
	private class StoryPagerAdapter extends FragmentStatePagerAdapter {

		private List<String> _illustrations;

		public StoryPagerAdapter(FragmentManager fm) {
			super(fm);
			_illustrations = new ArrayList<String>();
		}

		public void setIllustrations(List<String> illustrationIDs) {
			_illustrations = illustrationIDs;
			notifyDataSetChanged();
		}

		@Override
		public Fragment getItem(int pos) {
			IllustrationFragment frag = new IllustrationFragment();
			frag.setImage(_illustrations.get(pos));
			return frag;
		}

		@Override
		public int getCount() {
			return _illustrations.size();
		}

	}
	public static class IllustrationFragment extends Fragment {

		private View _rootView;
		private String _sID;

		public void onCreate(Bundle bundle) {
			super.onCreate(bundle);
		}
		public void setImage(String id) {
			_sID = id;
			setUpView();
		}
		@Override
		public View onCreateView(LayoutInflater inflater, 
				ViewGroup container, Bundle savedInstanceState) {

			_rootView = inflater.inflate(R.layout.fullscreen_illustration, 
					container, false);

			setUpView();

			return _rootView;
		}
		private void setUpView() {
			if (_sID == null) return;
			if (_rootView == null) return;

			ImageView image = (ImageView) _rootView.findViewById(R.id.image);
			image.setBackgroundResource(R.drawable.grumpy_cat2);

		}
	}


}
