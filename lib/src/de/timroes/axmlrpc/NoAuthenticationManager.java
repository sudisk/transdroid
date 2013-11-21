package de.timroes.axmlrpc;

import java.net.HttpURLConnection;
import java.security.InvalidParameterException;

/**
 * Specific case for when http requests are not authenticated.
 * 
 * @author Eric Kok
 */
public class NoAuthenticationManager implements AuthenticationManager {

	@Override
	public void clearAuthData() {
	}

	@Override
	public void setAuthData(String user, String pass) {
		throw new InvalidParameterException("The NoAuthenticaionManager explicitly does not support authentication.");
	}

	@Override
	public void prepareAuthentication(HttpURLConnection http) {
	}

	@Override
	public HttpURLConnection renewAuthentification(HttpURLConnection http) {
		return null;
	}

}
