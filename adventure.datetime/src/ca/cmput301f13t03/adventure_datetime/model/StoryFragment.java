package ca.cmput301f13t03.adventure_datetime.model;

import android.database.Cursor;

import java.util.Collection;

public class StoryFragment {

	private long storyID;
	private long fragmentID;
	private Collection<String> storyMedia;
	private String storyText;
	private Collection<Choice> choices;

	public StoryFragment(Cursor cursor) {
		storyID = cursor.getLong(cursor.getColumnIndex(StoryDB.STORYFRAGMENT_COLUMN_STORYID));
		fragmentID = cursor.getLong(cursor.getColumnIndex(StoryDB._ID));
		storyText = cursor.getString(cursor.getColumnIndex(StoryDB.STORYFRAGMENT_COLUMN_CONTENT));
		/* TODO figure out JSON serialization for choices and media */
	}

	public long getStoryID() {
		return storyID;
	}

	public void setStoryID(long storyID) {
		this.storyID = storyID;
	}

	public long getFragmentID() {
		return fragmentID;
	}

	public void setFragmentID(long fragmentID) {
		this.fragmentID = fragmentID;
	}

	public Collection<String> getStoryMedia() {
		return storyMedia;
	}

	public void setStoryMedia(Collection<String> storyMedia) {
		this.storyMedia = storyMedia;
	}

	public void addMedia(String media) {
		storyMedia.add(media);
	}

	public void removeMedia(String media) {
		storyMedia.remove(media);
	}

	public String getMedia(int id) {
		return (String) storyMedia.toArray()[id];
	}

	public String getStoryText() {
		return storyText;
	}

	public void setStoryText(String storyText) {
		this.storyText = storyText;
	}

	public Collection<Choice> getChoices() {
		return choices;
	}

	public void setChoices(Collection<Choice> choices) {
		this.choices = choices;
	}

	public void addChoice(Choice choice) {
		choices.add(choice);
	}

	public void removeChoice(Choice choice) {
		choices.remove(choice);
	}

	public Choice getChoice(int id) {
		return (Choice) choices.toArray()[id];
	}
}
