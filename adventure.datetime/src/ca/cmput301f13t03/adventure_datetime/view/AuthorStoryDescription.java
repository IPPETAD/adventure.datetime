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

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import ca.cmput301f13t03.adventure_datetime.R;


public class AuthorStoryDescription extends FragmentActivity {
	private static final String TAG = "AuthorStoryDescription";
	public static final String ARG_ITEM_NUM = ".view.AuthorStoryDescription.item_num";

	private AuthorStoriesPagerAdapter _pageAdapter;
	private ViewPager _viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.author_descripts);

		int item = getIntent().getIntExtra(ARG_ITEM_NUM, 0);

		_pageAdapter = new AuthorStoriesPagerAdapter(getSupportFragmentManager());
		
		_viewPager = (ViewPager) findViewById(R.id.pager);
		_viewPager.setAdapter(_pageAdapter);
		_viewPager.setCurrentItem(item);
	}

	private class AuthorStoriesPagerAdapter extends FragmentStatePagerAdapter {

		public AuthorStoriesPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {

			Fragment fragment = new AuthorStoryDescriptionFragment();

			// TODO : Send story info through to fragment

			/* Sending through status of the story */
			Bundle args = new Bundle();
			if (i == 3)
				args.putInt(AuthorStoryDescriptionFragment.ARG_STATUS, 
						AuthorStoryDescriptionFragment.STATUS_SYNCED);
			else if (i == 5)
				args.putInt(AuthorStoryDescriptionFragment.ARG_STATUS, 
						AuthorStoryDescriptionFragment.STATUS_UNSYNC);
			else
				args.putInt(AuthorStoryDescriptionFragment.ARG_STATUS, 
						AuthorStoryDescriptionFragment.STATUS_UNPUBLISHED);


			fragment.setArguments(args);
			return fragment;

		}

		@Override
		public int getCount() {
			return 10;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return "Object " + (position+1);
		}
	}

	public static class AuthorStoryDescriptionFragment extends Fragment {
		public static final String ARG_STATUS = "status";
		public static final int STATUS_UNPUBLISHED = 0;
		public static final int STATUS_UNSYNC = 1;
		public static final int STATUS_SYNCED = 2;

		public void onCreate(Bundle bundle) {
			super.onCreate(bundle);
			setHasOptionsMenu(true);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

			View rootView = inflater.inflate(R.layout.author_descript, container, false);
			Bundle args = getArguments();

			RelativeLayout header = (RelativeLayout) rootView.findViewById(R.id.header);

			switch (args.getInt(ARG_STATUS)) {
			case STATUS_UNPUBLISHED:
				/* Light Blue */
				header.setBackgroundColor(Color.parseColor("#d4eef8"));
				break;
			case STATUS_UNSYNC:
				/* Light Orange */
				header.setBackgroundColor(Color.parseColor("#f8e7d4"));
				break;
			case STATUS_SYNCED:
				/* Light Green */
				header.setBackgroundColor(Color.parseColor("#d4f8e1"));
				break;
			default:
				Log.e(TAG, "Status unknown.");
			}


			return rootView;
		}

		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			inflater.inflate(R.menu.authordesc, menu);

			/* Disable 'Upload' if sync'd */
			switch (getArguments().getInt(ARG_STATUS)) {
			case STATUS_UNPUBLISHED:
				break;
			case STATUS_UNSYNC:
				menu.findItem(R.id.action_upload).setIcon(R.drawable.ic_action_refresh);
				break;
			case STATUS_SYNCED:
				menu.findItem(R.id.action_upload)
					.setEnabled(false)
					.setVisible(false);
				break;
			default:
				Log.e(TAG, "Status unknown.");
			}
			menu.findItem(R.id.action_upload);
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
}