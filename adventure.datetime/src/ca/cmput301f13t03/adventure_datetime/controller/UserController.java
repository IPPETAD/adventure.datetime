/*
 * Copyright (c) 2013 Andrew Fontaine, James Finlay, Jesse Tucker, Jacob Viau, and
 * Evan DeGraff
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package ca.cmput301f13t03.adventure_datetime.controller;

import android.util.Log;
import ca.cmput301f13t03.adventure_datetime.model.*;
import ca.cmput301f13t03.adventure_datetime.model.Interfaces.IReaderStorage;
import ca.cmput301f13t03.adventure_datetime.model.Interfaces.IStoryModelDirector;

public class UserController {
	private IStoryModelDirector m_storyDirector = null;

	public UserController(IStoryModelDirector director, IReaderStorage storage) {
		m_storyDirector = director;
	}

	/**
	 * 
	 * @param storyId
	 * @return true if the story was successfully selected, false if it doesn't
	 *         exist
	 */
	public boolean StartStory(String storyId) {
		try {
			m_storyDirector.selectStory(storyId);
			m_storyDirector.selectFragment(m_storyDirector.getStory(storyId).getHeadFragmentId());
			return true;
		} catch (NullPointerException e) {
			Log.e("UserController", e.getMessage());
			return false;
		}
		/* TODO::JT also select head fragment and create save */
	}

	/**
	 * 
	 * @param id
	 * @return true if story was successfully selected, false if it doesn't
	 *         exist
	 */
	public boolean ResumeStory(String id) {
		Bookmark bookmark = m_storyDirector.getBookmark(id);
		try {
			m_storyDirector.selectStory(bookmark.getStoryID());
			m_storyDirector.selectFragment(bookmark.getFragmentID());
			return true;
		} catch (NullPointerException e) {
			Log.e("UserController", e.getMessage());
			return false;
		}
	}

	public void SetBookmark(Bookmark bookmark) {
		m_storyDirector.setBookmark(bookmark);
	}

	public void AddComment(Comment comment) {
		/* TODO::JT */
	}

	public void MakeChoice(Choice choice) {
		m_storyDirector.selectFragment(choice.getTarget());
	}
}
