/**
 * \package com.bluevia.messagery.mt.mms This package contains the classes in order to semd MMS using Bluevia API.
 * \package com.bluevia.messagery.mt.mms.client This package contains REST client to send MMS using Bluevia API.
 */
package com.bluevia.messagery.mt.mms.client;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.bluevia.commons.Encoding;
import com.bluevia.commons.Entity;
import com.bluevia.commons.Utils;
import com.bluevia.commons.connector.GenericResponse;
import com.bluevia.commons.connector.http.multipart.BlueviaPartBase;
import com.bluevia.commons.connector.http.multipart.FilePart;
import com.bluevia.commons.connector.http.multipart.PartContainer;
import com.bluevia.commons.connector.http.multipart.StringPart;
import com.bluevia.commons.data.JaxbEntity;
import com.bluevia.commons.exception.BlueviaException;
import com.bluevia.commons.exception.ConnectorException;
import com.bluevia.commons.parser.xml.XmlConstants;
import com.bluevia.commons.parser.xml.XmlParser;
import com.bluevia.commons.parser.xml.XmlSerializer;
import com.bluevia.messagery.mt.client.BVMtClient;
import com.bluevia.messagery.mt.data.DeliveryInfo;
import com.bluevia.messagery.mt.data.DeliveryInfoList;
import com.bluevia.messagery.mt.mms.data.Attachment;
import com.bluevia.messagery.mt.mms.data.MmsMessageReq;
import com.bluevia.messagery.mt.mms.data.Attachment.ContentType;
import com.telefonica.schemas.unica.rest.mms.v1.MessageDeliveryStatusType;

/**
 * 
 * Abstract client interface for the REST binding of the Bluevia MMS MT Service.
 * 
 * @author Telefonica R&D
 *
 */
public abstract class BVMtMmsClient extends BVMtClient {

	private static Logger log = Logger.getLogger(BVMtMmsClient.class.getName());


	/**
	 * Initializer for common attributes
	 */
	protected void init(){
		mEncoding = Encoding.APPLICATION_XML;
		mParser = new XmlParser();
		mSerializer = new XmlSerializer();
	}
	

