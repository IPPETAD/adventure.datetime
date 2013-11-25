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

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import ca.cmput301f13t03.adventure_datetime.R;
import ca.cmput301f13t03.adventure_datetime.model.StoryFragment;
import ca.cmput301f13t03.adventure_datetime.model.Interfaces.ICurrentFragmentListener;
import ca.cmput301f13t03.adventure_datetime.serviceLocator.Locator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 *
 * View accessed by clicking image in filmstrip
 *
 * Show fullscreen version of image / play video
 *
 * @author James Finlay
 *
 */
public class FullScreen_Image extends FragmentActivity implements ICurrentFragmentListener {
    private static final String TAG = "FragmentActivity";
    public static final String TAG_AUTHOR = "yolo.swag.AuthorEh";
    public static final int GALLERY = 42;
    public static final int CAMERA = 23;

    private StoryFragment _fragment;
    private ViewPager _viewPager;
    private StoryPagerAdapter _pageAdapter;

    @Override
    public void OnCurrentFragmentChange(StoryFragment newFragment) {
        _fragment = newFragment;
        setUpView();
    }

    private void setUpView() {
        if (_fragment == null) return;
        if (_pageAdapter == null) return;

        Button gallery = (Button) findViewById(R.id.gallery);
        Button camera = (Button)  findViewById(R.id.camera);
        Button delete = (Button)  findViewById(R.id.action_delete);

        gallery.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, GALLERY);
            }


        });
        camera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, CAMERA);
            }
        });


        //_pageAdapter.setIllustrations(_fragment.getStoryMedia());
        ArrayList<String> list = new ArrayList<String>();
        for (int i=0; i<5; i++) list.add(""+i);
        _pageAdapter.setIllustrations(list, getIntent().
                getBooleanExtra(TAG_AUTHOR, false));
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.fullscreen_image);


        _pageAdapter = new StoryPagerAdapter(getSupportFragmentManager());
        _viewPager = (ViewPager) findViewById(R.id.author_pager);
        _viewPager.setAdapter(_pageAdapter);

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
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        if (resultCode != RESULT_OK) return;

        switch (requestCode) {
            case GALLERY:
                Uri selectedImage = imageReturnedIntent.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(
                        selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();
                _fragment.addMedia(filePath);
                break;
            case CAMERA:

                break;
        }
    }

    private class StoryPagerAdapter extends FragmentStatePagerAdapter {

        private List<String> _illustrations;
        private boolean _author;

        public StoryPagerAdapter(FragmentManager fm) {
            super(fm);
            _illustrations = new ArrayList<String>();
        }

        public void setIllustrations(List<String> illustrationIDs, boolean author) {
            _illustrations = illustrationIDs;
            _author = author;
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int pos) {
            IllustrationFragment frag = new IllustrationFragment();
            frag.init(_illustrations.get(pos), pos, _illustrations.size(), _author);

            return frag;
        }

        @Override
        public int getCount() {
            return _illustrations.size();
        }

    }
    public static class IllustrationFragment extends Fragment {

        private View _rootView;
        private String _sID;
        private String _position;
        private boolean _author;

        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
        }
        public void init(String id, int position, int total, boolean author) {
            _sID = id;
            _position = (position+1) + "/" + total;
            _author = author;
            setUpView();
        }
        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {

            _rootView = inflater.inflate(R.layout.fullscreen_illustration,
                    container, false);

            setUpView();

            return _rootView;
        }
        private void setUpView() {
            if (_sID == null) return;
            if (_rootView == null) return;

            /** Layout items **/
            ImageView image = (ImageView) _rootView.findViewById(R.id.image);

            TextView counter = (TextView) _rootView.findViewById(R.id.count);

            // TODO: Set counter by location

            image.setBackgroundResource(R.drawable.grumpy_cat2);
            counter.setText(_position);

            Button gallery = (Button) _rootView.findViewById(R.id.gallery);
            Button camera = (Button)  _rootView.findViewById(R.id.camera);
            Button delete = (Button)  _rootView.findViewById(R.id.action_delete);



            // turn off author buttons if necessary
            if (!_author) {
                gallery.setVisibility(View.GONE);
                camera.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);
            }
        }

    }
}
