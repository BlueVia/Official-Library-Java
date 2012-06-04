package com.bluevia.messagery.mo.mms.client;

import com.bluevia.commons.exception.BlueviaException;

/**
 * 
 * Client interface for the REST binding of the Bluevia MMS MO Service.
 * 
 * @author Telefonica R&D
 *
 */
public class BVMoMms extends BVMoMmsClient {

	protected static final String FEED_MMS_BASE_URI = "/REST/MMS";

	/**
	 * Constructor
	 * 
	 * @param mode
	 * @param consumerKey
	 * @param consumerSecret
	 * @throws BlueviaException
	 */
	public BVMoMms(Mode mode, String consumerKey, String consumerSecret) throws BlueviaException{
		initUntrusted(mode, consumerKey, consumerSecret);
		
		init();
		mBaseUri += buildUri(mMode, FEED_MMS_BASE_URI) + FEED_INBOUND_REQUESTS;
	}

	
}
