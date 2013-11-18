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
import android.graphics.BitmapFactory;
import ca.cmput301f13t03.adventure_datetime.R;
import ca.cmput301f13t03.adventure_datetime.model.Interfaces.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Manages all transactions between views, controllers, and models.
 * Creates new Stories, StoryFragments, etc.
 * Fetches and caches Stories and StoryFragments
 */
public final class StoryManager implements IStoryModelPresenter,
		IStoryModelDirector {
	final String DEFAULT_FRAGMENT_TEXT = "<insert content here...>";
	private static final String TAG = "StoryManager";

	private StoryDB m_db = null;
	private Context m_context = null;

	// Current focus
	private Story m_currentStory = null;
	private StoryFragment m_currentFragment = null;
	
	private Map<String, Story> m_storyList = null;
	private Map<String, Bookmark> m_bookmarkList = null;
	private Map<String, StoryFragment> m_fragmentList = null;

	// Listeners
	private Set<ICurrentFragmentListener> m_fragmentListeners = new HashSet<ICurrentFragmentListener>();
	private Set<ICurrentStoryListener> m_storyListeners = new HashSet<ICurrentStoryListener>();
	private Set<IStoryListListener> m_storyListListeners = new HashSet<IStoryListListener>();
	private Set<IBookmarkListListener> m_bookmarkListListeners = new HashSet<IBookmarkListListener>();

	/**
	 * Create a new story manager and initializes other components using the provided context.
	 * The provided context MUST be the application context
	 */
	 public StoryManager(Context context) {
		m_context = context;
		m_db = new StoryDB(context);
		
		m_fragmentList = new HashMap<String, StoryFragment>();
	}

	// ============================================================
	//
	// IStoryModelPresenter
	//
	// The design is such that a publish will to the subscriber will
	// occur immediately if data is available. If not the data will
	// be supplied later once it is available.
	//
	// ============================================================

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
		} else {
			LoadStories();
			PublishStoryListChange();
		}
	}

	public void Subscribe(IBookmarkListListener bookmarkListListener) {
		m_bookmarkListListeners.add(bookmarkListListener);
		if (m_bookmarkList != null) {
			bookmarkListListener.OnBookmarkListChange(m_bookmarkList);
		} else {
			LoadBookmarks();
			PublishBookmarkListChange();
		}
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

	public void Unsubscribe(IBookmarkListListener bookmarkListListener) {
		m_bookmarkListListeners.remove(bookmarkListListener);
	}

	// ============================================================
	//
	// Publish
	//
	// ============================================================

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

	private void PublishBookmarkListChange() {
		for (IBookmarkListListener bookmarkListener : m_bookmarkListListeners) {
			bookmarkListener.OnBookmarkListChange(m_bookmarkList);
		}
	}

	// ============================================================
	//
	// IStoryModelDirector
	//
	// ============================================================

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
	* Create a new story and head fragment and insert them into the local database
	*/
	public Story CreateNewStory()
	{
		Story newStory = new Story();
		StoryFragment headFragment = new StoryFragment(newStory.getId(), DEFAULT_FRAGMENT_TEXT);
		
		newStory.setHeadFragmentId(headFragment);
		
		m_storyList.put(newStory.getId(), newStory);
		m_fragmentList.put(headFragment.getFragmentID(), headFragment);
		
		PublishCurrentStoryChange();
		
		return newStory;
	}

	public boolean putStory(Story story) {
		// Set default image if needed
		if (story.getThumbnail() == null)
			story.setThumbnail(BitmapFactory.decodeResource(
					m_context.getResources(), R.drawable.logo));
		return m_db.setStory(story);
	}

	/**
	 * Delete a story from the database
	 */
	public void deleteStory(String storyId) {
		m_db.deleteStory(storyId);
	}

	/**
	 * Get a story from the database or cloud
	 */
	public Story getStory(String storyId) {
		if(m_storyList == null)
		{
			LoadStories();
		}
		
		// returns null if there isn't one
		return m_storyList.get(storyId);
	}

	/**
	 * Save a fragment to the database
	 */
	public boolean putFragment(StoryFragment fragment) {
		
		// this really should be transactional...
		boolean result = m_db.setStoryFragment(fragment);
		if(result)
		{
			result = m_db.setStory(m_currentStory);
		}
		
		return result;
	}

	/**
	 * Delete a fragment from the database
	 */
	public void deleteFragment(UUID fragmentId) {
		m_db.deleteStoryFragment(fragmentId.toString());
	}

	/**
	 * Get a fragment from either the database or the cloud
	 */
	public StoryFragment getFragment(String fragmentId) {
		// The fragment should be part of the current story
		HashSet<String> fragmentIds = m_currentStory.getFragments();
		String theId = null;
		StoryFragment result = null;
		
		// verify that the id is indeed part of the current story!
		for(String id : fragmentIds)
		{
			if(fragmentId.equalsIgnoreCase(id))
			{
				theId = id;
			}
		}
		
		if(theId == null)
		{
			// Then you requested an id not attached to the current story!
			throw new RuntimeException("Requested Fragment Id not attached to current story!");
		}
		
		if(m_fragmentList.containsKey(theId))
		{
			// great we have it cached!
			result = m_fragmentList.get(theId);
		}
		else
		{
			// shit, gotta load, may be in DB or online
			// attempt DB first
			result = m_db.getStoryFragment(theId);
			if(result == null)
			{
				// then it wasn't in the database
				// TODO try fetch from online!
			}
			else
			{
				// add it to the cache
				m_fragmentList.put(result.getFragmentID(), result);
			}
		}
		
		return result;
	}

	public ArrayList<Story> getStoriesAuthoredBy(String author) {
		if(m_storyList == null)
		{
			LoadStories();
		}
		
		ArrayList<Story> results = new ArrayList<Story>();
		
		for(Story story : m_storyList.values())
		{
			if(author.equalsIgnoreCase(story.getAuthor()))
			{
				results.add(story);
			}
		}
		
		return results;
	}

	/**
	 * Fetch a bookmark from local database
	 */
	public Bookmark getBookmark(String id) {
		if(m_bookmarkList == null)
		{
			LoadBookmarks();
		}
		
		return m_bookmarkList.get(id);
	}

	public void setBookmark() {
		Bookmark newBookmark = new Bookmark(m_currentStory.getId(), m_currentFragment.getFragmentID());
		m_db.setBookmark(newBookmark);
		PublishBookmarkListChange();
	}
	
	private void LoadStories()
	{
		m_storyList = new HashMap<String, Story>();
		ArrayList<Story> localStories = m_db.getStories();
		
		for(Story story : localStories)
		{
			m_storyList.put(story.getId(), story);
		}
		
		// TODO load from online
	}
	
	private void LoadBookmarks()
	{
		m_bookmarkList = new HashMap<String, Bookmark>();
		ArrayList<Bookmark> bookmarks = m_db.getAllBookmarks();
		
		for(Bookmark bookmark : bookmarks)
		{
			m_bookmarkList.put(bookmark.getStoryID(), bookmark);
		}
		
		// TODO load from online
	}
}