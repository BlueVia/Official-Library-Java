/**
 * \package com.bluevia.messagery.mo.mms This package contains the classes in order to receive MMS using Bluevia API.
 * \package com.bluevia.messagery.mo.mms.client This package contains REST client to receive MMS using Bluevia API.
 */
package com.bluevia.messagery.mo.mms.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.log4j.Logger;

import com.bluevia.commons.Encoding;
import com.bluevia.commons.Entity;
import com.bluevia.commons.GsdpConstants;
import com.bluevia.commons.Utils;
import com.bluevia.commons.connector.GenericResponse;
import com.bluevia.commons.data.JaxbEntity;
import com.bluevia.commons.data.UserId;
import com.bluevia.commons.data.UserId.Type;
import com.bluevia.commons.exception.BlueviaException;
import com.bluevia.commons.exception.ConnectorException;
import com.bluevia.commons.parser.ParseException;
import com.bluevia.commons.parser.xml.XmlConstants;
import com.bluevia.commons.parser.xml.XmlParser;
import com.bluevia.messagery.mo.client.BVMoClient;
import com.bluevia.messagery.mo.data.ReceivedMessageList;
import com.bluevia.messagery.mo.mms.data.MimeContent;
import com.bluevia.messagery.mo.mms.data.MmsMessage;
import com.bluevia.messagery.mo.mms.data.MmsMessageInfo;
import com.bluevia.messagery.mo.mms.parser.MultipartMmsParser;
import com.telefonica.schemas.unica.rest.common.v1.SimpleReferenceType;
import com.telefonica.schemas.unica.rest.common.v1.UserIdType;
import com.telefonica.schemas.unica.rest.mms.v1.MessageNotificationType;
import com.telefonica.schemas.unica.rest.mms.v1.ReceivedMessagesType;

/**
 * 
 * Abstract client interface for the REST binding of the Bluevia MMS MO Service.
 * 
 * @author Telefonica R&D
 *
 */
public abstract class BVMoMmsClient extends BVMoClient {

	private static Logger log = Logger.getLogger(BVMoMmsClient.class.getName());

	private static final String ATTACHMENTS_FEED_PATH = "/attachments";

	/**
	 * Initializer of common attributes
	 */
	protected void init(){
		mEncoding = Encoding.APPLICATION_XML;
		mParser = new XmlParser();
		mSerializer = null;	//No serializer
	}

