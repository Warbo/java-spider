import java.net.*;

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
	public boolean keepRunning;
	
	public Handler() {
		cache = new Cache();
		spider = new Spider(cache);
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

	public void run() {
		keepRunning = true;
		while (keepRunning) {
			spider.iterate();
		}
	}
	
}
