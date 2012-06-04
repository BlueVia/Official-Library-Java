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

import org.apache.log4j.Logger;

import com.bluevia.commons.client.BVBaseClient.Mode;
import com.bluevia.commons.connector.http.oauth.OAuthToken;
import com.bluevia.commons.exception.BlueviaException;
import com.bluevia.location.client.BVLocation;
import com.bluevia.location.data.LocationInfo;


/**
 * Location API Class Examples
 * 
 * @package com.bluevia.examples
 */
public class LocationExample {

	// Logger
	private static Logger log = Logger.getLogger(LocationExample.class.getName());

    // API path (Mode Live/Sandbox)
	public static Mode mode = Mode.SANDBOX;

    // CREDENTIALS: 
    // User must DEFINE VALID VALUES FOR consumer token & access token  
    // Consumer Key - Consumer Token
	public static OAuthToken consumerToken = new OAuthToken("vw12012654505986", "WpOl66570544");	
    // Access Key - Access Token
	public static OAuthToken accessToken = new OAuthToken("ad3f0f598ffbc660fbad9035122eae74", "4340b28da39ec36acb4a205d3955a853");

	
    public static void main(String[] args) {
        getLocation();
    }
    
    /**
     * Location API - TEST Get Location
     * 
     * @param
     * @return
     * @throws
     */
    public static void getLocation() {

    	System.out.println("***** Example Location getLocation");
    	BVLocation client = null;
            
         try {
           
            int acceptableAccuracy = 1000;
            
			client = new BVLocation(mode, 
					consumerToken.getToken(), consumerToken.getSecret(),
					accessToken.getToken(), accessToken.getSecret());
			
			
			LocationInfo response = client.getLocation(acceptableAccuracy);
						
			System.out.println("Accuracy: " + response.getAccuracy());
			System.out.println("Latitude: " + response.getLatitude());
			System.out.println("Longitude: " + response.getLongitude());
			System.out.println("Date: " + response.getTimestamp());
			System.out.println("ReportStatus: " + response.getReportStatus().toString());
			
			

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
