import java.util.HashTable;

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
public class Cache {

	// Use HashTables for our storage, since they're O(1)
	private Map robots;
	private Map links_to;
	private Map links_from;
	private Map data;
	private Map last_checked;

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
	public URL get_unprocessed() {
	}
}
	
