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
import com.bluevia.java.gap.Advertisement;
import com.bluevia.java.gap.CreativeElement;
import com.bluevia.java.gap.CreativeElements;
import com.bluevia.java.gap.SimpleAd;
import com.bluevia.java.oauth.OAuthToken;

/**
 * Advertising API Class Examples
 * 
 * @package com.bluevia.examples
 */
public class AdvertisingExample {

    // API path (Mode Live/Sandbox)
    public static Mode mode = Mode.SANDBOX;

    // CREDENTIALS: 
    // User must DEFINE VALID VALUES FOR consumer token & access token
    
    // Consumer Key - Consumer Token
    public static OAuthToken consumer = new OAuthToken("vw12012654505986", "WpOl66570544");
    
    // Access Token - Access Token Secret
    public static OAuthToken accesstoken = new OAuthToken("ad3f0f598ffbc660fbad9035122eae74", "4340b28da39ec36acb4a205d3955a853");

    public static void main(String[] args) {
        getAvertising();
    }

    /**
     * Advertising API - Get Advertising example
     * 
     */
    public static void getAvertising() {

        try {

            System.out.println("***** AppTestAdvertising getAvertising");

            SimpleAd request = new SimpleAd();
            request.setAdreqId("10654CC10-11-05T20:31:13c6c72731ad");
            request.setUserAgent("Mozilla/5.0");
            request.setAdSpace("10977");
            request.setProtection_policy("1");
            request.setCountry("UK");
            Advertisement ad = new Advertisement(consumer, accesstoken, mode);

            CreativeElements response = ad.send(request);
            if (response != null) {
                for (CreativeElement e : response.getCreative_elements()) {
                    System.out.println("creative_element_type_id: " + e.getType_id());
                    System.out.println("creative_element_type_name: " + e.getType_name());
                    System.out.println("creative_element_value: " + e.getValue());
                    System.out.println("creative_element_interaction: " + e.getInteraction());
                }
            }

        } catch (BlueviaException ex) {
            Logger.getLogger(AdvertisingExample.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } catch (JAXBException ex) {
            Logger.getLogger(AdvertisingExample.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
