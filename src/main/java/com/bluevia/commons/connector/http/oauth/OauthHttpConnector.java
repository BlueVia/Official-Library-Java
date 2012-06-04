package com.bluevia.commons.connector.http.oauth;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.log4j.Logger;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.OAuthProviderListener;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import oauth.signpost.http.HttpParameters;

import com.bluevia.commons.Utils;
import com.bluevia.commons.connector.http.HttpConnector;
import com.bluevia.commons.connector.http.HttpException;
import com.bluevia.commons.exception.BlueviaException;

/**
 * Implementation of HttpConnector to sign requests using OAuth
 *
 */
public class OauthHttpConnector extends HttpConnector implements IOAuth {

	private static Logger log = Logger.getLogger(OauthHttpConnector.class.getName());

	protected OAuthConsumer mConsumer;
	protected OAuthProvider mProvider;


	/**
	 * OAuthHttpConnector constructor
	 * Creates an OauthHttpConnector 1.0a and instantiates the OauthConsumer and OauthProvider with the
	 * information supplied
	 * 
	 * @param consumer the consumer data
	 * @param accessToken the access token
	 * @param authorizationWebsiteUrl
	 */
	public OauthHttpConnector(OAuthToken consumer, OAuthToken accessToken){

		mConsumer = new CommonsHttpOAuthConsumer(consumer.getToken(), consumer.getSecret());
		mConsumer.setTokenWithSecret(accessToken.getToken(), accessToken.getSecret());
	}


	/**
	 * OAuthHttpConnector constructor, 2-legged mode
	 * Creates an OauthHttpConnector 1.0a and instantiates the OauthConsumer and OauthProvider with the
	 * information supplied
	 * 
	 * @param consumer the consumer data
	 * @param accessToken the access token
	 * @param authorizationWebsiteUrl
	 */
	public OauthHttpConnector(OAuthToken consumer){
		mConsumer = new CommonsHttpOAuthConsumer(consumer.getToken(), consumer.getSecret());
	}

	/**
	 * OAuthHttpConnector constructor, 2-legged mode
	 * Creates an OauthHttpConnector 1.0a and instantiates the OauthConsumer and OauthProvider with the
	 * information supplied
	 * 
	 * @param consumer the consumer data
	 * @param accessToken the access token
	 * @param requestTokenEndpointUrl
	 * @param accessTokenEndpointUrl
	 * @param authorizationWebsiteUrl
	 */
	public OauthHttpConnector(OAuthToken consumer, String requestTokenEndpointUrl, 
			String accessTokenEndpointUrl, String authorizationWebsiteUrl){

		mConsumer = new CommonsHttpOAuthConsumer(consumer.getToken(), consumer.getSecret());
		mConsumer.setSigningStrategy(new BVAuthorizationHeaderSigningStrategy());
		
		mProvider = new BVCommonsHttpOauthProvider(requestTokenEndpointUrl, accessTokenEndpointUrl, authorizationWebsiteUrl);
		mProvider.setOAuth10a(true);
	}

	/**
	 * OAuthHttpConnector constructor
	 * Creates an OauthHttpConnector 1.0a and instantiates the OauthConsumer and OauthProvider with the
	 * information supplied
	 * 
	 * @param consumer the consumer data
	 * @param accessToken the access token
	 * @param cert the path to client certificate
	 * @param certFilePass the password of client certificate
	 * @param authorizationWebsiteUrl
	 */
	public OauthHttpConnector(OAuthToken consumer, OAuthToken accessToken, String cert, String certFilePass){

		mKeyStorePath = cert;
		mKeyStorePass = certFilePass;
		
		mConsumer = new CommonsHttpOAuthConsumer(consumer.getToken(), consumer.getSecret());
		mConsumer.setTokenWithSecret(accessToken.getToken(), accessToken.getSecret());
	}

	/**
	 * OAuthHttpConnector constructor, 2-legged mode
	 * Creates an OauthHttpConnector 1.0a and instantiates the OauthConsumer and OauthProvider with the
	 * information supplied
	 * 
	 * @param consumer the consumer data
	 * @param cert the path to client certificate
	 * @param certFilePass the password of client certificate
	 * @param authorizationWebsiteUrl
	 */
	public OauthHttpConnector(OAuthToken consumer, String cert, String certFilePass){
		mKeyStorePath = cert;
		mKeyStorePass = certFilePass;
		mConsumer = new CommonsHttpOAuthConsumer(consumer.getToken(), consumer.getSecret());
	}

