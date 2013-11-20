package ca.cmput301f13t03.adventure_datetime.model.Interfaces;

import java.util.Map;

import ca.cmput301f13t03.adventure_datetime.model.Comment;

public interface ICommentsListener {
	
	void OnCommentsChange(Map<String, Comment> newComments);

}
