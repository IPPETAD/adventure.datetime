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
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class FragmentView extends Fragment implements ICurrentFragmentListener
{
	public static final String FOR_SERVER = "emagherd.server";

	private HorizontalScrollView _filmstrip;
	private TextView _content;
	private LinearLayout _filmLayout;
	private Button _choices;
	private View _rootView = null;

	private boolean _isEditing = false;
	private StoryFragment _fragment;
	
	public void SetFragment(StoryFragment frag)
	{
		_fragment = frag;
		SetUpView();
	}
	
	@Override
	public void OnCurrentFragmentChange(StoryFragment newFragment) 
	{
		_fragment = newFragment;
		SetUpView();
	}
	
	public View onCreateView(LayoutInflater inflater, 
			ViewGroup container, Bundle savedInstanceState) 
	{
		_rootView = inflater.inflate(R.layout.fragment_view, container, false);
		Locator.getPresenter().Subscribe(this);
		SetUpView();
		return _rootView;
	}
	
	@Override
	public void onDestroyView()
	{
		Locator.getPresenter().Unsubscribe(this);
		super.onDestroyView();
	}
	
	public void SetIsEditing(boolean isEditing)
	{
		_isEditing = isEditing;
	}
	
	private void SetUpView() 
	{
		if (_fragment == null) return;
		if(_rootView == null) return;
		
		/** Layout items **/
		
		_filmLayout = (LinearLayout) _rootView.findViewById(R.id.filmstrip);
		_filmstrip = (HorizontalScrollView) _rootView.findViewById(R.id.filmstrip_wrapper);
		_choices = (Button) _rootView.findViewById(R.id.choices);
		
		_content = (TextView) _rootView.findViewById(R.id.content);

		if (_fragment.getStoryMedia() == null)
			_fragment.setStoryMedia(new ArrayList<Uri>());

		/** Programmatically set filmstrip height **/
		// TODO::JF Unshitify this, aka not static value
		//if (_fragment.getStoryMedia().size() > 0)
		_filmstrip.getLayoutParams().height = 300;

		_content.setText(_fragment.getStoryText());

		// 1) Create new ImageView and add to the LinearLayout
		// 2) Set appropriate Layout Params to ImageView
		// 3) Give onClickListener for going to fullscreen
		LinearLayout.LayoutParams lp;
		//for (int i = 0; i < _fragment.getStoryMedia().size(); i++) {
		for (int i = 0; i < 5; i++) 
		{
			// TODO::JF Get images from fragment
			ImageView li = new ImageView(this.getActivity());
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
				public void onClick(View v) 
				{
					Intent intent = new Intent(getActivity(), FullScreen_Image.class);
					intent.putExtra(FullScreen_Image.TAG_AUTHOR, false);
					startActivity(intent);
				}
			});
		}

		if (_fragment.getChoices().size() > 0) {
			/** Choices **/
			final List<String> choices = new ArrayList<String>();
			for (Choice choice : _fragment.getChoices())
				choices.add(choice.getText());
			choices.add("I'm feeling lucky.");

			_choices.setText("Actions");
			_choices.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					new AlertDialog.Builder(v.getContext())
					.setTitle("Actions")
					.setCancelable(true)
					.setItems(choices.toArray(new String[choices.size()]), 
							new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

							/** You feeling lucky, punk? **/
							if (which == _fragment.getChoices().size())
								which = (int) (Math.random() * _fragment.getChoices().size());

							Choice choice = _fragment.getChoices().get(which);

							Toast.makeText(FragmentView.this.getActivity(), 
									choice.getText(), Toast.LENGTH_LONG).show();
							Locator.getUserController().MakeChoice(choice);
						}
					})
					.create().show();
				}
			});
		} else {
			/** End of story **/
			Locator.getUserController().deleteBookmark();
			_choices.setText("The End");
			_choices.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					new AlertDialog.Builder(v.getContext())
					.setTitle("La Fin")
					.setCancelable(true)
					.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Locator.getUserController().StartStory(_fragment.getStoryID());							
						}
					})
					.setNegativeButton("Change Adventures", new DialogInterface.OnClickListener() 
					{
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							if(!_isEditing)
							{
								getActivity().onBackPressed();
							}
						}
					})
					.create().show();
				}
			});
		}
	}
}
