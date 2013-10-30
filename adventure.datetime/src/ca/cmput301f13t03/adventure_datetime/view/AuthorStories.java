package ca.cmput301f13t03.adventure_datetime.view;

import ca.cmput301f13t03.adventure_datetime.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

public class AuthorStories extends FragmentActivity {

	private AuthorStoriesPagerAdapter _pageAdapter;
	private ViewPager _viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.author_stories);

		_pageAdapter = new AuthorStoriesPagerAdapter(getSupportFragmentManager());
		_viewPager = (ViewPager) findViewById(R.id.pager);
		_viewPager.setAdapter(_pageAdapter);


	}

	private class AuthorStoriesPagerAdapter extends FragmentStatePagerAdapter {

		public AuthorStoriesPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {

			if (i == 0) {
				Fragment fragment = new AuthorListFragment();
				return fragment;
			} else {
				Fragment fragment = new AuthorStoryDescriptionFragment();
				Bundle args = new Bundle();

				args.putInt(AuthorStoryDescriptionFragment.ARG_OBJECT, i + 1);
				fragment.setArguments(args);

				return fragment;
			}
		}

		@Override
		public int getCount() {
			return 100;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return "Object " + (position+1);
		}
	}

	public static class AuthorStoryDescriptionFragment extends Fragment {
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


}
