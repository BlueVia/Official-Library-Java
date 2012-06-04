/**
 * \package com.bluevia.messagery.mt This package contains the classes in order to send SMS and MMS using Bluevia API.
 * \package com.bluevia.messagery.mt.client This package contains REST client to send SMS and MMS using Bluevia API.
 */
package com.bluevia.messagery.mt.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.bluevia.commons.Utils;
import com.bluevia.commons.client.BVBaseClient;
import com.bluevia.commons.data.UserId;
import com.bluevia.commons.data.UserId.Type;
import com.bluevia.commons.exception.BlueviaException;
import com.bluevia.messagery.data.AbstractMessage;
import com.bluevia.messagery.mt.data.DeliveryInfo;


/**
 * 
 * Abstract client interface for the REST binding of the Bluevia Messagery MT Service.
 * 
 * @author Telefonica R&D
 *
 */
public abstract class BVMtClient extends BVBaseClient {

	private static Logger log = Logger.getLogger(BVMtClient.class.getName());

	protected static final String FEED_OUTBOUND_REQUESTS = "/outbound/requests";

	protected static final String DELIVERY_STATUS_PATH = "/deliverystatus";

	protected static final String HEADER_X_CHARGED_ID = "X-ChargedId";

	private static final String MESSAGEID_LOCATION_HEADER = "Location";

	/**
	 * Check if the message passed as parameter is valid and throws an BlueviaException if not.
	 *
	 * @param message message to check.
	 * @throws BlueviaException if the message parameter is not valid.
	 */
	protected static void checkMessage(AbstractMessage message) throws BlueviaException {
		if (!Utils.isValid(message))
			throw new BlueviaException("Bad request: message parameters are either null or not valid", BlueviaException.BAD_REQUEST_EXCEPTION);
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
			String regex = feed + "/(.*)" + DELIVERY_STATUS_PATH +"/?$";

			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(messageId);

			if (matcher.matches()) {

				log.debug("matcher.group(1): "+matcher.group(1));
				return matcher.group(1);

			} else throw new BlueviaException("Location received does not correspond to the pattern of an MessageID URI" +
					"LocationUri: "+messageId+" regex: "+regex, BlueviaException.INTERNAL_CLIENT_ERROR);

		} else throw new BlueviaException("Location header is empty", BlueviaException.INTERNAL_CLIENT_ERROR);

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
	public abstract ArrayList<DeliveryInfo> getDeliveryStatus(String messageId) throws BlueviaException, IOException;
	
	/**
	 * 
	 * @param addresses
	 * @return
	 * @throws BlueviaException
	 */
	protected ArrayList<UserId> buildUserIdList(ArrayList<String> addresses) throws BlueviaException{

		if ((addresses == null) || (addresses.size()<=0))
			throw new BlueviaException("Bad request: addresses parameter should contain at least one valid address", BlueviaException.BAD_REQUEST_EXCEPTION);
		
		ArrayList<UserId> addressList = new ArrayList<UserId>();
		for (String phoneNumber : addresses) {
			addressList.add (new UserId(Type.PHONE_NUMBER,phoneNumber));
		}
		return addressList;
	}
	
}
