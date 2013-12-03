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

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;

/**
 * Class for interacting with a user's account
 */
public class AccountService {
	
	private static final String[] projection = { ContactsContract.Contacts.DISPLAY_NAME }; 
	
	private static String userName;
	
	private AccountService() {	}
	
	/**
	 * Returns a users display name from their contacts profile.
	 * @param cr for querying the user's contacts profile.
	 * @return the display name from the contacts profile
	 */
	public static String getUserName(ContentResolver cr) {
		// TODO: Should this remain static?
		
		if (userName == null) {
			setUserName(cr);
		}
		
		return userName != null ? userName : "no_name";
	}
	
	private static void setUserName(ContentResolver cr) {
		Cursor c = cr.query(
				ContactsContract.Profile.CONTENT_URI, // Query only the user's profile 
				projection, // get display name
				null, // get everything
				null, // get everything
				null  // 1 item doesn't need ordering
				);
		
		if (c.moveToFirst()) {
			userName = c.getString(c.getColumnIndex(projection[0]));
		}
		
		c.close();
	}
	
}
