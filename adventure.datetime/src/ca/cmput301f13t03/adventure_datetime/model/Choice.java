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

import java.util.UUID;

/**
 * A choice the user cna make at the end of the fragment
 *
 * @author Andrew Fontaine
 * @version 1.0
 * @since 23/10/13
 */
public class Choice {

	/**
	 * The text describing the choice
	 */
	private String text;
	/**
	 * The UUID of the target fragment
	 */
	private UUID target;

    /**
     * Constructs a new Choice
     *
     * @param text Text of the new Choice
     * @param target UUID of the target StoryFragment
     */
	public Choice(String text, String target) {
		this.text = text;
		this.target = UUID.fromString(target);
	}

    /**
     * Gets the text of the Choice
     *
     * @return Text of Choice
     */
	public String getText() {
		return text;
	}

    /**
     * Sets the text of the Choice
     *
     * @param text The text to set
     */
	public void setText(String text) {
		this.text = text;
	}

    /**
     * Gets the UUID of the target StoryFragment
     *
     * @return UUID of the target StoryFragment
     */
	public String getTarget() {
		return target.toString();
	}

    /**
     * Sets the new target for the Choice
     *
     * @param target UUID of the new target
     */
	public void setTarget(String target) {
		this.target = UUID.fromString(target);
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Choice choice = (Choice) o;

        if (!target.equals(choice.target)) return false;
        if (!text.equals(choice.text)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = text.hashCode();
        result = 31 * result + target.hashCode();
        return result;
    }
}
