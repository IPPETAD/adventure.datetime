/*
 *	Copyright (c) 2013 Andrew Fontaine, James Finlay, Jesse Tucker, Jacob Viau, and
 * 	Evan DeGraff
 *
 * 	Permission is hereby granted, free of charge, to any person obtaining a copy of
 * 	this software and associated documentation files (the "Software"), to deal in
 * 	the Software without restriction, including without limitation the rights to
 * 	use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * 	the Software, and to permit persons to whom the Software is furnished to do so,
 * 	subject to the following conditions:
 *
 * 	The above copyright notice and this permission notice shall be included in all
 * 	copies or substantial portions of the Software.
 *
 * 	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * 	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * 	FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * 	COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * 	IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * 	CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package ca.cmput301f13t03.adventure_datetime.view;

import ca.cmput301f13t03.adventure_datetime.R;
import ca.cmput301f13t03.adventure_datetime.view.treeView.TreeView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * 
 * Fragment owned by the AuthorEdit view
 * 
 * Shows a dynamically-created tree representing
 * the nodes of the current story.
 * 
 * TODO : Not quite everything.
 * 
 * @author James Finlay + Jesse Tucker
 *
 */
public class AuthorEdit_Overview extends Fragment
{
	private IFragmentSelected m_fragmentCallback = null;
	private AuthorEdit.OnCreatedCallback m_creationCallback = null;
	
	public void SetFragmentSelectionCallback(IFragmentSelected callback)
	{
		m_fragmentCallback = callback;
	}
	
	public void SetOnCreatedCallback(AuthorEdit.OnCreatedCallback callback)
	{
		m_creationCallback = callback;
	}
	
	@Override
	public View onCreateView(	LayoutInflater inflater,
								ViewGroup container, 
								Bundle savedInstanceState) 
	{
		View rootView = inflater.inflate(R.layout.overview_edit, container, false);
		
		TreeView treeView = (TreeView)(rootView.findViewById(R.id.v_treeViewBase));
		treeView.SetFragmentCallback(m_fragmentCallback);
		
		if(m_creationCallback != null)
		{
			m_creationCallback.OnCreated(treeView);
		}
		
		return rootView;
	}
}
