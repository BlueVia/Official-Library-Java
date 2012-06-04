package com.bluevia.commons.connector.http.oauth;

import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.commonshttp.HttpRequestAdapter;
import oauth.signpost.http.HttpRequest;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;

/**
 * Extension of OAuth signpost CommonsHttpOAuthProvider to include a HTTP body in
 * a retrieveRequestToken petition.
 * 
 * @author Telefonica R&D
 */
public class BVCommonsHttpOauthProvider extends CommonsHttpOAuthProvider {

	private static final long serialVersionUID = 3819915669259754610L;
	
	private byte[] data;

	public BVCommonsHttpOauthProvider(String requestTokenEndpointUrl,
			String accessTokenEndpointUrl, String authorizationWebsiteUrl) {
		super(requestTokenEndpointUrl, accessTokenEndpointUrl, authorizationWebsiteUrl);
	}
	
	public void setData(byte[] data){
		this.data = data;
	}
	
	//Modification in CommonsHttpOAuthProvider to include data
    @Override
    protected HttpRequest createRequest(String endpointUrl) throws Exception {
        HttpPost request = new HttpPost(endpointUrl);
        
        if (data != null){
	        ByteArrayEntity entity = new ByteArrayEntity(data);
			entity.setContentType("application/x-www-form-urlencoded");
	        request.setEntity(entity);
        }
        return new HttpRequestAdapter(request);
    }

}
