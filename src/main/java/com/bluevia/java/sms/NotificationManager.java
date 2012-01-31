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

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import com.bluevia.java.Utils;
import com.bluevia.java.exception.BlueviaException;
import com.bluevia.java.oauth.OAuthToken;
import com.telefonica.schemas.unica.rest.sms.v1.ObjectFactory;
import com.telefonica.schemas.unica.rest.sms.v1.SMSNotificationType;

/**
 * Notification
 * 
 * @package com.bluevia.java.sms
 */

/**
 * <p>Java class for Notificatin SMS Api
 * 
 */


public class NotificationManager extends SMSClient {
	
	private static final String SUBSCRIPTION_PATH = "/inbound/subscriptions";

    /**
     * Constructor
     * 
     * @param consumer
     * @param mode
     * @throws JAXBException
     */
    public NotificationManager(OAuthToken consumer, Mode mode) throws JAXBException {
        super(consumer, null, mode);
    }

    /**
     * Subscribes to notifications of sent messages in the endpoint passed in parameters.
     * 
     * @param params SMS Notification type parameters
     * @return the id of the subscription
     * @throws JAXBException
     * @throws BlueviaException
     */
    public String subscribe(SMSNotificationType params) throws JAXBException, BlueviaException {
    	
    	if (params == null)
    		throw new IllegalArgumentException("Invalid parameter: params cannot be null");
    	
    	if (params.getReference() == null)
    		throw new IllegalArgumentException("Invalid parameter: reference cannot be null");
    	
    	if (Utils.isEmpty(params.getReference().getEndpoint()))
    		throw new IllegalArgumentException("Invalid parameter: endpoint");
    	
    	if (Utils.isEmpty(params.getReference().getCorrelator()))
    		throw new IllegalArgumentException("Invalid parameter: correlator");
    	
    	if (Utils.isEmpty(params.getCriteria()))
    		throw new IllegalArgumentException("Invalid parameter: criteria");
    	
    	if (params.getDestinationAddress() == null || params.getDestinationAddress().size() == 0)
    		throw new IllegalArgumentException("Invalid parameter: destination adresses");
    	
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectFactory of = new ObjectFactory();
        JAXBElement<SMSNotificationType> sms = of.createSmsNotification(params);
        this.m.marshal(sms, os);
        String url = this.uri + SUBSCRIPTION_PATH + "?version=v1";
        String res = this.restConnector.post(url, os.toByteArray());
        
        String regex = this.uri + SUBSCRIPTION_PATH + "/(.*)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(res);
        if (matcher.matches()){
        	return matcher.group(1);
        } else return res;
    }

    /**
     * Unsuscribes notifications of the corresponding notification id.
     * 
     * @param notifId the notification id of the subscription
     * @throws JAXBException
     * @throws BlueviaException
     */
    public void unsubscribeNotification(String notifId) throws JAXBException, BlueviaException {
        
    	if (Utils.isEmpty(notifId))
    		throw new IllegalArgumentException("Invalid parameter: notification id");
    	
    	String url = this.uri + SUBSCRIPTION_PATH + "/" + notifId + "?version=v1";
        this.restConnector.get(url, "DELETE");
        return;
    }
}
