/**
 * Run and control a Spider, and implement the required API.
 */

import java.net.*;
import java.util.*;
import java.io.*;

/**
 * Control a Spider via its Handler, invoking the latter in a separate thread as and
 * when needed.
 *
 */
public class SpiderProgram implements myIWCrawler {

	// This is the thread running the spider
	private Handler spiderHandler;

	public SpiderProgram() {
		// We need this in case we are asked to resume without
		// actually having run yet.
		spiderHandler = new Handler();
	}
	
	/**
	 * Makes a spider thread and starts it, beginning
	 * with the supplied URL.
	 */
	public void startIWCrawler(final URL mySeed) {
		// Clear whatever we had
		spiderHandler = new Handler();
		
		// Initialise the spider
		spiderHandler.start_with(mySeed);
		
		// Set it crawling
		spiderHandler.start();
	}

	/**
	 * Checks whether the given URL is allowed to be
	 * accessed.
	 * @return Whether the given URL is allowed to be accessed.
	 */
	public synchronized boolean isIWRobotSafe(final URL myUrl) {
		// We may need to download a robots.txt, so
		// we can't use a dump of the cache here!
		
		// If the handler is running then dual-access to
		// the cache could corrupt it, so we need a method
		// of checking this URL when the handler's running
		// and one for when it's not.
		if (spiderHandler.isAlive()) {
			
			// If the handler is running then add this URL
			// to its set of URLs awaiting robot checks
			// This is safe as we're the only thread adding
			// to this set
			spiderHandler.awaitingRobotCheck.add(myUrl);
			
			// The handler should get around to processing this
			// in URL, so be patient
			while (!spiderHandler.robotResults.containsKey(myUrl)) {
				
				// Of course, if the handler has died then we won't
				// get an answer, but in this case it's safe to get
				// it ourselves
				if (!spiderHandler.isAlive()) {
					return spiderHandler.spider.in_robots(myUrl);
				}
				
				// If the handler's running then wait for a bit
				// before checking again
				try{
					Thread.sleep(100);
				}
				catch (Exception e) {
					// Not worth handling
				}
			}
			// If we reach this point then the robotResults must
			// contain our URL, since we're the only thread which
			// removes anything from this map.
			return spiderHandler.robotResults.remove(myUrl).booleanValue();
			
		}
		
		// If the handler's not running then go ahead and check the URL
		else {
			return spiderHandler.spider.in_robots(myUrl);
		}
	}

	/**
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
		
		// Wait for the handler to die
		while (spiderHandler.isAlive()) {
			try {
				Thread.sleep(100);
			}
			catch (InterruptedException e) {
				// Not worth handling
			}
		}
		
		// The spider should no-longer be running
		// so we may as well make a duplicate of
		// its cache at this point
		spiderHandler.cache = spiderHandler.spider.dumpCache();
	}

	/**
	 * Start a spider handler, based on the contents
	 * of any previous handler. Is not guaranteed to
	 * work for killed spiders.
	 */
	public void resumeIWCrawler() {
		// If we have a stopped spider then we'll
		// have its state in our cache.
		spiderHandler = new Handler(spiderHandler.spider.dumpCache());
		spiderHandler.start();
	}

	/**
	 * Forcibly stop the spider, potentially leaving
	 * us in an inconsistent state.
	 */
	public void killIWCrawler() {
		spiderHandler.destroy();
	}

	public List<URL> getLocalIWUrls() {
		Cache cache = spiderHandler.spider.dumpCache();
		return cache.get_local_urls();
	}

	public List<URL> getExternalIWURLs() {
		Cache cache = spiderHandler.spider.dumpCache();
		// TODO: Get pages for a domain, then get links for each page
		return cache.get_external_urls();
	}
	
	public static void main(String[] args) {
		SpiderProgram main_thread = new SpiderProgram();
		
		// Start, for the assignment, with the following URL
		String givenURL = "http://ext.dcs.shef.ac.uk/~u0082/intelweb/";
		URL DCS_test = null;
		try {
			DCS_test = new URL(givenURL);
		}
		catch (MalformedURLException e) {
			System.out.println("URL "+givenURL+" is malformed.");
			System.exit(1);
		}
		// Now, before we access anything, check for robots.txt permissions
		if (!main_thread.isIWRobotSafe(DCS_test)) {
			System.out.println("Not allowed to access "+givenURL+"!");
			System.exit(1);
		}
		
		System.out.println("Starting spider...");
		
		// We should be safe to continue now
		main_thread.startIWCrawler(DCS_test);
		
		System.out.println("Spider is operational.");
	
		// Simple controls to allow killing the spider
		System.out.println("Controls: K = kill, S = stop, R = resume, P = print stats.");
		
		BufferedReader commandline = new BufferedReader(new InputStreamReader(System.in));

		String command = null;
		boolean suspended = false;
		
		while (main_thread.spiderHandler.isAlive() || suspended) {
			try {
				command = commandline.readLine();
			}
			catch (IOException e) {
				System.out.println("Interface gave error, control is lost so killing spider!");
				main_thread.killIWCrawler();
				System.exit(1);
			}
			if (command.contains("K")) {
				main_thread.killIWCrawler();
			}
			else if(command.contains("S")) {
				main_thread.stopIWCrawler();
				suspended = true;
			}
			else if(command.contains("R")) {
				main_thread.resumeIWCrawler();
				suspended = false;
			}
			else if(command.contains("P")) {
				Cache currentCache = main_thread.spiderHandler.spider.dumpCache();
				System.out.println(currentCache);
			}
		}

		System.out.println("Spider has died, exiting.");
		
	}

}
