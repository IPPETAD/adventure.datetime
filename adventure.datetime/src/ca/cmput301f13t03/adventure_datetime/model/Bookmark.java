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

package ca.cmput301f13t03.adventure_datetime.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * A Bookmark pointing to a Story and Fragment to continue where the User left off
 * @author Andrew Fontaine
 * @version 1.0
 * @since 23/10/13
 */
public class Bookmark {

	/**
	 * The UUID of the fragment the Bookmark points to
	 */
	private UUID fragmentID;
	/**
	 * The UUID of the story the Bookmark points to
	 */
	private UUID storyID;
	/**
	 * The date of creation of the Bookmark
	 */
	private Date date;

    /**
     * Creates a new bookmark with the given Story, StoryFragment, and current time
     * @param storyID UUID of the Story
     * @param fragmentID UUID of the StoryFragment
     */
	public Bookmark(UUID storyID, UUID fragmentID) {
		this.fragmentID = fragmentID;
		this.storyID = storyID;
		this.date = Calendar.getInstance().getTime();
	}

    /**
     * Creates a new bookmark with the given Story, StoryFragment, and Date
     * @param fragmentID UUID of the StoryFragment
     * @param storyID UUID of the Story
     * @param date Date that the bookmark was originally set
     */
	public Bookmark(UUID fragmentID, UUID storyID, Date date) {
		this.fragmentID = fragmentID;
		this.storyID = storyID;
		this.date = date;
	}

    /**
     * Gets the UUID of the StoryFragment
     *
     * @return UUID of the fragment
     */
	public UUID getFragmentID() {
		return fragmentID;
	}

    /**
     * Sets the UUID of the StoryFragment
     *
     * @param fragmentID StoryFragment the Bookmark points to
     */
	public void setFragmentID(UUID fragmentID) {
		this.fragmentID = fragmentID;
	}

    /**
     * Gets the Date associated with the Bookmark
     *
     * @return Date the bookmark was set
     */
	public Date getDate() {
		return date;
	}

    /**
     * Sets a new date for the Bookmark
     *
     * @param date The Date the bookmark will be set to
     */
	public void setDate(Date date) {
		this.date = date;
	}

    /**
     * Gets the UUID of the Story the Bookmark points to.
     *
     * @return UUID of the Story
     */
	public UUID getStoryID() {

		return storyID;
	}

    /**
     * Sets the UUID the Bookmark points to
     *
     * @param storyID UUID of Story to point to
     */
	public void setStoryID(UUID storyID) {
		this.storyID = storyID;
	}

    /**
     * Creates a formatted string for the Date associated with the Bookmark
     *
     * @return The date, formatted as "mm/dd/yyyy"
     */
	public String getFormattedTimestamp() {
		SimpleDateFormat format = new SimpleDateFormat("mm/dd/yyyy");
		return format.format(date);
	}
}
