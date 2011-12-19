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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.telefonica.schemas.unica.rest.mms.v1.MessageType;
import com.telefonica.schemas.unica.rest.mms.v1.ReceivedMessagesType;

public class MessageMO extends MMSClient {

	private static final String MESSAGE_MO_PATH = "/inbound";
	
	/**
	 * 
	 * @param consumer
	 * @param token
	 * @param mode
	 * @throws JAXBException
	 */
    public MessageMO(OAuthToken consumer, OAuthToken token, Mode mode) throws JAXBException {
        super(consumer, token, mode);
    }

    /**
     * Retrieves the list of received messages
     * Note: the origin address of the received MMS will contain an alias, not a phone number.
     * 
     * @param registrationId the registration id (short number) that receives the messages
     * @return a ReceivedMessagesType containing the list of messages.
     * @throws JAXBException
     * @throws BlueviaException
     */
    public ReceivedMessagesType getMessages(String registrationId) throws JAXBException, BlueviaException {

    	if (Utils.isEmpty(registrationId))
    		throw new IllegalArgumentException("Invalid parameter: registrationId");
    	
        String url = this.uri + MESSAGE_MO_PATH + "/" + registrationId + "/messages?version=v1";

        String res = this.restConnector.get(url);

        if (Utils.isEmpty(res))
        	return null;
        
        JAXBElement<ReceivedMessagesType> e = this.u.unmarshal(new StreamSource(new StringReader(res)), ReceivedMessagesType.class);
        return e.getValue();
    }

    public ReceivedMMS getMessage(String registrationId, String messageId) throws JAXBException, BlueviaException {

    	if (Utils.isEmpty(registrationId))
    		throw new IllegalArgumentException("Invalid parameter: registrationId");

    	if (Utils.isEmpty(messageId))
    		throw new IllegalArgumentException("Invalid parameter: messageId");
    	
        String url = this.uri + MESSAGE_MO_PATH + "/" + registrationId + "/messages/" + messageId + "?version=v1";

        MimeMultipart mime = restConnector.getMms(url, "GET");
        
        return parseMultipart(mime);
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
        						attachList.add(parseBodyPart(part));
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

    private MimeContent parseBodyPart(BodyPart part) throws MessagingException, IOException{
    	MimeContent content = new MimeContent();

    	String contentType = part.getContentType();
    	
    	//Content-Type
    	Pattern p = Pattern.compile("(.*);(.*)");
    	Matcher m = p.matcher(contentType);
    	if (m.matches()){
    		content.setContentType(m.group(1));
    	} else content.setContentType(contentType);
    	
    	//Content-Transfer-Encoding
    	String[] cte = part.getHeader("Content-Transfer-Encoding");
    	if (cte != null && cte.length > 0)
    		content.setContentEncoding(cte[0]);
    	
    	//Filename
    	Pattern pattern = Pattern.compile("(.*)ame=(.*)");
    	Matcher matcher = pattern.matcher(contentType);
    	if (matcher.matches()){
    		content.setFileName(matcher.group(2));
    	}
    	
    	//Content
    	if (part.getContent() instanceof String){
    		content.setContent(part.getContent());
    	} else if (part.getContent() instanceof InputStream){
    		
    		InputStream is = (InputStream) part.getContent();
    		ByteArrayOutputStream os = new ByteArrayOutputStream();
    		byte[] buf = new byte[1024];
    		int read = 0;
    		while ((read = is.read(buf)) != -1) {
    			os.write(buf, 0, read);
    		}
    		content.setContent(os.toByteArray());
    		is.close();
    	} 
    	return content;
    }
}
