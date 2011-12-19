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
     * @param tokenConsumer
     * @param token
     * @param mode
     * @throws JAXBException
     */

    public NotificationManager(OAuthToken tokenConsumer, OAuthToken token, Mode mode) throws JAXBException {
        super(tokenConsumer, token, mode);
    }

    /**
     * 
     * @param snt
     * @return
     * @throws JAXBException
     * @throws BlueviaException
     */
    public String subscribe(SMSNotificationType snt) throws JAXBException, BlueviaException {

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectFactory of = new ObjectFactory();
        JAXBElement<SMSNotificationType> sms = of.createSmsNotification(snt);
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
     * 
     * @param notifId the notifId returned by subscribe function
     * @throws JAXBException
     * @throws BlueviaException
     */
    public void unsubscribeNotification(String notifId) throws JAXBException, BlueviaException {
        String url = this.uri + SUBSCRIPTION_PATH + "/" + notifId + "?version=v1";
        this.restConnector.get(url, "DELETE");
        return;
    }
}
