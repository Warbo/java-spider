/*
 * This implements a cache for our spider. It stores:
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