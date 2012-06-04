package com.bluevia.commons.connector.http.oauth;

import java.util.HashMap;

import oauth.signpost.OAuthProviderListener;

import com.bluevia.commons.connector.IAuth;
import com.bluevia.commons.exception.BlueviaException;

/**
 * Interface to include the functions to implement the complete OAuth workflow
 * 
 * @author Telefonica R&D
 */
public interface IOAuth extends IAuth {

	/**
	 * Retrieves a request token
	 * 
	 * @param callback
	 * @param data
	 * @param listener
	 * @return
	 * @throws BlueviaException
	 */
    public RequestToken getRequestToken(String callback, byte[] data, OAuthProviderListener listener) throws BlueviaException;
	
	/**
	 * Retrieves an access token
	 * 
	 * @param requestToken the request token previously obtained
	 * @param oauthVerifier the oauth verifier to the token
	 * @return the resulting access token
	 * @throws BlueviaException
	 */
	public OAuthToken getAccessToken(OAuthToken requestToken, String oauthVerifier) throws BlueviaException;
	
	/**
	 * Returns the stored oauth token
	 * 
	 * @return the stored oauth token
	 */
	public OAuthToken getOauthToken();
	
	/**
	 * Sets the oauth token
	 * 
	 * @param the oauth token
	 */
	public void setOauthToken(OAuthToken token);
	
	/**
	 * Sets the Oauth additional parameters
	 * 
	 * @param the Oauth additional parameters
	 */
	public void setAdditionalParameters(HashMap<String, String> params);
	
}
