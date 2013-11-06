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
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import ca.cmput301f13t03.adventure_datetime.R;
import ca.cmput301f13t03.adventure_datetime.model.Story;
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
public class StoryDescription extends FragmentActivity implements IStoryListListener {
	private static final String TAG = "StoryDescription";
	
	public static final String ARG_STORYID = ".view.StoryDescription.StoryID";
	
	private StoryPagerAdapter _pageAdapter;
	private ViewPager _viewPager;
	private String sID;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager);
		
		sID = getIntent().getStringExtra(ARG_STORYID);
		
		_pageAdapter = new StoryPagerAdapter(getSupportFragmentManager());
		
		_viewPager = (ViewPager) findViewById(R.id.pager);
		_viewPager.setAdapter(_pageAdapter);
	}
	
	@Override
	public void OnCurrentStoryListChange(Collection<Story> newStories) {
		_pageAdapter.setStoryList(newStories);
		
		int i=0;
		for (Story story : newStories) {
			if (story.getId().equals(sID)) {
				_viewPager.setCurrentItem(i);
				return;
			}
			i++;
		}		
	}
	
	@Override
	public void onResume() {
		Locator.initializeLocator(getApplicationContext());
		Locator.getPresenter().Subscribe(this);
		super.onResume();
	}
	
	@Override
	public void onPause() {
		Locator.getPresenter().Unsubscribe(this);
		super.onPause();
	}
	
	private class StoryPagerAdapter extends FragmentStatePagerAdapter {
		
		private Collection<StoryDescriptionFragment> _fragments;
		
		public StoryPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		
		public void setStoryList(Collection<Story> stories) {
			
			_fragments = new ArrayList<StoryDescriptionFragment>();
			
			for (Story story : stories) {
				StoryDescriptionFragment frag = new StoryDescriptionFragment();
				frag.setStoryDescription(story);
				_fragments.add(frag);
			}			
			notifyDataSetChanged();
		}
		
		@Override
		public Fragment getItem(int i) {
			if (_fragments == null)
				return null; 
			return ((List<StoryDescriptionFragment>)_fragments).get(i);
		}
		
		@Override
		public int getCount() {
			if (_fragments == null)
				return 0;
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
		
		public void onCreate(Bundle bundle) {
			super.onCreate(bundle);
			setHasOptionsMenu(true);
		}
		
		public void setStoryDescription(Story story) {
			_story = story;
			setUpView();
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			
			_rootView = inflater.inflate(R.layout.story_descript, container, false);
			
			/** Action bar **/
			getActivity().getActionBar().setTitle("Story Name");
			
			setUpView();
			
			return _rootView;			
		}
		
		private void setUpView() {
			if (_story == null) return;
			if (_rootView == null) return;

			/** Layout items **/
			Button play = (Button) _rootView.findViewById(R.id.play); 
			TextView title  = (TextView) _rootView.findViewById(R.id.title);
			TextView author  = (TextView) _rootView.findViewById(R.id.author);
			TextView datetime = (TextView) _rootView.findViewById(R.id.datetime);
			TextView fragments = (TextView) _rootView.findViewById(R.id.fragments);
			TextView content = (TextView) _rootView.findViewById(R.id.content);
			
			title.setText(_story.getTitle());
			author.setText("Author: " + _story.getAuthor());
			datetime.setText("Last Modified: " + _story.getFormattedTimestamp());
			fragments.setText("Fragments: " + "Idk");
			content.setText(_story.getSynopsis());
			
			// TODO::JF Load data from model
			play.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// Launch Story
					Locator.getUserController().StartStory(_story.getId());
					Intent intent = new Intent(getActivity(), FragmentView.class);
					startActivity(intent);
				}
			});
			
			
		}
				
	}
}
