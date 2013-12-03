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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class Comment {

	private UUID _id;
	private UUID targetId;
	private String author;
	private String content;
	private Long timestamp;
	
	private transient Image image;
	
	/**
	 * Construct a comment object with a random ID
	 * and timestamp set to now.
	 */
	public Comment() {
		this._id = UUID.randomUUID();
		this.timestamp = System.currentTimeMillis();
	}
	
	/**
	 * Construct a Comment object with a new ID and populated fields
	 * @param targetId the targetId
	 * @param author the Author
	 * @param content the Content
	 */
	public Comment(UUID targetId, String author, String content) {
		this(); 
		this.targetId = targetId;
		this.author = author;
		this.content = content;
	}
	
	/**
	 * Construct a Comment object with populated fields
	 * @param webId the webId
	 * @param targetId the targetId
	 * @param author the Author
	 * @param content the Content
	 */
	public Comment(UUID id, UUID targetId, String author, String content) {
		this(targetId, author, content);
		this._id = id;
	}

	public UUID getId() {
		return _id;
	}

	public void setId(UUID id) {
		this._id = id;
	}

	public UUID getTargetId() {
		return targetId;
	}

	public void setTargetId(UUID targetId) {
		this.targetId = targetId;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	/**
	 * Returns the decoded bitmap from the image.
	 * Or null if image is null.
	 * @return Decoded image or null if no image.
	 */
	public Bitmap decodeImage() {
		return image == null ? null : image.decodeBitmap();
	}
	
	/**
	 * Set the bitmap to the internal image object
	 * @param bitmap the bitmap to set
	 */
	public void setImage(String bitmap) {
		if (bitmap == null)
			this.image = null;
		else if (this.image == null)
			this.image = new Image(this.getId(), bitmap);
		else
			this.image.setBitmap(bitmap);
	}
	
	/**
	 * Set the bitmap to the internal image object
	 * @param bitmap the bitmap to set
	 */
	public void setImage(Bitmap bitmap) {
		if (bitmap == null)
			this.image = null;
		else if (this.image == null)
			this.image = new Image(this.getId(), bitmap);
		else
			this.image.setBitmap(bitmap);
	}
	
	public Image getImage() {
		return image;
	}
	
    /**
     * Gets a formatted timestamp string
     *
     * @return Timestamp in a DateTime format
     */
	public String getFormattedTimestamp() {
		DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timestamp);
		return dateFormat.format(cal.getTime());
	}
	
	public void updateTimestamp() {
		this.timestamp = System.currentTimeMillis();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj == null || obj.getClass() != this.getClass())
			return false;
		
		Comment c = (Comment) obj;
		
		return _id == null ? c._id == null : _id.equals(c._id)
			&& targetId == null ? c.targetId == null : targetId.equals(c.targetId)
			&& author == null ? c.author == null : author.equals(c.author)
			&& content == null ? c.content == null : content.equals(c.content);
	}
}
