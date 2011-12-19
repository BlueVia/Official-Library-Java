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
import com.bluevia.java.mms.data.Attachment;
import com.bluevia.java.mms.data.Attachment.ContentType;
import com.bluevia.java.mms.data.MimeContent;
import com.bluevia.java.mms.data.ReceivedMMS;
import com.bluevia.java.oauth.OAuthToken;
import com.telefonica.schemas.unica.rest.mms.v1.DeliveryInformationType;
import com.telefonica.schemas.unica.rest.mms.v1.MessageDeliveryStatusType;
import com.telefonica.schemas.unica.rest.mms.v1.MessageReferenceType;
import com.telefonica.schemas.unica.rest.mms.v1.ReceivedMessagesType;

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
    public static OAuthToken consumer = new OAuthToken("", "");

    // Access Token - Access Token Secret
    public static OAuthToken accesstoken = new OAuthToken("", "");
    
    public static void main(String[] args) {
    	sendMms();
    	//getMessages();
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
            String addresses[] = { "5421100001" };
            Attachment[] files = {new Attachment("image.jpg", ContentType.IMAGE_JPEG),
            		new Attachment("message.txt", ContentType.TEXT_PLAIN)};		//Fake attachments
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

    public static void getMessages() {

        try {

        	System.out.println("***** Get MMS Example");

        	// Create mms receiver
            MessageMO mmsReceiver = new MessageMO(consumer, accesstoken, mode);
            
            // Prepare params
            String registrationId = "546780";
            
            // Get messages info
            ReceivedMessagesType response = mmsReceiver.getMessages(registrationId);
            
            if (response == null || 
            		response.getReceivedMessages() == null
            		|| response.getReceivedMessages().isEmpty()){
            	
            	System.out.println("No messages");
            
            } else {
            	List<MessageReferenceType> list = response.getReceivedMessages();
                
                for (MessageReferenceType messageReference : list){
                	// Get message
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

        } catch (BlueviaException ex) {
            Logger.getLogger(MmsExample.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JAXBException ex) {
            Logger.getLogger(MmsExample.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}