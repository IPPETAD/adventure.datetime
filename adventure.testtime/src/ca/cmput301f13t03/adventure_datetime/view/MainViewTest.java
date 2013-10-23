package ca.cmput301f13t03.adventure_datetime.view;

import android.test.ActivityInstrumentationTestCase2;

/**
 * This is a simple framework for a test of an Application.  See {@link android.test.ApplicationTestCase
 * ApplicationTestCase} for more information on how to write and extend Application tests.
 * <p/>
 * To run this test, you can type: adb shell am instrument -w \ -e class ca.cmput301f13t03.adventure_datetime.view.MainViewTest
 * \ ca.cmput301f13t03.adventure_datetime.tests/android.test.InstrumentationTestRunner
 */
public class MainViewTest extends ActivityInstrumentationTestCase2<MainView> {

	public MainViewTest() {
		super("ca.cmput301f13t03.adventure_datetime", MainView.class);
	}

}
