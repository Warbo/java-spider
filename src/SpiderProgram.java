/*
 * Run and control a Spider, and implement the required API.
 */

public class SpiderProgram implements myIWCrawler {

	// This is the thread running the spider
	private Handler spiderHandler;

	/*
	 * Makes a spider thread and starts it, beginning
	 * with the supplied URL.
	 */
	public void startIWCrawler(final URL mySeed) {
		cache = new Cache();
		spiderHandler = new Handler(cache);
		spider.start_with(mySeed);
	}

	/*
	 * Checks whether the given URL is allowed to be
	 * accessed.
	 */
	public boolean isIWRobotSafe(final URL myUrl) {
		//TODO: Implement passing a URL
		return spider.in_robots(myUrl);
	}

	/*
	 * Stops any running spider threads.
	 */
	public void stopIWCrawler() {
		try {
			spiderHandler.stop()
		}
		catch (Exception e) {}
	}

	/*
	 * Start a spider handler, based on the contents
	 * of any previous handler.
	 */
	public void resumeIWCrawler() {
		spider = new Spider(cache);
		spider.start();
	}

	/*
	 * Forcibly stop the spider, potentially leaving
	 * us in an inconsistent state.
	 */
	public void killIWCrawler() {
		spiderHandler.destroy()
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
