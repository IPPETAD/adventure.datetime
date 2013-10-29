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

/**
 * @author Andrew Fontaine
 * @version 1.0
 * @since 28/10/13
 */
public class StoryDB implements BaseColumns {

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

	public Story getStory(long id) {
		SQLiteDatabase db = mDbHelper.getReadableDatabase();

		Cursor cursor = db.query(STORY_TABLE_NAME,
				new String[] {STORY_COLUMN_TITLE, STORY_COLUMN_AUTHOR, STORY_COLUMN_HEAD_FRAGMENT, STORY_COLUMN_SYNOPSIS,
						STORY_COLUMN_TIMESTAMP, STORY_COLUMN_THUMBNAIL},
				_ID + " = ?",
				new String[] {String.valueOf(id)},
				null,
				null,
				null,
				"1");

		if (cursor.moveToFirst()) {
			Story story = new Story(cursor);
			cursor.close();
			db.close();
			return story;
		}
		else
			return null;
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
