package ca.cmput301f13t03.adventure_datetime.model;

import java.io.IOException;
import java.util.UUID;

import junit.framework.TestCase;


public class WebClientTest extends TestCase {

	private WebClient client;
	
	protected void setUp() throws Exception {
		super.setUp();
		client = new WebClient();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	
	public void testComment() throws IllegalStateException, IOException {
		Comment c = new Comment();
		c.setTargetId(UUID.randomUUID());
		c.setWebId(UUID.randomUUID());
		c.setAuthor("lolcat");
		c.setContent("can I haz post");
		
		client.putComment(c);
		client.deleteComment(c.getWebId());		
	}

}
