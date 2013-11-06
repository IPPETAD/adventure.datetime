/*
 *        Copyright (c) 2013 Andrew Fontaine, James Finlay, Jesse Tucker, Jacob Viau, and
 *         Evan DeGraff
 *
 *         Permission is hereby granted, free of charge, to any person obtaining a copy of
 *         this software and associated documentation files (the "Software"), to deal in
 *         the Software without restriction, including without limitation the rights to
 *         use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 *         the Software, and to permit persons to whom the Software is furnished to do so,
 *         subject to the following conditions:
 *
 *         The above copyright notice and this permission notice shall be included in all
 *         copies or substantial portions of the Software.
 *
 *         THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *         IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 *         FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 *         COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 *         IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *         CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package ca.cmput301f13t03.adventure_datetime.model;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.ClientConfig;

/**
 * Container class for ElasticSearch singletons
 */
public class ES {
	
	/**
	 * The JEST client for ElasticSearch
	 */
	public static class Client {
		
		private static final String CON_URL = 
			"http://cmput301.softwareprocess.es:8080/cmput301f13t03/";
		private static JestClient jestClient;
		
		/**
		 * Gets the HTTP JestClient singleton for:
		 * http://cmput301.softwareprocess.es:8080/cmput301f13t03/
		 * @return JestClient for our ES server
		 */
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
