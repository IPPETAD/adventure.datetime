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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import ca.cmput301f13t03.adventure_datetime.model.Story;
import ca.cmput301f13t03.adventure_datetime.model.StoryFragment;
import ca.cmput301f13t03.adventure_datetime.model.Interfaces.IStoryModelDirector;

/**
 * Controller for aspects of authoring a story
 * 
 * @author Evan DeGraff
 *
 */
public class AuthorController {
	private IStoryModelDirector m_storyDirector = null;

	public AuthorController(IStoryModelDirector director) {
		m_storyDirector = director;
	}
	
	public boolean saveStory(Story story){
		return m_storyDirector.putStory(story);
	}
	
	public Story getStory(String storyId) {
		return m_storyDirector.getStory(storyId);
	}
	
	public void deleteStory(String storyId){
		Story story = m_storyDirector.getStory(storyId);
		HashSet<String> fragments = story.getFragmentIds();
		Iterator<String> iterator = fragments.iterator();
		String fragmentId;
		
		while(iterator.hasNext()){
			fragmentId = iterator.next();
			deleteFragment(fragmentId);
		}
		
		m_storyDirector.deleteStory(storyId);
	}
	
	public boolean saveFragment(StoryFragment fragment) {
		return m_storyDirector.putFragment(fragment);
	}
	
	public void deleteFragment(String fragmentId){
		m_storyDirector.deleteFragment(fragmentId);
	}
	
	/*public void publish(long storyID){
		m_storyDirector.publish(storyID);
	}*/
}
