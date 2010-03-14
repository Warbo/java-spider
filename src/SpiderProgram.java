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
		spiderHandler = new Handler();
		spiderHandler.start_with(mySeed);
	}

	/*
	 * Checks whether the given URL is allowed to be
	 * accessed.
	 */
	public boolean isIWRobotSafe(final URL myUrl) {
		// We may need to download a robots.txt, so
		// we can't use a dump of the cache here
		return spiderHandler.spider.in_robots(myUrl);
	}

	/*
	 * Stops any running spider threads (after they've
	 * finished what they're in the middle of).
	 */
	public void stopIWCrawler() {
		// We can't use stop() since it's
		// fundamentally flawed, so set this to
		// false so that the handler knows to
		// stop. This is not immediate, so that's
		// why we also have a kill method.
		spiderHandler.keepRunning = false;
		
		// The spider should no-longer be running
		// so we may as well make a duplicate of
		// its cache at this point
		spiderHandlercache = spiderHandler.getCache();
	}

	/*
	 * Start a spider handler, based on the contents
	 * of any previous handler. Is not guaranteed to
	 * work for killed spiders.
	 */
	public void resumeIWCrawler() {
		// If we have a stopped spider then we'll
		// have its state in our cache.
		spiderHandler = new Handler(spiderHandler.getCache());
		spiderHandler.start();
	}

	/*
	 * Forcibly stop the spider, potentially leaving
	 * us in an inconsistent state.
	 */
	public void killIWCrawler() {
		spiderHandler.destroy()
	}

	public List<URL> getLocalIWUrls() {
		spiderHandler.getCache();
		return cache.get_local_urls();
	}

	public List<URL> getExternalIWURLs() {
		cache = spider.dumpCache();
		// TODO: Get pages for a domain, then get links for each page
		return cache.get_external_urls();
	}
