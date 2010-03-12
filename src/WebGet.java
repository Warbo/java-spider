import java.net.*;
import java.io.*;

public class WebGet {

	 public static String get(URL currentURL) throws Exception {
		String output = "";
		BufferedReader in = new BufferedReader(
			new InputStreamReader(currentURL.openStream())
		);

		String inputLine;
		while ((inputLine = in.readLine()) != null)
		    output = output + inputLine;
		}
		in.close();
		return output;
	}

}

