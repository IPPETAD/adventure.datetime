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

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Andrew Fontaine
 * @version 1.0
 * @since 28/10/13
 */
public class StoryDB implements BaseColumns, IAuthorStorage, IReaderStorage {

	public static final String STORY_TABLE_NAME = "Story";
	public static final String STORY_COLUMN_AUTHOR = "Author";
	public static final String STORY_COLUMN_HEAD_FRAGMENT = "HeadFragment";
	public static final String STORY_COLUMN_TIMESTAMP = "Timestamp";
	public static final String STORY_COLUMN_SYNOPSIS = "Synopsis";
	public static final String STORY_COLUMN_THUMBNAIL = "Thumbnail";
	public static final String STORY_COLUMN_TITLE = "Title";

	public static final String STORYFRAGMENT_TABLE_NAME = "StoryFragment";
	public static final String STORYFRAGMENT_COLUMN_STORYID = "StoryID";
	public static final String STORYFRAGMENT_COLUMN_CONTENT = "Content";
	public static final String STORYFRAGMENT_COLUMN_CHOICES = "Choices";

	private StoryDBHelper mDbHelper;

	public StoryDB(Context context) {
		mDbHelper = new StoryDBHelper(context);
	}

	/**
	 * Grabs a story with the given id from the local database
	 *
	 * @param storyId The _ID of the story
	 *
	 * @return The Story object or null if the id doesn't exist in the database
	 */
	@Override
	public Story getStory(long storyId) {
		SQLiteDatabase db = mDbHelper.getReadableDatabase();

		Cursor cursor = db.query(STORY_TABLE_NAME,
				new String[] {_ID, STORY_COLUMN_TITLE, STORY_COLUMN_AUTHOR, STORY_COLUMN_HEAD_FRAGMENT, STORY_COLUMN_SYNOPSIS,
						STORY_COLUMN_TIMESTAMP, STORY_COLUMN_THUMBNAIL},
				_ID + " = ?",
				new String[] {String.valueOf(storyId)},
				null,
				null,
				null,
				"1");

		Story story;

		if (cursor.moveToFirst()) {
			story = new Story(cursor);
		}
		else
			story = null;

		cursor.close();
		db.close();
		return story;
	}

	/**
	 * Retrieves all stories located on local storage
	 *
	 * @return Collection of all stories on local storage. Collection is empty if there are no stories
	 */
	@Override
	public Collection<Story> getAllStories() {
		SQLiteDatabase db = mDbHelper.getReadableDatabase();

		Cursor cursor = db.query(STORY_TABLE_NAME,
				new String[] {_ID, STORY_COLUMN_TITLE, STORY_COLUMN_AUTHOR, STORY_COLUMN_HEAD_FRAGMENT, STORY_COLUMN_SYNOPSIS,
						STORY_COLUMN_TIMESTAMP, STORY_COLUMN_THUMBNAIL},
				null,
				null,
				null,
				null,
				null);
		Collection<Story> stories = new ArrayList<Story>();

		cursor.moveToFirst();
		do {
			stories.add(new Story(cursor));
		} while(cursor.moveToNext());

		cursor.close();
		db.close();
		return stories;
	}

	/**
	 * Retrieves all stories located on local storage by an author
	 *
	 * @return Collection of all stories on local storage by an author. Collection is empty if there are no stories
	 * by author.
	 */
	public Collection<Story> getStoriesAuthoredBy(String author) {
		SQLiteDatabase db = mDbHelper.getReadableDatabase();

		Cursor cursor = db.query(STORY_TABLE_NAME,
				new String[] {_ID, STORY_COLUMN_TITLE, STORY_COLUMN_AUTHOR, STORY_COLUMN_HEAD_FRAGMENT, STORY_COLUMN_SYNOPSIS,
						STORY_COLUMN_TIMESTAMP, STORY_COLUMN_THUMBNAIL},
				STORY_COLUMN_AUTHOR + " = ?",
				new String[] {author},
				null,
				null,
				null);
		Collection<Story> stories = new ArrayList<Story>();

		cursor.moveToFirst();
		do {
			stories.add(new Story(cursor));
		} while(cursor.moveToNext());

		cursor.close();
		db.close();
		return stories;
	}