	/**
	 *
	 * Allows to know the delivery status of a previous sent MMS using Bluevia API
	 * @param mmsId the id of the mms previously sent using this API
	 * @param payer the payer
	 * @return the delivery status of the MMS message id
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	protected String sendMms(MmsMessageReq mmsMessage, String payer) throws BlueviaException, IOException {
		String result = null;

		checkMessage(mmsMessage);

		GenericResponse response = null;
		InputStream is = null;

		try {


			JaxbEntity messageEntity= new JaxbEntity();
			messageEntity.setJcInstance(XmlConstants.XSD_MMS_API_INSTANCE);
			messageEntity.setNamespace(XmlConstants.NS_MMS_API_URI);
			messageEntity.setQname(XmlConstants.XSD_MMSTEXTTYPE_MMSMESSAGE);
			messageEntity.setObject(mmsMessage.toMessageType());

			byte[] bytes = serializeEntity(messageEntity);

			BlueviaPartBase[] parts = buildMultipart(mmsMessage, bytes);

			String uri = mBaseUri;

			HashMap<String, String> params = new HashMap<String, String>();
			params.put(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);

			HashMap<String, String> headers = new HashMap<String, String>();
			if (!Utils.isEmpty(payer)) {
				headers.put(HEADER_X_CHARGED_ID, payer);				
			}
			response = mConnector.create(uri, params, parts, headers);
			
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
	 * Build a multipart Part Base adapted to Bluevia
	 * 
	 * @param message
	 * @return
	 * @throws BlueviaException
	 * @throws IOException
	 */
	private BlueviaPartBase[] buildMultipart(MmsMessageReq message, byte[] serializedRoots) throws BlueviaException, IOException {

		String entryString = new String(serializedRoots, "UTF-8");

		//Build root-field
		StringPart rootField = new StringPart("root-fields", entryString);

		switch (mEncoding) {
		case APPLICATION_XML:
			rootField.setContentType(BlueviaPartBase.APPLICATION_XML);
			break;
		case APPLICATION_JSON:
			rootField.setContentType(BlueviaPartBase.APPLICATION_JSON);
			break;
		case APPLICATION_URL_ENCODED:
			rootField.setContentType(BlueviaPartBase.APPLICATION_URL_ENCODED);
		}

		BlueviaPartBase[] multipart = new BlueviaPartBase[2];
		multipart[0] = rootField;

		ArrayList<Attachment> attachmentList = message.getAttachementList();

		int partssize= 1; // for adding message Text to parts
		if (attachmentList!=null) {
			partssize+= attachmentList.size();
		}
		BlueviaPartBase[] parts = new BlueviaPartBase[partssize];

		StringPart textPart= new StringPart("message.txt", message.getTextMessage());
		textPart.setFilename(textPart.getName());
		parts[0]= (StringPart) textPart;		
		parts[0].setName("attachment");
		parts[0].setContentDisposition("form-data");

		for (int i=1; i<partssize; i++) {
			int index= i - 1;
			File file= new File(attachmentList.get(index).getFilePath());
			FilePart attach= new FilePart(file.getName(), file, attachmentList.get(index).getStringContentType());
			
			// Text encondig (from user system default encoding to utf-8) for text/plain
			if (attachmentList.get(index).getStringContentType().equals(BlueviaPartBase.TEXT_PLAIN)) {
				
				FileInputStream filein= new FileInputStream(file);
				ByteArrayOutputStream os= new ByteArrayOutputStream();			
				byte[] buf = new byte[1024];
				int len;
				while ((len = filein.read(buf)) > 0) {
					os.write(buf, 0, len);
				}
				filein.close();
				
				String fileText = null;
				fileText = new String(os.toString(Charset.defaultCharset().displayName()));

				StringPart textPlainPart= new StringPart(attachmentList.get(index).getFilePath(), fileText);
				textPlainPart.setFilename(textPlainPart.getName());
				parts[i]= (StringPart) textPlainPart;		
				parts[i].setName("attachment");
				parts[i].setContentDisposition("form-data");				
				
			} else {
				parts[i]= attach;
				parts[i].setName("attachment");
				parts[i].setContentDisposition("form-data");
			}
		}

		PartContainer part_container = new PartContainer("attachments", parts);
		multipart[1] = part_container;

		return multipart;
	}

	/**
	 * Adds the message parameter as a string attachment
	 * 
	 * @param message
	 * @param attachmentList
	 * @throws IOException
	 */
	
	protected void addMessageToAttachments(String message, ArrayList<Attachment> attachmentList) throws IOException{
		if (!Utils.isEmpty(message)){
			if (attachmentList == null) {
				attachmentList = new ArrayList<Attachment>();
			}
			attachmentList.add(new Attachment("message.txt", ContentType.TEXT_PLAIN));

		}
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
		
		((XmlParser) mParser).setParseClass(MessageDeliveryStatusType.class);
		Entity response = retrieve(feedUri, parameters);

		//Check if response is instance of JaxbEntity
		if ((response == null) || (! (response instanceof JaxbEntity)))
			throw new BlueviaException("Error during request. Response received does not correspond to an DeliveryInfo",
					BlueviaException.INTERNAL_CLIENT_ERROR);
		JaxbEntity parseEntity= (JaxbEntity) response;
		
		
		//Check if parseEntity is instance of MessageDeliveryStatusType
		MessageDeliveryStatusType deliveryStatusType= new MessageDeliveryStatusType();
		if ((parseEntity.getObject() == null) || (! (parseEntity.getObject() instanceof MessageDeliveryStatusType)))
			throw new BlueviaException("Error during request. Response received does not correspond to an DeliveryInfo",
					BlueviaException.INTERNAL_CLIENT_ERROR);
    	deliveryStatusType = (MessageDeliveryStatusType) parseEntity.getObject();
    	
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
