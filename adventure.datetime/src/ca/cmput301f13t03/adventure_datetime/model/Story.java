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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

public class Story {

	private String headFragmentId; /** The GUID of the head fragment of the Story */
	private String id; /** The GUID of the story, -1 if there is no _ID */
	private long timestamp; /** The UNIX time of the last time the Story was updated/downloaded */
	private String author; /** The author of the Story */
	private String title; /** The title of the Story */
	private String synopsis; /** The synopsis of the Story */
	private Bitmap thumbnail; /** The bitmap image of the Story */
	private Collection<String> tags; /** A collection of Tags for the Story */
	private HashSet<String> fragmentIDs; /** The collection of fragment _GUIDs attached to the story */

	public Story(String headFragmentId, String id, String author, long timestamp, String synopsis,
	             Bitmap thumbnail, String title) {
		this.headFragmentId = headFragmentId;
		this.id = id;
		this.author = author;
		this.timestamp = timestamp;
		this.synopsis = synopsis;
		this.thumbnail = thumbnail;
		this.title = title;
		fragmentIDs = new HashSet<String>();
		fragmentIDs.add(headFragmentId);
	}

	public Story(String author, String title, String synopsis) {
		this.author = author;
		this.title = title;
		this.synopsis = synopsis;
		this.thumbnail = Bitmap.createBitmap(50, 50, Bitmap.Config.RGB_565); /* for testing purposes */
		id = UUID.randomUUID().toString();
		headFragmentId = new UUID(0,0).toString();
		fragmentIDs = new HashSet<String>();
		tags = new ArrayList<String>();
		tags.add("new");
		timestamp = System.currentTimeMillis() / 1000L;
	}

	public Story() {
		id = UUID.randomUUID().toString();
		title = "";
		headFragmentId = new UUID(0,0).toString();
		fragmentIDs = new HashSet<String>();
		tags = new ArrayList<String>();
		tags.add("new");
		timestamp = System.currentTimeMillis() / 1000L;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
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

	public String getHeadFragmentId() {
		return headFragmentId;
	}

	public void setHeadFragmentId(String headFragmentId) {
		this.headFragmentId = headFragmentId;
	}

	public void setHeadFragmentId(StoryFragment frag) {
		this.headFragmentId = frag.getFragmentID();
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

	public void addFragment(String id) {
		this.fragmentIDs.add(id);
	}

	public void addFragment(StoryFragment frag) {
		this.fragmentIDs.add(frag.getFragmentID());
	}

	public boolean removeFragment(String id) {
		return this.fragmentIDs.remove(id);
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
