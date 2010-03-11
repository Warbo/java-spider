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

public class Cache {

	// Use HashTables for our storage, since they're O(1)
	private HashTable robots;
	private HashTable links_to;
	private HashTable links_from;
	private HashTable data;
	private HashTable last_checked;

	/*
	 * Make a new, empty cache.
	 */
	public Cache() {
		//TODO: Put templates here!
		robots = new HashTable();
		links_to = new HashTable();
		links_from = new HashTable();
		data = new HashTable();
		last_checked = new HashTable();
	}

	/*
	 * Returns a URL from the cache which has not yet been processed.
	 */
	public get_unprocessed() {
	}

	