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

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import com.bluevia.java.AbstractClient.Mode;
import com.bluevia.java.exception.BlueviaException;
import com.bluevia.java.oauth.AccessToken;
import com.bluevia.java.oauth.OAuthToken;
import com.bluevia.java.payment.PaymentOperation;
import com.bluevia.java.payment.ServiceInfo;
import com.bluevia.java.payment.oauth.PaymentRequestToken;
import com.telefonica.schemas.unica.rpc.payment.v1.GetPaymentStatusResultType;
import com.telefonica.schemas.unica.rpc.payment.v1.PaymentInfoType;
import com.telefonica.schemas.unica.rpc.payment.v1.PaymentParamsType;
import com.telefonica.schemas.unica.rpc.payment.v1.PaymentResultType;

/**
 * Advertising API Class Examples
 * 
 * @package com.bluevia.examples
 */
public class PaymentExample {

	// API path (Mode Live/Sandbox)
	public static Mode mode = Mode.SANDBOX;

	// CREDENTIALS: 
	// User must DEFINE VALID VALUES FOR consumer token & access token

	// Consumer Key - Consumer Token
	public static OAuthToken consumer = new OAuthToken("", "");

	// Access Token - Access Token Secret
	// Must be obtained for each Payment operation
	public static OAuthToken accesstoken = null;

	public static void main(String[] args) throws JAXBException, BlueviaException {
		payment();
		//cancel();
	}

	/**
	 * Payment API - Get Oauth Token example
	 * 
	 */
	public static void oauthProcess() {

		System.out.println("***** PaymentExample getRequestToken");

		try {

			PaymentRequestToken rt = new PaymentRequestToken(consumer, mode);

			PaymentParamsType params = new PaymentParamsType();
			PaymentInfoType paymentInfo = new PaymentInfoType();
			paymentInfo.setAmount(177);
			paymentInfo.setCurrency("EUR");
			params.setPaymentInfo(paymentInfo);

			// NOTE: fake values, user must fill correct data
			ServiceInfo serviceInfo = new ServiceInfo("service_id", "bluevia");
			
			OAuthToken requestToken = rt.getPaymentRequestToken(null, params, serviceInfo);

			System.out.println("Token: " + requestToken.getToken());
			System.out.println("Secret: " + requestToken.getSecret());
			System.out.println("Url: " + requestToken.getUrl());

			System.out.println("oauth_verifier: ");
			Scanner in = new Scanner(System.in);     
			String oauth_verifier = in.nextLine();

			System.out.println("***** PaymentExample getAccessToken");
			AccessToken at = new AccessToken(consumer, requestToken);

			accesstoken = at.get(oauth_verifier);
			System.out.println("Access Token:" + accesstoken.getToken());
			System.out.println("Access Token Secret:" + accesstoken.getSecret());

		} catch (JAXBException ex) {
			Logger.getLogger(PaymentExample.class.getName()).log(Level.SEVERE, null, ex);
		} 
	}

	/**
	 * Payment API - Payment example
	 * @throws JAXBException 
	 * @throws BlueviaException 
	 * 
	 */
	public static void payment() throws JAXBException {

		//Get credentials
		oauthProcess();

		System.out.println("***** PaymentExample payment operation");

		try {

			PaymentOperation op = new PaymentOperation(consumer, accesstoken, mode);

			PaymentParamsType params = new PaymentParamsType();

			PaymentInfoType pInfo = new PaymentInfoType();
			pInfo.setAmount(177);
			pInfo.setCurrency("EUR");
			params.setPaymentInfo(pInfo);

			PaymentResultType result = op.payment(params);

			System.out.println("Transaction id: " + result.getTransactionId());
			System.out.println("Transaction status: " + result.getTransactionStatus());
			System.out.println("Description: " + result.getTransactionStatusDescription());

			System.out.println("***** PaymentExample get payment status");

			GetPaymentStatusResultType status = op.getStatus(result.getTransactionId());

			System.out.println("Transaction status: " + result.getTransactionStatus());
			System.out.println("Description: " + status.getTransactionStatusDescription());

		} catch (BlueviaException ex) {
			Logger.getLogger(PaymentExample.class.getName()).log(Level.SEVERE, null, ex);
		} 
	}

	/**
	 * Payment API - Cancel example
	 * @throws JAXBException 
	 * @throws BlueviaException 
	 * 
	 */
	public static void cancel() throws JAXBException {

		//Get credentials
		oauthProcess();

		System.out.println("***** PaymentExample cancel authorization");

		try {

			PaymentOperation op = new PaymentOperation(consumer, accesstoken, mode);
			op.cancelAuthorization();

			System.out.println("Authorization cancelled");

		} catch (BlueviaException ex) {
			Logger.getLogger(PaymentExample.class.getName()).log(Level.SEVERE, null, ex);
		} 
	}

}
