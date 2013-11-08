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

import ca.cmput301f13t03.adventure_datetime.R;
import ca.cmput301f13t03.adventure_datetime.model.Story;
import ca.cmput301f13t03.adventure_datetime.model.StoryFragment;
import ca.cmput301f13t03.adventure_datetime.model.Interfaces.ICurrentFragmentListener;
import ca.cmput301f13t03.adventure_datetime.model.Interfaces.ICurrentStoryListener;
import ca.cmput301f13t03.adventure_datetime.serviceLocator.Locator;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * 
 * View containing three fragments/tabs:
 *  - AuthorEdit_Preview
 *  - AuthorEdit_Overview
 *  - AuthorEdit_Edit
 * 
 * TODO: Load data from the model
 * TODO: Send changes to the controller
 * 
 * @author James Finlay
 *
 */
public class AuthorEdit extends FragmentActivity implements ICurrentFragmentListener, ICurrentStoryListener {
	private static final String TAG = "AuthorEdit";

	private ViewPager _viewPager;
	private ViewPagerAdapter _adapter;
	private Story _story;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager);

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
				.setText("Edit")
				.setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab()
				.setText("Overview")
				.setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab()
				.setText("Preview")
				.setTabListener(tabListener));

		/* Change tabs when View Pager swiped */
		_viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				getActionBar().setSelectedNavigationItem(position);
			}
		});
		
		// Select 'Overview' at start
		getActionBar().setSelectedNavigationItem(1);

	}
	@Override
	public void onResume() {
		Locator.getPresenter().Subscribe((ICurrentFragmentListener)this);
		Locator.getPresenter().Subscribe((ICurrentStoryListener)this);
		super.onResume();
	}
	@Override
	public void onPause() {
		Locator.getPresenter().Subscribe((ICurrentFragmentListener)this);
		Locator.getPresenter().Unsubscribe((ICurrentStoryListener)this);
		super.onPause();
	}
	@Override
	public void OnCurrentFragmentChange(StoryFragment newFragment) {
		_adapter.setFragment(newFragment);
	}
	@Override
	public void OnCurrentStoryChange(Story newStory) {
		_adapter.setStory(newStory);
		_story = newStory;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.authoredit, menu);
		return true;		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_back:
			finish();
			break;
		case R.id.action_save:
			_adapter.saveFragment();
			Toast.makeText(getApplicationContext(), "Saved Fragment!", Toast.LENGTH_SHORT).show();
			break;
		case R.id.action_discard:
			/* Ensure user is not retarded and actually wants to do this */
			new AlertDialog.Builder(this)
			.setTitle("Delete Story Fragment")
			.setMessage("This will only delete the current story fragment. You cannot undo.")
			.setCancelable(true)
			.setPositiveButton("Kill the fucker!", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO::JTF Kill the node. If last node, kill story
					finish();
				}
			})
			.setNegativeButton("NO! Don't hurt GRAMGRAM!", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			})
			.create().show();
			break;
		default:
		}
		return super.onOptionsItemSelected(item);
	}
	
	public class ViewPagerAdapter extends FragmentPagerAdapter {
		
		private AuthorEdit_Edit _edit;
		private AuthorEdit_Overview _overview;
		private AuthorEdit_Preview _preview;
		
		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
			_edit = new AuthorEdit_Edit();
			_overview = new AuthorEdit_Overview();
			_preview = new AuthorEdit_Preview();
		}

		@Override
		public Fragment getItem(int i) {
			switch (i) {
			case 0:
				return _edit;
			case 1:
				return _overview;
			case 2:
				return _preview;
			default:
				Log.e(TAG, "invalid item #");
				return null;
			}
		}
		public void setStory(Story st) {
			_edit.setStory(st);
			_overview.setStory(st);
			_preview.setStory(st);
		}
		public void setFragment(StoryFragment sf) {
			_edit.setFragment(sf);
			_overview.setFragment(sf);
			_preview.setFragment(sf);
		}
		public void saveFragment() {
			_edit.saveFragment();
			_overview.saveFragment();
			_preview.saveFragment();
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0: return "Edit";
			case 1: return "Overview";
			case 2: return "Preview";
			default: return "It be a Pirate!";
			}
		}
	}
}
