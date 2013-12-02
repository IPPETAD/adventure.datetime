package ca.cmput301f13t03.adventure_datetime.model.Interfaces;

import ca.cmput301f13t03.adventure_datetime.model.Comment;

import java.util.List;

public interface ICommentsListener {
	
	void OnCommentsChange(List<Comment> newComments);

}
