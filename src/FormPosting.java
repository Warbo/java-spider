import java.io.*;
import java.net.*;
import java.util.*;


public class FormPosting {

	public void postRequest(String webPage, Hashtable ctHash) {
		URL url;                      
		HttpURLConnection urlConn;      
		DataOutputStream printout;    
		BufferedReader input;
		try {
			String content= createQueryString (ctHash);
			url = new URL(webPage);
			urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setRequestProperty("User-Agent", "Java Spider");
			urlConn.setDoOutput(true);
			//set request method
			urlConn.setRequestMethod("POST");
			//set request type: form filling
		      urlConn.setRequestProperty("Content-Type", 
					      "application/x-www-form-urlencoded");
			    urlConn.setRequestProperty("Content-Length", 
								content.length() + "");
          // Send POST output.
			    printout = new DataOutputStream 
			 				(urlConn.getOutputStream());
			    printout.writeBytes(content);

			    printout.flush();
			    printout.close();
			  
			      // Get response data.
			    input = new BufferedReader(new 
			                  InputStreamReader(urlConn.getInputStream()));

			    String str;
			    while (null != ((str = input.readLine())))
			        System.out.println(str);

			    input.close();

			  } catch (MalformedURLException me) {
			    System.err.println("MalformedURLException; " + me);
			  } catch (IOException ioe) {
			    System.err.println("IOException; " + ioe.getMessage());
          
			 }}
			    
	
	String createQueryString (Hashtable ctHash){
		System.out.println(ctHash);
		StringBuilder content = new StringBuilder();
		Enumeration e = ctHash.keys();
		boolean first = true;
		while(e.hasMoreElements()){  
			// For each key and value pair in the hashtable
			Object key = e.nextElement();
			Object value = ctHash.get(key);

			// If this is not the first key-value pair in hashtable
			// concantenate a Ò+Ó sign to the constructed String

			// append to a single string. 
			// Encode the value portion
			try {
				String encoded = URLEncoder.encode((String)value, "UTF-8");
				if (first) {
					content.append("?");
					first = false;
				}
				else {
					content.append("&");
				}
				content.append((String)key + "=" + encoded);
			} catch (UnsupportedEncodingException exc) {
				exc.printStackTrace(); 
			}
		}
		System.out.println(content.toString());
		System.exit(2);
		return content.toString();
	}
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Hashtable ctHash = new Hashtable();
		ctHash.put("q", "ciravegna");
		ctHash.put("submit", "Search");
		ctHash.put("sort","rel");
		ctHash.put("ic", "0");      // Include citations
		FormPosting qf = new FormPosting();

		qf.postRequest("http://citeseerx.ist.psu.edu/search", ctHash);
	}

}
