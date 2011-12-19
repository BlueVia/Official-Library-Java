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

import java.io.StringReader;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;

import com.bluevia.java.Utils;
import com.bluevia.java.exception.BlueviaException;
import com.bluevia.java.oauth.OAuthToken;
import com.telefonica.schemas.unica.rest.sms.v1.ReceivedSMSType;


/**
 * MessageMO
 * @package com.bluevia.java.sms 
 */

/**
 * <p>Java class to get SMS message from SMS API
 *
 */

public class MessageMO extends SMSClient {

	private static final String MESSAGE_MO_PATH = "/inbound";

    /**
     * Constructor
     * @param tokenConsumer
     * @param token
     * @param mode
     * @throws JAXBException
     */
    public MessageMO(OAuthToken tokenConsumer, OAuthToken token, Mode mode) throws JAXBException {
        super(tokenConsumer, token, mode);
    }

    /**
     * Get Messages from SMS API
     * Note: the origin address of the received SMS will contain an alias, not a phone number.
     * 
     * @param registrationID
     * @return a ReceivedSMSType containing the list of received messages
     * @throws JAXBException
     * @throws BlueviaException
     */
    public ReceivedSMSType getMessages(String registrationID) throws JAXBException, BlueviaException {

    	if (Utils.isEmpty(registrationID))
    		throw new IllegalArgumentException("Invalid parameter: registrationID");
    	
        String url = this.uri + MESSAGE_MO_PATH + "/" + registrationID + "/messages?version=v1";
        String res = this.restConnector.get(url);

        if (Utils.isEmpty(res))
        	return null;
        
        JAXBElement<ReceivedSMSType> e = this.u.unmarshal(new StreamSource(new StringReader(res)), ReceivedSMSType.class);
        return e.getValue();
    }
}