	/**
	 * Retrieves story fragments by specific ID from local storage
	 *
	 * @param fragmentId The _ID of the fragment
	 *
	 * @param storyId the _ID of the story
	 *
	 * @return StoryFragment instance or null
	 */
	@Override
	public StoryFragment getFragment(long storyId, long fragmentId) {
		SQLiteDatabase db = mDbHelper.getReadableDatabase();

		Cursor cursor = db.query(STORYFRAGMENT_TABLE_NAME,
				new String[] {_ID, STORYFRAGMENT_COLUMN_STORYID, STORYFRAGMENT_COLUMN_CHOICES, STORYFRAGMENT_COLUMN_CONTENT},
				_ID + " = ?, " + STORYFRAGMENT_COLUMN_STORYID + " = ?",
				new String[] {String.valueOf(fragmentId), String.valueOf(storyId)},
				null,
				null,
				"1");

		StoryFragment frag;
		if(cursor.moveToFirst())
			frag = new StoryFragment(cursor);
		else
			frag = null;
		cursor.close();
		db.close();
		return frag;
	}

	/**
	 * Retrieves all StoryFragments for a particular Story's _ID
	 *
	 * @param storyid The _ID for a story
	 *
	 * @return A Collection of StoryFragments. Empty if none exist.
	 */
	@Override
	public Collection<StoryFragment> getAllFragmentsForStory(long storyid) {
		SQLiteDatabase db = mDbHelper.getReadableDatabase();

		Cursor cursor = db.query(STORYFRAGMENT_TABLE_NAME,
				new String[] {_ID, STORYFRAGMENT_COLUMN_STORYID, STORYFRAGMENT_COLUMN_CHOICES, STORYFRAGMENT_COLUMN_CONTENT},
				STORYFRAGMENT_COLUMN_STORYID + " = ?",
				new String[] {String.valueOf(storyid)},
				null,
				null,
				null);

		Collection<StoryFragment> fragments = new ArrayList<StoryFragment>();

		cursor.moveToFirst();
		do {
			fragments.add(new StoryFragment(cursor));
		} while(cursor.moveToNext());

		cursor.close();
		db.close();
		return fragments;
	}

	/**
	 * Subscribe to a story to receive updates
	 *
	 * @param storyId the story id
	 *
	 * @return True if subscribed, false if it fails
	 */
	@Override
	public boolean subsribe(long storyId) {
		return false;  //TODO Implement
	}

	/**
	 * Unsubscribe from a story
	 *
	 * @param storyId the story id
	 *
	 * @return True if unsubscribed, false otherwise
	 */
	@Override
	public boolean unsubscribe(long storyId) {
		return false;  //TODO Implement
	}

	/**
	 * Put a story in local storage
	 *
	 * @param storyId the story id
	 *
	 * @return True is saved locally, false otherwise
	 */
	@Override
	public boolean saveStoryLocally(long storyId) {
		return false;  //TODO Implement
	}

	/**
	 * Deletes a story from local storage
	 *
	 * @param storyId the story to delete
	 *
	 * @return True if story deleted, false otherwise
	 */
	@Override
	public boolean deleteStoryLocally(long storyId) {
		return false;  //TODO Implement
	}

	/**
	 * Gets a collection of comments for a given story fragment
	 *
	 * @param storyId    the story id
	 * @param fragmentId the fragment id
	 *
	 * @return A collection of Comment objects
	 */
	@Override
	public Collection<Comment> getComments(long storyId, long fragmentId) {
		return null;  //TODO Implement
	}

