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
public class StoryFragment implements Comparable<StoryFragment>
{

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

    /**
     * Constructor for making StoryFragments, used by @link{StoryDB}
     *
     * @param storyID UUID of the Story the StoryFragment is a part of
     * @param fragmentID UUID of the StoryFragment
     * @param storyText Text of the StoryFragment
     * @param storyMedia List of media associated with the StoryFragment
     * @param choices List of Choices associated with the Story Fragment
     */
	public StoryFragment(UUID storyID, UUID fragmentID, String storyText,
	                     ArrayList<String> storyMedia, ArrayList<Choice> choices) {
		this.storyID = storyID;
		this.fragmentID = fragmentID;
		this.storyText = storyText;
		this.storyMedia = storyMedia;
		this.choices = choices;
	}

    /**
     * Constructor for making a StoryFragment, used by @link{StoryManager}
     * It creates a new UUID for the new StoryFragment
     *
     * @param storyID UUID of the Story the StoryFragment is a part of
     * @param storyText Text of the StoryFragment
     * @param choice New Choice for the new StoryFragment
     */
	public StoryFragment(UUID storyID, String storyText, Choice choice) {
		this.storyID = storyID;
		this.fragmentID = UUID.randomUUID();
		this.choices = new ArrayList<Choice>();
		this.choices.add(choice);
		this.storyText = storyText;
		this.storyMedia = new ArrayList<String>();
	}

    /**
     * Constructor for making a StoryFragment, used by @link{StoryManager}
     * It creates a new UUID for the new StoryFragment
     *
     * @param storyID UUID of the Story the StoryFragment is a part of
     * @param storyText Text of the StoryFragment
     */
	public StoryFragment(UUID storyID, String storyText) {
		this.storyID = storyID;
		this.fragmentID = UUID.randomUUID();
		this.storyText = storyText;
		this.choices = new ArrayList<Choice>();
		this.storyMedia = new ArrayList<String>();
	}

    /**
     * Constructor for making a StoryFragment, used by @link{StoryManager} adn @link{StoryDB}
     *
     *
     * @param choices List of Choices associated with the Story Fragment
     * @param storyID UUID of the Story the StoryFragment is a part of
     * @param fragmentID UUID of the StoryFragment
     * @param storyText Text of the StoryFragment
     */
	public StoryFragment(ArrayList<Choice> choices, UUID storyID, UUID fragmentID, String storyText) {
		this.choices = choices;
		this.storyID = storyID;
		this.fragmentID = fragmentID;
		this.storyText = storyText;
		this.storyMedia = new ArrayList<String>();
	}

    /**
     * Gets the UUID of the Story associated with the StoryFragment
     *
     * @return UUID of the Story
     */
	public UUID getStoryID() {
		return storyID;
	}

    /**
     * Gets the StoryFragment UUID
     *
     * @return UUID of the StoryFragment
     */
	public UUID getFragmentID() {
		return fragmentID;
	}

    /**
     * Gets the List of all Media associated with the StoryFragment
     *
     * @return List of all Media
     */
	public ArrayList<String> getStoryMedia() {
		return storyMedia;
	}

    /**
     * Sets the list of all story media
     *
     * @param storyMedia The new list of media
     */
	public void setStoryMedia(ArrayList<String> storyMedia) {
		this.storyMedia = storyMedia;
	}

    /**
     * Add media to the list of media
     *
     * @param media Media to add
     */
	public void addMedia(String media) {
		storyMedia.add(media);
	}

    /**
     * Removes media from the list of media
     *
     * @param media The media to remove
     */
	public void removeMedia(String media) {
		storyMedia.remove(media);
	}

    /**
     * Gets media at a certain index of the list
     *
     * @param id The index of the media
     *
     * @return The media
     */
	public String getMedia(int id) {
		return storyMedia.get(id);
	}

    /**
     * Gets the text of the StoryFragment
     *
     * @return Text of StoryFragment
     */
	public String getStoryText() {
		return storyText;
	}

    /**
     * Sets the text of the StoryFragment
     *
     * @param storyText The text to set to
     */
	public void setStoryText(String storyText) {
		this.storyText = storyText;
	}

    /**
     * Gets the list of all Choices associated with the StoryFragment
     *
     * @return The list of all Choices
     */
	public ArrayList<Choice> getChoices() {
		return choices;
	}

    /**
     * Gets all Choices associated with the StoryFragment as a JSON string
     *
     * @return All the Choices as a JSON string
     */
	public String getChoicesInJson() {
		Gson gson = new Gson();
		return gson.toJson(choices);
	}

    /**
     * Sets the list of Choices to a new list
     *
     * @param choices The list of Choices to be set to
     */
	public void setChoices(ArrayList<Choice> choices) {
		this.choices = choices;
	}

    /**
     * Add a Choice to the list of Choices
     *
     * @param choice The Choice to add
     */
	public void addChoice(Choice choice) {
		choices.add(choice);
	}

    /**
     * Removes a Choice from the list of Choices
     *
     * @param choice The Choice to remove
     */
	public void removeChoice(Choice choice) {
		choices.remove(choice);
	}

    /**
     * Gets a Choice at a specific index in the list
     *
     * @param id The index of the Choice
     *
     * @return The Choice at that index
     */
	public Choice getChoice(int id) {
		return (Choice) choices.toArray()[id];
	}

	@Override
	public int compareTo(StoryFragment other) 
	{
		return this.fragmentID.compareTo(other.fragmentID);
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StoryFragment fragment = (StoryFragment) o;

        if (choices != null ? !choices.equals(fragment.choices) : fragment.choices != null) return false;
        if (!fragmentID.equals(fragment.fragmentID)) return false;
        if (!storyID.equals(fragment.storyID)) return false;
        if (storyMedia != null ? !storyMedia.equals(fragment.storyMedia) : fragment.storyMedia != null) return false;
        if (!storyText.equals(fragment.storyText)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = storyID.hashCode();
        result = 31 * result + fragmentID.hashCode();
        result = 31 * result + (storyMedia != null ? storyMedia.hashCode() : 0);
        result = 31 * result + storyText.hashCode();
        result = 31 * result + (choices != null ? choices.hashCode() : 0);
        return result;
    }
}
