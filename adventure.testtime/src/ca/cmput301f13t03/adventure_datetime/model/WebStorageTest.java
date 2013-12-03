package ca.cmput301f13t03.adventure_datetime.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.AndroidTestCase;
import ca.cmput301f13t03.adventure_datetime.R;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WebStorageTest extends AndroidTestCase {

	WebStorage es;
	Bitmap bitmap;
	
	protected void setUp() throws Exception {
		super.setUp();
		es = new WebStorage();
		es.setIndex("testing");
		bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.grumpy_cat);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testDeleteComment() throws Exception {
		Comment c = new Comment();
		c.setAuthor("lolcat");
		c.setContent("Can I haz ID?");
		c.setTargetId(UUID.randomUUID());
		c.setId(UUID.randomUUID());
		
		boolean result = es.putComment(c);
		assertTrue(es.getErrorMessage(), result);
		
		System.out.println("Comment ID: " + c.getId());
		
		result = es.deleteComment(c.getId());
		assertTrue(es.getErrorMessage(), result);
	}
	
	public void testGetComments() throws Exception {
		List<Comment> comments = new ArrayList<Comment>();
		List<Comment> returned = new ArrayList<Comment>();
		UUID targetId = UUID.randomUUID();
		
		for (int i = 0; i < 5; i++) {
			comments.add(createComment(i, targetId));
		}

		for (Comment c : comments) {
			boolean result = es.putComment(c);
			assertTrue(es.getErrorMessage(), result);
		}
		
		// give elasticsearch some time to sort out its life issues
		Thread.sleep(4000);
		
		returned = es.getComments(targetId, 0, 10);
		
		assertEquals("Lists different size!, " + es.getErrorMessage(), comments.size(), returned.size());
		
		for (Comment c : returned) {
			try {
				es.deleteComment(c.getId());
			}
			catch (Exception e) {
				
			}
		}
		
		for (Comment c : comments) {
			assertTrue("Comment missing from results", returned.contains(c));
		}
		
		for (Comment c : returned) {
				assertEquals(c.getId(), c.getImage().getId());
				assertNotNull(c.getImage().getEncodedBitmap());
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
		
		// Give elasticsearch some time, its a bit slow. In the head.
		Thread.sleep(4000);
		
		List<Story> result = es.getStories(0, 10);	
		
		for (Story s : stories) {
			es.deleteStory(s.getId());
		}
		
		for (Story s : stories) {
			assertTrue("Story missing from results", result.contains(s));
		}
		
		for (Story s : result) {
			assertEquals(s.getId(), s.getThumbnail().getId());
			assertNotNull(s.getThumbnail().getEncodedBitmap());
		}
		
	}
	
	public void testQueryStories() throws Exception {
		List<Story> stories = new ArrayList<Story>();
		for (int i = 0; i < 5; i++) {
			Story story = createStory(i);
			stories.add(story);
		}
		
		stories.get(0).setAuthor("Andrew Fontaine");
		stories.get(1).setTitle("Thanks Andrew");
		stories.get(2).setSynopsis("How Andrew ruined civ.");
		
		for (Story s : stories) {
			boolean result = es.publishStory(s, null);
			assertTrue(es.getErrorMessage(), result);
		}
		
		// Give elasticsearch some time, it is having a mid life crisis
		// about if it wants to be a server anymore.
		Thread.sleep(4000);
		
		String filter = "Andrew Fontaine";
		List<Story> result = es.queryStories(filter, 0, 10);	
		
		for (Story s : stories) {
			try {
				es.deleteStory(s.getId());
			}
			catch (Exception e) {
				// keep on trucking
			}
		}
		
		assertTrue(result.size() >= 3);
		assertTrue("Story missing from results", result.contains(stories.get(0)));
		assertTrue("Story missing from results", result.contains(stories.get(1)));
		assertTrue("Story missing from results", result.contains(stories.get(2)));
		
		for (Story s : result) {
			assertEquals(s.getId(), s.getThumbnail().getId());
			assertNotNull(s.getThumbnail().getEncodedBitmap());
		}
	}
	
	public void testGetStoryFragments() throws Exception {
		Story story = createStory(0);
		List<StoryFragment> fragments = new ArrayList<StoryFragment>();
		fragments.add(createFragment(story.getId(), 0));
		story.setHeadFragmentId(fragments.get(0));
		
		for (int i = 1; i < 5; i++) {
			fragments.add(createFragment(story.getId(), i));
			story.addFragment(fragments.get(i));
		}
		
		boolean result = es.publishStory(story, fragments);
		assertTrue(es.getErrorMessage(), result);
		
		// Elasticsearch must deeply reflect on itself every time you post to it.
		// You know, evaluate what it means to be a web server. If it is okay
		// with us just pushing random objects into it. Maybe it feels violated?
		Thread.sleep(4000);
		
		Story story2 = es.getStory(story.getId());
		List<StoryFragment> fragments2 = es.getFragmentsForStory(story.getId(), 0, 10);
		
		try {
			es.deleteStory(story.getId());
		}
		catch (Exception e){}
		
		for (StoryFragment f : fragments) {
			deleteFragment(f);
		}
		
		assertEquals(es.getErrorMessage(), story, story2);
		assertNotNull(es.getErrorMessage(), fragments2);
				
		for (int i = 0; i < fragments.size(); i++) {
			assertEquals("Image missing", fragments.get(i).getMedia(0).getEncodedBitmap(), 
					fragments2.get(i).getMedia(0).getEncodedBitmap());
		}
	}
	
	private Story createStory(int i) {
		Story story = new Story();
		
		story.setAuthor("Bad Writer " + i);
		story.setSynopsis("Bad Synopsis " + i);
		story.setTitle("Bad Story " + i);
		story.setHeadFragmentId(UUID.randomUUID());
		story.setThumbnail(bitmap);
	
		return story;
	}
	
	private StoryFragment createFragment(UUID storyId, int i) {
		StoryFragment fragment = new StoryFragment(storyId, "Fragment " + i);
		fragment.addMedia(new Image(bitmap));
		
		return fragment;
	}
	
	private Comment createComment(int i, UUID targetId) {
		Comment comment = new Comment();
		comment.setTargetId(targetId);
		comment.setAuthor("Pretentious Douchebag " + i);
		comment.setContent("This test sucks. 0/5 would not test again.");
		comment.setImage(bitmap);
		
		return comment;
	}
	
	private void deleteFragment(StoryFragment fragment) {
		try {
			for (UUID id : fragment.getMediaIds()) {
				es.deleteImage(id);
			}
			
			es.deleteFragment(fragment.getFragmentID());
		}
		catch (Exception e) {}
	}
}
