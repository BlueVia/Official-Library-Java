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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;

import com.bluevia.java.Utils;
import com.bluevia.java.exception.BlueviaException;
import com.bluevia.java.mms.data.MimeContent;
import com.bluevia.java.mms.data.ReceivedMMS;
import com.bluevia.java.oauth.OAuthToken;
import com.telefonica.schemas.unica.rest.mms.v1.MessageReferenceType;
import com.telefonica.schemas.unica.rest.mms.v1.MessageType;
import com.telefonica.schemas.unica.rest.mms.v1.ReceivedMessagesType;

public class MessageMO extends MMSClient {

	private static final String MESSAGE_MO_PATH = "/inbound";
	
	/**
	 * 
	 * @param consumer
	 * @param mode
	 * @throws JAXBException
	 */
    public MessageMO(OAuthToken consumer, Mode mode) throws JAXBException {
        super(consumer, mode);
    }

    /**
     * Retrieves the list of received messages without attachment ids.
     * 
     * @see getMessages(String registrationId, boolean useAttachmentsIds)
     * 
     * Note: the origin address of the received MMS will contain an alias, not a phone number.
     * 
     * @param registrationId the registration id (short number) that receives the messages
     * @return the list of messages.
     * @throws JAXBException
     * @throws BlueviaException
     */
    public List<MessageReferenceType> getMessages(String registrationId) throws JAXBException, BlueviaException {
    	return getMessages(registrationId, false);
    }

    /**
     * Retrieves the list of received messages. Depending on the value of the useAttachmentsIds parameter, the response will
     * include the IDs of the attachments or not. 
     * If the ids are retrieved, the function 'getAttachment' can be used; otherwise, the attachments must be obtained throught the getMessage function.
     * 
     * @see getMessage(String registrationId, String messageId)
     * @see getAttachment(String registrationId, String messageId, String attachmentId)
     * 
     * Note: the origin address of the received MMS will contain an alias, not a phone number.
     * 
     * @param registrationId the registration id (short number) that receives the messages
     * @param useAttachmentIds the boolean parameter to retrieve the IDs of the attachments or not
     * @return the list of messages.
     * @throws JAXBException
     * @throws BlueviaException
     */
    public List<MessageReferenceType> getMessages(String registrationId, boolean useAttachmentIds) throws JAXBException, BlueviaException {

    	if (Utils.isEmpty(registrationId))
    		throw new IllegalArgumentException("Invalid parameter: registrationId");
    	
        String url = this.uri + MESSAGE_MO_PATH + "/" + registrationId + "/messages?";
        
        if (useAttachmentIds)
        	url += "useAttachmentURLs=true&";
         
        url += "version=v1";

        String res = this.restConnector.get(url);

        if (Utils.isEmpty(res))
        	return null;
        
        JAXBElement<ReceivedMessagesType> e = this.u.unmarshal(new StreamSource(new StringReader(res)), ReceivedMessagesType.class);
        
        if (e.getValue() != null)
        	return e.getValue().getReceivedMessages();
        else return null;
    }

    /**
     * Gets a MMS as a ReceivedMMS object, which includes the metadata of the MMS and the list of the attachments.
     * 
     * @param registrationId the registration id (short number) that receives the messages
     * @param messageId the message id (obtained in getMessages function)
     * 
     * @see getMessages(String registrationId)
     * @see getMessages(String registrationId, boolean useAttachmentsIds)
     * 
     * @return the MMS with id 'messageId' received in the 'registrationId' inbox
     * @throws JAXBException
     * @throws BlueviaException
     */
    public ReceivedMMS getMessage(String registrationId, String messageId) throws JAXBException, BlueviaException {

    	if (Utils.isEmpty(registrationId))
    		throw new IllegalArgumentException("Invalid parameter: registrationId");

    	if (Utils.isEmpty(messageId))
    		throw new IllegalArgumentException("Invalid parameter: messageId");
    	
        String url = this.uri + MESSAGE_MO_PATH + "/" + registrationId + "/messages/" + messageId + "?version=v1";

        MimeMultipart mime = restConnector.getMms(url);
        
        return parseMultipart(mime);
    }
    
