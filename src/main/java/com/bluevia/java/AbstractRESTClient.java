package com.bluevia.java;

import javax.xml.bind.JAXBException;

import com.bluevia.java.oauth.OAuthToken;

public class AbstractRESTClient extends AbstractClient {
	
    protected OauthRESTConnector restConnector;
    
    public AbstractRESTClient(OAuthToken tokenConsumer, OAuthToken token, Mode mode, String pathLive, String pathSandbox) throws JAXBException{
        super(mode, pathLive, pathSandbox);
        
        if (!Utils.validateToken(tokenConsumer))
    		throw new IllegalArgumentException("Invalid parameter: token consumer");

    	this.restConnector = new OauthRESTConnector(tokenConsumer);
        this.restConnector.setToken(token);
        
    }
	
}
