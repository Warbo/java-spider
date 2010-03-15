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
	private Map<URL, Boolean> in_robots;
	private Map<URL, List<URL>> links_to;
	private Map<URL, List<URL>> links_from;
	private Map<URL, String> data;
	private Map<URL, Integer> last_checked;
	private List<URL> unprocessed;

	/*
	 * Make a new, empty cache.
	 */
	public Cache() {
		//TODO: Put templates here!
		in_robots = new Hashtable<URL, Boolean>();
		links_to = new Hashtable<URL, List<URL>>();
		links_from = new Hashtable<URL, List<URL>>();
		data = new Hashtable<URL, String>();
		last_checked = new Hashtable<URL, Integer>();
		unprocessed = new LinkedList<URL>();
	}

	/*
	 * Returns a URL from the cache which has not yet been processed.
	 */
	public URL get_unprocessed() {
		return null;
	}

	public static URL get_domain(URL currentURL) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean checked_robots(URL currentURL) {
		return in_robots.containsKey(currentURL);
	}

	public boolean in_robots(URL currentURL) {
		// TODO Auto-generated method stub
		return false;
	}

	public List<URL> get_local_urls() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<URL> get_external_urls() {
		return null;
	}

}
	