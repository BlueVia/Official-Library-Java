package com.bluevia.commons.connector.http.oauth;

/**
 * This object extends the OAuthToken class to include the URL to verify the Request OAuthToken. 
 *
 */
public class RequestToken extends OAuthToken {
	
	private String mVerificationUrl;
	
	/**
	 * Instantiates the OAuthToken with its parameters
	 * 
	 * @param oauthToken the oauth_token identifier
	 * @param oauthTokenSecret the oauth shared-secret
	 * @param url verification url
	 */
	public RequestToken(String oauthToken, String oauthTokenSecret, String url) {
		super(oauthToken, oauthTokenSecret);
		mVerificationUrl = url;
	}
	
	/**
	 * Instantiates the OAuthToken with it parameter
	 * 
	 * @param token
	 * @param url verification url
	 */
	public RequestToken(OAuthToken token, String url){
		this(token.getToken(), token.getSecret(), url);
	}
	
	/**
	 * Gets the verification url
	 * 
	 * @return The verification url
	 */
	public String getVerificationUrl(){
		return mVerificationUrl;
	}
	
	/**
	 * Sets the verification url
	 * 
	 * @param url the verification url
	 */
	public void setVerificationUrl(String url){
		mVerificationUrl = url;
	}
	
	/** 
	 * 	Checks that the token is valid. 
	 * 	@return true if the OAuthToken seems to be valid, false otherwise
	 */
	@Override
	public boolean isValid(){
		return super.isValid();		//VerificationUrl can be null
	}
	
}
