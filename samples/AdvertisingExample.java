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

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.bluevia.ad.client.BVAdvertising;
import com.bluevia.ad.data.AdRequest;
import com.bluevia.ad.data.AdRequest.ProtectionPolicyType;
import com.bluevia.ad.data.AdRequest.Type;
import com.bluevia.ad.data.simple.CreativeElement;
import com.bluevia.ad.data.simple.SimpleAdResponse;
import com.bluevia.commons.client.BVBaseClient.Mode;
import com.bluevia.commons.connector.http.oauth.OAuthToken;
import com.bluevia.commons.exception.BlueviaException;


/**
 * Advertising API Class Examples
 * 
 * @package com.bluevia.examples
 */
public class AdvertisingExample {

	// Logger
	private static Logger log = Logger.getLogger(AdvertisingExample.class.getName());

    // API path (Mode Live/Sandbox)
	public static Mode mode = Mode.SANDBOX;

    // CREDENTIALS: 
    // User must DEFINE VALID VALUES FOR consumer token & access token  
    // Consumer Key - Consumer Token
	public static OAuthToken consumerToken = new OAuthToken("vw12012654505986", "WpOl66570544");	
    // Access Key - Access Token
	public static OAuthToken accessToken = new OAuthToken("ad3f0f598ffbc660fbad9035122eae74", "4340b28da39ec36acb4a205d3955a853");
	
    // Consumer Key - Consumer Token - Oauth 2 legged
	public static OAuthToken consumerToken2l = new OAuthToken("xo12050967399553", "blFe48931024");	

	// Advertising Params
	public static final String ADSPACE = "ad_space";
	public static final Type AD_PRESENTATION = AdRequest.Type.IMAGE;

	
	
    public static void main(String[] args) {
    	
    	// Logger
    	BasicConfigurator.configure();

        getAvertising3l();
        //getAvertising2l();
    }

    /**
     * Advertising API - Get Advertising example
     * Oauth 3 legged
     * 
     */
    public static void getAvertising3l() {

        System.out.println("***** Sample Advertising getAvertising");
    	BVAdvertising client = null;
    	
        try {

            String adRequestId= "10654CC10-11-05T20:31:13c6c72731ad";
            String userAgent= "Mozilla/5.0";
            String country= "UK";
            String[] keywords= null;
 
			client = new BVAdvertising(mode, 
					consumerToken.getToken(), consumerToken.getSecret(),
					accessToken.getToken(), accessToken.getSecret());
			
			SimpleAdResponse res = client.getAdvertising3l(ADSPACE, country, adRequestId, 
														   AD_PRESENTATION, keywords, 
														   ProtectionPolicyType.SAFE, userAgent);
						
			System.out.println("RequestId: " + res.getRequestId());
			
			ArrayList<CreativeElement> creativeList= res.getAdvertisingList();
			for (CreativeElement element: creativeList) {
				System.out.println("Creative List Element Interaction: " + element.getInteraction());				
				System.out.println("Creative List Element Value: " + element.getValue());				
				System.out.println("Creative List Element Type: " + element.getType().toString());	
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
    
    /**
     * Advertising API - Get Advertising example
     * Oauth 2 legged
     * 
     */
    public static void getAvertising2l() {

        System.out.println("***** Sample Advertising getAvertising");
    	BVAdvertising client = null;
    	
        try {

            String adRequestId= "10654CC10-11-05T20:31:13c6c72731ad";
            String userAgent= "Mozilla/5.0";
            String country= "UK";
            String[] keywords= null;
            String target_user_id = null;
 
			client = new BVAdvertising(mode, 
					consumerToken2l.getToken(), consumerToken2l.getSecret(),
					accessToken.getToken(), accessToken.getSecret());
			
			SimpleAdResponse res = client.getAdvertising2l(ADSPACE, country, target_user_id, adRequestId,
					AD_PRESENTATION, keywords, ProtectionPolicyType.SAFE, userAgent);
						
			System.out.println("RequestId: " + res.getRequestId());
			
			ArrayList<CreativeElement> creativeList= res.getAdvertisingList();
			for (CreativeElement element: creativeList) {
				System.out.println("Creative List Element Interaction: " + element.getInteraction());				
				System.out.println("Creative List Element Value: " + element.getValue());				
				System.out.println("Creative List Element Type: " + element.getType().toString());	
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

}