	/**
	 * OAuthHttpConnector constructor, 2-legged mode
	 * Creates an OauthHttpConnector 1.0a and instantiates the OauthConsumer and OauthProvider with the
	 * information supplied
	 * 
	 * @param consumer the consumer data
	 * @param requestTokenEndpointUrl
	 * @param accessTokenEndpointUrl
	 * @param authorizationWebsiteUrl
	 * @param cert the path to client certificate
	 * @param certFilePass the password of client certificate
	 */
	public OauthHttpConnector(OAuthToken consumer, String requestTokenEndpointUrl, 
			String accessTokenEndpointUrl, String authorizationWebsiteUrl,
			String cert, String certFilePass){

		mKeyStorePath = cert;
		mKeyStorePass = certFilePass;

		mConsumer = new CommonsHttpOAuthConsumer(consumer.getToken(), consumer.getSecret());
		mConsumer.setSigningStrategy(new BVAuthorizationHeaderSigningStrategy());
		
		mProvider = new BVCommonsHttpOauthProvider(requestTokenEndpointUrl, accessTokenEndpointUrl, authorizationWebsiteUrl);
		mProvider.setOAuth10a(true);
	}


	@Override
	public void authenticate() throws BlueviaException {
		try {
			mConsumer.sign(mRequest);
		} catch (OAuthMessageSignerException e) {
			log.error("Unable to sign request" + e.getMessage());
			throw new BlueviaException("Unable to sign request", e, BlueviaException.INTERNAL_CLIENT_ERROR);
		} catch (OAuthExpectationFailedException e) {
			log.error("Unable to sign request" + e.getMessage());
			throw new BlueviaException("Unable to sign request", e, BlueviaException.INTERNAL_CLIENT_ERROR);
		} catch (OAuthCommunicationException e) {
			log.error(e.getLocalizedMessage());
			if (e.getResponseBody() != null){
				log.error(e.getResponseBody());
				ByteArrayInputStream bis = new ByteArrayInputStream(e.getResponseBody().getBytes());
				throw new HttpException(HttpException.CONNECTION_ERROR, bis, null);
			} else throw new BlueviaException(e.getMessage(), e, BlueviaException.CONNECTION_ERROR);
		}
	}

	
	/**
     * Retrieves a request token. 
     * 
     * TODO
     * 
     * HTTP body data is allowed.
     * @param callback the callback for the get request token. Available values are:
     * <ul>
     * 	<li>An url: if your app can receive callbacks and you want to get informed about the result of the authorization process</li>
     * 	<li>A phone number: if you want the user to receive the oauth verifier by sms</li>
     * 	<li>NULL: if your app cannot receive callbacks</li>
     * </ul>
     * @param data HTTP body data
     * @param listener
     * 
     * @return OAuthToken {@link OAuthToken}
	 * @throws BlueviaException 
     */
    public RequestToken getRequestToken(String callback, byte[] data, OAuthProviderListener listener) throws BlueviaException {

    	validate();
    	
    	mProvider.setListener(listener);
    	
    	((BVCommonsHttpOauthProvider)mProvider).setData(data);
        
        try {
        	
        	String url = null;
        	
            if(!Utils.isEmpty(callback))
                url = mProvider.retrieveRequestToken(mConsumer, callback);
            else url = mProvider.retrieveRequestToken(mConsumer, OAuth.OUT_OF_BAND);
            
            RequestToken result = new RequestToken(mConsumer.getToken(), mConsumer.getTokenSecret(), url);
            
            return result;
            
        } catch (OAuthMessageSignerException e) {
        	log.error("Unable to sign request" + e.getMessage());
			throw new BlueviaException("Unable to sign request", e, BlueviaException.INTERNAL_CLIENT_ERROR);
		} catch (OAuthNotAuthorizedException e) {
			log.error(e.getLocalizedMessage());
			if (e.getResponseBody() != null){
				log.error(e.getResponseBody());
				ByteArrayInputStream bis = new ByteArrayInputStream(e.getResponseBody().getBytes());
				throw new HttpException(HttpException.UNAUTHORIZED, bis, null);
			} else throw new BlueviaException(e.getMessage(), e, BlueviaException.INTERNAL_CLIENT_ERROR);
		} catch (OAuthExpectationFailedException e) {
			log.error("Unable to sign request" + e.getMessage());
			throw new BlueviaException("Unable to sign request", e, BlueviaException.INTERNAL_CLIENT_ERROR);
		} catch (OAuthCommunicationException e) {
			log.error(e.getLocalizedMessage());
			if (e.getResponseBody() != null){
				log.error(e.getResponseBody());
				ByteArrayInputStream bis = new ByteArrayInputStream(e.getResponseBody().getBytes());
				throw new HttpException(HttpException.CONNECTION_ERROR, bis, null);
			} else throw new BlueviaException(e.getMessage(), e, BlueviaException.CONNECTION_ERROR);
		}
    }
	
