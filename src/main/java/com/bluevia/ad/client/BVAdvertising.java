package com.bluevia.ad.client;

import java.io.IOException;

import com.bluevia.ad.data.AdRequest;
import com.bluevia.ad.data.AdResponse;
import com.bluevia.ad.data.simple.SimpleAdResponse;
import com.bluevia.commons.Utils;
import com.bluevia.commons.connector.http.oauth.IOAuth;
import com.bluevia.commons.exception.BlueviaException;

/**
 *  This class provides access to the set of functions which any user could use to access
 *  the Advertising service functionality
 *
 * @author Telefonica R&D
 *
 */
public class BVAdvertising extends BVAdvertisingClient {

	protected static final String FEED_AD_BASE_URI = "/REST/Advertising";
	
	/**
	 * Constructor
	 * 
	 * @param mode
	 * @param consumerKey
	 * @param consumerSecret
	 * @throws BlueviaException
	 */
	public BVAdvertising(Mode mode, String consumerKey, String consumerSecret) throws BlueviaException{
		initUntrusted(mode, consumerKey, consumerSecret);
		init();
		mBaseUri += buildUri(mMode, FEED_AD_BASE_URI) + FEED_AD_REQUESTS_URI;
	}
	
	/**
	 * Constructor
	 * 
	 * @param mode
	 * @param consumerKey
	 * @param consumerSecret
	 * @param token
	 * @param tokenSecret
	 * @throws BlueviaException
	 */
	public BVAdvertising(Mode mode, String consumerKey, String consumerSecret, String token, String tokenSecret) throws BlueviaException{
		initUntrusted(mode, consumerKey, consumerSecret, token, tokenSecret);
		init();
		mBaseUri += buildUri(mMode, FEED_AD_BASE_URI) + FEED_AD_REQUESTS_URI;
	}
	
	/**
	 * Requests the retrieving of an advertisement.
	 * This function can only be used in 3-legged-mode (OAuth token must have been included in the construction of the client)
	 * 
	 * @param adSpace the adSpace of the Bluevia application (mandatory)
	 * @param country country where the target user is located. Must follow ISO-3166 (see http://www.iso.org/iso/country_codes.htm). (optional)
	 * @param adRequestId an unique id for the request. (optional: if it is not set, the SDK will generate it automatically)
	 * @param adPresentation the value is a code that represents the ad format type (optional)
	 * @param keywords the keywords the ads are related to (optional)
	 * @param protectionPolicy the adult control policy. It will be safe, low, high. It should be checked with the application SLA in the gSDP (optional)
	 * @param userAgent the user agent of the client (optional)
	 * @return the result returned by the server that contains the ad meta-data
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public SimpleAdResponse getAdvertising3l(String adSpace, String country, String adRequestId, AdRequest.Type adPresentation, String[] keywords, AdRequest.ProtectionPolicyType protectionPolicy, String userAgent)
			throws BlueviaException, IOException {
		
		if (((IOAuth)mConnector).getOauthToken() == null)
			throw new BlueviaException("Function only available in 3-legged mode", BlueviaException.INVALID_MODE_EXCEPTION);

		AdResponse response = getSimpleAd(adSpace, null, country, null, adRequestId, adPresentation, keywords, protectionPolicy, userAgent);
		
		return simplifyResponse(response); 
	}

	/**
	 * Requests the retrieving of an advertisement.
	 * This functions can only be used in 2-legged-mode (OAuth token cannot have been included in the construction of the client)
	 * 
	 * @param adSpace the adSpace of the Bluevia application (mandatory)
	 * @param country country where the target user is located. Must follow ISO-3166 (see http://www.iso.org/iso/country_codes.htm) (optional).
	 * @param targetUserId Identifier of the Target User (optional). 
	 * @param adRequestId an unique id for the request. (optional: if it is not set, the SDK will generate it automatically)
	 * @param adPresentation the value is a code that represents the ad format type (optional)
	 * @param keywords the keywords the ads are related to (optional)
	 * @param protectionPolicy the adult control policy. It will be safe, low, high. It should be checked with the application SLA in the gSDP (optional)
	 * @param userAgent the user agent of the client (optional)
	 * @return the result returned by the server that contains the ad meta-data
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public SimpleAdResponse getAdvertising2l(String adSpace, String country, String targetUserId, String adRequestId, AdRequest.Type adPresentation, String[] keywords, AdRequest.ProtectionPolicyType protectionPolicy, String userAgent)
			throws BlueviaException, IOException {
		
		if (((IOAuth)mConnector).getOauthToken() != null)
			throw new BlueviaException("Function only available in 2-legged mode", BlueviaException.INVALID_MODE_EXCEPTION);

		// Mandatory parameter validation
		if (Utils.isEmpty(country)) /* any other specific format for requestid to validate?*/
			throw new BlueviaException("Bad request: country cannot be null nor empty", BlueviaException.BAD_REQUEST_EXCEPTION);


		AdResponse response = getSimpleAd(adSpace, null, country, targetUserId, adRequestId, adPresentation, keywords, protectionPolicy, userAgent);
		
		return simplifyResponse(response); 
	}
}