	/**
	 * 
	 * Retrieves the list of received messages without attachment ids.
	 * 
	 * @see getAllMessages(String registrationId, boolean attachUrl)
	 * 
	 * Note: the origin address of the received MMS will contain an alias, not a phone number.
	 * 
	 * @param registrationId the registration id (short number) that receives the messages
	 * @return the list of messages.
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public ArrayList<MmsMessageInfo> getAllMessages(String registrationId) throws BlueviaException, IOException{
		return getAllMessages(registrationId, false);
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
	 * @param attachUrl the boolean parameter to retrieve the IDs of the attachments or not
	 * @return the list of Received MMSs (list will be empty if the are no messages)
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public ArrayList<MmsMessageInfo> getAllMessages(String registrationId, boolean attachUrl) throws BlueviaException, IOException{
		ArrayList<MmsMessageInfo> receivedMmsList = null;

		// Check params
		checkRegistrationId(registrationId);

		// Build feed uri for the request
		String feedUri = "/" + registrationId + RECEIVED_MESSAGES;


		HashMap<String, String> parameters = new HashMap<String, String>();
		if (attachUrl)
			parameters.put(XmlConstants.PARAM_USE_ATTACHMENT_URLS, "true");
		parameters.put(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);    		

		((XmlParser) mParser).setParseClass(ReceivedMessagesType.class);
		Entity response = retrieve(feedUri, parameters);

		receivedMmsList = getReceivedMmsListFromResponse(response);

		return receivedMmsList;
	}

	/**
	 * Gets the received MMS list from the response Entity
	 * 
	 * @param response response Entity that contains the result
	 * @return the received MMS list
	 * @throws BlueviaException if the response or the list are not valid.
	 */
	private ArrayList<MmsMessageInfo> getReceivedMmsListFromResponse(Entity response) throws BlueviaException{
		ArrayList<MmsMessageInfo> receivedMmsList = null;

		//If there are no messages, return empty list
		if (response == null)
			return new ArrayList<MmsMessageInfo>();

		//Check if response is instance of JaxbEntity
		else if (! (response instanceof JaxbEntity))
			throw new BlueviaException("Error during request. Response received does not correspond to an ReceivedMmsList",
					BlueviaException.INTERNAL_CLIENT_ERROR);
		JaxbEntity parseEntity= (JaxbEntity) response;

		//Check if parseEntity is instance of ReceivedMessagesType
		ReceivedMessagesType receivedMessagesType= new ReceivedMessagesType();
		if ((parseEntity.getObject() == null) || (! (parseEntity.getObject() instanceof ReceivedMessagesType)))
			throw new BlueviaException("Error during request. Response received does not correspond to an MessageReferenceType",
					BlueviaException.INTERNAL_CLIENT_ERROR);
		receivedMessagesType = (ReceivedMessagesType) parseEntity.getObject();
 		        
		log.debug("Creating response ReceivedMessageList");
		Entity retEntity = new ReceivedMessageList(receivedMessagesType);
		
		//Check if response is instance of DeliveryInfo
		if ((retEntity == null) || (! (retEntity instanceof ReceivedMessageList)))
			throw new BlueviaException("Error during request. Response received does not correspond to an ReceivedMessageList",
					BlueviaException.INTERNAL_CLIENT_ERROR);

		//Get received data
		receivedMmsList = ((ReceivedMessageList) retEntity).getList();

		// Check list
		if (receivedMmsList == null)
			throw new BlueviaException("ReceivedMessage list is null", BlueviaException.INTERNAL_CLIENT_ERROR);
	
		// Check list
		if (receivedMmsList == null)
			throw new BlueviaException(ReceivedMessageList.class.getName() + " object is null", BlueviaException.INTERNAL_CLIENT_ERROR);

		return receivedMmsList;
	}


