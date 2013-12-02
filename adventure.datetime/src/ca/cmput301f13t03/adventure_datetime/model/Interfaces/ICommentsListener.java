package ca.cmput301f13t03.adventure_datetime.model.Interfaces;

import java.util.List;

import ca.cmput301f13t03.adventure_datetime.model.Comment;

public interface ICommentsListener {
	
	void OnCommentsChange(List<Comment> newComments);

}
