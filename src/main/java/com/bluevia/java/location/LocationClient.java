/**
 * 
 * @category bluevia
 * @package com.bluevia.java.location
 * @copyright Copyright (c) 2010 Telefonica I+D (http://www.tid.es)
 * @author Bluevia <support@bluevia.com>
 * 
 * 
 *          BlueVia is a global iniciative of Telefonica delivered by Movistar
 *          and O2. Please, check out http://www.bluevia.com and if you need
 *          more information contact us at support@bluevia.com
 */

package com.bluevia.java.location;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import com.bluevia.java.AbstractRESTClient;
import com.bluevia.java.Utils;
import com.bluevia.java.oauth.OAuthToken;

/**
 * DirectoryClient
 * @package com.bluevia.unica.directory.client 
 */

/**
 * <p>Java class for Directory API. This class  is used to initialize objects.
 *
 */

public class LocationClient extends AbstractRESTClient {

	private static String PATH_LIVE = "/REST/Location/TerminalLocation";
	private static String PATH_SANDBOX = "/REST/Location_Sandbox/TerminalLocation";
	
    /**
     * Constructor
     * 
     * @param consumer
     * @param token
     * @param mode
     * @throws JAXBException
     */
    public LocationClient(OAuthToken consumer, OAuthToken token, Mode mode) throws JAXBException {
    	super(consumer, token, mode, PATH_LIVE, PATH_SANDBOX);

        if (!Utils.validateToken(token))
    		throw new IllegalArgumentException("Invalid parameter: oauth token");
    	
        this.jc = JAXBContext.newInstance("com.telefonica.schemas.unica.rest.location.v1");
        this.u = jc.createUnmarshaller();
        this.m=jc.createMarshaller();

    }
}


