package de.timroes.axmlrpc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.security.InvalidParameterException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

/**
 * An AuthenticationManager that can handle digest HTTP authentication.
 * 
 * @author Eric Kok
 */
public class DigestAuthenticationManager implements AuthenticationManager {
	
	private String user;
	private String pass;

	/**
	 * Clear the username and password. No digest HTTP authentication will be used
	 * in the next calls.
	 */
	@Override
	public void clearAuthData() {
		this.user = null;
		this.pass = null;
	}
	
	/**
	 * Set the username and password that should be used to perform digest http authentication.
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
	 * Prepare the authentication at the HttpURLConnection to try basic authentication first.
	 * 
	 * @param http The HttpURLConnection to set authentication.
	 */
	@Override
	public void prepareAuthentication(HttpURLConnection http) {
		
		if(user == null || pass == null 
				|| user.length() <= 0 || pass.length() <= 0) {
			throw new InvalidParameterException("Digest authentication require a username and password");
		}

		// But nothing to do here (we wait until the first request returns the WWW-Authenticate header)
	}
	
	@Override
	public HttpURLConnection renewAuthentification(HttpURLConnection http) {
		
		// Implementation based on https://gist.github.com/slightfoot/5624590
		
		// See if we received a digest response header
		String auth = http.getHeaderField("WWW-Authenticate");
		if(auth == null || !auth.startsWith("Digest ")){
			return null;
		}
		final HashMap<String, String> authFields = splitAuthFields(auth.substring(7));
		
		MessageDigest md5 = null;
		try{
			md5 = MessageDigest.getInstance("MD5");
		}
		catch(NoSuchAlgorithmException e){
			return null;
		}
		
		String HA1 = null;
		try{
			md5.reset();
			String ha1str = this.user + ":" + authFields.get("realm") + ":" + this.pass;
			md5.update(ha1str.getBytes("ISO-8859-1"));
			byte[] ha1bytes = md5.digest();
			HA1 = bytesToHexString(ha1bytes);
		}
		catch(UnsupportedEncodingException e){
			return null;
		}
		
		String HA2 = null;
		try{
			md5.reset();
			String ha2str = http.getRequestMethod() + ":" + http.getURL().getPath();
			md5.update(ha2str.getBytes("ISO-8859-1"));
			HA2 = bytesToHexString(md5.digest());
		}
		catch(UnsupportedEncodingException e){
			return null;
		}
		
		String HA3 = null;
		try{
			md5.reset();
			String ha3str = HA1 + ":" + authFields.get("nonce") + ":" + HA2;
			md5.update(ha3str.getBytes("ISO-8859-1"));
			HA3 = bytesToHexString(md5.digest());
		}
		catch(UnsupportedEncodingException e){
			return null;
		}
		
		StringBuilder sb = new StringBuilder(128);
		sb.append("Digest ");
		sb.append("username").append("=\"").append(this.user               ).append("\",");
		sb.append("realm"   ).append("=\"").append(authFields.get("realm") ).append("\",");
		sb.append("nonce"   ).append("=\"").append(authFields.get("nonce") ).append("\",");
		sb.append("uri"     ).append("=\"").append(http.getURL().getPath() ).append("\",");
		//sb.append("qop"     ).append('='  ).append("auth"                  ).append(",");
		sb.append("response").append("=\"").append(HA3                     ).append("\"");
		
		try{
			final HttpURLConnection result = (HttpURLConnection)http.getURL().openConnection();
			result.addRequestProperty("Authorization", sb.toString());
			return result;
		}
		catch(IOException e){
			return null;
		}
	}
	
	private static HashMap<String, String> splitAuthFields(String authString)
	{
		final HashMap<String, String> fields = new HashMap<String, String>();
		String[] commas = authString.split(",");
		for (String comma : commas) {
			String[] equals = comma.trim().split("=");
			fields.put(equals[0].trim(), equals[1].trim().substring(1, equals[1].trim().length() - 1));
		}
		return fields;
	}
	
	private static final String HEX_LOOKUP = "0123456789abcdef";
	private static String bytesToHexString(byte[] bytes)
	{
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		for(int i = 0; i < bytes.length; i++){
			sb.append(HEX_LOOKUP.charAt((bytes[i] & 0xF0) >> 4));
			sb.append(HEX_LOOKUP.charAt((bytes[i] & 0x0F) >> 0));
		}
		return sb.toString();
	}
	
}