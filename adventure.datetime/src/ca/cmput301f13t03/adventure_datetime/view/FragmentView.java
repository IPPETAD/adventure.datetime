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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;
import android.widget.ImageView.ScaleType;
import ca.cmput301f13t03.adventure_datetime.R;
import ca.cmput301f13t03.adventure_datetime.model.Choice;
import ca.cmput301f13t03.adventure_datetime.model.Image;
import ca.cmput301f13t03.adventure_datetime.model.Interfaces.ICurrentFragmentListener;
import ca.cmput301f13t03.adventure_datetime.model.StoryFragment;
import ca.cmput301f13t03.adventure_datetime.serviceLocator.Locator;

import java.util.ArrayList;
import java.util.List;

/**
 * View Accessed via MainView > Continue > ~Select Item~ or via MainView > BrowseView > StoryDescription > ~Play /
 * Continue item ~
 * <p/>
 * Holds Horizontal filmstrip containing illustrations at top of page, story fragment text in the view, and an actions
 * buttons at the bottom of the page.
 *
 * @author James Finlay
 */
public class FragmentView extends Fragment implements ICurrentFragmentListener {
    private static final String TAG = "FragmentView";
    public static final String FOR_SERVER = "emagherd.server";

    private HorizontalScrollView _filmstrip;
    private TextView _content;
    private LinearLayout _filmLayout;
    private Button _choices;
    private boolean forServerEh;
    private View _rootView = null;
    private boolean _isEditing = false;
    private StoryFragment _fragment;

    private static final int FILM_STRIP_SIZE = 300;

    @Override
    public void OnCurrentFragmentChange(StoryFragment newFragment) {
        _fragment = newFragment;
        setUpView();
    }
    public void SetFragment(StoryFragment frag)
    {
        _fragment = frag;
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
    public void onDestroyView()
    {
        Locator.getPresenter().Unsubscribe(this);
        super.onDestroyView();
    }

    public void SetIsEditing(boolean isEditing)
    {
        _isEditing = isEditing;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        _rootView = inflater.inflate(R.layout.fragment_view, container, false);
        Locator.getPresenter().Subscribe(this);
        setUpView();
        return _rootView;
    }
    public void setUpView() {
        if (_fragment == null) return;

        /** Layout items **/
        _filmLayout = (LinearLayout) _rootView.findViewById(R.id.filmstrip);
        _filmstrip = (HorizontalScrollView) _rootView.findViewById(R.id.filmstrip_wrapper);
        _choices = (Button) _rootView.findViewById(R.id.choices);
        _content = (TextView) _rootView.findViewById(R.id.content);

        if (_fragment.getStoryMedia() == null)
            _fragment.setStoryMedia(new ArrayList<Image>());

        /* Run on UI Thread for server stuff */
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {


                /** Programmatically set filmstrip height **/
                if (_fragment.getStoryMedia().size() > 0)
                    _filmstrip.getLayoutParams().height = FILM_STRIP_SIZE;
                else
                    _filmstrip.getLayoutParams().height = 0;

                _content.setText(_fragment.getStoryText());
                _filmLayout.removeAllViews();

                // 1) Create new ImageView and add to the LinearLayout
                // 2) Set appropriate Layout Params to ImageView
                // 3) Give onClickListener for going to fullscreen
                LinearLayout.LayoutParams lp;
                //for (int i = 0; i < _fragment.getStoryMedia().size(); i++) {
                for (int i = 0; i < _fragment.getStoryMedia().size(); i++) {

                    ImageView li = new ImageView(getActivity());
                    li.setScaleType(ScaleType.CENTER_INSIDE);
                    li.setImageBitmap(_fragment.getStoryMedia().get(i).decodeBitmap());
                    _filmLayout.addView(li);

                    lp = (LinearLayout.LayoutParams) li.getLayoutParams();
                    lp.setMargins(10, 10, 10, 10);
                    lp.width = FILM_STRIP_SIZE;
                    lp.height = FILM_STRIP_SIZE;
                    lp.gravity = Gravity.CENTER_VERTICAL;
                    li.setLayoutParams(lp);

                    li.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
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
                                    .setNegativeButton("Change Adventures", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if(!_isEditing)
                                                getActivity().onBackPressed();
                                        }
                                    })
                                    .create().show();

                        }
                    });
                }
            }});

    }
}
