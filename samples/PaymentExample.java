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
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.bluevia.commons.client.BVBaseClient.Mode;
import com.bluevia.commons.connector.http.oauth.OAuthToken;
import com.bluevia.commons.connector.http.oauth.RequestToken;
import com.bluevia.commons.exception.BlueviaException;
import com.bluevia.payment.client.BVPayment;
import com.bluevia.payment.data.PaymentResult;
import com.bluevia.payment.data.PaymentStatus;
import com.bluevia.payment.data.PaymentStatus.TransactionStatusType;


/**
 * Advertising API Class Examples
 * 
 * @package com.bluevia.examples
 */
public class PaymentExample {

	// Logger
	private static Logger log = Logger.getLogger(PaymentExample.class.getName());

    // API path (Mode Live/Sandbox)
	public static Mode mode = Mode.SANDBOX;

    // CREDENTIALS: 
    // User must DEFINE VALID VALUES FOR consumer token & access token  
    // Consumer Key - Consumer Token
	public static OAuthToken consumerToken = new OAuthToken("vw12012654505986", "WpOl66570544");	
    // Access Key - Access Token
	public static OAuthToken accessToken = new OAuthToken("ad3f0f598ffbc660fbad9035122eae74", "4340b28da39ec36acb4a205d3955a853");


	public static void main(String[] args) {
		payment();
		//cancel();
	}


	/**
	 * Payment API - Payment example
	 * 
	 */
	public static void payment() {
		
		System.out.println("***** PaymentExample payment operation");
		BVPayment client = null;
		
		try {

			RequestToken response = null;
			OAuthToken access = null;
			String serviceName = "bluevia";
			String serviceId = "";

			int amount = 177;
			String currency = "EUR";

			client = new BVPayment(mode, 
					consumerToken.getToken(), consumerToken.getSecret());

			// Get credentials
			response = client.getPaymentRequestToken(amount, currency, serviceName, serviceId);	
			System.out.println("Token: " + response.getToken());
			System.out.println("Secret: " + response.getSecret());
			System.out.println("Url: " + response.getVerificationUrl());

			// Waiting for verifier
			System.out.println("oauth_verifier: ");
			Scanner in = new Scanner(System.in);     
			String oauth_verifier = in.nextLine();

			// Get Access Token
			access = client.getPaymentAccessToken(oauth_verifier, response.getToken(), response.getSecret());
			System.out.println("Access Token:" + access.getToken());
			System.out.println("Access Token Secret:" + access.getSecret());
			
			// Make payment
			PaymentResult res = client.payment(amount, currency);		
			System.out.println("transaction : " + res.getTransactionId());
			System.out.println("status : " + res.getTransactionStatus());
			System.out.println("description : " + res.getTransactionStatusDescription());
			
			// Get Payment Status
			System.out.println("***** PaymentExample get payment status");
			String transactionId = res.getTransactionId();
			PaymentStatus resStatus = client.getPaymentStatus(transactionId);
			TransactionStatusType transactionStatus = resStatus.getTransactionStatus();
			System.out.println("transaction status : " + transactionStatus.toString());
			

		} catch (BlueviaException e){
			log.error(e.getMessage());
		} catch (NumberFormatException e){
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		} finally {
			// Close the client
			if (client != null)	client.close();			
		}

	}
		

	/**
	 * Payment API - Cancel example
	 * @throws JAXBException 
	 * @throws BlueviaException 
	 * 
	 */
	public static void cancel() {

		System.out.println("***** PaymentExample cancel authorization");
		BVPayment client = null;

		try {
			client = new BVPayment(mode, 
					consumerToken.getToken(), consumerToken.getSecret());
			client.cancelAuthorization();

			System.out.println("Authorization cancelled");

        } catch (BlueviaException ex) {
        	log.error("BlueviaException:" + ex.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		} finally {
			// Close the client
			if (client != null)	client.close();			
		}

	}


}
