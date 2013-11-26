package ca.cmput301f13t03.adventure_datetime.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.AndroidTestCase;
import ca.cmput301f13t03.adventure_datetime.R;

import com.google.gson.Gson;

public class ImageTest extends AndroidTestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testJson() throws Exception {
		Image image = new Image(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.grumpy_cat));
		Gson gson = new Gson();
		
		Bitmap bitmap = image.decodeBitmap();
		assertEquals(bitmap, image.decodeBitmap());
		
		String json = gson.toJson(image);
		Image image2 = gson.fromJson(json, Image.class);
		
		assertEquals(image.getId(), image2.getId());
		assertEquals(image.getEncodedBitmap(), image2.getEncodedBitmap());
		
	}

}
