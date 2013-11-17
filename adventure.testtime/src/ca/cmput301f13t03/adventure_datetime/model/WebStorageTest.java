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

	public void testDeleteComment() throws Exception {
		Comment c = new Comment();
		c.setAuthor("lolcat");
		c.setContent("Can I haz ID?");
		c.setTargetId(UUID.randomUUID());
		c.setWebId(UUID.randomUUID());
		
		boolean result = es.putComment(c);
		assertTrue(es.getErrorMessage(), result);
		
		System.out.println("Comment ID: " + c.getWebId());
		
		result = es.deleteComment(c.getWebId());
		assertTrue(es.getErrorMessage(), result);
	}
	
	public void testGetComments() throws Exception {
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

		for (Comment c : comments) {
			boolean result = es.putComment(c);
			assertTrue(es.getErrorMessage(), result);
		}
		
		returned = es.getComments(targetId);
		
		assertEquals("Lists different size!, " + es.getErrorMessage(), comments.size(), returned.size());
		Set<UUID> ids = new HashSet<UUID>();
		
		for (Comment c : comments) {
			ids.add(c.getWebId());
		}
		
		for (Comment c : returned) {
			assertTrue("Id found in returned, but not in original", ids.contains(c.getWebId()));
		}
		
	}

}
