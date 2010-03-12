//package bik;
import java.net.*;
import java.io.*;
public class SimpleAccess {

	 public static void main(String[] args) throws Exception{
		URL myURL = new URL("http://www.shef.ac.uk/dcs/");
		BufferedReader in = 
			new BufferedReader(
				new InputStreamReader(myURL.openStream()));
		String inputLine;
		while ((inputLine = in.readLine()) != null)
		    System.out.println(inputLine);

		in.close();      
	}

}
