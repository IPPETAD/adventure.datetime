/*
 *	Copyright (c) 2013 Andrew Fontaine, James Finlay, Jesse Tucker, Jacob Viau, and
 * 	Evan DeGraff
 *
 * 	Permission is hereby granted, free of charge, to any person obtaining a copy of
 * 	this software and associated documentation files (the "Software"), to deal in
 * 	the Software without restriction, including without limitation the rights to
 * 	use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * 	the Software, and to permit persons to whom the Software is furnished to do so,
 * 	subject to the following conditions:
 *
 * 	The above copyright notice and this permission notice shall be included in all
 * 	copies or substantial portions of the Software.
 *
 * 	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * 	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * 	FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * 	COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * 	IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * 	CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package ca.cmput301f13t03.adventure_datetime.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import ca.cmput301f13t03.adventure_datetime.R;

/**
 * View Accessed via MainView > Continue > ~Select Item~ or via MainView > BrowseView > StoryDescription > ~Play /
 * Continue item ~
 * <p/>
 * Holds Horizontal filmstrip containing illustrations at top of page, story fragment text in the view, and an actions
 * buttons at the bottom of the page.
 *
 * @author James Finlay
 */
public class FragmentViewActivity extends FragmentActivity 
{
	public static final String FOR_SERVER = "emagherd.server";

	private boolean forServerEh;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_activity_view);
		forServerEh = getIntent().getBooleanExtra(FOR_SERVER, false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		if (forServerEh)
			getMenuInflater().inflate(R.menu.fragment_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
		{
		case R.id.action_comment:
			/* Open comments activity */
			Intent intent = new Intent(this, CommentsView.class);
			intent.putExtra(CommentsView.COMMENT_TYPE, false);
			startActivity(intent);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
