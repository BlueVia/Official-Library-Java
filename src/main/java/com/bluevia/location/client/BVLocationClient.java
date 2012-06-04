/**
 * \package com.bluevia.location This package contains the classes in order to get Location using Bluevia API.
 * \package com.bluevia.location.client This package contains REST client to get Location using Bluevia API.
 */
package com.bluevia.location.client;

import java.io.IOException;

import java.util.HashMap;

import com.bluevia.commons.Encoding;
import com.bluevia.commons.Entity;
import com.bluevia.commons.client.BVBaseClient;
import com.bluevia.commons.connector.http.oauth.IOAuth;
import com.bluevia.commons.data.JaxbEntity;
import com.bluevia.commons.data.UserId;
import com.bluevia.commons.data.UserId.Type;
import com.bluevia.commons.exception.BlueviaException;
import com.bluevia.commons.parser.xml.XmlConstants;
import com.bluevia.commons.parser.xml.XmlParser;
import com.bluevia.location.data.LocationInfo;
import com.bluevia.location.data.LocationInfo.ReportStatus;
import com.telefonica.schemas.unica.rest.location.v1.LocationDataType;
import com.telefonica.schemas.unica.rest.location.v1.LocationInfoType;

/**
 * Client interface for the REST binding of the Bluevia Location Service.
 *
 * @author Telefonica R&D
 * 
 */
public abstract class BVLocationClient extends BVBaseClient {

	protected static final String GET_LOCATION_FEED_PATH = "/TerminalLocation";

	protected static final String LOCATED_PARTY_KEY = "locatedParty";
	protected static final String ACCEPTABLE_ACCURACY_KEY = "acceptableAccuracy";

	/**
	 * Initializer for common attributes
	 */
	protected void init(){
		mEncoding = Encoding.APPLICATION_XML;
		mParser = new XmlParser();
		mSerializer = null;	//No serializer
	}

	/**
	 * Retrieves the location of the terminal.
	 * 
	 * @param user the user to be located
	 * @param acceptableAccuracy Accuracy, in meters, that is acceptable for a response (optional).
	 * @return An entity object containing the Location Data.
	 * @throws BlueviaException 
	 * @throws IOException 
	 */
	protected LocationInfo getLocation(UserId user, Integer acceptableAccuracy) throws BlueviaException, IOException {

		LocationInfo res = null;

		//Common mandatory params
		HashMap<String, String> httpQueryParameters = new HashMap<String, String>();

		if (acceptableAccuracy != null)
			httpQueryParameters.put(ACCEPTABLE_ACCURACY_KEY, String.valueOf(acceptableAccuracy));


		//Located party
		UserId locatedParty = null;
		if (user == null){
			locatedParty = new UserId(Type.ALIAS, ((IOAuth)mConnector).getOauthToken().getToken());
		} else locatedParty = user;
		String lp = locatedParty.getStringType() + ":" + locatedParty.getUserIdValue();
		httpQueryParameters.put(LOCATED_PARTY_KEY, lp);

		//Version
		httpQueryParameters.put(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);

		((XmlParser) mParser).setParseClass(LocationDataType.class);
		Entity response = retrieve("", httpQueryParameters);

		//Check if response is instance of LocationDataType 
		if (response == null){
			throw new BlueviaException("Error during request. Response received is null", BlueviaException.INTERNAL_CLIENT_ERROR);
		}
		//Check if response is instance of JaxbEntity
		else if (!(response instanceof JaxbEntity)) {
			throw new BlueviaException("Error during request. Response received does not correspond to an SimpleAdResponse",
					BlueviaException.INTERNAL_CLIENT_ERROR);
		}
		JaxbEntity parseEntity= (JaxbEntity) response;
			
		//Check if parseEntity is instance of LocationDataType or TerminalLocationListType
		LocationDataType locationDataType= new LocationDataType();
		if ((parseEntity.getObject() == null) || (! (parseEntity.getObject() instanceof LocationDataType)))
			throw new BlueviaException("Error during request. Response received does not correspond to an LocationData",
					BlueviaException.INTERNAL_CLIENT_ERROR);
		locationDataType = (LocationDataType) parseEntity.getObject();

		res = simplifyResponse(locationDataType);

		if (!res.isValid()){
			throw new BlueviaException("The " + LocationDataType.class.getName()
					+ " received is empty, incomplete or not valid", BlueviaException.INTERNAL_CLIENT_ERROR);
		}

		return res;
	}

	/**
	 * Transforms the response received from the server in a simple response object
	 * 
	 * @param data the complete response
	 * @return a simple LocationInfo object
	 */
	protected LocationInfo simplifyResponse(LocationDataType data){
		
		if (data == null || data.getReportStatus() == null)
			return null;

		ReportStatus reportStatus= null;
		if (XmlConstants.XSD_TERMINALLOCATION_REPORTSTATUS_ATTR_RETRIEVED_VALUE.equals(data.getReportStatus().value())) {
			reportStatus = ReportStatus.RETRIEVED;
		} else if (XmlConstants.XSD_TERMINALLOCATION_REPORTSTATUS_ATTR_NONRETRIEVED_VALUE.equals(data.getReportStatus().value())) {
			reportStatus = ReportStatus.NON_RETRIEVED;
		} else if (XmlConstants.XSD_TERMINALLOCATION_REPORTSTATUS_ATTR_ERROR_VALUE.equals(data.getReportStatus().value())) {
			reportStatus = ReportStatus.ERROR;
		} 
		
		if (reportStatus == null) {
			return null;
		}
		
		LocationInfo res = new LocationInfo(reportStatus);

		LocationInfoType location = data.getCurrentLocation();
		if (location != null){
			if (location.getCoordinates() != null){
				res.setLatitude(location.getCoordinates().getLatitude());
				res.setLongitude(location.getCoordinates().getLongitude());
			}
			res.setAccuracy(location.getAccuracy());
			res.setTimestamp(location.getTimestamp().toString());
		}
		return res;
	}

}
