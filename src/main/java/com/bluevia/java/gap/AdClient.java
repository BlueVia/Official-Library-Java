/**
 * 
 * @category bluevia
 * @package com.bluevia.java.gap
 * @copyright Copyright (c) 2010 Telefonica I+D (http://www.tid.es)
 * @author Bluevia <support@bluevia.com>
 * 
 * 
 *          BlueVia is a global iniciative of Telefonica delivered by Movistar
 *          and O2. Please, check out http://www.bluevia.com and if you need
 *          more information contact us at support@bluevia.com
 */

package com.bluevia.java.gap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import com.bluevia.java.AbstractRESTClient;
import com.bluevia.java.oauth.OAuthToken;

/**
 * AdClient
 * 
 * @package com.bluevia.java.gap
 */

/**
 * <p>Java class for Advertising API. This class is used to initialize objects.
 * 
 */
public class AdClient extends AbstractRESTClient {

	private static String PATH_LIVE = "/REST/Advertising/simple/requests";
	private static String PATH_SANDBOX = "/REST/Advertising_Sandbox/simple/requests";

    /**
     * Constructor
     * 
     * @param consumer
     * @param token
     * @param url_prefix
     * @throws JAXBException
     */
    public AdClient(OAuthToken consumer, OAuthToken token, Mode mode) throws JAXBException {
    	super(consumer, token, mode, PATH_LIVE, PATH_SANDBOX);
    	
        this.jc = JAXBContext.newInstance("com.telefonica.schemas.unica.rest.sgap.v1");
        this.u = jc.createUnmarshaller();
        this.m = jc.createMarshaller();
    }
}