package ca.cmput301f13t03.adventure_datetime.model;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.ClientConfig;

public class ES {
	
	public static class Client {
		
		private static final String CON_URL = 
			"http://cmput301.softwareprocess.es:8080/cmput301f13t03/";
		private static JestClient jestClient;
		
		public static JestClient getClient() {
			if (jestClient == null) {
				ClientConfig clientConfig = new ClientConfig.Builder(CON_URL).multiThreaded(true).build();
				JestClientFactory factory = new JestClientFactory();
				factory.setClientConfig(clientConfig);
				jestClient = factory.getObject();
			}
			
			return jestClient;
		}
		
	}
		
}
