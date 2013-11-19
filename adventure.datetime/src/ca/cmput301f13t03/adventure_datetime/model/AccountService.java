package ca.cmput301f13t03.adventure_datetime.model;

import android.app.Activity;
import android.database.Cursor;
import android.provider.ContactsContract;

public class AccountService {
	
	private static final String[] projection = { ContactsContract.Contacts.DISPLAY_NAME }; 
	
	private static String userName;
	
	private AccountService() {	}
	
	public static String getUserName(Activity context) {
		if (userName == null) {
			setUserName(context);
		}
		
		return userName;
	}
	
	private static void setUserName(Activity context) {
		Cursor c = context.getContentResolver().query(
				ContactsContract.Profile.CONTENT_URI, // Query only the user's profile 
				projection, // get display name
				null, // get everything
				null, // get everything
				null  // 1 item doesn't need ordering
				);
		
		if (c.moveToFirst()) {
			userName = c.getString(0);
		}
		
		c.close();
	}
	
}
