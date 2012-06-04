/**
 * \package com.bluevia.messagery.mt.sms This package contains the classes in order to semd SMS using Bluevia API.
 * \package com.bluevia.messagery.mt.sms.client This package contains REST client to send SMS using Bluevia API.
 */
package com.bluevia.messagery.mt.sms.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.bluevia.commons.Encoding;
import com.bluevia.commons.Entity;
import com.bluevia.commons.GsdpConstants;
import com.bluevia.commons.Utils;
import com.bluevia.commons.connector.GenericResponse;
import com.bluevia.commons.data.JaxbEntity;
import com.bluevia.commons.data.UserId;
import com.bluevia.commons.exception.BlueviaException;
import com.bluevia.commons.exception.ConnectorException;
import com.bluevia.commons.parser.SerializeException;
import com.bluevia.commons.parser.xml.XmlConstants;
import com.bluevia.commons.parser.xml.XmlParser;
import com.bluevia.commons.parser.xml.XmlSerializer;
import com.bluevia.messagery.mt.client.BVMtClient;
import com.bluevia.messagery.mt.data.DeliveryInfo;
import com.bluevia.messagery.mt.data.DeliveryInfoList;
import com.telefonica.schemas.unica.rest.common.v1.SimpleReferenceType;
import com.telefonica.schemas.unica.rest.common.v1.UserIdType;
import com.telefonica.schemas.unica.rest.sms.v1.SMSDeliveryStatusType;
import com.telefonica.schemas.unica.rest.sms.v1.SMSTextType;

/**
 * 
 * Abstract client interface for the REST binding of the Bluevia SMS MT Service.
 * 
 * @author Telefonica R&D
 *
 */
public abstract class BVMtSmsClient extends BVMtClient {

	private static Logger log = Logger.getLogger(BVMtSmsClient.class.getName());


	/**
	 * Initializer for common attributes
	 */
	protected void init(){

		// TODO Revisar mBaseUri ()
		//mBaseUri += buildUri(mMode, FEED_SMS_BASE_URI) + FEED_OUTBOUND_REQUESTS;
		mEncoding = Encoding.APPLICATION_XML;
		mParser = new XmlParser();
		mSerializer = new XmlSerializer();
	}


