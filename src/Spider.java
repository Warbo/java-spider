/**
 * This class implements a simple Web spider
 */

import java.io.*;
import java.net.*;

public class Spider {

	// Stores our findings
	private Cache webCache;
	private URL currentURL;
	public boolean debug;

	/**
	 * Create a new Spider with the given cache, allowing it to carry on where
	 * it left off.
	 */
	public Spider(Cache old_cache) {
		// Disable this to stop printout
		debug = true;
		
		webCache = old_cache;
		currentURL = null;
	}

	/**
	 * Grabs a new, unprocessed URL from the cache (from a different domain if
	 * possible), and sets it as the working URL.
	 * @return Whether a new URL has been set (ie. if there was one in the queue)
	 */
	private boolean next_url() {
		try {
			// TODO: Maybe change this from being infinite?
			while (true) {
				// Grab a URL
				currentURL = webCache.get_unprocessed();
			
				// We get null if all URLs have been accessed recently
				if (currentURL == null) {
					// Wait for the loop to restart
				}
				else {
					// If we get a valid URL then use it
					if (debug) {
						System.out.println("New URL: "+currentURL.toString());
					}
					return true;
				}
			}
		}
		catch (Exception e) {
			// An exception will be thrown if there are no more URLs
			return false;
		}
	}

	/**
	 * Attempts to download the data from the working URL.
	 * TODO: Handle things other than "OK"
	 */
	private void get() {
		
		if (debug) {
			System.out.println("Getting data at "+currentURL.toString());
		}
		
		try {
			webCache.setData(currentURL, WebGet.get(currentURL));
		}
		catch (Exception e) {
			
		}
	}

	/**
	 * Looks for a robots.txt in the domain of the working URL.
	 * @return Whether the Spider's current working URL is forbidden by
	 * a robots.txt
	 */
	private boolean in_robots() {
		//See if we have checked this domain for a robots.txt yet
		return in_robots(currentURL);
	}

	/**
	 * Looks for a robots.txt in the domain of the provided URL.
	 * @return Whether the given URL is forbidden by a robots.txt
	 */
	public boolean in_robots(URL myUrl) {
		if (debug) {
			System.out.println("Looking for robots.txt for "+myUrl.toString());
		}
		
		//See if we have checked this domain for a robots.txt yet
		if (!webCache.checked_robots(myUrl)) {
			// If we haven't then do that now
			String domain = myUrl.getHost();
			// Add robots.txt to this domain
			String robot = domain + "/robots.txt";
			
			//////////////////////////////////////////////////////////////////
			// SPECIAL CASE: If the domain is http://ext.dcs.shef.ac.uk the //
			// file is http://ext.dcs.shef.ac.uk/~u0082/intelweb/robots.txt //
			// due to the assignment, so check for that here!               //
			//////////////////////////////////////////////////////////////////
			if (domain.equals("http://ext.dcs.shef.ac.uk")) {
				robot = "http://ext.dcs.shef.ac.uk/~u0082/intelweb/robots.txt";
			}
			
			// Try to download it
			try {
				webCache.addRobot(domain, WebGet.get(new URL(robot)));
			}
			catch (Exception e) {
				// TODO: Handle exceptions when accessing the URL
			}
		}
		// New we definitely have a robots.txt, if there is one, so we can see
		// if we're in it
		return webCache.in_robots(myUrl);
	}
	
	/**
	 * Performs the desired operations on the downloaded data (eg. keyword
	 * indexing)
	 */
	private void process() {
		if (debug) {
			System.out.println("Processing "+currentURL.toString());
		}
	}

	/**
	 * Set up the spider with the given URL.
	 */
	public void start_with(URL startURL) {
		currentURL = startURL;
	}

	/**
	 * Start the spider, supplying no URL.
	 */
	public void iterate() throws FinishedException {
		// If we have no URL then see if we can get one from the cache.
		// If not then exit.
		if (currentURL == null) {
			if (!next_url()) {
				
				if (debug) {
					System.out.println("No more URLs.");
				}
				
				throw new FinishedException();
			}
		}

		// If we have reached here then we have a URL set, either from
		// start_with or next_url, so we can do something.
		
		// Ensure that the working URL is not in a robots.txt
		if (!in_robots()) {
			// If not then we're free to go ahead.

			// Get the data
			get();

			// Act on it
			process();
		}
		
	}

	/**
	 * Returns a copy of the Spider's cache. Do not modify this copy.
	 * @return The Cache being used by this Spider.
	 */
	public Cache dumpCache() {
		// TODO: Implement a proper deep-copy of the cache. This requires
		// traversing all of the lists and maps, appending copies of
		// everything to new lists and maps WITHOUT using temporary bindings,
		// since that would make all new entries point to the temporary
		// names, and thus break it all
		// To get things up and running, just send a pointer to the cache for now
		return webCache;
	}
	
}