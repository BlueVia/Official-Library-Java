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


import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.bluevia.commons.client.BVBaseClient.Mode;
import com.bluevia.commons.connector.http.oauth.OAuthToken;
import com.bluevia.commons.exception.BlueviaException;
import com.bluevia.messagery.mo.mms.client.BVMoMms;
import com.bluevia.messagery.mo.mms.data.MimeContent;
import com.bluevia.messagery.mo.mms.data.MmsMessage;
import com.bluevia.messagery.mo.mms.data.MmsMessageInfo;
import com.bluevia.messagery.mo.mms.data.MmsMessageInfo.AttachmentInfo;
import com.bluevia.messagery.mt.data.DeliveryInfo;
import com.bluevia.messagery.mt.mms.client.BVMtMms;
import com.bluevia.messagery.mt.mms.data.Attachment;
import com.bluevia.messagery.mt.mms.data.Attachment.ContentType;


/**
 * MMS API Class Examples
 * 
 * @package com.bluevia.examples
 */
public class MmsExample {

	// Logger
	private static Logger log = Logger.getLogger(MmsExample.class.getName());

    // API path (Mode Live/Sandbox)
	public static Mode mode = Mode.SANDBOX;

    // CREDENTIALS: 
    // User must DEFINE VALID VALUES FOR consumer token & access token  
    // Consumer Key - Consumer Token
	public static OAuthToken consumerToken = new OAuthToken("vw12012654505986", "WpOl66570544");	
    // Access Key - Access Token
	public static OAuthToken accessToken = new OAuthToken("ad3f0f598ffbc660fbad9035122eae74", "4340b28da39ec36acb4a205d3955a853");
	
	
	// Temp folder to store files
	private static String tempFolder= "/path/to/temp/folder";


	public static void main(String[] args) {
		
		sendMms();
		//getMessages(true);
	}

	/**
	 * MMS API - MMS MT EXAMPLE
	 *  
	 */
	public static void sendMms() {

		System.out.println("***** Send MMS Example");
		BVMtMms client = null;

		try {
			client = new BVMtMms(mode, consumerToken.getToken(), consumerToken.getSecret(),
											   accessToken.getToken(), accessToken.getSecret());

			// Prepare SMS parameters
			String address = "5698765";

			ArrayList<Attachment> files = new ArrayList<Attachment>();
			files.add(new Attachment("image.jpg", ContentType.IMAGE_JPEG));
			String subject = "Hello Bluevia!";
			String message = "Example of MMS with image jpg atteached";

			// Sending MMS
			String mmsId = client.send(address, subject, message, files);

			if (mmsId != null) {
				System.out.println("Mms was sent. Id: " + mmsId);
			}
	
			// Getting delivery status
			ArrayList<DeliveryInfo> response = client.getDeliveryStatus(mmsId);


			for (DeliveryInfo status : response) {
				System.out.println("Delivery status: " + status.getStatus());
			}

		} catch (BlueviaException e) {
			log.error("BlueviaException: " + e.getMessage());
		} catch (IOException ex) {
			log.error("IOException: " + ex);
		} finally {
			// Close the client
			if (client != null)	client.close();			
		}
	}

	/*
	 * README to understand the example
	 * 
	 * Uses the function getMessages
	 * 
	 * If the boolean parameter useGetAttachments is true, 
	 * the attachments will be obtained using the function getAttachments for each one;
	 * if it is false, the method getMessage 
	 * (which gets the complete list of attachments in a single call)
	 * 
	 */
	public static void getMessages(boolean useAttachmentUris) {

		System.out.println("***** Get MMS Example");
		BVMoMms client = null;
			
		try {
			// Prepare params
			String registrationId = "546780";

			client = new BVMoMms(mode, consumerToken.getToken(), consumerToken.getSecret());

			ArrayList<MmsMessageInfo> list = client.getAllMessages(registrationId, useAttachmentUris);

			if (list == null || list.isEmpty()){
				System.out.println("No messages");
			}

			for (MmsMessageInfo mms : list){
				System.out.println("Message received: " + mms.getMessageId());
				System.out.println("From: " + mms.getOriginAddress());
				System.out.println("Subject: " + mms.getSubject());

				if (useAttachmentUris){
					for (AttachmentInfo uri : mms.getAttachmentsInfo()){
						System.out.println("URL: " + uri.getUrl());
						MimeContent mime = client.getAttachment(registrationId, mms.getMessageId(), uri.getUrl());

						System.out.println("Attachment name: " + mime.getName());
						System.out.println("Attachment type: " + mime.getContentType());

						if ((mime.getContentType().contains("text")) || 
							(mime.getContentType().contains("xml")) || 
							(mime.getContentType().contains("smil"))) {
							System.out.println("Text Attachement: ");
							System.out.println((String) mime.getContent());
						} else {
							ByteArrayInputStream bis = new ByteArrayInputStream((byte[]) mime.getContent());
							
							//Do stuff...
							System.out.println(mime.getName());							
							String file_name= tempFolder + mime.getName();
							FileOutputStream file = new FileOutputStream(file_name);							
							byte[] buf = new byte[1024];
							int len;	
							while ((len = bis.read(buf)) > 0) {
							    file.write(buf, 0, len);
							}
							bis.close();
							file.close();
							
						}

					}
				} else {

					MmsMessage completeMMS = client.getMessage(registrationId, mms.getMessageId());

					System.out.println("#Number of Attachments: " + completeMMS.getAttachments().size());

					for (MimeContent mime : completeMMS.getAttachments()){
						System.out.println("Attachment name: " + mime.getName());
						System.out.println("Attachment type: " + mime.getContentType());

						if (!(mime.getContentType().equals("text/plain"))) {
							ByteArrayInputStream bis = new ByteArrayInputStream((byte[]) mime.getContent());
							
							//Do stuff...
							System.out.println(mime.getName());													
							String file_name= tempFolder + mime.getName();
							FileOutputStream file = new FileOutputStream(file_name);							
							byte[] buf = new byte[1024];
							int len;	
							while ((len = bis.read(buf)) > 0) {
							    file.write(buf, 0, len);
							}
							bis.close();
							file.close();							
						}
												  
					}

				}
			}


        } catch (BlueviaException ex) {
        	log.error("BlueviaException:" + ex.getMessage());
 		} catch (IOException e) {
 			log.error("IO Exception" + e.getMessage());
		} finally {
			// Close the client
			if (client != null)	client.close();			
		}
	}

	public static void notifications(){

		System.out.println("***** Subscribe/Unsubscribe to Notificacions Example");
		BVMoMms client = null;
		
		try {
			String phoneNumber = "546780";
			String endpoint= "https://www.example.com";
			String criteria= "MO_key";

			client = new BVMoMms(mode, consumerToken.getToken(), consumerToken.getSecret());

			//Susbscribe
			String correlator= client.startNotification(phoneNumber, endpoint, criteria);
			System.out.println("Suscribed with Correlator ["+ correlator +"]");
			
			//Unsubscribe
			client.stopNotification(correlator);

        } catch (BlueviaException e) {
        	log.error("BlueviaException:" + e.getMessage());
		} catch (IOException e) {
        	log.error("IOException:" + e.getMessage());
		} finally {
			// Close the client
			if (client != null)	client.close();			
		}
	}

}