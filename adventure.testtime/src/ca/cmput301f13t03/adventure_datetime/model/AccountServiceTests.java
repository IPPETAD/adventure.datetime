package ca.cmput301f13t03.adventure_datetime.model;

import android.test.AndroidTestCase;
import android.util.Log;

public class AccountServiceTests extends AndroidTestCase {

	public void testGetUserName() {
		String userName = AccountService.getUserName(getContext().getContentResolver());
		Log.d("TEST", "User name found: " + userName);
		assertNotNull(userName);
	}

}
