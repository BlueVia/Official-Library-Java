package com.bluevia.messagery.mo.sms.client;

import com.bluevia.commons.exception.BlueviaException;

/**
 * 
 * Client interface for the REST binding of the Bluevia SMS MO Service.
 * 
 * @author Telefonica R&D
 *
 */
public class BVMoSms extends BVMoSmsClient {
	
	protected static final String FEED_SMS_BASE_URI = "/REST/SMS";

	/**
	 * Constructor
	 * 
	 * @param mode
	 * @param consumerKey
	 * @param consumerSecret
	 * @throws BlueviaException
	 */
	public BVMoSms(Mode mode, String consumerKey, String consumerSecret) throws BlueviaException{
		initUntrusted(mode, consumerKey, consumerSecret);
		
		init();
		mBaseUri += buildUri(mMode, FEED_SMS_BASE_URI) + FEED_INBOUND_REQUESTS;
	}
}
