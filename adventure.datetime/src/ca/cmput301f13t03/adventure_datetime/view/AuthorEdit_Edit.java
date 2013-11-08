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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ca.cmput301f13t03.adventure_datetime.R;
import ca.cmput301f13t03.adventure_datetime.model.Choice;
import ca.cmput301f13t03.adventure_datetime.model.Story;
import ca.cmput301f13t03.adventure_datetime.model.StoryFragment;
import ca.cmput301f13t03.adventure_datetime.model.Interfaces.ICurrentFragmentListener;
import ca.cmput301f13t03.adventure_datetime.serviceLocator.Locator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Fragment controlled by AuthorEdit view
 * 
 * Contains editing fields for a custom FragmentView
 * 
 * @author James Finlay
 *
 */
public class AuthorEdit_Edit extends Fragment {

	private View _rootView;
	private StoryFragment _fragment;
	private Story _story;
	
	private Button _media, _choices;
	private EditText _content;
	
	public void setFragment(StoryFragment sf) {
		_fragment = sf;
		setUpView();
	}
	public void setStory(Story st) {
		_story = st;
		setUpView();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, 
			ViewGroup container, Bundle savedInstanceState) {

		_rootView = inflater.inflate(R.layout.fragment_edit, container, false);
		setUpView();
		return _rootView;
	}
	private void setUpView() {
		if (_rootView == null) return;
		if (_fragment == null) return;
		//if (_story == null) return;
		

		/** Layout items **/
		_media = (Button) _rootView.findViewById(R.id.btn_addMedia);
		_choices = (Button) _rootView.findViewById(R.id.choices);
		_content = (EditText) _rootView.findViewById(R.id.content);
		
		_content.setText(_fragment.getStoryText());

		/** Choices **/
		final List<String> lchoices = new ArrayList<String>();
		for (Choice choice : _fragment.getChoices())
			lchoices.add(choice.getText());
	
	}
	public void saveFragment() {
		_fragment.setStoryText(_content.getText().toString());
		_story.updateTimestamp();
		Locator.getAuthorController().saveFragment(_fragment);
		Locator.getAuthorController().saveStory(_story);
	}
}
