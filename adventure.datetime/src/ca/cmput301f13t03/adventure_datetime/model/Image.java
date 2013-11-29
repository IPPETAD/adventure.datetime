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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

public class Image {
	
	private UUID _id;
	private String encodedBitmap;
	private transient Bitmap bitmap;
	private transient boolean dirty;
	
	public Image(String bitmap) {
		this._id = UUID.randomUUID();
		this.encodedBitmap = bitmap;
	}
	
	public Image(Bitmap bitmap) {
		this._id = UUID.randomUUID();
		encodeBitmap(bitmap);
	}
	
	public Image(UUID id, String bitmap) {
		this(bitmap);
		this._id = id;
	}
	
	public Image(UUID id, Bitmap bitmap) {
		this(bitmap);
		this._id = id;
	}

	public UUID getId() {
		return _id;
	}
	
	public void setId(UUID id) {
		this._id = id;
	}
	
	public String getEncodedBitmap() {
		return encodedBitmap;
	}
	
	public void setBitmap(String bitmap) {
		this.dirty = true;
		this.encodedBitmap = bitmap;
	}
	
	public void setBitmap(Bitmap bitmap) {
		this.dirty = true;
		encodeBitmap(bitmap);
	}
	
	public Bitmap decodeBitmap() {
		if (dirty || bitmap == null) {
			byte[] decodedThumbnail = Base64.decode(this.encodedBitmap, 0);
			this.bitmap = BitmapFactory.decodeByteArray(decodedThumbnail, 0, decodedThumbnail.length);
			this.dirty = false;
		}
		
		return bitmap;
	}
	
	private void encodeBitmap(Bitmap bitmap) {
		Bitmap bitmapex = bitmap;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmapex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		this.encodedBitmap = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
		
		try {
			baos.close();
		} catch (IOException e) {
			Log.e("Story", "Error closing stream", e);
		}
	}
}
