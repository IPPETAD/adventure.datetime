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
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import ca.cmput301f13t03.adventure_datetime.R;
import ca.cmput301f13t03.adventure_datetime.model.Bookmark;
import ca.cmput301f13t03.adventure_datetime.model.Story;
import ca.cmput301f13t03.adventure_datetime.model.Interfaces.IBookmarkListListener;
import ca.cmput301f13t03.adventure_datetime.model.Interfaces.ICurrentStoryListener;
import ca.cmput301f13t03.adventure_datetime.model.Interfaces.IStoryListListener;
import ca.cmput301f13t03.adventure_datetime.serviceLocator.Locator;

/**
 * 
 * View accessed via MainView > BrowseView > ~Select item~
 * 
 * Show synopsis & more details about selected story. User can then play the selected story.
 * Utilizes fragments to allow swiping through available stories.
 * 
 * @author James Finlay
 *
 */
public class StoryDescription extends FragmentActivity implements IStoryListListener, 
				ICurrentStoryListener, IBookmarkListListener {
	private static final String TAG = "StoryDescription";
	
	private StoryPagerAdapter _pageAdapter;
	private ViewPager _viewPager;
	private Map<String, Bookmark> _bookmarks;
	private Map<String, Story> _stories;
	private Story _story;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager);
		
		_pageAdapter = new StoryPagerAdapter(getSupportFragmentManager());
		
		_viewPager = (ViewPager) findViewById(R.id.pager);
		_viewPager.setAdapter(_pageAdapter);
		_viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				List<Story> storyList = new ArrayList<Story>(_stories.values());
				StoryDescription.this.getActionBar().setTitle(storyList.get(position).getTitle());
			}
		});
	}
	@Override
	public void OnBookmarkListChange(Map<String, Bookmark> newBookmarks) {
		_bookmarks = newBookmarks;
		setUpView();
	}
	@Override
	public void OnCurrentStoryChange(Story story) {
		_story = story;
		setUpView();
	}
	@Override
	public void OnCurrentStoryListChange(Map<String, Story> newStories) {
		_stories = newStories;	
		setUpView();
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {}

	private void setUpView() {
		if (_story == null) return;
		if (_stories == null) return;
		if (_bookmarks == null) return;
		
		Collection<Story> stories = _stories.values();
		String title = null;
		
		/* Get index of story in list */
		int index = 0;
		for (Story story : stories)
		{
			if (_story.equals(story))
			{
				title = story.getTitle();
				break;
			}
			index++;
		}
				
		_pageAdapter.setStories(new ArrayList<Story>(stories), _bookmarks);
		_viewPager.setCurrentItem(index);
		getActionBar().setTitle(title);
	}
	
	@Override
	public void onResume() {
		Locator.getPresenter().Subscribe((IStoryListListener)this);
		Locator.getPresenter().Subscribe((ICurrentStoryListener)this);
		Locator.getPresenter().Subscribe((IBookmarkListListener)this);
		super.onResume();
	}
	
	@Override
	public void onPause() {
		Locator.getPresenter().Unsubscribe((IStoryListListener)this);
		Locator.getPresenter().Unsubscribe((ICurrentStoryListener)this);
		Locator.getPresenter().Unsubscribe((IBookmarkListListener)this);
		super.onPause();
	}
	
	private class StoryPagerAdapter extends FragmentStatePagerAdapter {
		
		private List<StoryDescriptionFragment> _fragments;
		
		public StoryPagerAdapter(FragmentManager fm) {
			super(fm);
			_fragments = new ArrayList<StoryDescriptionFragment>();
		}
		public void setStories(List<Story> newStories, Map<String, Bookmark> bookmarks) {
			_fragments = new ArrayList<StoryDescriptionFragment>();
			for (Story story : newStories) {
				StoryDescriptionFragment fragment = new StoryDescriptionFragment();
				
				fragment.setStory(story, bookmarks.containsKey(story.getId()));
				_fragments.add(fragment);
			}
			
			this.notifyDataSetChanged();
		}
		
		@Override
		public Fragment getItem(int i) {
			return _fragments.get(i);
		}
		@Override
		public int getCount() {
			return _fragments.size();
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			return "Object " + (position+1);
		}
	}
	
	public static class StoryDescriptionFragment extends Fragment {
		
		private Story _story;
		private View _rootView;
		private boolean _bookmarked;
		
		public void onCreate(Bundle bundle) {
			super.onCreate(bundle);
			setHasOptionsMenu(true);
		}
		public void setStory(Story story, boolean bookmarked) {
			_story = story;
			_bookmarked = bookmarked;
			setUpView();
		}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			
			_rootView = inflater.inflate(R.layout.story_descript, container, false);
			
			setUpView();
			
			return _rootView;			
		}
		
		private void setUpView() {
			if (_story == null) return;
			if (_rootView == null) return;
			
			/** Layout items **/
			Button play = (Button) _rootView.findViewById(R.id.play); 
			Button restart = (Button) _rootView.findViewById(R.id.restart);
			TextView title  = (TextView) _rootView.findViewById(R.id.title);
			TextView author  = (TextView) _rootView.findViewById(R.id.author);
			TextView datetime = (TextView) _rootView.findViewById(R.id.datetime);
			TextView fragments = (TextView) _rootView.findViewById(R.id.fragments);
			TextView content = (TextView) _rootView.findViewById(R.id.content);
			
			title.setText(_story.getTitle());
			author.setText("Author: " + _story.getAuthor());
			datetime.setText("Last Modified: " + _story.getFormattedTimestamp());
			fragments.setText("Fragments: " + _story.getFragmentIds().size());
			content.setText(_story.getSynopsis());

			if (_bookmarked) {
				play.setText("Continue Story");
				restart.setText("Start from the Beginning");
			} else {
				play.setText("Play Story");
				restart.setVisibility(View.GONE);
			}

			play.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// Launch Story
					Locator.getUserController().ResumeStory(_story.getId());
					Intent intent = new Intent(getActivity(), FragmentView.class);
					startActivity(intent);
				}
			});
			
			restart.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// Restart & Launch Story
					Locator.getUserController().StartStory(_story.getId());
					Intent intent = new Intent(getActivity(), FragmentView.class);
					startActivity(intent);
				}
			});
			
		}
	}
}