	/**
	 * Gets the content of a message with a 'messageId' sent to the 'registrationId'
	 * 
	 * @param registrationId the registration id (short number) that receives the messages
	 * @param messageId the message id (obtained in getAllMessages function)
	 * @return the MmsMessage  the complete MmsMessage (including attachments)
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public MmsMessage getMessage(String registrationId, String messageId) throws BlueviaException, IOException{

		// Check params
		checkRegistrationId(registrationId);

		if (Utils.isEmpty(messageId))
			throw new BlueviaException("Bad request: Message identifier is either null or empty", BlueviaException.BAD_REQUEST_EXCEPTION);

		// Build feed uri for the request
		String feedUri = "/" + registrationId + RECEIVED_MESSAGES +
				"/" + messageId;

		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);    		


		((XmlParser) mParser).setParseClass(ReceivedMessagesType.class);
		Entity response = retrieve(feedUri, parameters);

		if (response == null || !(response instanceof MmsMessage))
			throw new BlueviaException("Error during request. Response received does not correspond to an " + 
					MmsMessage.class.getName(), BlueviaException.INTERNAL_CLIENT_ERROR);

		return (MmsMessage) response;
	}

	/**
	 * 
	 * Gets the attachment with the specified id of the received message.
	 * 
	 * @param registrationId the registration id (short number) that receives the messages
	 * @param messageId the message id (obtained in getAllMessages function)
	 * @param attachmentId the attachment id (obtained in getAllMessages function)
	 * @return the attachment of the received MMS.
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public MimeContent getAttachment(String registrationId, String messageId, String attachmentId) throws BlueviaException, IOException {

		// Check params
		checkRegistrationId(registrationId);

		if (Utils.isEmpty(messageId))
			throw new BlueviaException("Bad request: Message identifier is either null or empty", BlueviaException.BAD_REQUEST_EXCEPTION);

		if (Utils.isEmpty(attachmentId))
			throw new BlueviaException("Bad request: Attachment identifier is either null or empty", BlueviaException.BAD_REQUEST_EXCEPTION);

		// Build feed uri for the request
		String feedUri = "/" + registrationId + 
				RECEIVED_MESSAGES + "/" + messageId +
				ATTACHMENTS_FEED_PATH + "/" + attachmentId;


		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);   

		((XmlParser) mParser).setParseClass(ReceivedMessagesType.class);
		Entity response = this.retrieve(feedUri, parameters);

		if (response == null || !(response instanceof MimeContent))
			throw new BlueviaException("Error during request. Response received does not correspond to a "
					+ MimeContent.class.getName(), BlueviaException.INTERNAL_CLIENT_ERROR);

		return (MimeContent) response;

	}

	@Override
	protected Entity retrieve(String feedUri, HashMap<String, String> parameters)
			throws IOException, BlueviaException {

		if (feedUri.endsWith(RECEIVED_MESSAGES)){

			return super.retrieve(feedUri, parameters);

		} else {

			String uri = mBaseUri + feedUri;

			GenericResponse response = mConnector.retrieve(uri, parameters);

			InputStream is = response.getAdditionalData().getBody();
			HashMap<String, String> responseHeaders = response.getAdditionalData().getHeaders();

			try {

				String contentType = responseHeaders.get("Content-Type");
				String contentDisp = responseHeaders.get("Content-Disposition");

				MultipartMmsParser parser = new MultipartMmsParser(mParser);

				if (feedUri.contains(ATTACHMENTS_FEED_PATH)){

					if (contentType.contains("xml") || contentType.contains("smil") || contentType.contains("text"))
						return (Entity) parser.buildMimeContent(contentType, contentDisp, Utils.convertStreamToString(is), true);
					else return (Entity) parser.buildMimeContent(contentType, contentDisp, is, true);

				} else {
					ByteArrayDataSource ds = new ByteArrayDataSource(is, contentType);
					MimeMultipart multipart = new MimeMultipart(ds);
					return (Entity) parser.parseMultipart(multipart);
				}

			} catch (MessagingException e) {
				throw new ParseException("Error parsing multipart: " + e.getLocalizedMessage(), e);
			} finally {
				if (is != null)
					is.close();
			}
		}
	}


	/**
	 * Subscribe notifications
	 *
	 * @param phoneNumber the phoneNumber
	 * @param endpoint the uri to receive notifications
	 * @param criteria field with keyword with criteria notifications
	 * @return the correlator from the response.
	 * @throws BlueviaException if the response or the result are not valid.
	 * @throws IOException 
	 */
	public String startNotification (String phoneNumber, String endpoint, String criteria) throws IOException, BlueviaException{
		return startNotification(phoneNumber, endpoint, criteria, null);
	}

	/**
	 * Subscribe notifications
	 *
	 * @param phoneNumber the phoneNumber
	 * @param endpoint the uri to receive notifications
	 * @param criteria field with keyword with criteria notifications
	 * @param correlator field with correlator to identify subscriptions
	 * @return the correlator from the response.
	 * @throws BlueviaException if the response or the result are not valid.
	 * @throws IOException 
	 */
	public String startNotification (String phoneNumber, String endpoint, String criteria, String correlator) throws IOException, BlueviaException{

		MessageNotificationType notification= new MessageNotificationType();
       
        if (Utils.isEmpty(correlator)) {
        	correlator= generateCorrelator(criteria);
        }        
        
        //Endpoint and correlator
        SimpleReferenceType ref = new SimpleReferenceType();
        ref.setEndpoint(endpoint);

        ref.setCorrelator(correlator);
        notification.setReference(ref);
        
        //Addresses
        UserId userId= new UserId(Type.PHONE_NUMBER, phoneNumber);
        List<UserIdType> addresses = new ArrayList<UserIdType>();
        addresses.add(userId.toUserIdType());
        notification.setDestinationAddress(addresses);
        
        //Criteria
        notification.setCriteria(criteria);
        
		return startNotification(notification);
	}

