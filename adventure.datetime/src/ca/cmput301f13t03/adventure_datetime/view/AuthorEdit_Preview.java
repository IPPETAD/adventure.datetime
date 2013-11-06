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

import ca.cmput301f13t03.adventure_datetime.R;
import ca.cmput301f13t03.adventure_datetime.model.StoryFragment;
import ca.cmput301f13t03.adventure_datetime.model.Interfaces.ICurrentFragmentListener;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

/**
 * 
 * Fragment owned by the AuthorEdit view.
 * 
 * Shows a preview identical to the FragmentView for the author
 * to see expected fragment format.
 * 
 * TODO : Link with model
 * 
 * @author James Finlay
 *
 */
public class AuthorEdit_Preview extends Fragment implements ICurrentFragmentListener {
	private static final String TAG = "AuthorEdit";
	
	private View _rootView;
	private StoryFragment _sFragment;
	
	@Override
	public void OnCurrentFragmentChange(StoryFragment fragment) {
		_sFragment = fragment;
		setUpView();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, 
			ViewGroup container, Bundle savedInstanceState) {

		_rootView = inflater.inflate(R.layout.fragment_view, container, false);
		setUpView();
		return _rootView;
	}
	private void setUpView() {
		if (_rootView == null) return;
		if (_sFragment == null) return;
		
		/** Layout items **/
		LinearLayout filmLayout = (LinearLayout) _rootView.findViewById(R.id.filmstrip);
		HorizontalScrollView filmstrip = (HorizontalScrollView) _rootView.findViewById(R.id.filmstrip_wrapper);
		TextView content = (TextView) _rootView.findViewById(R.id.content);

		if (_sFragment.getStoryMedia() == null)
			_sFragment.setStoryMedia(new ArrayList<String>());
		
		/** Programmatically set filmstrip height **/
		// TODO : Unshitify this, aka not static value
		if (_sFragment.getStoryMedia().size() > 0)
			filmstrip.getLayoutParams().height = 300;
		
		content.setText(_sFragment.getStoryText());

		

		// 1) Create new ImageView and add to the LinearLayout
		// 2) Set appropriate Layout Params to ImageView
		// 3) Give onClickListener for going to fullscreen
		LinearLayout.LayoutParams lp;
		for (int i=0; i<_sFragment.getStoryMedia().size(); i++) {
			//TODO::JF Get images from fragment
			/*ImageView li = new ImageView(getActivity());
			li.setScaleType(ScaleType.CENTER_INSIDE);
			li.setImageResource(R.drawable.grumpy_cat2);
			filmLayout.addView(li);

			lp = (LinearLayout.LayoutParams) li.getLayoutParams();
			lp.setMargins(10, 10, 10, 10);
			lp.width = LayoutParams.WRAP_CONTENT;
			lp.gravity = Gravity.CENTER_VERTICAL;
			li.setLayoutParams(lp);

			li.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO: Open image in fullscreen
				}
			});*/
		}
	}

}
