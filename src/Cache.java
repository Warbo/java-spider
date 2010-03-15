/*
 * This implements a cache for our spider. It stores:
 * unprocessed_urls
 * failed_urls
 * domain->robots.txt
 * domain->links_to
 * domain->links_from
 * domain->data_or_error
 * domain->last_checked
 */

import java.util.*;
import java.io.*;
import java.net.*;

public class Cache {

	// Use HashTables for our storage, since they're O(1)
	private Map<String, List<String>> in_robots;
	private Map<URL, List<URL>> links_to;
	private Map<URL, List<URL>> links_from;
	private Map<URL, String> data;
	private Map<String, Integer> last_checked;
	private List<URL> unprocessed;
	
	// The useragent string we're using
	private String userAgent;
	
	// How long to leave between fetches from the same domain, in seconds
	private int check_interval;

	/*
	 * Make a new, empty cache.
	 */
	public Cache() {
		// If we haven't been given a user agent string then default to this
		init("University of Sheffield Assignment", 5);
	}
	
	/*
	 * Sets up the cache
	 */
	private void init(String agent, int interval) {
		in_robots = new Hashtable<String, List<String>>();
		links_to = new Hashtable<URL, List<URL>>();
		links_from = new Hashtable<URL, List<URL>>();
		data = new Hashtable<URL, String>();
		last_checked = new Hashtable<String, Integer>();
		unprocessed = new LinkedList<URL>();
		userAgent = agent;
		check_interval = interval;
	}

	/*
	 * Returns a URL from the cache which has not yet been processed.
	 */
	public URL get_unprocessed() throws Exception {
		if (unprocessed.size() == 0) {
			throw new Exception("No URLs available");
		}
		
		// Walk through our entire unprocessed list
		for (int i=0; i<unprocessed.size(); i++) {
			// Don't use this URL if we've visited the domain recently
			if (last_checked.containsKey(unprocessed.get(i).getHost()) &&
					last_checked.get(unprocessed.get(i).getHost()).intValue() < 
						((int) System.currentTimeMillis() / 1000) - check_interval) {
				
				// Don't return this one
			}
			else {
				return unprocessed.remove(i);
			}
		}
		// If we've reached here then we have no valid URLs, so wait a second
		try {
			Thread.sleep(1000);
		}
		catch (Exception e) {
			// Not worth handling
		}
		return null;
	}

	public boolean checked_robots(URL currentURL) {
		return in_robots.containsKey(currentURL.getHost());
	}

	public boolean in_robots(URL currentURL) {
		// If everything's banned in the domain then return true
		if (in_robots.get(currentURL.getHost()).contains("/")) {
			return true;
		}
	
		// If not then we'll have to dig a bit deeper
		String[] path = currentURL.getPath().split("/");
		
		// Keep a temporary path which we can build up
		String currentPath;
		for (int i=0; i<path.length; i++) {
			currentPath = "";
			
			// Loop through each directory we're up to
			for (int j=0; j<i; j++) {
				currentPath = currentPath + "/" + path[j];
			}
			
			// See if our partial path is blocked
			if (in_robots.get(currentURL.getHost()).contains(currentPath)) {
				return true;
			}
		}
		
		// If we survived the above then we mustn't be banned from the given URL
		return false;
	}

	public void setData(URL currentURL, String newData) {
		data.put(currentURL, newData);
		
		last_checked.put(currentURL.getHost(), new Integer((int) System.currentTimeMillis() / 1000));

	}
	
	/*
	 * Adds all of the contents of the robots.txt to the cache
	 */
	public void addRobot(String domain, String contents) {
		// Split our robots.txt into an array of each line
		String[] lines = contents.split("\n");
		// This will contain the user-agent string being banned
		String currentAgent = null;
		// Tells us whether the rules being parsed apply to us or not
		boolean take_heed = false;
		
		// Loop through each line
		for (int i=0; i<lines.length; i++) {
			// Only look at what's before a comment character ("#")
			String valid_line = lines[i].split("#")[0];
			// Only bother acting if the line's not empty or all commented
			if (!valid_line.trim().equals("")) {
				
				// If we're on a line defining the banned user-agent then see
				// what it says
				if (valid_line.contains("User-agent:")) {
					currentAgent = valid_line.replaceFirst("User-agent:", "").trim();
					
					// We need to take note of the coming rules if the string matches
					// our user agent or the wildcard *
					if (currentAgent.equals(userAgent) || currentAgent.equals("*")) {
						take_heed = true;
					}
				}
				else {
					if (valid_line.contains("Disallow:") && take_heed) {
						// If this is our first rule then make the required list
						if (!in_robots.containsKey(domain)) {
							in_robots.put(domain, new LinkedList<String>());
						}
						
						// New we know there's a rules list, so append to it
						List<String> rules = in_robots.get(domain);
						rules.add(valid_line.replaceFirst("Disallow:", "").trim());
						in_robots.put(domain, rules);
					}
				}
			}
		}
	}
	
	public List<URL> get_local_urls() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<URL> get_external_urls() {
		return null;
	}

}
	