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

import java.util.ArrayList;
import java.util.Collection;

import android.net.Uri;

public class Story {

	private long headFragmentId;
	private long id;
	private String title;
	private String synopsis;
	private Uri thumbnail;
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

	public Uri getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String uri) {
		this.thumbnail = new Uri.Builder().path(uri).build();
	}

	public void setThumbnail(Uri uri) {
		this.thumbnail = uri;
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

}