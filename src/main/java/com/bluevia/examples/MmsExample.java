/**
 * 
 * @category bluevia
 * @package com.bluevia.examples
 * @copyright Copyright (c) 2010 Telefonica I+D (http://www.tid.es)
 * @author Bluevia <support@bluevia.com>
 * 
 * 
 *          BlueVia is a global iniciative of Telefonica delivered by Movistar
 *          and O2. Please, check out http://www.bluevia.com and if you need
 *          more information contact us at support@bluevia.com
 */

package com.bluevia.examples;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import com.bluevia.java.AbstractClient.Mode;
import com.bluevia.java.exception.BlueviaException;
import com.bluevia.java.mms.MessageMO;
import com.bluevia.java.mms.MessageMT;
import com.bluevia.java.mms.NotificationManager;
import com.bluevia.java.mms.data.Attachment;
import com.bluevia.java.mms.data.Attachment.ContentType;
import com.bluevia.java.mms.data.MimeContent;
import com.bluevia.java.mms.data.ReceivedMMS;
import com.bluevia.java.oauth.OAuthToken;
import com.telefonica.schemas.unica.rest.common.v1.SimpleReferenceType;
import com.telefonica.schemas.unica.rest.common.v1.UserIdType;
import com.telefonica.schemas.unica.rest.mms.v1.DeliveryInformationType;
import com.telefonica.schemas.unica.rest.mms.v1.MessageDeliveryStatusType;
import com.telefonica.schemas.unica.rest.mms.v1.MessageNotificationType;
import com.telefonica.schemas.unica.rest.mms.v1.MessageReferenceType;
import com.telefonica.schemas.unica.rest.mms.v1.MessageURIType;

/**
 * MMS API Class Examples
 * 
 * @package com.bluevia.examples
 */
public class MmsExample {

	// API path (Mode Live/Sandbox)
	public static Mode mode = Mode.SANDBOX;

	// CREDENTIALS: 
	// User must DEFINE VALID VALUES FOR consumer token & access token

    // Consumer Key - Consumer Token
    public static OAuthToken consumer = new OAuthToken("vw12012654505986", "WpOl66570544");
    
    // Access Token - Access Token Secret
    public static OAuthToken accesstoken = new OAuthToken("ad3f0f598ffbc660fbad9035122eae74", "4340b28da39ec36acb4a205d3955a853");

    //Keyword
    public static final String KEYWORD = "SANDBLUEDEMOS";
    
	public static void main(String[] args) {
		sendMms();
		//getMessages(true);
		//notifications();
	}

	/**
	 * MMS API - MMS MT EXAMPLE
	 *  
	 */
	public static void sendMms() {

		try {

			System.out.println("***** Send MMS Example");

			// Create message sender
			MessageMT messageSender = new MessageMT(consumer, accesstoken, mode);

			// Prepare SMS parameters
			String addresses[] = { "546780" };
			
			//NOTE: Fake attachments, change the paths for other real files
			Attachment[] files = {new Attachment("image.jpg", ContentType.IMAGE_JPEG),
					new Attachment("message.txt", ContentType.TEXT_PLAIN)};		
			String subject = "Hello Bluevia!";

			// Sending SMS
			String mmsId = messageSender.send(addresses, files, subject);

			if (mmsId != null) {
				System.out.println("Mms was sent. Id: " + mmsId);
			}

			// Getting delivery status
			MessageDeliveryStatusType response = messageSender.getStatus(mmsId);
			List<DeliveryInformationType> list = response.getMessageDeliveryStatus();


			for (DeliveryInformationType status : list) {
				System.out.println("Delivery status: " + status.getDeliveryStatus());
			}

		} catch (BlueviaException ex) {
			Logger.getLogger(SmsExample.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
		} catch (JAXBException ex) {
			Logger.getLogger(SmsExample.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(SmsExample.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/*
	 * README to understand the example
	 * 
	 * Uses the function getMessages
	 * 
	 * If the boolean parameter useGetAttachments is true, the attachments will be obtained using the function getAttachments for each one;
	 * if it is false, the method getMessage (which gets the complete list of attachments in a single call)
	 * 
	 */
	public static void getMessages(boolean useGetAttachments) {

		try {

			System.out.println("***** Get MMS Example");

			// Create mms receiver
			MessageMO mmsReceiver = new MessageMO(consumer, mode);

			// Prepare params
			String registrationId = "546780";

			// Get messages info
			List<MessageReferenceType> list = mmsReceiver.getMessages(registrationId, useGetAttachments);

			if (list == null || list.isEmpty()){
				System.out.println("No messages");

			} else {

				if (useGetAttachments){
					
					//Using getAttachments
					for (MessageReferenceType messageReference : list){

						System.out.println("Message from: " + messageReference.getOriginAddress().getPhoneNumber());
						System.out.println(" > Subject: " + messageReference.getSubject());

						List<MessageURIType> atts = messageReference.getAttachmentURL();
						for (MessageURIType uri : atts){
							System.out.println("Attachment:");
							System.out.println("Id: " + uri.getHref());
							System.out.println("Content-type: " + uri.getContentType());

							//Get attachments
							MimeContent attachment = mmsReceiver.getAttachment(registrationId, 
									messageReference.getMessageIdentifier(), uri.getHref());

							//Do stuff...
							System.out.println(attachment.getFileName());
						}
					}
					
				} else {

					//Using getMessage
					for (MessageReferenceType messageReference : list){
						
						ReceivedMMS mms = mmsReceiver.getMessage(registrationId, messageReference.getMessageIdentifier());
						System.out.println("Message from: " + mms.getMessage().getOriginAddress().getPhoneNumber());
						System.out.println(" > Subject: " + mms.getMessage().getSubject());


						// Get attachments...
						ArrayList<MimeContent> contents = mms.getAttachments();
						for (MimeContent content : contents){
							//Do stuff...
							System.out.println(content.getFileName());
						}
					}
				}
			}

		} catch (BlueviaException ex) {
			Logger.getLogger(MmsExample.class.getName()).log(Level.SEVERE, null, ex);
		} catch (JAXBException ex) {
			Logger.getLogger(MmsExample.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static void notifications(){
		try {

			System.out.println("***** Subscribe to Notificacions Example");

			NotificationManager nm = new NotificationManager(consumer, mode);

			MessageNotificationType mnt = new MessageNotificationType();

			//Endpoint and correlator
			SimpleReferenceType ref = new SimpleReferenceType();
			ref.setEndpoint("https://www.example.com");
			ref.setCorrelator("123456");
			mnt.setReference(ref);

			//Addresses
			List<UserIdType> addresses = new ArrayList<UserIdType>();
			UserIdType address = new UserIdType();
			address.setPhoneNumber("546780");
			addresses.add(address);
			mnt.setDestinationAddress(addresses);

			//Criteria
			mnt.setCriteria("MO_key");

			//Susbscribe
			String response = nm.subscribe(mnt);
			System.out.println("Suscribed with correlator: " + response);

			//Unsubscribe
			nm.unsubscribeNotification(response);

		} catch (BlueviaException ex) {
			Logger.getLogger(SmsExample.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
		} catch (JAXBException ex) {
			Logger.getLogger(SmsExample.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}