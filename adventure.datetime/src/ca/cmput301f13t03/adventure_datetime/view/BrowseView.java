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

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import ca.cmput301f13t03.adventure_datetime.R;
import ca.cmput301f13t03.adventure_datetime.model.Interfaces.ILocalStoriesListener;
import ca.cmput301f13t03.adventure_datetime.model.Interfaces.IOnlineStoriesListener;
import ca.cmput301f13t03.adventure_datetime.model.Story;
import ca.cmput301f13t03.adventure_datetime.serviceLocator.Locator;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

/**
 *
 * Contains three fragments/tabs where user can see list of cached stories, 
 * authored stories, and online stories.
 * 
 * @author James Finlay
 *
 */
public class BrowseView extends FragmentActivity implements ILocalStoriesListener,
											IOnlineStoriesListener {
	private static final String TAG = "BrowseView";

	private ViewPager _viewPager;
	private ViewPagerAdapter _adapter;
	private LinearLayout _searchBar;
	
	@Override
	public void OnLocalStoriesChange(Map<UUID, Story> newStories) {
		_adapter.setLocalStories(Locator.getAuthorController().checkIfNotAuthored(newStories.values()));	
		_adapter.setAuthorStories(Locator.getAuthorController().checkIfAuthored(newStories.values()));
	}
	@Override
	public void OnOnlineStoriesChange(Map<UUID, Story> newStories) {
		Log.v(TAG, "online stories");
		_adapter.setOnlineStories(newStories.values());
	}
	
	// TODO::JF Listen for Server stories
	
	@Override
	public void onResume() {
		Locator.getPresenter().Subscribe((ILocalStoriesListener)this);
		Locator.getPresenter().Subscribe((IOnlineStoriesListener)this);
		super.onResume();
	}
	@Override
	public void onPause() {
		Locator.getPresenter().Unsubscribe((ILocalStoriesListener)this);
		Locator.getPresenter().Unsubscribe((IOnlineStoriesListener)this);
		super.onPause();
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.browse_viewpager);
		
		/* Search bar */
		final Button btnSearch = (Button) findViewById(R.id.search);
		final EditText txtSearch = (EditText) findViewById(R.id.content);
		_searchBar = (LinearLayout) findViewById(R.id.header);
		_searchBar.setVisibility(View.GONE);
		txtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					Locator.getUserController().search(txtSearch.getText().toString());
					txtSearch.setText("");
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
					_searchBar.setVisibility(View.GONE);
				}
				return false;
			}
		});
		btnSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Locator.getUserController().search(txtSearch.getText().toString());
				txtSearch.setText("");
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
				_searchBar.setVisibility(View.GONE);
			}
		});
		
		
		/* Set up View Pager */
		_adapter = new ViewPagerAdapter(getSupportFragmentManager());
		_viewPager = (ViewPager) findViewById(R.id.pager);
		_viewPager.setAdapter(_adapter);

		/* Set up Tabs */
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		ActionBar.TabListener tabListener = new ActionBar.TabListener() {
			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {}

			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) {
				_viewPager.setCurrentItem(tab.getPosition());
			}
			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {}
		};

		actionBar.addTab(actionBar.newTab()
				.setText("Saved")
				.setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab()
				.setText("My Stories")
				.setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab()
				.setText("Online")
				.setTabListener(tabListener));

		/* Change tabs when View Pager swiped */
		_viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				getActionBar().setSelectedNavigationItem(position);
				if (position != 2)
					_searchBar.setVisibility(View.GONE);
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.search_menu, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_search:
			getActionBar().setSelectedNavigationItem(2);
			_searchBar.setVisibility(View.VISIBLE);
			break;
		}
		return super.onOptionsItemSelected(item);
	}


	public class ViewPagerAdapter extends FragmentPagerAdapter {
		
		private BrowseFragment cached, authored, online;
		
		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
			
			cached = new BrowseFragment();
			authored = new BrowseFragment();
			online = new BrowseFragment();
		}
		
		public void setLocalStories(Collection<Story> stories) {
			cached.setStories(stories, BrowseFragment.SOURCE_CACHE);
		}
		public void setAuthorStories(Collection<Story> stories) {
			authored.setStories(stories, BrowseFragment.SOURCE_AUTHOR);
		}
		public void setOnlineStories(Collection<Story> stories) {
			online.setStories(stories, BrowseFragment.SOURCE_ONLINE);
		}

		@Override
		public Fragment getItem(int i) {
			switch(i) {
			case 0: return cached;
			case 1: return authored;
			case 2: return online;
			default: return null;
			}
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0: return "Saved";
			case 1: return "My Stories";
			case 2: return "Online";
			default: return "It be a Pirate!";
			}
		}
	}


}
