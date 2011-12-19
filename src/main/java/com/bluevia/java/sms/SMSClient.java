/**
 * 
 * @category bluevia
 * @package com.bluevia.java.sms
 * @copyright Copyright (c) 2010 Telefonica I+D (http://www.tid.es)
 * @author Bluevia <support@bluevia.com>
 * 
 * 
 *          BlueVia is a global iniciative of Telefonica delivered by Movistar
 *          and O2. Please, check out http://www.bluevia.com and if you need
 *          more information contact us at support@bluevia.com
 */

package com.bluevia.java.sms;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import com.bluevia.java.AbstractRESTClient;
import com.bluevia.java.oauth.OAuthToken;

/**
 * SMSClient
 * @package com.bluevia.java.sms 
 */

/**
 * <p>Java class for SMS Api. This class  is used to initialize objects.
 *
 */

public class SMSClient extends AbstractRESTClient {
	
	private static String PATH_LIVE = "/REST/SMS";
	private static String PATH_SANDBOX = "/REST/SMS_Sandbox";

    /**
     * Constructor 
     * @param consumer
     * @param token
     * @param mode
     * @throws JAXBException
     */
    public SMSClient(OAuthToken consumer, OAuthToken token,  Mode mode) throws JAXBException {
        super(consumer, token);
        this.jc = JAXBContext.newInstance("com.telefonica.schemas.unica.rest.sms.v1");
        this.u = jc.createUnmarshaller();
        this.m=jc.createMarshaller();
        
        switch (mode){
        case LIVE:
        	this.uri = BASE_ENDPOINT + PATH_LIVE;
        	break;
        case SANDBOX:
        	this.uri = BASE_ENDPOINT + PATH_SANDBOX;
        	break;
        }
    }
}
