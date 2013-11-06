package ca.cmput301f13t03.adventure_datetime.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import junit.framework.TestCase;

public class WebStorageTest extends TestCase {

	WebStorage es;
	
	protected void setUp() throws Exception {
		super.setUp();
		es = new WebStorage();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testDeleteComment() {
		Comment c = new Comment();
		c.setAuthor("lolcat");
		c.setContent("Can I haz ID?");
		c.setTargetId(UUID.randomUUID());
		c.setWebId(UUID.randomUUID());
		
		try {
			es.putComment(c);
		}
		catch (Exception ex) {
			fail("Exception return: " + es.getErrorMessage());
			ex.printStackTrace();
		}
		
		System.out.println("Comment ID: " + c.getWebId());
		
		try {
			es.deleteComment(c.getWebId());
		}
		catch (Exception ex) {
			ex.printStackTrace();
			fail("Exception return: " + es.getErrorMessage());
		}
	}
	
	public void testGetComments() {
		List<Comment> comments = new ArrayList<Comment>();
		List<Comment> returned = new ArrayList<Comment>(); // so it compiles
		UUID targetId = UUID.randomUUID();
		
		for (int i = 0; i < 5; i++) {
			Comment c = new Comment();
			c.setAuthor("Pretentious Douchebag " + i);
			c.setContent("This test sucks. 0/5 would not test again.");
			c.setTargetId(targetId);
			c.setWebId(UUID.randomUUID());
			comments.add(c);
		}
		
		String state = "";
		
		try {
			for (Comment c : comments) {
				state = "putting " + c.getAuthor();
				es.putComment(c);
			}
			state = "getting comments";
			returned = es.getComments(targetId);		
		}
		catch (Exception ex) {
			ex.printStackTrace();
			fail(String.format("Exception from client during %s: %s", state, es.getErrorMessage()));
		}
		
		assertEquals("Lists different size!", comments.size(), returned.size());
		Set<UUID> ids = new HashSet<UUID>();
		
		for (Comment c : comments) {
			ids.add(c.getWebId());
		}
		
		for (Comment c : returned) {
			assertTrue(ids.contains(c.getWebId()));
		}
		
	}
}
