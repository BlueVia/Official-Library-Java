/**
 * \package com.bluevia.messagery.mo.sms This package contains the classes in order to receive SMS using Bluevia API.
 * \package com.bluevia.messagery.mo.sms.client This package contains REST client to receive SMS using Bluevia API.
 */
package com.bluevia.messagery.mo.sms.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.bluevia.commons.parser.xml.XmlConstants;
import com.bluevia.commons.parser.xml.XmlParser;
import com.bluevia.commons.parser.xml.XmlSerializer;
import com.bluevia.messagery.mo.client.BVMoClient;
import com.bluevia.messagery.mo.data.ReceivedMessageList;
import com.bluevia.messagery.mo.sms.data.SmsMessage;
import com.telefonica.schemas.unica.rest.common.v1.SimpleReferenceType;
import com.telefonica.schemas.unica.rest.common.v1.UserIdType;
import com.telefonica.schemas.unica.rest.sms.v1.ReceivedSMSType;
import com.telefonica.schemas.unica.rest.sms.v1.SMSNotificationType;

/**
 * 
 * Abstract client interface for the REST binding of the Bluevia SMS MO Service.
 * 
 * @author Telefonica R&D
 *
 */
public abstract class BVMoSmsClient extends BVMoClient {

	private static Logger log = Logger.getLogger(BVMoSmsClient.class.getName());
	
	protected static final String FEED_SMS_BASE_URI = "/REST/SMS";

	/**
	 * Initializer of common attributes
	 */
	protected void init(){
		mEncoding = Encoding.APPLICATION_XML;
		mParser = new XmlParser();
		mSerializer= new XmlSerializer();
	}

	/**
	 * 
	 * Allow to request for the list of the received SMSs for the app provisioned and authorized
	 * 
	 * @param registrationId The Bluevia service number for your country, including the country code without the + symbol
	 * @return the list of the received messages
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public ArrayList<SmsMessage> getAllMessages(String registrationId) throws BlueviaException, IOException {
		ArrayList<SmsMessage> receivedSmsList = null;

		// Check params
		checkRegistrationId(registrationId);

		// Build feed uri for the request
		String feedUri = "/" + registrationId + RECEIVED_MESSAGES;

		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);    		

		((XmlParser) mParser).setParseClass(ReceivedSMSType.class);
		Entity response = retrieve(feedUri, parameters);

		receivedSmsList = getReceivedSmsListFromResponse(response);

		return receivedSmsList;
	}

	/**
	 * Gets the received SMS list from the response Entity
	 * 
	 * @param response response Entity that contains the result
	 * @return the received SMS list
	 * @throws BlueviaException if the response or the list are not valid.
	 */
	private ArrayList<SmsMessage> getReceivedSmsListFromResponse(Entity response) throws BlueviaException{
		ArrayList<SmsMessage> receivedSmsList = null;


		//If there are no messages, return empty list
		if (response == null)
			return new ArrayList<SmsMessage>();
		//Check if response is instance of JaxbEntity
		else if (! (response instanceof JaxbEntity))
			throw new BlueviaException("Error during request. Response received does not correspond to an ReceivedSmsList",
					BlueviaException.INTERNAL_CLIENT_ERROR);
		JaxbEntity parseEntity= (JaxbEntity) response;

		//Check if parseEntity is instance of ReceivedSMSType
		ReceivedSMSType receivedSMSType= new ReceivedSMSType();
		if ((parseEntity.getObject() == null) || (! (parseEntity.getObject() instanceof ReceivedSMSType)))
			throw new BlueviaException("Error during request. Response received does not correspond to an SMSMessageType",
					BlueviaException.INTERNAL_CLIENT_ERROR);
		receivedSMSType = (ReceivedSMSType) parseEntity.getObject();
 		        
		log.debug("Creating response ReceivedMessageList");
		Entity retEntity = new ReceivedMessageList(receivedSMSType);
		
		//Check if response is instance of DeliveryInfo
		if ((retEntity == null) || (! (retEntity instanceof ReceivedMessageList)))
			throw new BlueviaException("Error during request. Response received does not correspond to an ReceivedMessageList",
					BlueviaException.INTERNAL_CLIENT_ERROR);

		//Get received data
		receivedSmsList = ((ReceivedMessageList) retEntity).getList();

		// Check list
		if (receivedSmsList == null)
			throw new BlueviaException("ReceivedSMS list is null", BlueviaException.INTERNAL_CLIENT_ERROR);

		return receivedSmsList;

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

		SMSNotificationType notification= new SMSNotificationType();
       
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
	protected String startNotification (SMSNotificationType notification) throws IOException, BlueviaException{

		String result = null;
		
		GenericResponse response = null;
		InputStream is = null;

		try {

			// Build feed uri for the request
			String feedUri = SUBSCRIPTION_PATH;
					
			HashMap<String, String> params = new HashMap<String, String>();
			params.put(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);
	
			JaxbEntity messageEntity= new JaxbEntity();
			messageEntity.setJcInstance(XmlConstants.XSD_SMS_API_INSTANCE);
			messageEntity.setNamespace(XmlConstants.NS_SMS_API_URI);
			messageEntity.setQname(XmlConstants.XSD_SMSNOTIFICATION_SMSMESSAGE);
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
			throw new BlueviaException("Invalida parameter: correlator can't be empty",
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


	/**
	 * Gets the messageId from the response headers.
	 *
	 * @param responseHeaders headers containing the messageId.
	 * @param feed field of the uri to distinguish SMS and MMS
	 * @return the messageId from the response.
	 * @throws BlueviaException if the response or the result are not valid.
	 */
	protected static String getMesageIdFromResponse(HashMap<String, String> responseHeaders, String feed) throws BlueviaException{

		// In this case, when sending a Message, the response is not returned using an XML (is is null)
		// but using header location
		String messageId = responseHeaders.get(MESSAGEID_LOCATION_HEADER);
		if (!Utils.isEmpty(messageId)) {
			// Extract the SMSID from the delivery http URL using Java patterns
			// Location Url is like  "https://bluevia.com/gSDP/REST/{SMS|MMS}/outbound/requests/MessageId/deliverystatus"
			String regex = feed + "/(.*)";

			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(messageId);

			if (matcher.matches()) {

				log.debug("matcher.group(1): "+matcher.group(1));
				return matcher.group(1);

			} else throw new BlueviaException("Error during request. Location received does not correspond to the pattern of an MessageID URI" +
					"LocationUri: "+messageId+" regex: "+regex, BlueviaException.INTERNAL_CLIENT_ERROR);

		} else throw new BlueviaException("Location header is empty", BlueviaException.INTERNAL_CLIENT_ERROR);

	}


	
}
