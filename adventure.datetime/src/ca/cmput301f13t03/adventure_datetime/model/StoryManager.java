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

import android.content.Context;
import ca.cmput301f13t03.adventure_datetime.model.Interfaces.*;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class StoryManager implements IStoryModelPresenter, IStoryModelDirector
{	
	private static final String TAG = "StoryManager";

	private StoryDB m_db = null;

	// Current focus
	private Story m_currentStory = null;
	private StoryFragment m_currentFragment = null;
	private Collection<Story> m_storyList = null;

	// Listeners
	private Set<ICurrentFragmentListener> m_fragmentListeners = new HashSet<ICurrentFragmentListener>();
	private Set<ICurrentStoryListener> m_storyListeners = new HashSet<ICurrentStoryListener>();
	private Set<IStoryListListener> m_storyListListeners = new HashSet<IStoryListListener>();

	/**
	 * Create a new story manager and initializes other components using the provided context.
	 * The provided context MUST be the application context
	 */
	public StoryManager(Context context)
	{
		m_db = new StoryDB(context);
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

	/**
	 * Subscribe for changes to the current fragment
	 */
	public void Subscribe(ICurrentFragmentListener fragmentListener) {
		m_fragmentListeners.add(fragmentListener);
		if (m_currentFragment != null) {
			fragmentListener.OnCurrentFragmentChange(m_currentFragment);
		}
	}

	/**
	 * Subscribe for changes to the current story
	 */
	public void Subscribe(ICurrentStoryListener storyListener) {
		m_storyListeners.add(storyListener);
		if (m_currentStory != null) {
			storyListener.OnCurrentStoryChange(m_currentStory);
		}
	}

	/**
	 * Subscribe to changes for the current list of stories
	 */
	public void Subscribe(IStoryListListener storyListListener) {
		m_storyListListeners.add(storyListListener);
		if (m_storyList != null) {
			storyListListener.OnCurrentStoryListChange(m_storyList);
		}
		m_storyList = new ArrayList<Story>();
		m_storyList.addAll(m_db.getStories());
		PublishStoryListChange();
	}

	/**
	 * Unsubscribe from callbacks when the current fragment changes
	 */
	public void Unsubscribe(ICurrentFragmentListener fragmentListener) {
		m_fragmentListeners.remove(fragmentListener);
	}

	/**
	 * Unsubscribe from callbacks when the current story changes
	 */
	public void Unsubscribe(ICurrentStoryListener storyListener) {
		m_storyListeners.remove(storyListener);
	}

	/**
	 * Unsubscribe from callbakcs when the current list of stories changes
	 */
	public void Unsubscribe(IStoryListListener storyListListener) {
		m_storyListListeners.remove(storyListListener);
	}

	//============================================================
	//
	//	Publish
	//
	//============================================================

	/**
	 * Publish a change to the current story to all listeners
	 */
	private void PublishCurrentStoryChange() {
		for (ICurrentStoryListener storyListener : m_storyListeners) {
			storyListener.OnCurrentStoryChange(m_currentStory);
		}
	}

	/**
	 * Publish a change to the current fragment to all listeners
	 */
	private void PublishCurrentFragmentChange() {
		for (ICurrentFragmentListener fragmentListener : m_fragmentListeners) {
			fragmentListener.OnCurrentFragmentChange(m_currentFragment);
		}
	}

	/**
	 * Publish a changed to the current list of stories to all listeners
	 */
	private void PublishStoryListChange() {
		for (IStoryListListener listListener : m_storyListListeners) {
			listListener.OnCurrentStoryListChange(m_storyList);
		}
	}

	//============================================================
	//
	//	IStoryModelDirector
	//
	//============================================================

	/**
	 * Select a story
	 */
	public void selectStory(String storyId) {
		m_currentStory = getStory(storyId);
		PublishCurrentStoryChange();
	}

	/**
	 * Select a fragment as the current fragment
	 */
	public void selectFragment(String fragmentId) {
		m_currentFragment = getFragment(fragmentId);
		PublishCurrentFragmentChange();
	}

	/**
	 * Save a story to the database
	 */
	public boolean putStory(Story story) {
		return m_db.setStory(story);
	}

	/**
	 * Delete a story from the database
	 */
	public void deleteStory(String storyId) {
		// TODO Needs to be implemented in database.

	}

	/**
	 * Get a story from the database or cloud
	 */
	public Story getStory(String storyId) {
		// TODO this should pull from our cache list
		return m_db.getStory(storyId);
	}

	/**
	 * Save a fragment to the database
	 */
	public boolean putFragment(StoryFragment fragment) {
		return m_db.setStoryFragment(fragment);
	}

	/**
	 * Delete a fragment from the database
	 */
	public void deleteFragment(UUID fragmentId) {
		// TODO Needs to be implemented in database.

	}

	/**
	 * Get a fragment from either the database or the cloud
	 */
	public StoryFragment getFragment(String fragmentId) {
		// TODO should actually pull from the cache
		return m_db.getStoryFragment(fragmentId);
	}

	/**
	 * Fetch a list of stories produced by a particular author
	 */
	public ArrayList<Story> getStoriesAuthoredBy(String author) {
		// TODO should actually pull from the cache
		return m_db.getStoriesAuthoredBy(author);
	}


	@Override
	public void Subscribe(IBookmarkListListener bookmarkListListener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void Unsubscribe(IBookmarkListListener bookmarkListListener) {
		// TODO Auto-generated method stub

	}

	/**
	 * Fetch a bookmark from local database
	 */
	public Bookmark getBookmark(String id) {
		// should pull from cache, only hit DB if bookmark list is null
		return m_db.getBookmark(id);
	}

	/**
	 * Save a bookmark to the database
	 * 
	 * WHY DOES THIS TAKE A BOOKMARK!
	 */
	public void setBookmark(Bookmark bookmark) {
		// TODO shouldn't take any parameters... we have
		// enough info to construct the bookmark right here
		m_db.setBookmark(bookmark);
	}
}