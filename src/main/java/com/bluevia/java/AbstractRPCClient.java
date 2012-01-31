package com.bluevia.java;

import javax.xml.bind.JAXBException;

import com.bluevia.java.oauth.OAuthToken;

public class AbstractRPCClient extends AbstractClient {

    protected OauthRPCConnector rpcConnector;
    
    public AbstractRPCClient(OAuthToken tokenConsumer, OAuthToken token, Mode mode, String pathLive, String pathSandbox) throws JAXBException{
    	super(mode, pathLive, pathSandbox);
    	
        if (!Utils.validateToken(tokenConsumer))
    		throw new IllegalArgumentException("Invalid parameter: token consumer");
        
    	this.rpcConnector = new OauthRPCConnector(tokenConsumer);
        this.rpcConnector.setToken(token);
    }
	
}