    /**
     * 
     * Gets the attachment with the specified id of the received message.
     * 
     * @param registrationId the registration id (short number) that receives the messages
     * @param messageId the message id (obtained in getMessages function)
     * @param attachmentId the attachment id (obtained in getMessages function)
     * @return the attachment of the received MMS.
     * @throws JAXBException
     * @throws BlueviaException
     */
    public MimeContent getAttachment(String registrationId, String messageId, String attachmentId) throws JAXBException, BlueviaException {
    	
    	if (Utils.isEmpty(registrationId))
    		throw new IllegalArgumentException("Invalid parameter: registrationId");

    	if (Utils.isEmpty(messageId))
    		throw new IllegalArgumentException("Invalid parameter: messageId");
    	
    	if (Utils.isEmpty(attachmentId))
    		throw new IllegalArgumentException("Invalid parameter: attachment Id");
    	
    	String url = this.uri + MESSAGE_MO_PATH + "/" + registrationId + "/messages/" +
    			messageId + "/attachments/" + attachmentId + "?version=v1";
    	
    	return restConnector.getAttachment(url);
    }
    
    private ReceivedMMS parseMultipart(MimeMultipart multipart) throws BlueviaException{
    	ReceivedMMS result = null;
    	
    	try {
    		
    		if (multipart == null)
    			throw new BlueviaException("Error parsing multipart: null mimemultipart");
        	
        	if (multipart.getCount() < 2)	//At least 2: "root-fields" & "multiparts"
    			throw new BlueviaException("Error parsing multipart: no body parts");
        	
        	result = new ReceivedMMS();
        	
        	String[] headers = null;
        	
        	//Parse root-fields
        	BodyPart rootFieldsPart = multipart.getBodyPart(0);
        	headers = rootFieldsPart.getHeader("Content-Disposition");
        	if (headers != null && headers.length > 0 && headers[0] != null && headers[0].contains("root-fields")){
        		if (rootFieldsPart.getContent() instanceof ByteArrayInputStream){
        			ByteArrayInputStream is = (ByteArrayInputStream) rootFieldsPart.getContent();
        			String res = Utils.convertStreamToString(is);
        			JAXBElement<MessageType> e = this.u.unmarshal(new StreamSource(new StringReader(res)), MessageType.class);
        			result.setMessage(e.getValue());
        			is.close();
        		}
        	}
        	
        	BodyPart attachmentsPart = multipart.getBodyPart(1);
        	headers = attachmentsPart.getHeader("Content-Disposition");
        	if (headers != null && headers.length > 0 && headers[0] != null && headers[0].contains("attachments")){
        		
        		ArrayList<MimeContent> attachList = new ArrayList<MimeContent>();
        		
        		headers = attachmentsPart.getHeader("Content-Type");
        		if (headers != null && headers.length > 0 && headers[0] != null){
        			
        			if (headers[0].contains("multipart")){
        				//Multipart/mixed or related
        				MimeMultipart attachments = null;
        				if (attachmentsPart.getContent() instanceof MimeMultipart){
        					attachments = (MimeMultipart) attachmentsPart.getContent();
        				} else if (attachmentsPart.getContent() instanceof InputStream){
        					ByteArrayDataSource ds = new ByteArrayDataSource((InputStream) attachmentsPart.getContent(), 
        							headers[0]);
        					attachments = new MimeMultipart(ds);
        				}
        				if (attachments != null){
            				for (int i=0; i<attachments.getCount(); i++){
        						BodyPart part = attachments.getBodyPart(i);
        						
        						String contentType = part.getContentType();
        				    	
        						String contentDisp = null;
        						String[] cte = part.getHeader("Content-Transfer-Encoding");
        				    	if (cte != null && cte.length > 0)
        				    		contentDisp = cte[0];
        						Object content = part.getContent();
        						
        						MimeContent mime = Utils.buildMimeContent(contentType, contentDisp, content, false);
        						
        						attachList.add(mime);
        					}
        				}
        			}
        		}
        		
        		result.setAttachments(attachList);
        	}

    	} catch (MessagingException e){
    		Logger.getLogger(MessagingException.class.getName()).log(Level.SEVERE, null, e);
			throw new BlueviaException("Error parsing multipart: " + e.getLocalizedMessage());
    	} catch (IOException e) {
    		Logger.getLogger(IOException.class.getName()).log(Level.SEVERE, null, e);
			throw new BlueviaException("Error parsing multipart: " + e.getLocalizedMessage());
		} catch (JAXBException e) {
			Logger.getLogger(JAXBException.class.getName()).log(Level.SEVERE, null, e);
			throw new BlueviaException("Error parsing multipart: " + e.getLocalizedMessage());
		}
    	
    	return result;
    }

}
