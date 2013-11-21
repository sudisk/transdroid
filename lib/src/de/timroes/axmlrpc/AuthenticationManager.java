package de.timroes.axmlrpc;

import java.net.HttpURLConnection;

/**
 * Interface for every type of authentication manager to handle some sort of HTTP authentication.
 * 
 * @author Eric Kok
 */
public interface AuthenticationManager {
	
	/**
	 * Clear any authentication-related local data, such as the username and password.
	 */
	public void clearAuthData();

	/**
	 * Set the username and password that should be used to perform http authentication.
	 * 
	 * @param user Username
	 * @param pass Password
	 */
	public void setAuthData(String user, String pass);

	/**
	 * Prepare some http request for authentification, such as by setting the correct headers.
	 * 
	 * @param http The HttpURLConnection to set authentication on.
	 */
	public void prepareAuthentication(HttpURLConnection http);
	
	/**
	 * Called after a failed first attempt, try to renew the authentication (based on the current response) so we can
	 * retry this http request.
	 * 
	 * @param http The old, failed HttpURLConnection, which now contains a response body and headers.
	 * @return A newly instantiated and initialized HttpURLCOnnection object, or null if not supported.
	 */
	public HttpURLConnection renewAuthentification(HttpURLConnection http);
	
}