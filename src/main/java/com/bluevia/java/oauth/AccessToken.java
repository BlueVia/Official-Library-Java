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

import java.util.logging.Level;
import java.util.logging.Logger;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import oauth.signpost.signature.HmacSha1MessageSigner;

import com.bluevia.java.AbstractClient;
import com.bluevia.java.Utils;

/**
 * AccessToken
 * 
 * @package com.bluevia.java.oauth
 */

/**
 * <p>Java class for get Access Token.
 * 
 */

public class AccessToken {

    private String access_endpoint;
    
	protected OAuthToken consumer;
	protected OAuthToken token;

    /**
     * Constructor
     * 
     * @param consumer
     * @param requestToken
     */
    public AccessToken(OAuthToken consumer, OAuthToken requestToken) {
    	
        if (!Utils.validateToken(consumer))
    		throw new IllegalArgumentException("Invalid parameter: consumer");
        
        if (!Utils.validateToken(requestToken))
    		throw new IllegalArgumentException("Invalid parameter: request token");
        
        this.consumer = consumer;
        this.token = requestToken;

    	this.access_endpoint = AbstractClient.BASE_ENDPOINT + "/REST/Oauth/getAccessToken";
    }

    /**
     * Get OAuthToken
     * @param verification
     * 
     * @return
     */
    public OAuthToken get(String verification) {
        OAuthConsumer client = new DefaultOAuthConsumer(consumer.getToken(), consumer.getSecret());
        client.setMessageSigner(new HmacSha1MessageSigner());
        client.setTokenWithSecret(token.getToken(), token.getSecret());
        OAuthProvider provider = new DefaultOAuthProvider("", access_endpoint, "");
        provider.setOAuth10a(true);       
        try {
            provider.retrieveAccessToken(client,verification);
        } catch (OAuthMessageSignerException ex) {
            Logger.getLogger(AccessToken.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OAuthNotAuthorizedException ex) {
            Logger.getLogger(AccessToken.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OAuthExpectationFailedException ex) {
            Logger.getLogger(AccessToken.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OAuthCommunicationException ex) {
            Logger.getLogger(AccessToken.class.getName()).log(Level.SEVERE, null, ex);
        }
        OAuthToken result =
                new OAuthToken(client.getToken(), client.getTokenSecret());
        return result;
    }
}
