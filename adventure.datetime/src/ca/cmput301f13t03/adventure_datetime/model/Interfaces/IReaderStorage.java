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

package ca.cmput301f13t03.adventure_datetime.model.Interfaces;

import java.util.Collection;

import ca.cmput301f13t03.adventure_datetime.model.*;

/**
 * Interface for user storage transactions
 */
public interface IReaderStorage {
	
	/**
	 * Retrieve a story from storage
	 * @param storyId the story id
	 * @return the Story object
	 */
	Story getStory(long storyId);
	
	/**
	 * Retrieve all stories from storage
	 * @return collection of Story objects
	 */
	Collection<Story> getAllStories();
	
	/**
	 * Retrieve a fragment for a story
	 * @param storyId the story id
	 * @param fragmentId the id of the fragment within the story
	 * @return the StoryFragment object
	 */
	StoryFragment getFragment(long storyId, long fragmentId);
	
	/**
	 * Gets all fragments for a story
	 * @param storyId the story id
	 * @return A collection of StoryFragment objects
	 */
	Collection<StoryFragment> getAllFragmentsForStory(long storyId); 
	
	/**
	 * Subscribe to a story to receive updates
	 * @param storyId the story id
	 * @return True if subscribed, false if it fails
	 */
	boolean subsribe(long storyId);
	
	/**
	 * Unsubscribe from a story
	 * @param storyId the story id
	 * @return True if unsubscribed, false otherwise
	 */
	boolean unsubscribe(long storyId);
	
	/**
	 * Put a story in local storage
	 * @param storyId the story id
	 * @return True is saved locally, false otherwise
	 */
	boolean saveStoryLocally(long storyId);
	
	/**
	 * Deletes a story from local storage
	 * @param storyId the story to delete
	 * @return True if story deleted, false otherwise
	 */
	boolean deleteStoryLocally(long storyId);
	
	/**
	 * Gets a collection of comments for a given story fragment
	 * @param storyId the story id
	 * @param fragmentId the fragment id
	 * @return A collection of Comment objects
	 */
	Collection<Comment> getComments(long storyId, long fragmentId);
	
	/**
	 * Comment on a story
	 * @param storyId the story to comment on
	 * @param fragmentId the fragment to comment on
	 * @param comment the comment
	 * @return True if comment successful, false otherwise
	 */
	boolean comment(long storyId, long fragmentId, Comment comment);
	
	/**
	 * Delete a comment from a story fragment
	 * @param storyId the story id
	 * @param fragmentId the fragment id
	 * @param commentId the comment id
	 * @return True if comment deleted, false otherwise
	 */
	boolean deleteComment(long storyId, long fragmentId, long commentId);
}
