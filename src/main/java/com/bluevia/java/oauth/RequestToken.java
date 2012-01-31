/**
 * 
 * @category bluevia
 * @package com.bluevia.java.oauth
 * @copyright Copyright (c) 2010 Telefonica I+D (http://www.tid.es)
 * @author Bluevia <support@bluevia.com>
 * 
 * 
 *          BlueVia is a global iniciative of Telefonica delivered by Movistar
 *          and O2. Please, check out http://www.bluevia.com and if you need
 *          more information contact us at support@bluevia.com
 */

package com.bluevia.java.oauth;

import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.OAuthProviderListener;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import oauth.signpost.http.HttpRequest;
import oauth.signpost.http.HttpResponse;
import oauth.signpost.signature.HmacSha1MessageSigner;

import com.bluevia.java.AbstractClient;
import com.bluevia.java.AbstractClient.Mode;
import com.bluevia.java.Utils;
import com.bluevia.java.exception.ClientException;
import com.bluevia.java.exception.ServerException;
import com.bluevia.java.payment.oauth.BlueviaAuthorizationHeaderSigningStrategy;
import com.bluevia.java.payment.oauth.BlueviaCommonsHttpOauthProvider;
import com.telefonica.schemas.unica.rest.common.v1.ClientExceptionType;
import com.telefonica.schemas.unica.rest.common.v1.ServerExceptionType;

/**
 * RequestToken
 * 
 * @package com.bluevia.java.oauth
 */

/**
 * <p>Java class for get Request Token.
 * 
 */

public class RequestToken {

    protected String endpoint;
    
	protected OAuthToken consumer;
	
	protected Mode mode;
	
	private static final String PATH = "/REST/Oauth/getRequestToken";

    protected static final String DEVELOPERS_ENDPOINT = "https://bluevia.com/test-apps/authorise";
    protected static final String CONNECT_ENDPOINT = "https://connect.bluevia.com/authorise";

	protected JAXBContext jc;
	protected Unmarshaller u;
	
	/**
	 * 
	 * @param consumer
	 * @param mode 
	 * @throws JAXBException
	 */
    public RequestToken(OAuthToken consumer, Mode mode) throws JAXBException {

        if (!Utils.validateToken(consumer))
    		throw new IllegalArgumentException("Invalid parameter: consumer");
        
        if (mode == null)
    		throw new IllegalArgumentException("Invalid parameter: mode cannot be null");
    	
    	this.consumer = consumer;
    	this.mode = mode;

    	this.endpoint = AbstractClient.BASE_ENDPOINT + PATH;

		this.jc = JAXBContext.newInstance("com.telefonica.schemas.unica.rest.common.v1");
		this.u = jc.createUnmarshaller();
    }
    
    /**
     * Retrieves a request token
     * No callback is retreived so 'Out Of Band' Oauth mode is used
     * 
     * @return OAuthToken {@link OAuthToken}
     */
    public OAuthToken getRequestToken() {
    	return getRequestToken(null);
    }

    /**
     * Retrieves a request token
     * @param callback the callback for the get request token. Available values are:
     * <ul>
     * 	<li>An URL: if your app can receive callbacks and you want to get informed about the result of the authorization process</li>
     * 	<li>A phone number: if you want the user to receive the oauth verifier by SMS</li>
     * 	<li>NULL: if your app cannot receive callbacks</li>
     * </ul>
     * 
     * @return OAuthToken {@link OAuthToken}
     */
    public OAuthToken getRequestToken(String callback) {
    	
    	//Callback validation
    	if (!Utils.isEmpty(callback)){	//Null and empty are valid (changed by "oob")
    		
    		//Other values than URLs, "oob" and a phone number are invalid
    		if (!Utils.isUrl(callback) && !callback.equals(OAuth.OUT_OF_BAND) && 
    				!Utils.isNumber(callback)){
        		throw new IllegalArgumentException("Invalid parameter: callback.");
    		}
    		
    		//SMS Handshake only for LIVE environment
            if (Utils.isNumber(callback) && mode != Mode.LIVE)
            	throw new IllegalArgumentException("Invalid request: SMS Handshake is only available for LIVE environment");
    	}
    	
    	OAuthProviderListener listener = new OAuthProviderListener() {
			
			@Override
			public void prepareSubmission(HttpRequest request) throws Exception {}
			
			@Override
			public void prepareRequest(HttpRequest request) throws Exception {}
			
			@Override
			public boolean onResponseReceived(HttpRequest request, HttpResponse response) throws Exception {
				handleErrors(request, response);
				return false;
			}
		};
    	
    	return getRequestToken(callback, null, listener);
    }
    
    /**
     * Retrieves a request token. 
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
     */
    protected OAuthToken getRequestToken(String callback, byte[] data, OAuthProviderListener listener) {
    	OAuthConsumer client = new DefaultOAuthConsumer(consumer.getToken(), consumer.getSecret());
        client.setMessageSigner(new HmacSha1MessageSigner());
        client.setSigningStrategy(new BlueviaAuthorizationHeaderSigningStrategy());

        OAuthProvider provider = new BlueviaCommonsHttpOauthProvider(endpoint, "", "", data);
        provider.setListener(listener);
        
        String url = "";
        
        try {
            if(!Utils.isEmpty(callback))
                url = provider.retrieveRequestToken(client, callback);
            else url = provider.retrieveRequestToken(client, OAuth.OUT_OF_BAND);
            
            OAuthToken result = new OAuthToken(client.getToken(), client.getTokenSecret());
            
            //Set URL
            if (Utils.isNumber(callback)){
            	result.setUrl(null);
            } else {
            	switch (mode) {
    			case LIVE:
    				result.setUrl(CONNECT_ENDPOINT + url);
    				break;
    			case TEST:
    			case SANDBOX:
    				result.setUrl(DEVELOPERS_ENDPOINT + url);
    			break;
    			}
            }
            
            return result;
            
        } catch (OAuthMessageSignerException ex) {
            Logger.getLogger(RequestToken.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OAuthNotAuthorizedException ex) {
            Logger.getLogger(RequestToken.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OAuthExpectationFailedException ex) {
            Logger.getLogger(RequestToken.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OAuthCommunicationException ex) {
            Logger.getLogger(RequestToken.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
	protected void handleErrors(HttpRequest request, HttpResponse response) throws Exception {
		if (response.getStatusCode() >= 400 && response.getStatusCode() < 500) {
			String doc = Utils.convertStreamToString(response.getContent());
			/* CLIENT EXCEPTION */
			JAXBElement<ClientExceptionType> e = u.unmarshal(new StreamSource(new StringReader(doc)),
					ClientExceptionType.class);
			throw new ClientException(e.getValue());
		}
		if (response.getStatusCode() >= 500) {
			String doc = Utils.convertStreamToString(response.getContent());
			/* SERVER EXCEPTION */
			JAXBElement<ServerExceptionType> e = u.unmarshal(new StreamSource(new StringReader(doc)),
					ServerExceptionType.class);
			throw new ServerException(e.getValue());
		}
	}

}
