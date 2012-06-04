package com.bluevia.messagery.mo.mms.parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import com.bluevia.commons.data.JaxbEntity;
import com.bluevia.commons.parser.IParser;
import com.bluevia.commons.parser.ParseException;
import com.bluevia.commons.parser.xml.XmlParser;
import com.bluevia.messagery.mo.mms.data.MimeContent;
import com.bluevia.messagery.mo.mms.data.MmsMessage;
import com.bluevia.messagery.mo.mms.data.MmsMessageInfo;
import com.telefonica.schemas.unica.rest.mms.v1.MessageType;

/**
 * Multipart MMS parser
 *
 */
public class MultipartMmsParser {
	
	private IParser mParser;
	
	public MultipartMmsParser(IParser parser){
		mParser = parser;
	}

	public MmsMessage parseMultipart(MimeMultipart multipart) throws ParseException {
		MmsMessage result = null;
    	
    	try {
    		
    		if (multipart == null)
    			return null;
        	
        	if (multipart.getCount() < 2)	//At least 2: "root-fields" & "multiparts"
        		return null;
        	
        	result = new MmsMessage();
        	
        	String[] headers = null;
        	
        	//Parse root-fields
        	BodyPart rootFieldsPart = multipart.getBodyPart(0);
        	headers = rootFieldsPart.getHeader("Content-Disposition");
        	if (headers != null && headers.length > 0 && headers[0] != null && headers[0].contains("root-fields")){
        		if (rootFieldsPart.getContent() instanceof ByteArrayInputStream){
        			ByteArrayInputStream is = (ByteArrayInputStream) rootFieldsPart.getContent();

        			((XmlParser) mParser).setParseClass(MessageType.class);
        			JaxbEntity entity = (JaxbEntity) mParser.parse(is);
        			is.close();

        			if (entity instanceof JaxbEntity){
        				MessageType messageType=(MessageType) entity.getObject();
        				MmsMessageInfo info= new MmsMessageInfo(messageType);
        				result.setMessageInfo(info);
        			} else throw new ParseException("Error parsing multipart: no message info");
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
        						
        						MimeContent mime = buildMimeContent(contentType, contentDisp, content, false);
        						
        						attachList.add(mime);
        					}
        				}
        			}
        		}
        		
        		result.setAttachments(attachList);
        	}

    	} catch (MessagingException e){
    		throw new ParseException("Error parsing multipart: " + e.getLocalizedMessage(), e);
    	} catch (IOException e) {
    		throw new ParseException("Error parsing multipart: " + e.getLocalizedMessage(), e);
    	} 
    	
    	return result;
    }

    public MimeContent buildMimeContent(String contentType, String contentDisp, Object content, boolean useAttachments) 
			throws IOException{

    	MimeContent mimeContent = new MimeContent();

    	//Content-Type
    	Pattern p = null;
    	if (useAttachments)
    		p = Pattern.compile("(.*);(.*);(.*)");
    	else p = Pattern.compile("(.*);(.*)");
    	Matcher m = p.matcher(contentType);
    	if (m.matches()){
    		mimeContent.setContentType(m.group(1));
    	} else mimeContent.setContentType(contentType);
    	
    	//Content-Transfer-Encoding
    	mimeContent.setEncoding(contentDisp);
    	
    	//Filename
    	Pattern pattern;
    	if (useAttachments)
    		pattern = Pattern.compile("(.*)ame=(.*);(.*)");
    	else pattern = Pattern.compile("(.*)ame=(.*)");
    	Matcher matcher = pattern.matcher(contentType);
    	if (matcher.matches()){
    		mimeContent.setName(matcher.group(2));
    	}
    	
    	//Content
    	if (content instanceof String){
    		mimeContent.setContent(content);
    	} else if (content instanceof InputStream){
    		
    		InputStream is = (InputStream) content;
    		ByteArrayOutputStream os = new ByteArrayOutputStream();
    		byte[] buf = new byte[1024];
    		int read = 0;
    		while ((read = is.read(buf)) != -1) {
    			os.write(buf, 0, read);
    		}
    		mimeContent.setContent(os.toByteArray());
    		is.close();
    	} 
    	return mimeContent;
    }
	
}
