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


import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.bluevia.commons.client.BVBaseClient.Mode;
import com.bluevia.commons.connector.http.oauth.OAuthToken;
import com.bluevia.commons.exception.BlueviaException;
import com.bluevia.messagery.mo.sms.client.BVMoSms;
import com.bluevia.messagery.mo.sms.data.SmsMessage;
import com.bluevia.messagery.mt.data.DeliveryInfo;
import com.bluevia.messagery.mt.sms.client.BVMtSms;


/**
 * SMS API Class Examples
 * 
 * @package com.bluevia.examples
 */
public class SmsExample {

	// Logger
	private static Logger log = Logger.getLogger(SmsExample.class.getName());

    // API path (Mode Live/Sandbox)
	public static Mode mode = Mode.SANDBOX;

    // CREDENTIALS: 
    // User must DEFINE VALID VALUES FOR consumer token & access token  
    // Consumer Key - Consumer Token
	public static OAuthToken consumerToken = new OAuthToken("vw12012654505986", "WpOl66570544");	
    // Access Key - Access Token
	public static OAuthToken accessToken = new OAuthToken("ad3f0f598ffbc660fbad9035122eae74", "4340b28da39ec36acb4a205d3955a853");
	

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

    	BVMtSms client = null;
        try {

            System.out.println("***** Send SMS Example");

            // Create message sender
			client = new BVMtSms(mode, 
					consumerToken.getToken(), consumerToken.getSecret(), 
					accessToken.getToken(), accessToken.getSecret());
			
            // Prepare SMS parameters
            String message = "Hello Bluevia!";
			ArrayList<String> addresses = new ArrayList<String>();
			addresses.add("54110000001");
			
            // Sending SMS
			String smsId = client.send(addresses, message);
            if (smsId != null) {
                System.out.println("Sms was sent. Id: " + smsId);
            }
			
            // Getting delivery status
			ArrayList<DeliveryInfo> list = client.getDeliveryStatus(smsId);
			System.out.println("ID: " + smsId);
			System.out.println("list size: " + list.size());
            for (DeliveryInfo status : list) {
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
    
    public static void getSms(){
    	

    	System.out.println("***** Get SMSs Example");
    	BVMoSms client = null;
            
        	try {
            // Create the sms receiver
			client = new BVMoSms(mode, consumerToken.getToken(), consumerToken.getSecret());
           
            // Prepare params
			String shortNumber = "546780";
           
            // Get Messages
			ArrayList<SmsMessage> list = client.getAllMessages(shortNumber);
 
			if (list.isEmpty()){
				System.out.println("No messages");
			} else {
				for (SmsMessage sms : list){
					String s = "Received SMS: [" + sms.getMessage() + "] from " + sms.getOriginAddress();
					System.out.println(s);
				}
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
    
    public static void notifications(){
    	
        System.out.println("***** Subscribe/Unsubscribe Notificacions Example");           
    	BVMoSms client = null;

    	try {
			OAuthToken consumerToken = new OAuthToken("", "");
			String phoneNumber = "546780";
			String endpoint= "https://www.example.com";
			String criteria= "MO_key";

			client = new BVMoSms(mode, consumerToken.getToken(), consumerToken.getSecret());
			
            //Susbscribe
			String correlator= client.startNotification(phoneNumber, endpoint, criteria);
			System.out.println("Subscribed with correlator ["+ correlator +"]");
			
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
