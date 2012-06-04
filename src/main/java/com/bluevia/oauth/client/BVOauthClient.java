/**
 * \package com.bluevia.oauth.client This package contains the clients in order to get the OAuth credentials for Bluevia applications
 */
package com.bluevia.oauth.client;

import java.io.IOException;

import oauth.signpost.OAuthProviderListener;

import org.apache.log4j.Logger;

import com.bluevia.commons.GsdpConstants;
import com.bluevia.commons.Utils;
import com.bluevia.commons.client.BVBaseClient;
import com.bluevia.commons.connector.http.oauth.IOAuth;
import com.bluevia.commons.connector.http.oauth.OAuthToken;
import com.bluevia.commons.connector.http.oauth.OauthHttpConnector;
import com.bluevia.commons.connector.http.oauth.RequestToken;
import com.bluevia.commons.exception.BlueviaException;
import com.bluevia.commons.exception.ConnectorException;

/**
 *  This class provides access to the set of functions to complete the OAuth workflow to
 *  retrieve the OAuth credentials for Bluevia applications
 *
 * @author Telefonica R&D
 *
 */
public abstract class BVOauthClient extends BVBaseClient {

	private static Logger log = Logger.getLogger(BVOauthClient.class.getName());

	protected RequestToken mRequestToken;

	/**
	 * Creates an BVOauthClient 1.0a an instantiate the OauthConsumer and OauthProvider with the
	 * information supplied
	 * @param mode mode
	 * @param consumerToken the consumer token
	 * @param consumerSecret the consumer secret
	 * 
	 * @throws BlueviaException
	 */
	public BVOauthClient(Mode mode, String consumerToken, String consumerSecret) throws BlueviaException {
		initCommon(mode);
		String authorizeUri = null;
		switch (mode){
		case LIVE:
			authorizeUri = GsdpConstants.AUTHORIZE_URL_CONNECT;
			break;
		case TEST:
		case SANDBOX:
			authorizeUri = GsdpConstants.AUTHORIZE_URL_DEVELOPERS;
			break;
		}
		this.mConnector = new OauthHttpConnector(new OAuthToken(consumerToken, consumerSecret), 
				GsdpConstants.REQUEST_TOKEN_URL, GsdpConstants.ACCESS_TOKEN_URL, authorizeUri);
		
		
	}

	/**
	 * Retrieves a request token
	 * 
	 * @return the request token
	 * @throws BlueviaException
	 */
	protected RequestToken getRequestToken() throws BlueviaException {
		return getRequestToken(null);
	}

	/**
	 * Retrieves a request token using a callback parameter
	 * 
	 * @param callback the callback to redirect the application once the request token has been authorized
	 * @return the request token
	 * @throws BlueviaException
	 */
	protected RequestToken getRequestToken(String callback) throws BlueviaException {
		return getRequestToken(callback, null, null);
	}
	
	/**
	 * Extension of getRequestToken to include content in the HTTP body
	 * 
	 * @param callback the callback to redirect the application once the request token has been authorized
	 * @param data the data to include in the body
	 * @param listener the OAuthProviderListener to manage the data
	 * @return the request token
	 * @throws BlueviaException
	 */
	protected RequestToken getRequestToken(String callback, byte[] data, OAuthProviderListener listener) throws BlueviaException {
		try {
			mRequestToken = ((IOAuth)mConnector).getRequestToken(callback, data, listener);
			log.info("Obtained request token: " + mRequestToken.getToken() + " :: " + mRequestToken.getSecret());
			log.info("Verification URL: " + mRequestToken.getVerificationUrl());
			return mRequestToken;
		} catch (ConnectorException e){
			String error = "";
			error = parseError(e.getAdditionalData().getBody());
			throw new ConnectorException(error, e.getCode());
		}
	}

	/**
	 * Retrieves the access token corresponding to the last request token received.
	 * 
	 * @param oauthVerifier the OAuth verifier for the token
	 * @return the access token
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	protected OAuthToken getAccessToken(String oauthVerifier) throws BlueviaException {
		return getAccessToken(oauthVerifier, mRequestToken.getSecret(), mRequestToken.getToken());
	}

	/**
	 * Retrieves the access token corresponding to request token parameter
	 * 
	 * @param oauthVerifier the OAuth verifier for the token
	 * @param token the request token received previously
	 * @param secret the secret of the request token received previously
	 * @return the access token
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	protected OAuthToken getAccessToken(String oauthVerifier, String token, String secret) throws BlueviaException {
		try {
			
			if (Utils.isEmpty(token) && Utils.isEmpty(secret)){
				if (mRequestToken != null){
					token = mRequestToken.getToken();
					secret = mRequestToken.getSecret();
				}
			}
			
			OAuthToken accessToken = ((IOAuth)mConnector).getAccessToken(new OAuthToken(token, secret), oauthVerifier);
			log.info("Obtained access token: " + accessToken.getToken() + " :: " + accessToken.getSecret());
			return accessToken;
		} catch (ConnectorException e){
			String error = "";
			error = parseError(e.getAdditionalData().getBody());
			throw new ConnectorException(error, e.getCode());
		}
	}

}

