package com.bluevia.java;

import javax.xml.bind.JAXBException;

import com.bluevia.java.oauth.OAuthToken;

public class AbstractRPCClient extends AbstractClient {

    protected OauthRPCConnector rpcConnector;
    
    public AbstractRPCClient(OAuthToken tokenConsumer, OAuthToken token) throws JAXBException{
    	this.rpcConnector = new OauthRPCConnector(tokenConsumer);
        this.rpcConnector.setToken(token);
    }
	
}