	/**
	 * Retrieves the access token corresponding to request token parameter
	 * 
	 * @param requestToken the request token received previously
	 * @param oauthVerifier the OAuth verifier for the token
	 * @return the access token
	 * @throws BlueviaException
	 */
	public OAuthToken getAccessToken(OAuthToken requestToken, String oauthVerifier) throws BlueviaException {

		if (requestToken == null || !requestToken.isValid())
			throw new BlueviaException("Bad request: RequestToken cannot be null nor empty", BlueviaException.BAD_REQUEST_EXCEPTION);

		if (Utils.isEmpty(oauthVerifier))
			throw new BlueviaException("Bad request: OAuthVerifier cannot be null nor empty", BlueviaException.BAD_REQUEST_EXCEPTION);

		validate();
		
    	((BVCommonsHttpOauthProvider)mProvider).setData(null);
		
    	mConsumer.setTokenWithSecret(requestToken.getToken(), requestToken.getSecret());
		mProvider.setOAuth10a(true);

		try {
			mProvider.retrieveAccessToken(mConsumer, oauthVerifier);
		} catch (OAuthMessageSignerException e) {
			log.error("Unable to sign request" + e.getMessage());
			throw new BlueviaException("Unable to sign request", e, BlueviaException.INTERNAL_CLIENT_ERROR);
		} catch (OAuthNotAuthorizedException e) {
			log.error(e.getLocalizedMessage());
			if (e.getResponseBody() != null){
				log.error(e.getResponseBody());
				ByteArrayInputStream bis = new ByteArrayInputStream(e.getResponseBody().getBytes());
				throw new HttpException(HttpException.UNAUTHORIZED, bis, null);
			} else throw new BlueviaException(e.getMessage(), e, BlueviaException.INTERNAL_CLIENT_ERROR);
		} catch (OAuthExpectationFailedException e) {
			log.error("Unable to sign request" + e.getMessage());
			throw new BlueviaException("Unable to sign request", e, BlueviaException.INTERNAL_CLIENT_ERROR);
		} catch (OAuthCommunicationException e) {
			log.error(e.getLocalizedMessage());
			if (e.getResponseBody() != null){
				log.error(e.getResponseBody());
				ByteArrayInputStream bis = new ByteArrayInputStream(e.getResponseBody().getBytes());
				throw new HttpException(HttpException.CONNECTION_ERROR, bis, null);
			} else throw new BlueviaException(e.getMessage(), e, BlueviaException.CONNECTION_ERROR);
		}

		return new OAuthToken(mConsumer.getToken(), mConsumer.getTokenSecret());
	}
	
	@Override
	public OAuthToken getOauthToken() {
		if (mConsumer == null)
			return null;
		if (Utils.isEmpty(mConsumer.getToken()) || Utils.isEmpty(mConsumer.getTokenSecret()))
			return null;
		return new OAuthToken(mConsumer.getToken(), mConsumer.getTokenSecret());
	}

	@Override
	public void setOauthToken(OAuthToken token) {
		if (mConsumer != null && token != null)
			mConsumer.setTokenWithSecret(token.getToken(), token.getSecret());
	}

	@Override
	public void setAdditionalParameters(HashMap<String, String> params) {
		HttpParameters oauthParameters = new HttpParameters();
		Iterator<Entry<String, String>> it = params.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, String> pairs = (Map.Entry<String, String>)it.next();
	        oauthParameters.put(pairs.getKey(), pairs.getValue());
	        it.remove(); 
	    }
		mConsumer.setAdditionalParameters(oauthParameters);
	}

	/**
	 * Validates the correct instantiation of the client attributes.
	 * 
	 */
	protected void validate() throws BlueviaException {
		if (mConsumer == null || mProvider == null || !(mProvider instanceof BVCommonsHttpOauthProvider))
			throw new IllegalStateException("Client is not correctly instantiated. This should not happen");
	}

}
