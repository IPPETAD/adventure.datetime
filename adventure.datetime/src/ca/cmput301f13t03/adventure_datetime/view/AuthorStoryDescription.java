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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import ca.cmput301f13t03.adventure_datetime.R;
import ca.cmput301f13t03.adventure_datetime.model.Story;
import ca.cmput301f13t03.adventure_datetime.model.Interfaces.ICurrentStoryListener;
import ca.cmput301f13t03.adventure_datetime.model.Interfaces.IStoryListListener;
import ca.cmput301f13t03.adventure_datetime.serviceLocator.Locator;

/**
 * View containing description of story create by author.
 * 
 * User can swipe between the fragments to view other stories
 * in list.
 * 
 * TODO: Link with model, controller
 * 
 * @author James Finlay
 *
 */
public class AuthorStoryDescription extends FragmentActivity implements IStoryListListener, ICurrentStoryListener {
	private static final String TAG = "AuthorStoryDescription";

	private AuthorStoriesPagerAdapter _pageAdapter;
	private ViewPager _viewPager;
	private List<Story> _stories;
	private Story _story;
	
	@Override
	public void OnCurrentStoryListChange(Collection<Story> newStories) {
		_stories = (List<Story>) newStories;
		setUpView();
	}
	@Override
	public void OnCurrentStoryChange(Story story) {
		_story = story;
		setUpView();
	}
	private void setUpView() {
		if (_stories == null) return;
		if (_story == null) return;
		
		_pageAdapter.setCount(_stories.size());
		_viewPager.setCurrentItem(_stories.indexOf(_story));		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager);

		_pageAdapter = new AuthorStoriesPagerAdapter(getSupportFragmentManager());

		_viewPager = (ViewPager) findViewById(R.id.pager);
		_viewPager.setAdapter(_pageAdapter);
		
		_viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				Locator.getDirector().selectStory(_stories.get(position).getId());
			}
		});
	}
	@Override
	public void onResume() {
		Locator.getPresenter().Subscribe((IStoryListListener)this);
		Locator.getPresenter().Subscribe((ICurrentStoryListener)this);
		super.onResume();
	}
	@Override
	public void onPause() {
		Locator.getPresenter().Unsubscribe((IStoryListListener)this);
		Locator.getPresenter().Unsubscribe((ICurrentStoryListener)this);
		super.onPause();
	}

	private class AuthorStoriesPagerAdapter extends FragmentStatePagerAdapter {

		private int count = 0;
		
		public AuthorStoriesPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		
		public void setCount(int c) {
			count = c;
			notifyDataSetChanged();
		}

		@Override
		public Fragment getItem(int i) {
			return new AuthorStoryDescriptionFragment();
		}

		@Override
		public int getCount() {
			return count;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return "Object " + (position+1);
		}
	}

	public static class AuthorStoryDescriptionFragment extends Fragment implements ICurrentStoryListener {
		public static final int STATUS_UNPUBLISHED = 0;
		public static final int STATUS_UNSYNC = 1;
		public static final int STATUS_SYNCED = 2;

		private Story _story;
		private View _rootView;


		public void onCreate(Bundle bundle) {
			super.onCreate(bundle);
			setHasOptionsMenu(true);
		}

		public void OnCurrentStoryChange(Story newStory) {
			_story = newStory;
			setUpView();
		}

		@Override
		public void onResume() {
			Locator.getPresenter().Subscribe(this);
			super.onResume();
		}
		public void onPause() {
			Locator.getPresenter().Unsubscribe(this);
			super.onPause();
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

			_rootView = inflater.inflate(R.layout.author_descript, container, false);

			/** Layout items **/
			ImageButton btnEditTitle = (ImageButton) _rootView.findViewById(R.id.edit_title);
			ImageButton btnEditSynopsis = (ImageButton) _rootView.findViewById(R.id.edit_content);

			btnEditTitle.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					new AlertDialog.Builder(getActivity())
					.setView(getActivity().getLayoutInflater().inflate(R.layout.dialog_edit, null))
					.setPositiveButton("OK!", null)
					.setNegativeButton("Cancel", null)
					.create().show();
				}
			});
			btnEditSynopsis.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					new AlertDialog.Builder(getActivity())
					.setView(getActivity().getLayoutInflater().inflate(R.layout.dialog_edit, null))
					.setPositiveButton("OK!", null)
					.setNegativeButton("Cancel", null)
					.create().show();
				}
			});

			setUpView();

			return _rootView;
		}

		public void setUpView() {
			if (_story == null) return;
			if (_rootView == null) return;

			/** Action bar **/
			getActivity().getActionBar().setTitle("Story Name");

			/** Layout items **/
			TextView title = (TextView) _rootView.findViewById(R.id.title);
			TextView author = (TextView) _rootView.findViewById(R.id.author);
			TextView content = (TextView) _rootView.findViewById(R.id.content);
			TextView datetime = (TextView) _rootView.findViewById(R.id.datetime);
			TextView fragments = (TextView) _rootView.findViewById(R.id.fragments);
			RelativeLayout header = (RelativeLayout) _rootView.findViewById(R.id.header);

			/* Text */
			title.setText(_story.getTitle());
			author.setText("Creator: " + _story.getAuthor());
			datetime.setText("Last Modified: " + _story.getFormattedTimestamp());
			fragments.setText("Fragments: " + "idk..");
			content.setText(_story.getSynopsis());


			/*	switch (_story.) {
			case STATUS_UNPUBLISHED:
				// Light blue
				header.setBackgroundColor(Color.parseColor("#d4eef8"));
				break;
			case STATUS_UNSYNC:
				// Light orange
				header.setBackgroundColor(Color.parseColor("#f8e7d4"));
				break;
			case STATUS_SYNCED:
				// Light green
				header.setBackgroundColor(Color.parseColor("#d4f8e1"));
				break;
			default:
				Log.e(TAG, "Status unknown.");
			}
			 */

		}

		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			inflater.inflate(R.menu.authordesc, menu);

			/*	switch (getArguments().getInt(ARG_STATUS)) {
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
			}*/
			menu.findItem(R.id.action_upload);

		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {

			switch (item.getItemId()) {
			case R.id.action_editfragments:
				Intent intent = new Intent(getActivity(), AuthorEdit.class);
				startActivity(intent);				
				break;
			case R.id.action_upload:
				break;
			case R.id.action_discard:
				/* Ensure user is not retarded and actually wants to do this */
				new AlertDialog.Builder(getActivity())
				.setTitle("Delete Story")
				.setMessage("This will delete the story. You cannot undo.")
				.setCancelable(true)
				.setPositiveButton("Kill the fucker!", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						/* TODO::JTF Delete the story */
						getActivity().finish();
					}
				})
				.setNegativeButton("NO! Don't hurt GRAMGRAM!", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				})
				.create().show();
				break;
			default:
				Log.e(TAG, "onOptionsItemSelected -> Unknown MenuItem");
				break;
			}

			return super.onOptionsItemSelected(item);
		}
	}
}