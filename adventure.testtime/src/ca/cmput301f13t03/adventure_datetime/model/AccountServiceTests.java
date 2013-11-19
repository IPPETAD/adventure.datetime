package ca.cmput301f13t03.adventure_datetime.model;

import junit.framework.TestCase;
import android.util.Log;
import ca.cmput301f13t03.adventure_datetime.view.MainView;

public class AccountServiceTests extends TestCase {

	public void testGetUserName() {
		// Does not work! MainView.getContentResoler() is null.
		// Works in production though.
		String userName = AccountService.getUserName(new MainView());
		Log.i("TEST", "User name found: " + userName);
		assertNotNull(userName);
	}

}