	/**
	 * Comment on a story
	 *
	 * @param storyId    the story to comment on
	 * @param fragmentId the fragment to comment on
	 * @param comment    the comment
	 *
	 * @return True if comment successful, false otherwise
	 */
	@Override
	public boolean comment(long storyId, long fragmentId, Comment comment) {
		return false;  //TODO Implement
	}

	/**
	 * Delete a comment from a story fragment
	 *
	 * @param storyId    the story id
	 * @param fragmentId the fragment id
	 * @param commentId  the comment id
	 *
	 * @return True if comment deleted, false otherwise
	 */
	@Override
	public boolean deleteComment(long storyId, long fragmentId, long commentId) {
		return false;  //TODO Implement
	}

	/**
	 * Publish a story to the web. May be an insert or update. Must be the author in case of an update.
	 *
	 * @param story     the story
	 * @param fragments the fragments for the story
	 *
	 * @return the ID of the story published
	 */
	@Override
	public int publishStory(Story story, Collection<StoryFragment> fragments) {
		return 0;  //TODO Implement
	}

	/**
	 * Remove a story from the web service. Must be the author of the story
	 *
	 * @param storyId the story id
	 *
	 * @return True if story deleted, false otherwise
	 */
	@Override
	public boolean unpublishStory(long storyId) {
		return false;  //TODO Implement
	}

	/**
	 * Save a story to local storage. Does not publish the story
	 *
	 * @param story     the story
	 * @param fragments the fragments for the story
	 *
	 * @return True if saved, false otherwise
	 */
	@Override
	public boolean saveStory(Story story, Collection<StoryFragment> fragments) {
		return false;  //TODO Implement
	}


	public class StoryDBHelper extends SQLiteOpenHelper {

		public static final int DATABASE_VERSION = 1;
		public static final String DATABASE_NAME = "adventure.database";

		private static final String CREATE_STORY_TABLE =
				"CREATE TABLE " + STORY_TABLE_NAME + " ("
				+ _ID + " INTEGER PRIMARY KEY, "
				+ STORY_COLUMN_TITLE + " TEXT, "
				+ STORY_COLUMN_AUTHOR + " TEXT, "
				+ STORY_COLUMN_SYNOPSIS + "TEXT, "
				+ STORY_COLUMN_HEAD_FRAGMENT + " INTEGER, "
				+ STORY_COLUMN_TIMESTAMP + " INTEGER, "
				+ STORY_COLUMN_HEAD_FRAGMENT + " INTEGER, "
				+ STORY_COLUMN_THUMBNAIL + " BLOB, "
				+ "FOREIGN KEY(" + STORY_COLUMN_HEAD_FRAGMENT
				+ ") REFERENCES " + STORYFRAGMENT_TABLE_NAME
				+ "(" +  _ID + ") )";

		private static final String CREATE_STORYFRAGMENT_TABLE =
				"CREATE TABLE " + STORYFRAGMENT_TABLE_NAME + " ("
				+ _ID + " INTEGER PRIMARY KEY, "
				+ STORYFRAGMENT_COLUMN_STORYID + " INTEGER, "
				+ STORYFRAGMENT_COLUMN_CONTENT + " TEXT, "
				+ STORYFRAGMENT_COLUMN_CHOICES + " BLOB, "
				+ "FOREIGN KEY(" + STORYFRAGMENT_COLUMN_STORYID
				+ ") REFERENCES " + STORY_TABLE_NAME + "("
				+ _ID + "))";

		private static final String DELETE_STORY_TABLE =
				"DROP TABLE IF EXISTS " + STORY_TABLE_NAME;

		private static final String DELETE_STORYFRAGMENT_TABLE =
				"DROP TABLE IF EXISTS " + STORYFRAGMENT_TABLE_NAME;

		public StoryDBHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_STORY_TABLE);
			db.execSQL(CREATE_STORYFRAGMENT_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(DELETE_STORYFRAGMENT_TABLE);
			db.execSQL(DELETE_STORY_TABLE);
		}

		@Override
		public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			onUpgrade(db, oldVersion, newVersion);

		}
	}
}
