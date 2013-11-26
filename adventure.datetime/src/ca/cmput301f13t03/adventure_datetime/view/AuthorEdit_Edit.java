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
import ca.cmput301f13t03.adventure_datetime.model.Story;
import ca.cmput301f13t03.adventure_datetime.model.StoryFragment;
import ca.cmput301f13t03.adventure_datetime.serviceLocator.Locator;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Fragment controlled by AuthorEdit view
 * 
 * Contains editing fields for a custom FragmentView
 * 
 * @author James Finlay
 *
 */
public class AuthorEdit_Edit extends Fragment implements OnClickListener
{
	private static final String TAG = "AuthorEdit_Edit";
	
	private View _rootView;
	private StoryFragment _fragment;
	private Story _story;
	
	private Button _addChoiceBtn;
	private EditText _content;
	private ListView _choiceList = null;
	
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
	
	private void setUpView() 
	{
		if (_rootView == null) return;
		if (_fragment == null) return;
		
		/** Layout items **/
		_addChoiceBtn = (Button) _rootView.findViewById(R.id.addChoice_btn);
		_choiceList = (ListView) _rootView.findViewById(R.id.choicesList);
		_content = (EditText) _rootView.findViewById(R.id.content);
		
		_content.setText(_fragment.getStoryText());
		
		_addChoiceBtn.setOnClickListener(this);
	
		_choiceList.setClickable(false);
		_choiceList.setAdapter(new ChoiceAdapter(this.getActivity(), R.layout.choice_item, _fragment.getChoices()));
	}
	
	public void saveFragment() 
	{
		_fragment.setStoryText(_content.getText().toString());
		_story.updateTimestamp();
		Locator.getAuthorController().saveFragment(_fragment);
		Locator.getAuthorController().saveStory(_story);
	}
	
	public void onClick(View v) 
	{
		// TODO::JT create a new choice and add it to the fragment
	}
	
	private class ChoiceAdapter extends ArrayAdapter<Choice>
	{
		private Context m_context;
		private List<Choice> m_values;
		private List<ChoiceButtonCallback> m_callbacks = new ArrayList<ChoiceButtonCallback>();
		
		public ChoiceAdapter(Context context, int resource, List<Choice> choices) 
		{
			super(context, resource, choices);

			m_context = context;
			m_values = choices;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			LayoutInflater inflater = (LayoutInflater) m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View rowView = inflater.inflate(R.layout.choice_item, parent, false);

			Choice item = m_values.get(position);
			
			/** Layout Items **/
			ImageButton deleteBtn = (ImageButton)rowView.findViewById(R.id.choice_remove_btn);
			ImageButton choiceBtn = (ImageButton)rowView.findViewById(R.id.choices_edit_btn);
			TextView txt = (TextView)rowView.findViewById(R.id.choice_txt);
			
			ChoiceButtonCallback callback = new ChoiceButtonCallback(_fragment, item);	
			m_callbacks.add(callback);
			
			txt.setText(item.getText());
			deleteBtn.setOnClickListener(callback);
			choiceBtn.setOnClickListener(callback);
			
			return rowView;
		}
		
		private class ChoiceButtonCallback implements OnClickListener
		{
			private StoryFragment m_origin = null;
			private Choice m_choice = null;
			
			public ChoiceButtonCallback(StoryFragment frag, Choice choice)
			{
				m_origin = frag;
				m_choice = choice;
			}
			
			public void onClick(View btn) 
			{
				if(btn.getId() == R.id.choice_remove_btn)
				{
					// TODO::JT
				}
				else if(btn.getId() == R.id.choices_edit_btn)
				{
					// TODO::JT
				}
				else
				{
					Log.e(TAG, "Somehow clicked on a button that shouldn't exist...?");
				}
			}
		}
	}
}
