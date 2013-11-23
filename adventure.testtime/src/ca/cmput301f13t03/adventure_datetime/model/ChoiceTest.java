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

import com.google.gson.Gson;
import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.UUID;

/**
 * @author Andrew Fontaine
 * @version 1.0
 * @since 05/11/13
 */
public class ChoiceTest extends TestCase {

	@Override
	public void setUp() throws Exception {
		super.setUp();    //TODO Implement
	}

	public void testConstruction() throws Exception {
		Choice choice = new Choice("test", UUID.randomUUID());
		Assert.assertNotNull("Choice is null", choice);
	}

	public void testJson() throws Exception {
		Choice choice = new Choice("test", UUID.randomUUID());
		Gson gson = new Gson();
		String json = gson.toJson(choice);
		Choice choice2 = gson.fromJson(json, Choice.class);
		Assert.assertEquals("Targets are not equal", choice.getTarget(), choice2.getTarget());
		Assert.assertEquals("Text is not equal", choice.getText(), choice2.getText());

	}

	@Override
	public void tearDown() throws Exception {
		super.tearDown();    //TODO Implement
	}
}
