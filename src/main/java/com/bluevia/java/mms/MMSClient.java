/**
 * 
 * @category bluevia
 * @package com.bluevia.java.mms
 * @copyright Copyright (c) 2010 Telefonica I+D (http://www.tid.es)
 * @author Bluevia <support@bluevia.com>
 * 
 * 
 *          BlueVia is a global iniciative of Telefonica delivered by Movistar
 *          and O2. Please, check out http://www.bluevia.com and if you need
 *          more information contact us at support@bluevia.com
 */

package com.bluevia.java.mms;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import com.bluevia.java.AbstractRESTClient;
import com.bluevia.java.oauth.OAuthToken;

/**
 * MMSClient
 * @package com.bluevia.java.mms 
 */

/**
 * <p>Java class for MMS API. This class  is used to initialize objects.
 *
 */
public class MMSClient extends AbstractRESTClient {

	private static String PATH_LIVE = "/REST/MMS";
	private static String PATH_SANDBOX = "/REST/MMS_Sandbox";

	/**
	 * Constructor
	 * 
	 * @param tokenConsumer
	 * @param token
	 * @param mode
	 * @throws JAXBException
	 */
    public MMSClient(OAuthToken consumer, OAuthToken token, Mode mode) throws JAXBException {
    	super(consumer, token, mode, PATH_LIVE, PATH_SANDBOX);
    	
        this.jc = JAXBContext.newInstance("com.telefonica.schemas.unica.rest.mms.v1");
        this.u = jc.createUnmarshaller();
        this.m=jc.createMarshaller();
        
    }
    
    /**
     * 
     * @param consumer
     * @param mode
     * @throws JAXBException
     */
    public MMSClient(OAuthToken consumer, Mode mode) throws JAXBException {
    	this(consumer, null, mode);
    }
}
