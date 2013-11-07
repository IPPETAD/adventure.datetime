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
import java.util.List;

import ca.cmput301f13t03.adventure_datetime.R;
import ca.cmput301f13t03.adventure_datetime.model.Choice;
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
public class AuthorEdit_Edit extends Fragment implements ICurrentFragmentListener {

	private View _rootView;
	private StoryFragment _sFragment;
	
	@Override
	public void OnCurrentFragmentChange(StoryFragment newFragment) {
		_sFragment = newFragment;
		setUpView();
	}
	@Override
	public void onResume() {
		Locator.getPresenter().Subscribe(this);
		super.onResume();
	}
	@Override
	public void onPause() {
		Locator.getPresenter().Unsubscribe(this);
		super.onPause();
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
		if (_sFragment == null) return;
		

		/** Layout items **/
		Button choices = (Button) _rootView.findViewById(R.id.choices);
		TextView content = (TextView) _rootView.findViewById(R.id.content);
		ImageButton editContent = (ImageButton) _rootView.findViewById(R.id.edit_content);
		
		content.setText(_sFragment.getStoryText());

		/** Choices **/
		final List<String> lchoices = new ArrayList<String>();
		for (Choice choice : _sFragment.getChoices())
			lchoices.add(choice.getText());
		
		choices.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().getActionBar().setSelectedNavigationItem(1);
				
				// TODO::JF Have Choice Editing here 

				new AlertDialog.Builder(v.getContext())
				.setTitle("Actions")
				.setCancelable(true)
				.setItems(lchoices.toArray(new String[lchoices.size()]), 
						new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//TODO::JF Set new fragment
					}
				})
				.create().show();
			}			
		});
		
		/* Edit Content */
		editContent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(getActivity())
				.setView(getActivity().getLayoutInflater().inflate(R.layout.dialog_edit, null))
				.setPositiveButton("OK!", null)
				.setNegativeButton("Cancel", null)
				.create().show();
			}
		});
		
		
	}
}
