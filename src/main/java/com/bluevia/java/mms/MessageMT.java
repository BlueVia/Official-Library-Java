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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;

import com.bluevia.java.Utils;
import com.bluevia.java.exception.BlueviaException;
import com.bluevia.java.mms.data.Attachment;
import com.bluevia.java.oauth.OAuthToken;
import com.telefonica.schemas.unica.rest.common.v1.SimpleReferenceType;
import com.telefonica.schemas.unica.rest.common.v1.UserIdType;
import com.telefonica.schemas.unica.rest.mms.v1.MessageDeliveryStatusType;
import com.telefonica.schemas.unica.rest.mms.v1.MessageType;
import com.telefonica.schemas.unica.rest.mms.v1.ObjectFactory;

public class MessageMT extends MMSClient {
	
	private static final String MESSAGE_MT_PATH = "/outbound/requests";
	private static final String MESSAGE_DELIVERY_STATUS = "/deliverystatus";

	/**
	 * 
	 * @param consumer
	 * @param token
	 * @param mode
	 * @throws JAXBException
	 */
    public MessageMT(OAuthToken consumer, OAuthToken token, Mode mode) throws JAXBException {
        super(consumer, token, mode);
        
        if (!Utils.validateToken(token))
    		throw new IllegalArgumentException("Invalid parameter: oauth token");
    }

    /**
     * Retrieves the delivery status of a previously sent MMS.
     * 
     * @param id the MMS id
     * @return the message delivery status
     * @throws JAXBException
     * @throws BlueviaException
     */
    public MessageDeliveryStatusType getStatus(String id) throws JAXBException, BlueviaException {

    	if (Utils.isEmpty(id))
    		throw new IllegalArgumentException("Invalid parameter: id");
    	
        String url = this.uri + MESSAGE_MT_PATH + "/" + id + MESSAGE_DELIVERY_STATUS + "?version=v1";

        String res = this.restConnector.get(url);

        JAXBElement<MessageDeliveryStatusType> e = this.u.unmarshal(new StreamSource(new StringReader(res)), MessageDeliveryStatusType.class);
        return e.getValue();
    }
    
    

    /**
     * Sends a MMS
     * 
     * @param destinationNumber list of destination numbers
     * @param attachments list of Attachments
     * @param subject subject of the MMS
     * @return the url containing the id of the sent MMS, necessary to retrieve the delivery status
     * @throws JAXBException
     * @throws IOException
     * @throws BlueviaException
     */
    public String send(String[] destinationNumber, Attachment[] attachments, String subject) throws JAXBException, IOException, BlueviaException {
        return send(destinationNumber, attachments, subject, null, null);
    }

    public String send(String[] destinationNumber, Attachment[] attachments, String subject, String endPoint, String correlator)
            throws JAXBException, IOException, BlueviaException {
    	
    	if (destinationNumber == null || destinationNumber.length == 0)
    		throw new IllegalArgumentException("Invalid parameter: destination number");
    	
        MessageType message = new MessageType();

        // Destination Number
        if (destinationNumber != null && destinationNumber.length > 0) {
            for (String number : destinationNumber) {
            	
            	if (Utils.isEmpty(number))
            		throw new IllegalArgumentException("Invalid parameter: destination number");
            	
                UserIdType user1 = new UserIdType();
                user1.setPhoneNumber(number);
                message.getAddress().add(user1);
            }
        }

        // Origin Address - Use Only Alias
        UserIdType user3 = new UserIdType();
        user3.setAlias(this.restConnector.getAccessToken());
        message.setOriginAddress(user3);

        // Subject
        if (!Utils.isEmpty(subject)) {
            message.setSubject(subject);
        }

        // Notification Info
        if (correlator != null && endPoint != null) {
	        SimpleReferenceType reference = new SimpleReferenceType();
	        reference.setCorrelator(correlator);
	        reference.setEndpoint(endPoint);
	        message.setReceiptRequest(reference);
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(os);

        String boundary = "asdfa487";
        dos.writeBytes("--" + boundary + "\r\n");
        dos.writeBytes("Content-Disposition: form-data; name=\"root-fields\"\r\n");
        dos.writeBytes("Content-Type:application/xml; charset=UTF-8\r\n");
        dos.writeBytes("Content-Transfer-Encoding: binary\r\n");
        dos.writeBytes("\r\n");

        //Write message
        ObjectFactory of = new ObjectFactory();
        JAXBElement<MessageType> mms = of.createMessage(message);
        ByteArrayOutputStream mmsOs = new ByteArrayOutputStream();
        this.m.marshal(mms, mmsOs);
        dos.write(mmsOs.toByteArray());
        
        dos.writeBytes("\r\n");
        dos.writeBytes("\r\n");

        if (attachments != null && attachments.length > 0) {
            dos.writeBytes("--" + boundary);
            dos.writeBytes("\r\n");

            String boundaryFiles = Utils.generateBoundary();
            dos.writeBytes("Content-Disposition: form-data; name=\"attachments\"");
            dos.writeBytes("\r\n");
            dos.writeBytes("Content-Type: multipart/mixed; boundary=" + boundaryFiles);
            dos.writeBytes("\r\n");
            dos.writeBytes("\r\n");

            for (Attachment attachment : attachments) {
            	
            	if (attachment == null || attachment.getContentType() == null ||
            			(Utils.isEmpty(attachment.getFilePath())))
            		throw new IllegalArgumentException("Invalid parameter: attachments");
            	
                File f = new File(attachment.getFilePath());
                String contentType = attachment.getStringContentType();
                Utils.insertAttach(dos, f, contentType, boundaryFiles);
            }
            dos.writeBytes("\r\n");
            dos.writeBytes("--" + boundaryFiles + "--");
            dos.writeBytes("\r\n");
            dos.writeBytes("--" + boundary + "--");
        } else {

            dos.writeBytes("--" + boundary + "--");
        }
        String res = generic_send(os.toByteArray());
        return res;
    }

    public String generic_send(byte[] data) throws BlueviaException, JAXBException {

        String url = this.uri + MESSAGE_MT_PATH + "?version=v1";

        String res = this.restConnector.postMMS(url, data);
        
        String regex = uri + MESSAGE_MT_PATH + "/(.*)" + MESSAGE_DELIVERY_STATUS +"/?$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(res);
        if (matcher.matches())
       		return matcher.group(1);
        else return res;
    }
}
