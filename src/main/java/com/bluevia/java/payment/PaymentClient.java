/**
 * 
 * @category opentel
 * @package com.bluevia.unica.payment
 * @copyright Copyright (c) 2010 Telefonica I+D (http://www.tid.es)
 * @author Bluevia <support@bluevia.com>
 * @version 1.3
 * 
 *          BlueVia is a global iniciative of Telefonica delivered by Movistar
 *          and O2. Please, check out http://www.bluevia.com and if you need
 *          more information contact us at support@bluevia.com
 */

package com.bluevia.java.payment;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import com.bluevia.java.AbstractRPCClient;
import com.bluevia.java.oauth.OAuthToken;

/**
 * SMSClient
 * @package com.bluevia.unica.sms 
 */

/**
 * <p>Java class for SMS Api. This class  is used to initialize objects.
 *
 */
public class PaymentClient extends AbstractRPCClient {

	private static String PATH_LIVE = "/RPC/Payment";
	private static String PATH_SANDBOX = "/RPC/Payment_Sandbox";

	/**
	 * Constructor
	 * 
     * @param consumer
     * @param token
     * @param url_prefix
     * @throws JAXBException
     */
    public PaymentClient(OAuthToken consumer, OAuthToken token,  Mode mode) throws JAXBException {
    	super(consumer, token);

		this.jc = JAXBContext.newInstance("com.telefonica.schemas.unica.rpc.payment.v1");
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
