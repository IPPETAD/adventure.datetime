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
import ca.cmput301f13t03.adventure_datetime.model.StoryFragment;
import ca.cmput301f13t03.adventure_datetime.model.Interfaces.ICurrentFragmentListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FragmentView extends Activity implements ICurrentFragmentListener {
	private static final String TAG = "FragmentView";

	private HorizontalScrollView _filmstrip;
	private TextView _content;
	private LinearLayout _filmLayout;
	private Button _choices;
	
	@Override
	public	void OnCurrentFragmentChange(StoryFragment newFragment) {
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_view);


		/** Layout items **/
		_filmLayout = (LinearLayout) findViewById(R.id.filmstrip);
		_filmstrip = (HorizontalScrollView) findViewById(R.id.filmstrip_wrapper);
		_choices = (Button) findViewById(R.id.choices);

		/** Programmatically set filmstrip height **/
		// TODO::JF Unshitify this, aka not static value
		_filmstrip.getLayoutParams().height = 300;

		//TODO::JF read actual content from model
		_content = (TextView) findViewById(R.id.content);
		String tempText = ("The Bundesens say that Tardar Sauce's face " +
				"appears grumpy because of feline dwarfism and an under bite." +
				"She and her brother Pokey were born to normal parents with " +
				"'a flat face, bubble eyes, and a short tail'. Tardar Sauce " +
				"is undersized and has hind legs that 'are a bit different'." +
				"Ironically, Tardar Sauce is calm and 'actually really nice,'" +
				"whereas Pokey has a grumpy personality.\n\n");
		tempText += ("According to the Bundesens, Tardar Sauce is a normal" + 
				"cat '99% of the time'. Photo sessions are only once a week, " +
				"and handling by strangers is limited. At SXSW Tardar Sauce " +
				"made limited two-hour appearances each day as Grumpy Cat.");
		tempText += "Grumpy Cat appeared in episodes of the Friskies YouTube" + 
				"game show 'Will Kitty Play With It?' TMZ reported that " +
				"Friskies paid for flying first class, a private hotel room " +
				"with king-sized bed, a personal assistant, a chauffeur, and " +
				"unlimited Friskies food and bottled water. Bryan Bundesen "+
				"said the car was a BMW X5 with tinted windows. On March 22, "+
				"2013, Grumpy Cat traveled to New York City promoting the sho"+
				"w and appeared on Good Morning America and Anderson Live and"+
				"visited Time for a photoshoot. Michael Noer 'interviewed' Gr"+
				"umpy Cat for Forbes, released March 25.";
		_content.setText(tempText);

		// TODO : Not use Bitmap, but proper object. Load illustrations from model
		Bitmap[] frags = new Bitmap[10];

		// 1) Create new ImageView and add to the LinearLayout
		// 2) Set appropriate Layout Params to ImageView
		// 3) Give onClickListener for going to fullscreen
		LinearLayout.LayoutParams lp;
		for (int i=0; i<frags.length; i++) {

			ImageView li = new ImageView(this);
			li.setScaleType(ScaleType.CENTER_INSIDE);
			li.setImageResource(R.drawable.grumpy_cat2);
			_filmLayout.addView(li);

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
			});
		}

		/** Choices **/
		final String[] sChoices = 
			{"Dance and Sing", "Cry a lot", "Go to RATT", 
				"Grade this app 100%", "Eat a gold brick wrapped in lettuce"};

		_choices.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(v.getContext())
				.setTitle("Actions")
				.setCancelable(true)
				.setItems(sChoices, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//TODO::JF Set new fragment
					}
				})
				.create().show();
			}
		});



	}

}
