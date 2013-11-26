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

import java.util.List;
import java.util.UUID;

import ca.cmput301f13t03.adventure_datetime.model.Comment;
import ca.cmput301f13t03.adventure_datetime.model.Image;
import ca.cmput301f13t03.adventure_datetime.model.Story;
import ca.cmput301f13t03.adventure_datetime.model.StoryFragment;

public interface IWebStorage {

	/**
	 * Gets a story from ES
	 * @param storyId ID of the story to retrieve
	 * @return The Story object
	 * @throws Exception, connection errors, etc. See JestClient
	 */
	public abstract Story getStory(UUID storyId) throws Exception;

	/**
	 * Tries to get "size" stories, starting at "from" from ES.
	 * May return less than "size" if end of stories is reached.
	 * @param from the start index to return stories from
	 * @param size the amount of stories to try to return
	 * @return List of stories
	 * @throws Exception, connection errors, etc. See JestClient
	 */
	public abstract List<Story> getStories(int from, int size) throws Exception;
	
	/**
	 * Queries elasticsearch with the provided filter for matching stories
	 * Searches across Author, title, tags, and synposis fields 
	 * @param filter the filter to search for.
	 * @param from the start index to return stories from
	 * @param size the amount of stories to try to return
	 * @return List of stories
	 * @throws Exception, connection errors, etc. See JestClient
	 */
	public List<Story> queryStories(String filter, int from, int size) throws Exception;

	/**
	 * Gets a fragment from ES
	 * All fragments are UUID so story reference is not needed
	 * @param fragmentId ID of the fragment to retrieve
	 * @return The StoryFragment object
	 * @throws Exception, connection errors, etc. See JestClient
	 */
	public abstract StoryFragment getFragment(UUID fragmentId) throws Exception;

	/**
	 * Gets fragments for a given story.
	 * Will return up to size fragments starting at from.
	 * @param storyId ID of the story to retrieve all fragments for
	 * @param from the start index to return fragments from
	 * @param size the amount of fragments to try to return
	 * @return List of StoryFragments
	 * @throws Exception, connection errors, etc. See JestClient
	 */
	public abstract List<StoryFragment> getFragmentsForStory(UUID storyId, int from, int size)
			throws Exception;

	/**
	 * Gets comments for the targetId. May be a StoryId or FragmentId.
	 * Will return up to size comments starting at from.
	 * @param targetId. The Story or StoryFragment to retrieve comments for.
	 * @param from the start index to return comments from
	 * @param size the amount of comments to try to return
	 * @return A List of comments
	 * @throws Exception, connection errors, etc. See JestClient
	 */
	public abstract List<Comment> getComments(UUID targetId, int from, int size) throws Exception;

	/**
	 * Puts a comment to ES
	 * @param comment the Comment to save to ES
	 * @return True if succeeded, false otherwise.
	 * @throws Exception, connection errors, etc. See JestClient
	 */
	public abstract boolean putComment(Comment comment) throws Exception;

	/**
	 * Deletes a comment from ES
	 * @param commentId ID of the comment to delete
	 * @return True if succeeded, false otherwise
	 * @throws Exception, connection errors, etc. See JestClient
	 */
	public abstract boolean deleteComment(UUID commentId) throws Exception;

	/**
	 * Publishes a Story to ES. Overwrites if already exists.
	 * Note: this does NOT check if the StoryFragments actually belong to the Story
	 * @param story the Story object to publish
	 * @param fragments List of StoryFragments to publish
	 * @return True if succeeded, false otherwise
	 * @throws Exception, connection errors, etc. See JestClient
	 */
	public abstract boolean publishStory(Story story,
			List<StoryFragment> fragments) throws Exception;
	
	/**
	 * Gets an image from elasticsearch
	 * @param imageId the image to get
	 * @return The retrieved Image object
	 * @throws Exception, connection errors, etc. See JestClient
	 */
	public Image getImage(UUID imageId) throws Exception;
	
	/**
	 * Puts an image into elasticsearch.
	 * Updates it if it already exists.
	 * @param Image, the image to put
	 * @return True if succeeded, false otherwise.
	 * @throws Exception, connection errors, etc. See JestClient
	 */
	public boolean putImage(Image image) throws Exception;
	
	/**
	 * Deletes an image from elasticsearch
	 * @param imageId the image to delete
	 * @return True if succeeded, false otherwise
	 * @throws Exception, connection errors, etc. See JestClient
	 */
	public boolean deleteImage(UUID imageId) throws Exception;
	
	/**
	 * Deletes a story from the web service. 
	 * @param storyId the story Id.
	 * @return True if succeeded, false otherwise.
	 * @throws Exception, connection errors, etc. See JestClient
	 */
	public boolean deleteStory(UUID storyId) throws Exception;
	
	/**
	 * Deletes a fragment from the web service. 
	 * @param fragId the fragment Id.
	 * @return True if succeeded, false otherwise.
	 * @throws Exception, connection errors, etc. See JestClient
	 */
	public boolean deleteFragment(UUID fragId) throws Exception;

}