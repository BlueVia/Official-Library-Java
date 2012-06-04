package com.bluevia.commons;

/**
 * 
 * Constants of the gSDP
 * 
 * @author Telefonica I+D
 *
 */
public class GsdpConstants {

	public static final String GSDP_BASE_URL = "https://api.bluevia.com/services";
	
	public static final String REQUEST_TOKEN_URL = GSDP_BASE_URL + "/REST/Oauth/getRequestToken";
	
	public static final String ACCESS_TOKEN_URL = GSDP_BASE_URL + "/REST/Oauth/getAccessToken";
	
	public static final String AUTHORIZE_URL_CONNECT = "https://connect.bluevia.com/authorise";
	
	public static final String AUTHORIZE_URL_DEVELOPERS = "https://bluevia.com/test-apps/authorise";
	
	public static final String HEADER_CONTENT_TYPE = "Content-Type";
	
	public static final String KEYSTORE_TYPE_PKCS12= "PKCS12";
}
