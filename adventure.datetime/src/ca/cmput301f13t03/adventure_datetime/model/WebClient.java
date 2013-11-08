package ca.cmput301f13t03.adventure_datetime.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

import com.google.gson.Gson;

public class WebClient {
	
	private static final String CON_URL = 
			"http://cmput301.softwareprocess.es:8080/cmput301f13t03/";

	private HttpClient client = new DefaultHttpClient();
	private Gson gson = new Gson();
	
	public WebClient() {
		client = new DefaultHttpClient();
		gson = new Gson();
	}
	
	public void putComment(Comment c) throws IllegalStateException, IOException {
		HttpPost post = new HttpPost(CON_URL + "comments/" + c.getWebId());
		StringEntity se = null;
	
		try {
			se = new StringEntity(gson.toJson(c));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		post.setHeader("Accept", "application/json");
		post.setEntity(se);
		
		HttpResponse response = null;

		response = client.execute(post);
		
		String status = response.getStatusLine().toString();
		Log.i("HTTP", status);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			InputStream instream = entity.getContent();
			String result = convertStreamToString(instream);
			instream.close();
			Log.i("HTTP", result);
		}
		
	}
	
	public void deleteComment(UUID id) throws ClientProtocolException, IOException {
		HttpDelete delete = new HttpDelete(CON_URL + "comments/" + id.toString());
		delete.addHeader("Accept", "application/json");
		
		HttpResponse response = client.execute(delete);
		
		String status = response.getStatusLine().toString();
		Log.i("HTTP", status);

		HttpEntity entity = response.getEntity();
		if (entity != null) {
			InputStream instream = entity.getContent();
			String result = convertStreamToString(instream);
			instream.close();
			Log.i("HTTP", result);
		}
		
	}
	
	private static String convertStreamToString(InputStream is) {
	    /*
	     * To convert the InputStream to String we use the BufferedReader.readLine()
	     * method. We iterate until the BufferedReader return null which means
	     * there's no more data to read. Each line will appended to a StringBuilder
	     * and returned as String.
	     */
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();

	    String line = null;
	    try {
	        while ((line = reader.readLine()) != null) {
	            sb.append(line + "\n");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            is.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return sb.toString();
	}
	
}
