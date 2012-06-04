package com.bluevia.messagery.mt.sms.client;

import java.io.IOException;
import java.util.ArrayList;

import com.bluevia.commons.connector.http.oauth.IOAuth;
import com.bluevia.commons.data.UserId;
import com.bluevia.commons.data.UserId.Type;
import com.bluevia.commons.exception.BlueviaException;


/**
 * 
 * Client interface for the REST binding of the Bluevia SMS MT Service.
 * 
 * @author Telefonica R&D
 *
 */
public class BVMtSms extends BVMtSmsClient {
	
	protected static final String FEED_SMS_BASE_URI = "/REST/SMS";

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
	public BVMtSms(Mode mode, String consumerKey, String consumerSecret,
			String token, String tokenSecret) throws BlueviaException{
		initUntrusted(mode, consumerKey, consumerSecret, token, tokenSecret);
		
		init();
		mBaseUri += buildUri(mMode, FEED_SMS_BASE_URI) + FEED_OUTBOUND_REQUESTS;
		
	}
	
	/**
	 * Allows to send and SMS to the gSDP. It returns a String containing the SMSID of the sent SMS,
	 * in order to ask later for the status of the message.
	 * The max length of the message must be less than 160 characters.
	 * @param destination the address of the recipient of the message
	 * @param text the text of the message
	 *
	 * @return the sent SMS ID
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public String send(String destination, String text) throws BlueviaException, IOException {
		return send(destination, text, null, null);
	}


	/**
	 * Allows to send and SMS to the gSDP. Sent SMS notifications will be received in the endpoint
	 * The SMSID of the sent SMS is returned in order to ask later for the status of the message as well.
	 * The max length of the message must be less than 160 characters.
	 * @param destination the address of the recipient of the message
	 * @param text the text of the message
	 * @param endpoint the endpoint to receive notifications of sent SMSs
	 * @param correlator the correlator
	 *
	 * @return the sent SMS ID
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public String send(String destination, String text, String endpoint, String correlator) throws BlueviaException, IOException {
		ArrayList<String> phoneNumber= new ArrayList<String>();
		phoneNumber.add(destination);
		
		return send(phoneNumber, text, endpoint, correlator);
		//return send(new SmsMessageReq(text, new UserId(Type.PHONE_NUMBER, destination), endpoint, correlator));
	}

	/**
	 * Allows to send and SMS to the gSDP. It returns a String containing the SMSID of the sent SMS,
	 * in order to ask later for the status of the message.
	 * The max length of the message must be less than 160 characters.
	 * @param destination the addresses of the recipients of the message
	 * @param text the text of the message
	 *
	 * @return the sent SMS ID
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public String send(ArrayList<String> destination, String text) throws BlueviaException, IOException {
		return send(destination, text, null, null);
	}

	/**
	 * 
	 * Allows to send and SMS to the gSDP. Sent SMS notifications will be received in the endpoint.
	 * The SMSID of the sent SMS is returned in order to ask later for the status of the message as well.
	 * The max length of the message must be less than 160 characters.
	 * @param destination the addresses of the recipients of the message
	 * @param text the text of the message
	 * @param endpoint the endpoint to receive notifications of sent SMSs
	 * @param correlator the correlator
	 *
	 * @return the sent SMS ID
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public String send(ArrayList<String> destination, String text, String endpoint, String correlator) throws BlueviaException, IOException {

		//Set the token for origin address
		UserId originAddress = new UserId(Type.ALIAS, ((IOAuth)mConnector).getOauthToken().getToken());

		return send(destination, text, originAddress, null, null, endpoint, correlator);

	}
	
	
}
