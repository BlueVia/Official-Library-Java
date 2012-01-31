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

import javax.xml.bind.JAXBException;

import com.bluevia.java.AbstractClient.Mode;
import com.bluevia.java.oauth.AccessToken;
import com.bluevia.java.oauth.OAuthToken;
import com.bluevia.java.oauth.RequestToken;

/**
 * OAuth API Class Examples
 * 
 * @package com.bluevia.examples
 */
public class OAuthExample {

	// API path (Mode Live/Sandbox)
	public static Mode mode = Mode.SANDBOX;

    // CREDENTIALS: 
    // User must DEFINE VALID VALUES FOR consumer token & access token
    
    // Consumer Key - Consumer Token
    public static OAuthToken consumer = new OAuthToken("vw12012654505986", "WpOl66570544");

    public static void main(String[] args) throws JAXBException {

        oauthProcess();

    }

    public static void oauthProcess() throws JAXBException {

        System.out.println("***** OAuthExample getRequestToken");
        
        RequestToken rt = new RequestToken(consumer, mode);

        /*
         * Define your callback URL
         */
        String callBackUrl = "https://www.example.com";
        OAuthToken requestToken = rt.getRequestToken(callBackUrl);

        System.out.println("Request Token:" + requestToken.getToken());
        System.out.println("Request Token Secret:" + requestToken.getSecret());
        System.out.println("Portal url:" + requestToken.getUrl());
        
        
        /*  
         *  1. redirect to portal
         *  2. get response and get oauth_verifier from url response
         *  3. user oauth_verifier & requestToken to call getAccessToken
         *  
         */
        
        System.out.println("oauth_verifier: ");
        Scanner in = new Scanner(System.in);     
        String oauth_verifier = in.nextLine(); // Obtain this value from the user

        System.out.println("***** OAuthExample getAccessToken");
        AccessToken at = new AccessToken(consumer, requestToken);

        OAuthToken accessToken = at.get(oauth_verifier);
        System.out.println("Access Token:" + accessToken.getToken());
        System.out.println("Access Token Secret:" + accessToken.getSecret());

    }

    
}
