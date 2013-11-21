package de.timroes.axmlrpc;

import de.timroes.base64.Base64;
import java.net.HttpURLConnection;
import java.security.InvalidParameterException;

/**
 * The AuthenticationManager handle basic HTTP authentication.
 * 
 * @author Tim Roes
 */
public class BasicAuthenticationManager implements AuthenticationManager {
	
	private String user;
	private String pass;

	/**
	 * Clear the username and password. No basic HTTP authentication will be used
	 * in the next calls.
	 */
	@Override
	public void clearAuthData() {
		this.user = null;
		this.pass = null;
	}
	
	/**
	 * Set the username and password that should be used to perform basic
	 * http authentication.
	 * 
	 * @param user Username
	 * @param pass Password
	 */
	@Override
	public void setAuthData(String user, String pass) {
		this.user = user;
		this.pass = pass;
	}

	/**
	 * Set the authentication at the HttpURLConnection.
	 * 
	 * @param http The HttpURLConnection to set authentication.
	 */
	@Override
	public void prepareAuthentication(HttpURLConnection http) {
		
		if(user == null || pass == null 
				|| user.length() <= 0 || pass.length() <= 0) {
			throw new InvalidParameterException("Basic authentication require a username and password");
		}

		String base64login = Base64.encode(user + ":" + pass);

		http.addRequestProperty("Authorization", "Basic " + base64login);
		
	}

	/**
	 * Not supported by this authentication manager.
	 */
	@Override
	public HttpURLConnection renewAuthentification(HttpURLConnection http) {
		return null;
	}
	
}