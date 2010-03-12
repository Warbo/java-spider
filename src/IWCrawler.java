import java.net.URL;
import java.util.List;

interface myIWCrawler {
	
	public void startIWCrawler(final URL mySeed);

	public boolean isIWRobotSafe(final URL myUrl);

	public void stopIWCrawler ();

	public void resumeIWCrawler ();

	public void killIWCrawler ();

	public List<URL> getLocalIWUrls();

	public List<URL> getExternalIWURLs();

}
