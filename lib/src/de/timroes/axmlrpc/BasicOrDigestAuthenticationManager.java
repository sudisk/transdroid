package de.timroes.axmlrpc;

import java.net.HttpURLConnection;

/**
 * An AuthenticationManager that can handle both basic and digest HTTP authentication.
 * 
 * @author Eric Kok
 */
public class BasicOrDigestAuthenticationManager implements AuthenticationManager {

	private final BasicAuthenticationManager basicAuth = new BasicAuthenticationManager();
	private final DigestAuthenticationManager digestAuth = new DigestAuthenticationManager();
	
	/**
	 * Clear the username and password. No HTTP authentication will be used
	 * in the next calls.
	 */
	@Override
	public void clearAuthData() {
		this.basicAuth.clearAuthData();
		this.digestAuth.clearAuthData();
	}
	
	/**
	 * Set the username and password that should be used to perform http authentication.
	 * 
	 * @param user Username
	 * @param pass Password
	 */
	@Override
	public void setAuthData(String user, String pass) {
		this.basicAuth.setAuthData(user, pass);
		this.digestAuth.setAuthData(user, pass);
	}

	/**
	 * Prepare the authentication at the HttpURLConnection to try basic authentication first.
	 * 
	 * @param http The HttpURLConnection to set authentication.
	 */
	@Override
	public void prepareAuthentication(HttpURLConnection http) {

		// Prepare as basic auth (when this fails, 
		basicAuth.prepareAuthentication(http);
		
	}

	@Override
	public HttpURLConnection renewAuthentification(HttpURLConnection http) {
		
		// Failed the basic auth, so try digest auth instead
		return digestAuth.renewAuthentification(http);
		
	}
	
}