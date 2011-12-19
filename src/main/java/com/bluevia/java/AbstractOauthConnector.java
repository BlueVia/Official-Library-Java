package com.bluevia.java;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.bluevia.java.oauth.OAuthToken;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.signature.HmacSha1MessageSigner;

/**
 * Abstract Client for Http communication with AUTHORIZATION
 * headers included in the requests
 *
 */
public abstract class AbstractOauthConnector {
	
	String token;
	String token_secret;
	OAuthConsumer consumer;
	JAXBContext jc;
	Unmarshaller u;
	
	/**
	 * Constructor
	 * 
	 * @param value allowed object is {@link OAuthToken }
	 * @throws JAXBException
	 */
	public AbstractOauthConnector(OAuthToken token) throws JAXBException {
		this.consumer = new DefaultOAuthConsumer(token.getToken(), token.getSecret());
		this.consumer.setMessageSigner(new HmacSha1MessageSigner());
	}
	

	/**
	 * Gets the access token
	 * 
	 * @return the access token
	 */
	public String getAccessToken() {
		return token;
	}
	
	/**
	 * Set Token value
	 * 
	 * @param token allowed object is {@link OAuthToken }
	 * 
	 */
	public void setToken(OAuthToken token) {
		if (token != null){
			this.token = token.getToken();
			this.token_secret = token.getSecret();
			this.consumer.setTokenWithSecret(token.getToken(), token.getSecret());
		}
	}

}
