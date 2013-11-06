/*
 *        Copyright (c) 2013 Andrew Fontaine, James Finlay, Jesse Tucker, Jacob Viau, and
 *         Evan DeGraff
 *
 *         Permission is hereby granted, free of charge, to any person obtaining a copy of
 *         this software and associated documentation files (the "Software"), to deal in
 *         the Software without restriction, including without limitation the rights to
 *         use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 *         the Software, and to permit persons to whom the Software is furnished to do so,
 *         subject to the following conditions:
 *
 *         The above copyright notice and this permission notice shall be included in all
 *         copies or substantial portions of the Software.
 *
 *         THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *         IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 *         FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 *         COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 *         IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *         CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package ca.cmput301f13t03.adventure_datetime.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.util.Log;

import ca.cmput301f13t03.adventure_datetime.model.Interfaces.*;

public final class StoryManager implements IStoryModelPresenter, IStoryModelDirector
{
	private static final String TAG = "StoryManager";
	
	// Current focus
	private Story m_currentStory = null;
	private StoryFragment m_currentFragment = null;
	private Collection<Story> m_storyList = null;
	
	// Listeners
	private Set<ICurrentFragmentListener> m_fragmentListeners = new HashSet<ICurrentFragmentListener>();
	private Set<ICurrentStoryListener> m_storyListeners = new HashSet<ICurrentStoryListener>();
	private Set<IStoryListListener> m_storyListListeners = new HashSet<IStoryListListener>();
	
	public StoryManager(Context context)
	{
		
	}
	
	//============================================================
	//
	//	IStoryModelPresenter
	//
	//	The design is such that a publish will to the subscriber will
	//	occur immediately if data is available. If not the data will
	//	be supplied later once it is available.
	//
	//============================================================
	
	public void Subscribe(ICurrentFragmentListener fragmentListener) 
	{
		m_fragmentListeners.add(fragmentListener);
		if(m_currentFragment != null)
		{
			fragmentListener.OnCurrentFragmentChange(m_currentFragment);
		}
		fragmentListener.OnCurrentFragmentChange(getFakeFragment());
	}
	public StoryFragment getFakeFragment() {
		Choice choice = new Choice("Go to Ratt.", 1);
		StoryFragment frag = new StoryFragment("sid1", 
			"Curiously enough, the only thing that"+
			" went through the mind of the bowl of"+
			" petunias as it fell was Oh no, not a"+
			"gain. Many people have speculated tha"+
			"t if we knew exactly why the bowl of "+
			"petunias had thought that we would kn"+
			"ow a lot more about the nature of the"+
			"Universe than we do now.", choice);
		frag.setStoryMedia(new ArrayList<String>());
		return frag;
	}

	public void Subscribe(ICurrentStoryListener storyListener) 
	{
		m_storyListeners.add(storyListener);
		if(m_currentStory != null)
		{
			storyListener.OnCurrentStoryChange(getClusterfuckOfStories().iterator().next());
		} else
			storyListener.OnCurrentStoryChange(getClusterfuckOfStories().iterator().next());
	}

	public void Subscribe(IStoryListListener storyListListener) 
	{
		m_storyListListeners.add(storyListListener);
		if(m_storyList != null)
		{
			storyListListener.OnCurrentStoryListChange(m_storyList);
		} else {
			storyListListener.OnCurrentStoryListChange(
					getClusterfuckOfStories());
		}
		/* This may be a good opportunity to async fetch the data from
		 * either local storage or server*/
	}
	
	public Collection<Story> getClusterfuckOfStories() {
		Collection<Story> stories = new java.util.ArrayList<Story>();
		for (int i=0; i<4; i++) {
			Story story = new Story("Douglas Adams", 
					"HitchHiker's Guide",
					"The book begins with contractors"+ 
					" arriving at Arthur Dent's house"+
					", in order to demolish it to mak"+
					"e way for a bypass. His friend, "+
					"Ford Prefect, arrives while Arth"+
					"ur is lying in front of the bull"+
					"dozers, to keep them from demoli"+
					"shing it. He tries to explain to"+
					" Arthur that the Earth is about "+
					"to be demolished. The Vogons, an"+
					" alien race, intend to destroy E"+
					"arth to make way for a hyperspac"+
					"e bypass.");
			story.setTimestamp(Calendar.getInstance().getTimeInMillis());
			story.setId("id"+i);
			stories.add(story);
		}
		return stories;
	}
	
	public void Unsubscribe(ICurrentFragmentListener fragmentListener) 
	{
		m_fragmentListeners.remove(fragmentListener);
	}

	public void Unsubscribe(ICurrentStoryListener storyListener) 
	{
		m_storyListeners.remove(storyListener);
	}

	public void Unsubscribe(IStoryListListener storyListListener) 
	{
		m_storyListListeners.remove(storyListListener);
	}
	
	//============================================================
	//
	//	Publish
	//
	//============================================================
	
	private void PublishCurrentStoryChange()
	{
		for(ICurrentStoryListener storyListener : m_storyListeners)
		{
			storyListener.OnCurrentStoryChange(m_currentStory);
		}
	}
	
	private void PublishCurrentFragmentChange()
	{
		for(ICurrentFragmentListener fragmentListener : m_fragmentListeners)
		{
			fragmentListener.OnCurrentFragmentChange(m_currentFragment);
		}
	}
	
	private void PublishStoryListChange()
	{
		for(IStoryListListener listListener : m_storyListListeners)
		{
			listListener.OnCurrentStoryListChange(m_storyList);
		}
	}

	//============================================================
	//
	//	IStoryModelDirector
	//
	//============================================================
	
	public void SelectStory(String storyId) 
	{
		for (Story story : getClusterfuckOfStories()) {
			if (story.getId().equals(storyId)) {
				m_currentStory = story;
				return;
			}
		}			
	}

	public void SelectFragment(long fragmentId) 
	{
		/* TODO::JT */
	}

	@Override
	public void Subscribe(IBookmarkListListener bookmarkListListener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Unsubscribe(IBookmarkListListener bookmarkListListener) {
		// TODO Auto-generated method stub
		
	}
}