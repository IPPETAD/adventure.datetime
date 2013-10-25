package ca.cmput301f13t03.adventure_datetime.view;

import ca.cmput301f13t03.adventure_datetime.R;
import ca.cmput301f13t03.adventure_datetime.model.StoryFragment;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

public class FragmentView extends Activity {
	private static final String TAG = "FragmentView";

	private HorizontalScrollView _filmstrip;
	private TextView _content;
	//private ListView _listView;
	private LinearLayout _filmLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_view);


		//_listView = (ListView) findViewById(R.id.list_view);
		_filmLayout = (LinearLayout) findViewById(R.id.filmstrip);
		_filmstrip = (HorizontalScrollView) findViewById(R.id.filmstrip_wrapper);

		// Programmatically set filmstrip height
		// TODO: Unshitify this, aka not static value
		_filmstrip.getLayoutParams().height = 300;

		//TODO: read actual content from model
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

		Bitmap[] frags = new Bitmap[10];
		for (int i=0; i<frags.length; i++) {
			ImageView li = new ImageView(this);
			li.setBackgroundResource(R.drawable.grumpy_cat);
			_filmLayout.addView(li);
		}



	}

}
