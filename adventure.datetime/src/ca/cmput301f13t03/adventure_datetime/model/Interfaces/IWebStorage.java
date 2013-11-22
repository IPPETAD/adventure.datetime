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
	 * Gets all stories from ES
	 * @return List of stories
	 * @throws Exception, connection errors, etc. See JestClient
	 */
	public abstract List<Story> getAllStories() throws Exception;

	/**
	 * Gets a fragment from ES
	 * All fragments are UUID so story reference is not needed
	 * @param fragmentId ID of the fragment to retrieve
	 * @return The StoryFragment object
	 * @throws Exception, connection errors, etc. See JestClient
	 */
	public abstract StoryFragment getFragment(UUID fragmentId) throws Exception;

	/**
	 * Gets all fragments for a given story
	 * @param storyId ID of the story to retrieve all fragments for
	 * @return List of StoryFragments
	 * @throws Exception, connection errors, etc. See JestClient
	 */
	public abstract List<StoryFragment> getAllFragmentsForStory(UUID storyId)
			throws Exception;

	/**
	 * Gets a comment for the targetId. May be a StoryId or FragmentId.
	 * @param targetId. The Story or StoryFragment to retrieve comments for.
	 * @return A List of comments
	 * @throws Exception, connection errors, etc. See JestClient
	 */
	public abstract List<Comment> getComments(UUID targetId) throws Exception;

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

}