	/**
	 * 
	 * Allows to send and SMS to the gSDP. Sent SMS notifications will be received in the endpoint.
	 * The SMSID of the sent SMS is returned in order to ask later for the status of the message as well.
	 * The max length of the message must be less than 160 characters.
	 * @param destination the addresses of the recipients of the message
	 * @param text the text of the message
	 * @param originAddress the origingAdress
	 * @param senderName the name of sender
	 * @param payer the payer
	 * @param endpoint the endpoint to receive notifications of sent SMSs
	 * @param correlator the correlator
	 *
	 * @return the sent SMS ID
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	protected String send(ArrayList<String> destination, String text, UserId originAddress, String senderName, String payer, String endpoint, String correlator) throws BlueviaException, IOException {
		
	    SMSTextType message = new SMSTextType();
         if (!Utils.isEmpty(endpoint)) {
            SimpleReferenceType srt= new SimpleReferenceType();
            srt.setCorrelator(correlator);
        	srt.setEndpoint(endpoint);
            message.setReceiptRequest(srt);
        }

	    message.setMessage(text);
	    
	    message.setOriginAddress(originAddress.toUserIdType());
	    
	    if (!Utils.isEmpty(senderName)) {
	    	message.setSenderName(senderName);
	    }
	    
        ArrayList<UserIdType> destinations= new ArrayList<UserIdType>();
        for (String number: destination) {
        	UserIdType destinationList= new UserIdType();
        	destinationList.setPhoneNumber(number);
        	destinations.add(destinationList);
        }
	    message.setAddress(destinations);
	    
	    return sendSmsMessage(message, payer);
	}
	
	
	 
	/**
	 * Allows to send and SMS to the gSDP.  It returns a String containing the SMSID of the sent SMS,
	 * in order to ask later for the status of the message.
	 * The max length of the message must be less than 160 characters.
	 *
	 * @param message the message to send via SMS. Message includes both message and message properties like senders, etc
	 * @return the sent SMS ID
	 * @throws BlueviaException
	 * @throws SerializeException 
	 * @throws IOException 
	 */
	protected String sendSmsMessage(SMSTextType message, String payer) throws BlueviaException, SerializeException, IOException {
		String result = null;

		// TODO: Check Message?
		//checkMessage(message);

		GenericResponse response = null;
		InputStream is = null;

		try {

			HashMap<String, String> params = new HashMap<String, String>();
			params.put(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);

			JaxbEntity messageEntity= new JaxbEntity();
			messageEntity.setJcInstance(XmlConstants.XSD_SMS_API_INSTANCE);
			messageEntity.setNamespace(XmlConstants.NS_SMS_API_URI);
			messageEntity.setQname(XmlConstants.XSD_SMSTEXTTYPE_SMSTEXT);
			messageEntity.setObject(message);
	        
			byte[] bytes = serializeEntity(messageEntity);

			String uri = mBaseUri;
			
			HashMap<String, String> headers = new HashMap<String, String>();
			headers.put(GsdpConstants.HEADER_CONTENT_TYPE, getEncoding(mEncoding));
			if (!Utils.isEmpty(payer)) {
				headers.put(HEADER_X_CHARGED_ID, payer);				
			}

			response = mConnector.create(uri, params, bytes, headers);

			is = response.getAdditionalData().getBody();

			result = getMesageIdFromResponse(response.getAdditionalData().getHeaders(), uri);

		} catch (ConnectorException e){
			if (e.getAdditionalData().getBody() == null)
				throw e;
			else {
				is = e.getAdditionalData().getBody();
				String error = parseError(is);
				throw new ConnectorException(error, e.getCode());
			}
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
	 * 
	 * Allows to know the delivery status of a previous sent message using Bluevia API
	 * 
	 * @param messageId the id of the message previously sent using this API
	 * @return an arrayList containing the DeliveryInfo for each destination address from the sent message.
	 * @throws BlueviaException
	 * @throws IOException 
	 *
	 */
	public ArrayList<DeliveryInfo> getDeliveryStatus(String messageId) throws BlueviaException, IOException {
		ArrayList<DeliveryInfo> status = null;

		if (Utils.isEmpty(messageId))
			throw new BlueviaException("Bad request: messageId is either null or empty", BlueviaException.BAD_REQUEST_EXCEPTION);

		//Build the status feed uri
		String feedUri = "/" + messageId + DELIVERY_STATUS_PATH;

		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);
		
		((XmlParser) mParser).setParseClass(SMSDeliveryStatusType.class);
		Entity response = retrieve(feedUri, parameters);

		//Check if response is instance of JaxbEntity
		if ((response == null) || (! (response instanceof JaxbEntity)))
			throw new BlueviaException("Error during request. Response received does not correspond to an DeliveryInfo",
					BlueviaException.INTERNAL_CLIENT_ERROR);
		JaxbEntity parseEntity= (JaxbEntity) response;
		
		
		//Check if parseEntity is instance of SMSDeliveryStatusType
		SMSDeliveryStatusType deliveryStatusType= new SMSDeliveryStatusType();
		if ((parseEntity.getObject() == null) || (! (parseEntity.getObject() instanceof SMSDeliveryStatusType)))
			throw new BlueviaException("Error during request. Response received does not correspond to an DeliveryInfo",
					BlueviaException.INTERNAL_CLIENT_ERROR);
    	deliveryStatusType = (SMSDeliveryStatusType) parseEntity.getObject();

 		log.debug("Creating response List of DeliveryInfo");				
	    Entity retEntity = new DeliveryInfoList(deliveryStatusType);

		//Check if response is instance of DeliveryInfo
		if ((retEntity == null) || (! (retEntity instanceof DeliveryInfoList)))
			throw new BlueviaException("Error during request. Response received does not correspond to an DeliveryInfo",
					BlueviaException.INTERNAL_CLIENT_ERROR);

		//Set the response
		status = ((DeliveryInfoList) retEntity).getDeliveryStatusList();

		if (status.isEmpty())
			throw new BlueviaException("The DeliveryInfo received is empty",
					BlueviaException.INTERNAL_CLIENT_ERROR);
		

		return status;
	}
	

}
