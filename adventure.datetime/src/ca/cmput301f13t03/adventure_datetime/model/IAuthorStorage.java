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

import java.util.Collection;

/**
 * Service for publishing Stories or saving them
 * locally while working on them.
 */
public interface IAuthorStorage extends IReaderStorage {
	
	/**
	 * Publish a story to the web. May be an insert or update.
	 * Must be the author in case of an update.
	 * @param story the story
	 * @param fragments the fragments for the story
	 * @return the ID of the story published
	 */
	int publishStory(Story story, Collection<StoryFragment> fragments);
	
	/**
	 * Remove a story from the web service.
	 * Must be the author of the story
	 * @param storyId the story id
	 * @return True if story deleted, false otherwise
	 */
	boolean unpublishStory(long storyId);
	
	/**
	 * Save a story to local storage. Does not publish the story
	 * @param story the story
	 * @param fragments the fragments for the story
	 * @return True if saved, false otherwise
	 */
	boolean saveStory(Story story, Collection<StoryFragment> fragments);
}
