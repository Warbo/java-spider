/**
 * This allows us to GOTO handlers when a HTTP access doesn't return 200:OK.
 */

/**
 *
 */
public class AccessException extends Exception {

	public String returnCode;
	
	public AccessException(String codeReceived) {
		returnCode = codeReceived;
	}
	
}
