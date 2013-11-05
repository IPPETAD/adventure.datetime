/*
 * Copyright (c) 2013 Andrew Fontaine, James Finlay, Jesse Tucker, Jacob Viau, and
 * Evan DeGraff
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package ca.cmput301f13t03.adventure_datetime.model;

import android.graphics.Bitmap;
import com.google.gson.Gson;
import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.UUID;

/**
 * @author Andrew Fontaine
 * @version 1.0
 * @since 04/11/13
 */
public class StoryTest extends TestCase {

	public void setUp() throws Exception {
		super.setUp();
	}

	public void testCreation() throws Exception {
		Story story = new Story();
		Assert.assertNotNull("Story is null", story);
		story = new Story("test", "test", "test");
		Assert.assertNotNull("Story is null", story);
		story = new Story(UUID.randomUUID().toString(), UUID.randomUUID().toString(), "test",
				System.currentTimeMillis() / 1000L, "test", Bitmap.createBitmap(10, 10, Bitmap.Config.RGB_565), "test");
		Assert.assertNotNull("Story is null", story);
	}

	public void testAddRemoveFragment() throws Exception {
		Story story = new Story();
		Assert.assertTrue("HashMap is not empty", story.getFragments().isEmpty());
		StoryFragment frag = new StoryFragment(story.getId(), "test");
		story.addFragment(frag);
		Assert.assertTrue("Story doesn't contain fragment", story.getFragments().contains(frag.getFragmentID()));
		Assert.assertEquals("Story head isn't new fragment", frag.getFragmentID(), story.getHeadFragmentId());
		story.removeFragment(frag.getFragmentID());
		Assert.assertFalse("Story contains fragment", story.getFragments().contains(frag.getFragmentID()));

		story.addFragment(frag.getFragmentID());
		Assert.assertTrue("Story doesn't contain fragment", story.getFragments().contains(frag.getFragmentID()));
		Assert.assertEquals("Story head isn't new fragment", frag.getFragmentID(), story.getHeadFragmentId());
		story.removeFragment(frag.getFragmentID());
		Assert.assertFalse("Story contains fragment", story.getFragments().contains(frag.getFragmentID()));
	}

	public void testAddRemoveTags() throws Exception {
		Story story = new Story();
		Assert.assertTrue("HashMap doesn't contain 'new'", story.getTags().contains("new"));
		Assert.assertEquals("HashMap doesn't contain 1 value", 1, story.getTags().size());
		story.addTag("Test");
		Assert.assertTrue("Story doesn't contain 'test'", story.getTags().contains("Test"));
		Assert.assertEquals("HashMap doesn't contain 2 values", 2, story.getTags().size());
		story.removeTag("Test");
		Assert.assertFalse("HashMap still contains 'test'", story.getTags().contains("Test"));
		Assert.assertEquals("HashMap doesn't contain 1 value", 1, story.getTags().size());
	}

	public void testUUID() throws Exception {
		Story story = new Story();
		String newUUID = UUID.randomUUID().toString();
		try {
			story.setHeadFragmentId("WRONG");
			Assert.fail("UUID was set");
		} catch (IllegalArgumentException e) {

		}
		story.setHeadFragmentId(newUUID);
		Assert.assertEquals("UUID was not set", newUUID, story.getHeadFragmentId());
	}

	public void testJSON() throws Exception {
		Story story = new Story();
		Gson gson = new Gson();
		String json = gson.toJson(story);
		Story story2;
		story2 = gson.fromJson(json, Story.class);
		Assert.assertEquals("UUIDs do not match", story.getId(), story2.getId());
	}

	public void tearDown() throws Exception {
		super.tearDown();
	}
}
