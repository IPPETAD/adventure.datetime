package ca.cmput301f13t03.adventure_datetime.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import ca.cmput301f13t03.adventure_datetime.R;

import android.graphics.BitmapFactory;
import android.test.AndroidTestCase;

public class WebStorageTest extends AndroidTestCase {

	WebStorage es;
	
	protected void setUp() throws Exception {
		super.setUp();
		es = new WebStorage();
		es.setIndex("testing");
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
		List<Comment> returned = new ArrayList<Comment>();
		Set<UUID> ids = new HashSet<UUID>();
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
			ids.add(c.getWebId());
			assertTrue(es.getErrorMessage(), result);
		}
		
		// give elasticsearch some time to sort out its life issues
		Thread.sleep(4000);
		
		returned = es.getComments(targetId, 0, 10);
		
		assertEquals("Lists different size!, " + es.getErrorMessage(), comments.size(), returned.size());
		
		for (UUID id : ids) {
			try {
				es.deleteComment(id);
			}
			catch (Exception e) {
				
			}
		}
		
		for (Comment c : returned) {
			assertTrue("Id found in returned, but not in original", ids.contains(c.getWebId()));
		}
		
	}
	
	public void testGetAllStories() throws Exception {
		List<Story> stories = new ArrayList<Story>();
		for (int i = 0; i < 5; i++) {
			Story story = createStory(i);
			stories.add(story);
			boolean result = es.publishStory(story, null);
			assertTrue(es.getErrorMessage(), result);
		}
		
		//give elasticsearch some time, its a bit slow. In the head.
		Thread.sleep(4000);
		
		List<Story> result = es.getStories(0, 10);	
		
		for (Story s : stories) {
			es.deleteStory(s.getId());
		}
		
		for (Story s : stories) {
			assertTrue("Story missing from results", result.contains(s));
		}
		
	}
	
	private Story createStory(int i) {
		Story story = new Story();
		
		story.setAuthor("Bad Writer " + i);
		story.setSynopsis("Bad Synopsis " + i);
		story.setTitle("Bad Story " + i);
		story.setHeadFragmentId(UUID.randomUUID());
		story.setThumbnail(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.grumpy_cat));
	
		return story;
	}

}
