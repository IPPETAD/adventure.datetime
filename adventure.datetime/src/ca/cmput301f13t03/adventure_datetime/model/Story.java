/*
 *  Copyright (c) 2013 Andrew Fontaine, James Finlay, Jesse Tucker, Jacob Viau, and
 *  Evan DeGraff
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of
 *  this software and associated documentation files (the "Software"), to deal in
 *  the Software without restriction, including without limitation the rights to
 *  use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 *  the Software, and to permit persons to whom the Software is furnished to do so,
 *  subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 *  FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 *  COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 *  IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package ca.cmput301f13t03.adventure_datetime.model;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.util.ArrayList;
import java.util.Collection;

public class Story {

	private long headFragmentId;
	private long id;
	private long timestamp;
	private String author;
	private String title;
	private String synopsis;
	private Bitmap thumbnail;
	private Collection<String> tags;
	private Collection<Long> fragmentIDs;

	public Story() {
		id = -1;
		title = "";
		headFragmentId = -1;
		fragmentIDs = new ArrayList<Long>();
		fragmentIDs.add(headFragmentId);
		tags = new ArrayList<String>();
		tags.add("new");
		timestamp = System.currentTimeMillis() / 1000L;
	}

	public Story(Cursor cursor) {
		id = cursor.getInt(cursor.getColumnIndex(StoryDB._ID));
		title = cursor.getString(cursor.getColumnIndex(StoryDB.STORY_COLUMN_TITLE));
		headFragmentId = cursor.getInt(cursor.getColumnIndex(StoryDB.STORY_COLUMN_HEAD_FRAGMENT));
		fragmentIDs = new ArrayList<Long>();
		fragmentIDs.add(headFragmentId);
		author = cursor.getString(cursor.getColumnIndex(StoryDB.STORY_COLUMN_AUTHOR));
		synopsis = cursor.getString(cursor.getColumnIndex(StoryDB.STORY_COLUMN_SYNOPSIS));
		byte[] thumb = cursor.getBlob(cursor.getColumnIndex(StoryDB.STORY_COLUMN_THUMBNAIL));
		thumbnail = BitmapFactory.decodeByteArray(thumb, 0, thumb.length);
		timestamp = cursor.getLong(cursor.getColumnIndex(StoryDB.STORY_COLUMN_THUMBNAIL));
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public long getHeadFragmentId() {
		return headFragmentId;
	}

	public void setHeadFragmentId(long headFragmentId) {
		this.headFragmentId = headFragmentId;
	}

	public Bitmap getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String bitmap) {
		this.thumbnail = BitmapFactory.decodeFile(bitmap);
	}

	public void setThumbnail(Bitmap bitmap) {
		this.thumbnail = bitmap;
	}

	public void addTag(String tag) {
		this.tags.add(tag);
	}

	public void removeTag(String tag) {
		this.tags.remove(tag);
	}

	public void addFragment(Long id) {
		this.fragmentIDs.add(id);
	}

	public void removeFragment(Long id) {
		this.fragmentIDs.remove(id);
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
}