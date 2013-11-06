/*
 *        Copyright (c) 2013 Andrew Fontaine, James Finlay, Jesse Tucker, Jacob Viau, and
 *         Evan DeGraff
 *
 *         Permission is hereby granted, free of charge, to any person obtaining a copy of
 *         this software and associated documentation files (the "Software"), to deal in
 *         the Software without restriction, including without limitation the rights to
 *         use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 *         the Software, and to permit persons to whom the Software is furnished to do so,
 *         subject to the following conditions:
 *
 *         The above copyright notice and this permission notice shall be included in all
 *         copies or substantial portions of the Software.
 *
 *         THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *         IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 *         FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 *         COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 *         IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *         CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package ca.cmput301f13t03.adventure_datetime.model.Interfaces;

/**
 * Presents the model information to an external package such as a view. Only supplies data
 * in a readonly format.
 *
 * AKA : IModelGet
 */
public interface IStoryModelPresenter
{
    void Subscribe(ICurrentFragmentListener fragmentListener);
    void Subscribe(ICurrentStoryListener storyListener);
    void Subscribe(IStoryListListener storyListListener);
    void Subscribe(IBookmarkListListener bookmarkListListener);
    
    void Unsubscribe(ICurrentFragmentListener fragmentListener);
    void Unsubscribe(ICurrentStoryListener storyListener);
    void Unsubscribe(IStoryListListener storyListListener);
    void Unsubscribe(IBookmarkListListener bookmarkListListener);
}