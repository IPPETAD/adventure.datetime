package ca.cmput301f13t03.adventure_datetime.model;

import java.util.UUID;

import android.graphics.BitmapFactory;
import android.test.AndroidTestCase;
import ca.cmput301f13t03.adventure_datetime.R;

import com.google.gson.Gson;

public class CommentTest extends AndroidTestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testJson() {
		Comment comment = new Comment(UUID.randomUUID(), "Bad Author", "Bad Content");
		comment.setImage(new Image(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.grumpy_cat)));
		assertNotNull(comment.getImageId());
		Gson gson = new Gson();
		
		String json = gson.toJson(comment);
		Comment comment2 = gson.fromJson(json, Comment.class);
		
		assertEquals(comment, comment2);
		assertEquals(comment.getFormattedTimestamp(), comment2.getFormattedTimestamp());
		assertNull(comment2.getImage());
		
	}

}
