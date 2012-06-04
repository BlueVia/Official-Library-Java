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

import java.util.Scanner;

import org.apache.log4j.Logger;

import com.bluevia.commons.client.BVBaseClient.Mode;
import com.bluevia.commons.connector.http.oauth.OAuthToken;
import com.bluevia.commons.connector.http.oauth.RequestToken;
import com.bluevia.commons.exception.BlueviaException;
import com.bluevia.oauth.client.BVOauth;


/**
 * OAuth API Class Examples
 * 
 * @package com.bluevia.examples
 */
public class OAuthExample {

	// Logger
	private static Logger log = Logger.getLogger(OAuthExample.class.getName());

    // API path (Mode Live/Sandbox)
	public static Mode mode = Mode.SANDBOX;

    // CREDENTIALS: 
    // User must DEFINE VALID VALUES FOR consumer token & access token  
    // Consumer Key - Consumer Token
	public static OAuthToken consumerToken = new OAuthToken("vw12012654505986", "WpOl66570544");	
    // Access Key - Access Token
	public static OAuthToken accessToken = new OAuthToken("ad3f0f598ffbc660fbad9035122eae74", "4340b28da39ec36acb4a205d3955a853");


    public static void main(String[] args)  {

        oauthProcess();

    }


	public static void oauthProcess() {
		
        System.out.println("***** OAuthExample getRequestToken");

        BVOauth client = null;
        RequestToken response = null;
		OAuthToken access = null;
		
		try {

	        /*
	         * Define your callback URL
	         */
	        String callBack = "https://www.example.com";

			client = new BVOauth(mode, consumerToken.getToken(), consumerToken.getSecret());

			response = client.getRequestTokenSmsHandshake(callBack);
		
			System.out.println("Token: " + response.getToken());
			System.out.println("Secret: " + response.getSecret());
			System.out.println("Url: " + response.getVerificationUrl());

	        /*  
	         *  1. redirect to portal
	         *  2. get response and get oauth_verifier from url response
	         *  3. user oauth_verifier & requestToken to call getAccessToken
	         *  
	         */
			System.out.println("oauth_verifier: ");
			Scanner in = new Scanner(System.in);     
			String oauth_verifier = in.nextLine();

			access = client.getAccessToken(oauth_verifier, response.getToken(), response.getSecret());
			System.out.println("Access Token:" + access.getToken());
			System.out.println("Access Token Secret:" + access.getSecret());
			
		} catch (BlueviaException e){
			log.error(e.getMessage());
		} catch (NumberFormatException e){
			log.error(e.getMessage());
		} finally {
			// Close the client
			if (client != null)	client.close();			
		}
		
	}

    
}
