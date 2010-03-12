import java.net.URL;
import java.util.*;

/*
 * API translation between the spider and the required interface.
 */

public class Handler implements myIWCrawler {

	private Spider spider;
	private Cache cache;
	
	public void startIWCrawler(final URL mySeed) {
		cache = new Cache();
		spider = new Spider(cache);
		spider.start_with(mySeed);
	}

	public boolean isIWRobotSafe(final URL myUrl) {
		//TODO: Implement passing a URL
		//return spider.in_robots(myUrl);
		return true;
	}

	public void stopIWCrawler () {
		cache = spider.dumpCache();
	}

	public void resumeIWCrawler () {
		spider = new Spider(cache);
		spider.start();
	}

	public void killIWCrawler () {
		// TODO: Would be nice to use threads here
	}

	public List<URL> getLocalIWUrls() {
		cache = spider.dumpCache();
		return cache.get_local_urls();
	}

	public List<URL> getExternalIWURLs() {
		cache = spider.dumpCache();
		// TODO: Get pages for a domain, then get links for each page
		return cache.get_external_urls();
	}
}