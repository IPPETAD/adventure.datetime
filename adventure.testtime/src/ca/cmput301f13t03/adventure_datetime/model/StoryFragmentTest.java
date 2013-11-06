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

import android.util.Log;
import com.google.gson.Gson;
import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.UUID;

/**
 * @author Andrew Fontaine
 * @version 1.0
 * @since 04/11/13
 */
public class StoryFragmentTest extends TestCase {

	public void setUp() throws Exception {
		super.setUp();
	}

	public void testCreation() throws Exception {
		StoryFragment frag = new StoryFragment(UUID.randomUUID().toString(), UUID.randomUUID().toString(),
				"test", new ArrayList<String>(), new ArrayList<Choice>());
		Assert.assertNotNull("Frag is null", frag);

		frag = new StoryFragment(UUID.randomUUID().toString(), "test",
				new Choice("test", UUID.randomUUID().toString()));
		Assert.assertNotNull("Frag is null", frag);

		frag = new StoryFragment(UUID.randomUUID().toString(), "test");
		Assert.assertNotNull("Frag is null", frag);

		frag = new StoryFragment(new ArrayList<Choice>(), UUID.randomUUID().toString(), UUID.randomUUID().toString(),
				"test");
		Assert.assertNotNull("Frag is null", frag);
	}

	public void testChoices() throws Exception {
		Gson gson = new Gson();
		String jsonchoice;
		StoryFragment frag = new StoryFragment(UUID.randomUUID().toString(), "test");
		Assert.assertEquals("Choices is not empty", 0, frag.getChoices().size());
		Choice choice = new Choice("test", UUID.randomUUID().toString());
		jsonchoice = gson.toJson(choice);
		frag.addChoice(choice);
		Assert.assertTrue("Choices doesn't contain choice", frag.getChoices().contains(choice));
		Assert.assertTrue("JSON doesn't match up", frag.getChoicesInJson().contains(jsonchoice));
		Assert.assertEquals("Choices doesn't have size of 1", 1, frag.getChoices().size());
		frag.removeChoice(choice);
		Assert.assertFalse("Choices still contains choice", frag.getChoices().contains(choice));
		Assert.assertEquals("Choices is not empty", 0, frag.getChoices().size());
		Assert.assertFalse("JSON still matches up", frag.getChoicesInJson().contains(jsonchoice));
	}

	public void testGson() throws Exception {
		Gson gson = new Gson();
		StoryFragment frag = new StoryFragment(UUID.randomUUID().toString(), "test");
		String json = gson.toJson(frag);
		StoryFragment frag2 = gson.fromJson(json, StoryFragment.class);
		Assert.assertEquals("Fragment ids are not equal", frag.getFragmentID(), frag2.getFragmentID());
		Assert.assertEquals("Fragment storyIds are not equal", frag.getStoryID(), frag2.getStoryID());
	}



	public void tearDown() throws Exception {
		super.tearDown();
	}
}
