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

import java.util.Collection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import ca.cmput301f13t03.adventure_datetime.R;
import ca.cmput301f13t03.adventure_datetime.model.Bookmark;
import ca.cmput301f13t03.adventure_datetime.model.Interfaces.IBookmarkListListener;
import ca.cmput301f13t03.adventure_datetime.model.Interfaces.IStoryListListener;
import ca.cmput301f13t03.adventure_datetime.serviceLocator.Locator;

/**
 * The first and main activity of the application.
 * 
 * Contains three buttons:
 *  - ContinueView (list of bookmarks)
 *  - BrowseView (list of stories)
 *  - Author (create & edit stories)
 * 
 * @author James Finlay
 *
 */
public class MainView extends Activity implements IBookmarkListListener {
	private static final String TAG = "MainView";
	
	private Button _browseBookmarks, _browseStories, _authorBrowse;

	@Override
	public void OnBookmarkListChange(Collection<Bookmark> newBookmarks) {
		if (newBookmarks.size() > 0)
			_browseBookmarks.setVisibility(View.VISIBLE);
	}
	/** Called when activity is first created */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Locator.initializeLocator(getApplicationContext());
		
		Locator.initializeLocator(getApplicationContext());
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);		
		
		_browseBookmarks = (Button) findViewById(R.id.btn_browseBookmarks);
		_browseBookmarks.setVisibility(View.INVISIBLE);
		_browseBookmarks.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainView.this, ContinueView.class);
				startActivity(intent);
			}
		});
		
		_browseStories = (Button) findViewById(R.id.btn_browseStories);
		_browseStories.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainView.this, BrowseView.class);
				startActivity(intent);
			}
		});
		
		_authorBrowse = (Button) findViewById(R.id.btn_authorList);
		_authorBrowse.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainView.this, AuthorStories.class);
				startActivity(intent);
			}
		});
			
	}
	
	@Override
	public void onResume() {
		Locator.getPresenter().Subscribe((IBookmarkListListener)this);
		super.onResume();
	}
	@Override
	public void onPause() {
		Locator.getPresenter().Unsubscribe((IBookmarkListListener)this);
		super.onPause();
	}
}
