package com.bluevia.oauth.client;

import java.io.IOException;

import com.bluevia.commons.Utils;
import com.bluevia.commons.connector.http.oauth.OAuthToken;
import com.bluevia.commons.connector.http.oauth.RequestToken;
import com.bluevia.commons.exception.BlueviaException;

/**
 *  This class provides access to the set of functions to complete the OAuth workflow to
 *  retrieve the OAuth credentials for Bluevia applications
 *
 * @author Telefonica R&D
 *
 */
public class BVOauth extends BVOauthClient {

	/**
	 * Constructor
	 * 
	 * @param mode mode
	 * @param consumerToken the consumer token
	 * @param consumerSecret the consumer secret
	 * @throws BlueviaException
	 */
	public BVOauth(Mode mode, String consumerToken, String consumerSecret) throws BlueviaException {
		super(mode, consumerToken, consumerSecret);
	}
	
	/**
	 * Retrieves a request token (redirect to callback)
	 * 
	 * @param callback the string of callback address
	 * @return the request token
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public RequestToken getRequestToken(String callback) throws BlueviaException {
		return super.getRequestToken(callback);
	}

	/**
	 * Retrieves a request token (Out of Band mode)
	 * 
	 * @return the request token
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public RequestToken getRequestToken() throws BlueviaException {
		return super.getRequestToken();
	}
	

	/**
	 * Retrieves a request token using the Bluevia SMS handshake
	 * The oauth verifier will be received vía SMS in the phone number specified as a parameter, instead of
	 * getting a verification url.
	 * 
	 * @param phoneNumber the phone number to receive the SMS with the oauth verifier (PIN code)
	 * @return the request token
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public RequestToken getRequestTokenSmsHandshake(String phoneNumber) throws BlueviaException {

		if (mMode != Mode.LIVE)
			throw new BlueviaException("OAuth SMS Handshake only available in LIVE mode", BlueviaException.INVALID_MODE_EXCEPTION);
		
		if (Utils.isEmpty(phoneNumber) || !Utils.isPhoneNumber(phoneNumber))
			throw new BlueviaException("Bad request: The parameter 'phoneNumber' is not a valid phone number", BlueviaException.BAD_REQUEST_EXCEPTION);
		
		RequestToken token = super.getRequestToken(phoneNumber);	  
		token.setVerificationUrl(null);
		return token;	
	}
	
	/**
	 * Retrieves the access token corresponding to request token parameter
	 * 
	 * @param oauthVerifier the OAuth verifier for the token
	 * 
	 * @return the access token
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public OAuthToken getAccessToken(String oauthVerifier) throws BlueviaException {
		return super.getAccessToken(oauthVerifier);
	}

	/**
	 * Retrieves the access token corresponding to request token parameter
	 * 
	 * @param oauthVerifier the OAuth verifier for the token
	 * @param token the request token received previously
	 * @param secret request token secret
	 * 
	 * @return the access token
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public OAuthToken getAccessToken(String oauthVerifier, String token, String secret) throws BlueviaException {
		return super.getAccessToken(oauthVerifier, token, secret);
	}
}
