/**
 * \package com.bluevia.messagery.mo This package contains the classes in order to receive SMS and MMS using Bluevia API.
 * \package com.bluevia.messagery.mo.client This package contains REST client to receive SMS and MMS using Bluevia API.
 */
package com.bluevia.messagery.mo.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;

import com.bluevia.commons.Utils;
import com.bluevia.commons.client.BVBaseClient;
import com.bluevia.commons.exception.BlueviaException;


/**
 * 
 * Abstract client interface for the REST binding of the Bluevia Messagery MO Service.
 * 
 * @author Telefonica R&D
 *
 */
public abstract class BVMoClient extends BVBaseClient {

	protected static final String FEED_INBOUND_REQUESTS = "/inbound";
    protected static final String RECEIVED_MESSAGES = "/messages";
    protected static final String SUBSCRIPTION_PATH = "/subscriptions";
	protected static final String MESSAGEID_LOCATION_HEADER = "Location";
  
	private static Logger log = Logger.getLogger(BVMoClient.class.getName());
	
    /**
     * Check if the registrationId passed as parameter is valid.
     *
     * @param id registrationId to check.
     * @throws BlueviaException if the id is not valid.
     */
    protected static void checkRegistrationId(String id) throws BlueviaException {
        if (Utils.isEmpty(id))
            throw new BlueviaException("Bad request: RegistrationId is either null or empty", BlueviaException.BAD_REQUEST_EXCEPTION);
    }
  
	public abstract String startNotification (String phoneNumber, String endpoint, String criteria) throws IOException, BlueviaException;

	public abstract String startNotification (String phoneNumber, String endpoint, String criteria, String correlator) throws IOException, BlueviaException;

	public abstract boolean stopNotification (String correlator) throws BlueviaException, IOException;


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

	protected String generateCorrelator(String criteria) {
		String newCorrelator= new String("");
		String randomString = RandomStringUtils.randomAlphanumeric(25);
    	newCorrelator= ((criteria.substring(0,5) + randomString).replaceAll("-", "_")).substring(0,20);
    	return newCorrelator;
	}



}
