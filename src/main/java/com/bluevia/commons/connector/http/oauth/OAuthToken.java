package com.bluevia.commons.connector.http.oauth;

import com.bluevia.commons.Entity;
import com.bluevia.commons.Utils;


/**
 * Class representing an OAuth token (valid for both consumer and token)
 * 
 */
public class OAuthToken implements Entity {
	
	protected String mOauthToken;
	protected String mOauthTokenSecret;
	
	/**
	 * Instantiates the OAuthToken with its parameters
	 * 
	 * @param oauthToken the oauth_token identifier
	 * @param oauthTokenSecret the oauth shared-secret
	 */
	public OAuthToken(String oauthToken, String oauthTokenSecret){
		this.mOauthToken = oauthToken;
		this.mOauthTokenSecret = oauthTokenSecret;
	}
	
	/**
	 * Get Oauth OAuthToken
	 * 
	 * @return the oauth_token property
	 */
	public String getToken() {
		return mOauthToken;
	}

	/** 
	 * Get Oauth OAuthToken Secret
	 * 
	 * @return the Oauth OAuthToken Secret property
	 */
	public String getSecret() {
		return mOauthTokenSecret;
	}

	/** 
	 * 	Checks that the token is valid. 
	 * 
	 * 	@return true if the OAuthToken seems to be valid, false otherwise
	 */
	public boolean isValid(){
		return !Utils.isEmpty(mOauthToken) && !Utils.isEmpty(mOauthTokenSecret);
	}
	
}
