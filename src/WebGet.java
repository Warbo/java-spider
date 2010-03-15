/*
 * This class holds code to grab Web pages (as Strings) from provided URLs.
 */

import java.net.*;
import java.io.*;

public class WebGet {

	/*
	 * Get whatever is living at currentURL. Can throw IOException, or
	 * 
	 */
	 public static String get(URL currentURL) throws IOException, AccessException {
		String output = "";
		BufferedReader in = new BufferedReader(
			new InputStreamReader(currentURL.openStream())
		);

		String inputLine;
		while ((inputLine = in.readLine()) != null) {
		    output = output + inputLine;
		}
		in.close();
		return output;
	}

}

