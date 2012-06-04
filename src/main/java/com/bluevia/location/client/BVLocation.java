package com.bluevia.location.client;

import java.io.IOException;

import com.bluevia.commons.exception.BlueviaException;
import com.bluevia.location.data.LocationInfo;

/**
 * Client interface for the REST binding of the Bluevia Location Service.
 *
 * @author Telefonica R&D
 * 
 */
public class BVLocation extends BVLocationClient {

	protected static final String FEED_LOCATION_BASE_URI = "/REST/Location";
	
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
	public BVLocation(Mode mode, String consumerKey, String consumerSecret,
			String token, String tokenSecret) throws BlueviaException{
		initUntrusted(mode, consumerKey, consumerSecret, token, tokenSecret);
		
		init();
		mBaseUri += buildUri(mMode, FEED_LOCATION_BASE_URI) + GET_LOCATION_FEED_PATH;
	}
	
	/**
	 * Retrieves the location of the terminal.
	 * 
	 * @param accuracy Accuracy, in meters, that is acceptable for a response (optional).
	 * @return An entity object containing the Location Data.
	 * @throws BlueviaException 
	 * @throws IOException 
	 */
	public LocationInfo getLocation(Integer accuracy) throws BlueviaException, IOException {
		return getLocation(null, accuracy);
	}
	
}
