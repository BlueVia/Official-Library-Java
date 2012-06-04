package com.bluevia.messagery.mt.mms.client;

import java.io.IOException;
import java.util.ArrayList;

import com.bluevia.commons.connector.http.oauth.IOAuth;
import com.bluevia.commons.data.UserId;
import com.bluevia.commons.data.UserId.Type;
import com.bluevia.commons.exception.BlueviaException;
import com.bluevia.messagery.mt.mms.data.Attachment;
import com.bluevia.messagery.mt.mms.data.MmsMessageReq;

/**
 * 
 * Client interface for the REST binding of the Bluevia MMS MT Service.
 * 
 * @author Telefonica R&D
 *
 */
public class BVMtMms extends BVMtMmsClient {

	protected static final String FEED_MMS_BASE_URI = "/REST/MMS";

	public BVMtMms(Mode mode, String consumerKey, String consumerSecret,
			String token, String tokenSecret) throws BlueviaException{
		initUntrusted(mode, consumerKey, consumerSecret, token, tokenSecret);
		
		init();
		mBaseUri += buildUri(mMode, FEED_MMS_BASE_URI) + FEED_OUTBOUND_REQUESTS;
		
	}
	

	
	/**
	 * Allows to send and MMS to the gSDP. It returns a String containing the mmsId of the MMS sent
	 * @param destination the address of the recipient of the message 
	 * @param subject the subject of the mms to send
	 * @param message text message of the MMS (optional)
	 *
	 * @return the sent MMS ID
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public String send(String destination, String subject, String message) throws BlueviaException, IOException {
		return send(destination, subject, message, null, null, null);
	}

	/**
	 * Allows to send and MMS to the gSDP. It returns a String containing the mmsId of the MMS sent
	 * @param destination the address of the recipient of the message
	 * @param subject the subject of the mms to send
	 * @param attachments The contents of the MMS to sent (image, video, etc)
	 * @param message text message of the MMS
	 *
	 * @return the sent MMS ID
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public String send(String destination, String subject, String message, ArrayList<Attachment> attachments) throws BlueviaException, IOException {
		return send(destination, subject, message, attachments, null, null);
	}

	/**
	 * Allows to send and MMS to the gSDP. Sent MMS notifications will be received in the endpoint.
	 * The MMSID of the sent MMS is returned in order to ask later for the status of the message as well.
	 * @param destination the address of the recipient of the message
	 * @param subject the subject of the mms to send
	 * @param attachments The contents of the MMS to sent (image, video, etc)
	 * @param endpoint the endpoint to receive notifications of sent SMSs
	 * @param correlator the correlator
	 * @param message text message of the MMS
	 * 
	 * @return the sent MMS ID
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public String send(String destination, String subject, String message, 
			ArrayList<Attachment> attachments, String endpoint, String correlator) throws BlueviaException, IOException {

		return send(new MmsMessageReq(subject, message, new UserId(Type.PHONE_NUMBER, destination), attachments, endpoint, correlator));
	}
	
	/**
	 * Allows to send and MMS to the gSDP to multiple recipients. It returns a String containing the mmsId of the MMS sent
	 * @param destination the addresses of the recipients of the message
	 * @param subject the subject of the mms to send
	 * @param message text message of the MMS
	 *
	 * @return the sent MMS ID
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public String send(ArrayList<String> destination, String subject, String message) throws BlueviaException, IOException {
		return send(destination, subject, message, null, null, null);
	}

	/**
	 * Allows to send and MMS to the gSDP to multiple recipients. It returns a String containing the mmsId of the MMS sent
	 * @param destination the addresses of the recipients of the message
	 * @param subject the subject of the mms to send
	 * @param attachments The contents of the MMS to sent (image, video, etc)
	 * @param message text message of the MMS
	 *
	 * @return the sent MMS ID
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public String send(ArrayList<String> destination, String subject, String message, ArrayList<Attachment> attachments) throws BlueviaException, IOException {
		return send(destination, subject, message, attachments, null, null);
	}

	/**
	 * Allows to send and MMS to the gSDP to multiple recipients. Sent MMS notifications will be received in the endpoint.
	 * The MMSID of the sent MMS is returned in order to ask later for the status of the message as well.
	 * @param destination the addresses of the recipients of the message
	 * @param subject the subject of the mms to send
	 * @param attachments The contents of the MMS to sent (image, video, etc)
	 * @param endpoint the endpoint to receive notifications of sent SMSs
	 * @param correlator the correlator
	 * @param message text message of the MMS
	 * 
	 * @return the sent MMS ID
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public String send(ArrayList<String> destination, String subject, String message, ArrayList<Attachment> attachments,
			String endpoint, String correlator) throws BlueviaException, IOException {

		//Convert string phone numbers to UserId objects
		ArrayList<UserId> addressList = buildUserIdList(destination);
		
		return send(new MmsMessageReq(subject, message, addressList, attachments, endpoint, correlator));
	}

	/**
	 *
	 * Allows to know the delivery status of a previous sent MMS using Bluevia API
	 * @param mmsId the id of the mms previously sent using this API
	 * @return the delivery status of the MMS message id
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	protected String send(MmsMessageReq mmsMessage) throws BlueviaException, IOException {

		//Set the token for origin address if no exists
		if (mmsMessage != null && mmsMessage.getOriginAddress() == null){
			UserId originAddress = new UserId(Type.ALIAS, ((IOAuth)mConnector).getOauthToken().getToken());
			mmsMessage.setOriginAddress(originAddress);
		}
		
		return sendMms(mmsMessage, null);
	}
	
}
