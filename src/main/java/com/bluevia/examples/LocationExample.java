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

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import com.bluevia.java.AbstractClient.Mode;
import com.bluevia.java.exception.BlueviaException;
import com.bluevia.java.location.TerminalLocation;
import com.bluevia.java.oauth.OAuthToken;
import com.telefonica.schemas.unica.rest.location.v1.LocationDataType;

/**
 * Location API Class Examples
 * 
 * @package com.bluevia.examples
 */
public class LocationExample {

	// API path (Mode Live/Sandbox)
    public static Mode mode = Mode.SANDBOX;
    
    // CREDENTIALS: 
    // User must DEFINE VALID VALUES FOR consumer token & access token
    
    // Consumer Key - Consumer Token
    public static OAuthToken consumer = new OAuthToken("vw12012654505986", "WpOl66570544");
    
    // Access Token - Access Token Secret
    public static OAuthToken accesstoken = new OAuthToken("ad3f0f598ffbc660fbad9035122eae74", "4340b28da39ec36acb4a205d3955a853");

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

        try {

            System.out.println("***** AppTestLocation getLocation");
            
            TerminalLocation location = new TerminalLocation(consumer, accesstoken, mode);
            
            int acceptableAccuracy = 1000;
            
            LocationDataType locationData = location.getLocation(acceptableAccuracy);

            System.out.println("Located Party: " + locationData.getLocatedParty().getAlias());
            System.out.println("Coordinates: " + 
            	locationData.getCurrentLocation().getCoordinates().getLatitude() + ", " +
    			locationData.getCurrentLocation().getCoordinates().getLatitude());
            System.out.println("Date: " + locationData.getCurrentLocation().getTimestamp().toString());


        } catch (BlueviaException ex) {
            Logger.getLogger(LocationExample.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } catch (JAXBException ex) {
            Logger.getLogger(LocationExample.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
}
