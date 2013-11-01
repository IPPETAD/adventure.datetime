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
import ca.cmput301f13t03.adventure_datetime.model.Story;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class AuthorStories extends FragmentActivity {

	private ListView _listView;
	private RowArrayAdapter _adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.browse_authored);

		_listView = (ListView) findViewById(R.id.list_view);
		_listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO : Launch activity from item click
			}			
		});
	}
	
	@Override
	public void onResume() {
		
		Story[] stories = new Story[10];
		for (int i=0; i<stories.length; i++) stories[i] = new Story();
		
		_adapter = new RowArrayAdapter(this, R.layout.listviewitem, stories);
		
		_listView.setAdapter(_adapter);
		_listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				// Get selected item
				ListView listView = (ListView) parent;
				Story item = (Story) listView.getItemAtPosition(position);
				
				// TODO: Send story id
				
				Intent intent = new Intent(AuthorStories.this, AuthorStoryDescription.class);
				startActivity(intent);
			}
		});
		
		super.onResume();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		getMenuInflater().inflate(R.menu.authorlist, menu);
		return true;
	}


	private class RowArrayAdapter extends ArrayAdapter<Story> {

		private Context context;
		private int layoutResourceID;
		private Story[] values;

		public RowArrayAdapter(Context context, int layoutResourceID, Story[] values) {
			super(context, layoutResourceID, values);

			this.context = context;
			this.layoutResourceID = layoutResourceID;
			this.values = values;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View rowView = inflater.inflate(R.layout.listviewitem, parent, false);

			ImageView thumbnail = (ImageView) rowView.findViewById(R.id.thumbnail);
			TextView title = (TextView) rowView.findViewById(R.id.title);
			TextView fragments = (TextView) rowView.findViewById(R.id.author);
			TextView lastModified = (TextView) rowView.findViewById(R.id.datetime);
			ImageView status = (ImageView) rowView.findViewById(R.id.status_icon);

			// TODO: fill out views from values[position]
			if (position == 3)
				status.setImageResource(R.drawable.ic_action_cloud);
			else if (position == 5)
				status.setImageResource(R.drawable.ic_action_sync);
			else
				; // no icon if not uploaded

			fragments.setText("Fragments: 69");
			lastModified.setText("Last Modified: 01/01/1969");


			return rowView;
		}
	}

}
