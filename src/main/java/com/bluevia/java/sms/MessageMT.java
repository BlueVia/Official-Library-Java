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
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;

import com.bluevia.java.exception.BlueviaException;
import com.bluevia.java.oauth.OAuthToken;
import com.telefonica.schemas.unica.rest.common.v1.SimpleReferenceType;
import com.telefonica.schemas.unica.rest.common.v1.UserIdType;
import com.telefonica.schemas.unica.rest.sms.v1.ObjectFactory;
import com.telefonica.schemas.unica.rest.sms.v1.SMSDeliveryStatusType;
import com.telefonica.schemas.unica.rest.sms.v1.SMSTextType;

/**
 * MessageMT
 * 
 * @package com.bluevia.java.sms
 */

/**
 * <p>Java class to send SMS messages from SMS API
 * 
 */
public class MessageMT extends SMSClient {
	
	private static final String MESSAGE_MT_PATH = "/outbound/requests";
	private static final String MESSAGE_DELIVERY_STATUS = "/deliverystatus";

    /**
     * Constructor
     * 
     * @param tokenConsumer
     * @param token
     * @param mode
     * @throws JAXBException
     */
    public MessageMT(OAuthToken tokenConsumer, OAuthToken token, Mode mode) throws JAXBException {
        super(tokenConsumer, token, mode);
    }

    /**
     * Get Deliverty Status from sms send
     * 
     * @param id the url containing the SMS id
     * @return the SMS delivery status
     * @throws JAXBException
     * @throws BlueviaException
     */
    public SMSDeliveryStatusType getStatus(String id) throws JAXBException, BlueviaException {
    	
    	if (id == null || id.trim().length() == 0)
    		throw new IllegalArgumentException("Invalid parameter: url");
    	
    	String url = this.uri + MESSAGE_MT_PATH + "/" + id + MESSAGE_DELIVERY_STATUS + "?version=v1";

        String res = this.restConnector.get(url);

        JAXBElement<SMSDeliveryStatusType> e = this.u.unmarshal(new StreamSource(new StringReader(res)), SMSDeliveryStatusType.class);
        return e.getValue();
    }

    /**
     * Send SMS TEXT
     * 
     * @param destinationNumber list of destination numbers
     * @param smsMessage the text of the message
     * @return the url containing the id of the sent SMS, necessary to retrieve the delivery status
     * @throws JAXBException
     * @throws BlueviaException
     */
    public String send(String[] destinationNumber, String smsMessage) throws JAXBException, BlueviaException {
    	return send(destinationNumber, smsMessage, null, null);
    }
    
    /**
     * Send SMS TEXT to be received in an endpoint
     * 
     * @param destinationNumber list of destination numbers
     * @param smsMessage the text of the message
     * @param endPoint
     * @param correlator
     * @return
     * @throws JAXBException
     * @throws BlueviaException
     */
    public String send(String[] destinationNumber, String smsMessage, String endPoint, String correlator) throws JAXBException, BlueviaException {

    	if (destinationNumber == null || destinationNumber.length == 0)
    		throw new IllegalArgumentException("Invalid parameter: destination number");
    	
    	if (smsMessage == null || smsMessage.trim().length() == 0)
    		throw new IllegalArgumentException("Invalid parameter: sms message");
    	
        SMSTextType message = new SMSTextType();

        // Message Text
        message.setMessage(smsMessage);

        // Origin Number is Alias = Access Token
        UserIdType user2 = new UserIdType();
        user2.setAlias(this.restConnector.getAccessToken());

        message.setOriginAddress(user2);

        // Destination Number
        for (String number : destinationNumber) {
            UserIdType user1 = new UserIdType();
            user1.setPhoneNumber(number);
            message.getAddress().add(user1);
        }
        
        //Notification
        if (correlator != null && endPoint != null) {
	        SimpleReferenceType reference=new SimpleReferenceType();
	        reference.setCorrelator(correlator);
	        reference.setEndpoint(endPoint);
	        message.setReceiptRequest(reference);
        }
        
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectFactory of = new ObjectFactory();
        JAXBElement<SMSTextType> sms = of.createSmsText(message);

        this.m.marshal(sms, os);

        String res = generic_send(os.toByteArray());

        return res;

    }

    private String generic_send(byte[] data) throws JAXBException, BlueviaException {

    	String url = this.uri + MESSAGE_MT_PATH + "?version=v1";
        String res = this.restConnector.post(url, data);
        
        String regex = uri + MESSAGE_MT_PATH + "/(.*)" + MESSAGE_DELIVERY_STATUS +"/?$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(res);
        if (matcher.matches())
       		return matcher.group(1);
        else return res;
    }
}
