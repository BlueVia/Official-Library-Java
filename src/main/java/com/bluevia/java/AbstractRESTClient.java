package com.bluevia.java;

import javax.xml.bind.JAXBException;

import com.bluevia.java.oauth.OAuthToken;

public class AbstractRESTClient extends AbstractClient {
	
    protected OauthRESTConnector restConnector;
    
    public AbstractRESTClient(OAuthToken tokenConsumer, OAuthToken token) throws JAXBException{
    	this.restConnector = new OauthRESTConnector(tokenConsumer);
        this.restConnector.setToken(token);
    }
	
}
