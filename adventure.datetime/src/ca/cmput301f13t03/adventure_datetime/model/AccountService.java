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
	 * @param cr, for querying the user's contacts profile.
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
