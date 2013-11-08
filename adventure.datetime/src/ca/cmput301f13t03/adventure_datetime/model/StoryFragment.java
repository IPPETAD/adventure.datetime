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

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.UUID;

/**
 * A model for a fragment of the Choose-Your-Own-Adventure
 *
 * @author Andrew Fontaine
 * @version 1.0
 * @since 23/10/13
 */
public class StoryFragment {

	/**
	 * The UUID of the story linked to the fragment
	 */
	final private UUID storyID;
	/**
	 * The UUID of the fragment
	 */
	final private UUID fragmentID;
	/**
	 * The list of all Story Media associated with the fragment
	 */
	private ArrayList<String> storyMedia;
	/**
	 * The text content of the fragment
	 */
	private String storyText;
	/**
	 * The list of choices associated with the fragment
	 */
	private ArrayList<Choice> choices;

	public StoryFragment(String storyID, String fragmentID, String storyText,
	                     ArrayList<String> storyMedia, ArrayList<Choice> choices) {
		this.storyID = UUID.fromString(storyID);
		this.fragmentID = UUID.fromString(fragmentID);
		this.storyText = storyText;
		this.storyMedia = storyMedia;
		this.choices = choices;
	}

	public StoryFragment(String storyID, String storyText, Choice choice) {
		this.storyID = UUID.fromString(storyID);
		this.fragmentID = UUID.randomUUID();
		this.choices = new ArrayList<Choice>();
		this.choices.add(choice);
		this.storyText = storyText;
		this.storyMedia = new ArrayList<String>();
	}

	public StoryFragment(String storyID, String storyText) {
		this.storyID = UUID.fromString(storyID);
		this.fragmentID = UUID.randomUUID();
		this.storyText = storyText;
		this.choices = new ArrayList<Choice>();
		this.storyMedia = new ArrayList<String>();
	}

	public StoryFragment(ArrayList<Choice> choices, String storyID, String fragmentID, String storyText) {
		this.choices = choices;
		this.storyID = UUID.fromString(storyID);
		this.fragmentID = UUID.fromString(fragmentID);
		this.storyText = storyText;
		this.storyMedia = new ArrayList<String>();
	}

	public String getStoryID() {
		return storyID.toString();
	}


	public String getFragmentID() {
		return fragmentID.toString();
	}

	public ArrayList<String> getStoryMedia() {
		return storyMedia;
	}

	public void setStoryMedia(ArrayList<String> storyMedia) {
		this.storyMedia = storyMedia;
	}

	public void addMedia(String media) {
		storyMedia.add(media);
	}

	public void removeMedia(String media) {
		storyMedia.remove(media);
	}

	public String getMedia(int id) {
		return storyMedia.get(id);
	}

	public String getStoryText() {
		return storyText;
	}

	public void setStoryText(String storyText) {
		this.storyText = storyText;
	}

	public ArrayList<Choice> getChoices() {
		return choices;
	}

	public String getChoicesInJson() {
		Gson gson = new Gson();
		return gson.toJson(choices);
	}

	public void setChoices(ArrayList<Choice> choices) {
		this.choices = choices;
	}

	public void addChoice(Choice choice) {
		choices.add(choice);
	}

	public void removeChoice(Choice choice) {
		choices.remove(choice);
	}

	public Choice getChoice(int id) {
		return (Choice) choices.toArray()[id];
	}
}