	/**
	 * Subscribe notifications
	 *
	 * @param notification the messageNotificationType with all data to subscribe
	 * @return the correlator from the response.
	 * @throws BlueviaException if the response or the result are not valid.
	 * @throws IOException 
	 */
	protected String startNotification (MessageNotificationType notification) throws IOException, BlueviaException{

		String result = null;
		
		GenericResponse response = null;
		InputStream is = null;

		try {

			// Build feed uri for the request
			String feedUri = SUBSCRIPTION_PATH;
					
			HashMap<String, String> params = new HashMap<String, String>();
			params.put(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);
	
			JaxbEntity messageEntity= new JaxbEntity();
			messageEntity.setJcInstance(XmlConstants.XSD_MMS_API_INSTANCE);
			messageEntity.setNamespace(XmlConstants.NS_MMS_API_URI);
			messageEntity.setQname(XmlConstants.XSD_MMSNOTIFICATION_MMSMESSAGE);
			messageEntity.setObject(notification);
	        
			byte[] bytes = serializeEntity(messageEntity);
	
			String uri = mBaseUri + feedUri;
			HashMap<String, String> headers = new HashMap<String, String>();
			headers.put(GsdpConstants.HEADER_CONTENT_TYPE, getEncoding(mEncoding));
	
			response = mConnector.create(uri, params, bytes, headers);
	
			is = response.getAdditionalData().getBody();
	
			result = getMesageIdFromResponse(response.getAdditionalData().getHeaders(), uri);

		} catch (ConnectorException e){
			log.error("Error during request. Connector Exception: " + e.getMessage());
			throw new BlueviaException("Error during request: " + e.getMessage(),
					BlueviaException.INTERNAL_CLIENT_ERROR);
		} catch (IOException e) {
			log.error("Error during request. IO Exception: " + e.getMessage());
			throw new IOException("Error during request: " + e.getMessage());
		} finally {
			//Close the stream
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					log.info(e.getMessage(), e);
				}      	
		}

		return result;

	}

	/**
	 * Unsubscribe notifications
	 *
	 * @param correlator the correlator to unsubscribe notification
	 * @return boolean with result of operation
	 * @throws BlueviaException if the response or the result are not valid.
	 * @throws IOException 
	 */
	public boolean stopNotification (String correlator) throws BlueviaException, IOException {

		boolean result = false;
		
    	if (Utils.isEmpty(correlator))
			throw new BlueviaException("Invalid parameter: correlator can't be empty",
					BlueviaException.INTERNAL_CLIENT_ERROR);

		GenericResponse response = null;
		InputStream is = null;

		try {

			// Build feed uri for the request
			String feedUri = SUBSCRIPTION_PATH + "/" + correlator;
					
			HashMap<String, String> params = new HashMap<String, String>();
			params.put(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);
		
			String uri = mBaseUri + feedUri;
			HashMap<String, String> headers = new HashMap<String, String>();
			headers.put(GsdpConstants.HEADER_CONTENT_TYPE, getEncoding(mEncoding));
	
			response = mConnector.delete(uri, params, headers);
			
			log.debug("[stopNotification] response status :" + response.getStatus());
			log.debug("[stopNotification] response message:" + response.getMessage());

			if (response.getStatus()== 204) {
				result= true;
			}

		} catch (ConnectorException e){
			log.error("Error during request. Connector Exception: " + e.getMessage());
			throw new BlueviaException("Error during request: " + e.getMessage(),
					BlueviaException.INTERNAL_CLIENT_ERROR);
		} catch (IOException e) {
			log.error("Error during request. IO Exception: " + e.getMessage());
			throw new IOException("Error during request: " + e.getMessage());
		} finally {
			//Close the stream
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					log.info(e.getMessage(), e);
				}      	
		}

		return result;
	}
	
}
