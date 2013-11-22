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
	/**
	 * Listens for change on the current fragment
	 */
    void Subscribe(ICurrentFragmentListener fragmentListener);
    
    /**
	 * Listens for change on the current story
	 */
    void Subscribe(ICurrentStoryListener storyListener);
    
    /**
	 * Listens for change on the current list of stories
	 */
    void Subscribe(ILocalStoriesListener storyListListener);
    
    void Subscribe(IOnlineStoriesListener storyListListener);
    
    /**
	 * Listens for change on the bookmark list
	 */
    void Subscribe(IBookmarkListListener bookmarkListListener);
    
    /**
     * Listens for changes to the entire set of fragments
     */
    void Subscribe(IAllFragmentsListener allFragmentsListener);
    
    /**
     * Listens for changes to the comments
     */
    void Subscribe(ICommentsListener commentsListener);
    
    //===========================================================
    
    /**
	 * Stop listening for changes on the current fragment
	 */
    void Unsubscribe(ICurrentFragmentListener fragmentListener);
    
    /**
	 * Stop listening for changes on the current story
	 */
    void Unsubscribe(ICurrentStoryListener storyListener);
    
    /**
	 * Stop listening for changes on the current story list
	 */
    void Unsubscribe(ILocalStoriesListener storyListListener);
    
    void Unsubscribe(IOnlineStoriesListener storyListListener);
    /**
	 * Stop listening for changes on the current bookmark list
	 */
    void Unsubscribe(IBookmarkListListener bookmarkListListener);
    
    /**
     * Stop listening for changes on the current set of fragments
     */
    void Unsubscribe(IAllFragmentsListener allFragmentsListener);
    
    /**
     * Stop listening for changes to the comments
     */
    void Unsubscribe(ICommentsListener commentsListener);
}