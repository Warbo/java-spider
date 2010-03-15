/*
 * This class implements a simple Web spider
 */

import java.io.*;
import java.net.*;

public class Spider {

	// Stores our findings
	private Cache webCache;
	private URL currentURL;

	/*
	 * Create a new Spider with the given cache, allowing it to carry on where
	 * it left off.
	 */
	public Spider(Cache old_cache) {
		webCache = old_cache;
		currentURL = null;
	}

	/*
	 * Grabs a new, unprocessed URL from the cache (from a different domain if
	 * possible), and sets it as the working URL.
	 */
	private boolean next_url() {
		try {
			currentURL = webCache.get_unprocessed();
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	/*
	 * Attempts to download the data from the working URL.
	 * TODO: Handle things other than "OK"
	 */
	private void get() {
		
	}

	/*
	 * Looks for a robots.txt in the domain of the working URL.
	 */
	private boolean in_robots() {
		//See if we have checked this domain for a robots.txt yet
		if (!webCache.checked_robots(currentURL)) {
			// If we haven't then do that now
			URL domain = Cache.get_domain(currentURL);
			// TODO: Add robots.txt to this domain
			// TODO: Try to download it
		}
		// New we definitely have a robots.txt, if there is one, so we can see
		// if we're in it
		return webCache.in_robots(currentURL);
		
	}

	/*
	 * Performs the desired operations on the downloaded data (eg. keyword
	 * indexing)
	 */
	private void process() {
	}

	/*
	 * Set up the spider with the given URL.
	 */
	public void start_with(URL startURL) {
		currentURL = startURL;
	}

	/*
	 * Start the spider, supplying no URL.
	 */
	public void iterate() {
		// If we have no URL then see if we can get one from the cache.
		// If not then exit.
		if (currentURL == null) {
			if (!next_url()) {
				System.out.println("No URL to work with.");
				System.exit(0);
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

	public Cache dumpCache() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean in_robots(URL myUrl) {
		// TODO Auto-generated method stub
		return false;
	}
	
}