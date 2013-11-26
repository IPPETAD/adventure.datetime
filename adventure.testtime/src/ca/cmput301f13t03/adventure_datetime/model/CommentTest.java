package ca.cmput301f13t03.adventure_datetime.model;

import java.util.UUID;

import junit.framework.TestCase;

import com.google.gson.Gson;

public class CommentTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testJson() {
		Comment comment = new Comment(UUID.randomUUID(), UUID.randomUUID(), "Bad Author", "Bad Content");
		Gson gson = new Gson();
		
		String json = gson.toJson(comment);
		Comment comment2 = gson.fromJson(json, Comment.class);
		
		assertEquals(comment, comment2);
		assertEquals(comment.getFormattedTimestamp(), comment2.getFormattedTimestamp());
		
	}

}
