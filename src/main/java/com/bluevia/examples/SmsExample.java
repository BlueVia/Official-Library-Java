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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import com.bluevia.java.AbstractClient.Mode;
import com.bluevia.java.exception.BlueviaException;
import com.bluevia.java.oauth.OAuthToken;
import com.bluevia.java.sms.MessageMO;
import com.bluevia.java.sms.MessageMT;
import com.bluevia.java.sms.NotificationManager;
import com.telefonica.schemas.unica.rest.common.v1.SimpleReferenceType;
import com.telefonica.schemas.unica.rest.common.v1.UserIdType;
import com.telefonica.schemas.unica.rest.sms.v1.DeliveryInformationType;
import com.telefonica.schemas.unica.rest.sms.v1.ReceivedSMSType;
import com.telefonica.schemas.unica.rest.sms.v1.SMSDeliveryStatusType;
import com.telefonica.schemas.unica.rest.sms.v1.SMSMessageType;
import com.telefonica.schemas.unica.rest.sms.v1.SMSNotificationType;

/**
 * SMS API Class Examples
 * 
 * @package com.bluevia.examples
 */
public class SmsExample {

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
    	sendSms();
    	//getSms();
    	//notifications();
    }

    /**
     * SMS API - SMS MT EXAMPLE (SEND SMS TEXT MESSAGE)
     * 
     */
    public static void sendSms() {

        try {

            System.out.println("***** Send SMS Example");

            // Create message sender
            MessageMT messageSender = new MessageMT(consumer, accesstoken, mode);
            
            // Prepare SMS parameters
            String addresses[] = { "546780" };
            String message = "Hello Bluevia!";

            // Sending SMS
            String smsId = messageSender.send(addresses, message);
            if (smsId != null) {
                System.out.println("Sms was sent. Id: " + smsId);
            }

            // Getting delivery status
            SMSDeliveryStatusType response = messageSender.getStatus(smsId);
            List<DeliveryInformationType> list = response.getSmsDeliveryStatus();
            
            for (DeliveryInformationType status : list) {
                System.out.println("Delivery status: " + status.getDeliveryStatus());
            }

        } catch (BlueviaException ex) {
            Logger.getLogger(SmsExample.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } catch (JAXBException ex) {
            Logger.getLogger(SmsExample.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void getSms(){
    	
    	try {

            System.out.println("***** Get SMSs Example");
            
            // Create the sms receiver
            MessageMO smsReceiver = new MessageMO(consumer, mode);
            
            // Prepare params
            String registrationId = "546780";
            
            // Get Messages
            ReceivedSMSType response = smsReceiver.getMessages(registrationId);
            List<SMSMessageType> list = response.getReceivedSMS();
            
            if (list == null || list.isEmpty()){
            	System.out.println("No messages");
            } else {
            	for (SMSMessageType sms : list){
                	System.out.println("Received SMS from '" + 
                			sms.getOriginAddress().getPhoneNumber() + 
                			"' > '" + sms.getMessage() + "'");
                }
            }
            
        } catch (BlueviaException ex) {
            Logger.getLogger(SmsExample.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } catch (JAXBException ex) {
            Logger.getLogger(SmsExample.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }
    
    public static void notifications(){
    	try {

            System.out.println("***** Subscribe to Notificacions Example");
            
            NotificationManager nm = new NotificationManager(consumer, mode);

            SMSNotificationType snt = new SMSNotificationType();
            
            //Endpoint and correlator
            SimpleReferenceType ref = new SimpleReferenceType();
            ref.setEndpoint("https://www.example.com");
            ref.setCorrelator("123456");
            snt.setReference(ref);
            
            //Addresses
            List<UserIdType> addresses = new ArrayList<UserIdType>();
            UserIdType address = new UserIdType();
            address.setPhoneNumber("546780");
            addresses.add(address);
            snt.setDestinationAddress(addresses);
            
            //Criteria
            snt.setCriteria("MO_key");
            
            //Susbscribe
            String response = nm.subscribe(snt);
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
