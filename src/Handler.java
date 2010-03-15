import java.net.*;
import java.util.concurrent.*;

/**
 * Handler is a thread which controls its own Spider
 */

/**
 * 
 *
 */
public class Handler extends Thread {

	protected Spider spider;
	protected Cache cache;
	public ConcurrentSkipListSet<URL> awaitingRobotCheck;
	public ConcurrentHashMap<URL, Boolean> robotResults;
	private URL currentlyChecking;
	public boolean keepRunning;
	
	public Handler() {
		cache = new Cache();
		spider = new Spider(cache);
		awaitingRobotCheck = new ConcurrentSkipListSet<URL>();
		currentlyChecking = null;
		robotResults = new ConcurrentHashMap<URL, Boolean>();
		keepRunning = false;
	}
	
	public Handler(Cache startCache) {
		cache = startCache;
		spider = new Spider(cache);
		keepRunning = false;
	}

	public void start_with(URL mySeed) {
		spider.start_with(mySeed);
	}

	/*
	 * Keep crawling until keepRunning is set to false
	 */
	public void run() {
		keepRunning = true;
		int count = 0;
		try {
			while (keepRunning && count < 5) {
				
				// See if we've been asked to check any URLs for robots.txt
				while (!awaitingRobotCheck.isEmpty()) {
					currentlyChecking = awaitingRobotCheck.last();
					robotResults.put(currentlyChecking, new Boolean(spider.in_robots(currentlyChecking)));
					awaitingRobotCheck.remove(currentlyChecking);
					currentlyChecking = null;
				}
				
				// Crawl another page
				spider.iterate();
				count++;
			}
		}
		catch (FinishedException e) {
			// Finished crawling
		}
	}
	
}
