/**
 * 
 * @category bluevia
 * @package com.bluevia.java.location
 * @copyright Copyright (c) 2010 Telefonica I+D (http://www.tid.es)
 * @author Bluevia <support@bluevia.com>
 * 
 * 
 *          BlueVia is a global iniciative of Telefonica delivered by Movistar
 *          and O2. Please, check out http://www.bluevia.com and if you need
 *          more information contact us at support@bluevia.com
 */

package com.bluevia.java.location;

import java.io.StringReader;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;

import com.bluevia.java.Utils;
import com.bluevia.java.exception.BlueviaException;
import com.bluevia.java.oauth.OAuthToken;
import com.telefonica.schemas.unica.rest.common.v1.UserIdType;
import com.telefonica.schemas.unica.rest.location.v1.LocationDataType;

/**
 * Terminal Location
 * 
 * @package com.bluevia.java.location
 */

/**
 * <p>Java class for retrieve terminal location through Location API
 * 
 */
public class TerminalLocation extends LocationClient {

	/**
	 * Constructor
	 * 
	 * @param consumerToken
	 * @param accessToken
	 * @param mode
	 * @throws JAXBException
	 */
	public TerminalLocation(OAuthToken consumerToken, OAuthToken accessToken, Mode mode) throws JAXBException {
		super(consumerToken, accessToken, mode);
	}

	/**
	 * Gets the location of the terminal
	 * 
	 * @param acceptableAccuracy the acceptable accuracy for the request
	 * @return the location data
	 * @throws JAXBException
	 * @throws BlueviaException
	 */
	public LocationDataType getLocation(Integer acceptableAccuracy) throws JAXBException, BlueviaException {

		String url = this.uri + "?version=v1";

		String data = toHttpQueryString(acceptableAccuracy);
		url = url + data;

		String res = this.restConnector.get(url);
		JAXBElement<LocationDataType> e = this.u.unmarshal(new StreamSource(new StringReader(res)), LocationDataType.class);

		return e.getValue();
	}

	/**
	 * Converts params to http query string
	 * 
	 * @param acceptableAccuracy
	 * @return
	 */
	private String toHttpQueryString(Integer acceptableAccuracy) {

		StringBuffer sb = new StringBuffer();
		Hashtable<String, String> ht = new Hashtable<String, String>();

		if (acceptableAccuracy != null) {
			String accAccuracy = String.valueOf(acceptableAccuracy);
			ht.put("acceptableAccuracy", accAccuracy);
		}

		Enumeration<String> keys = ht.keys();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			String value = (String) ht.get(key);
			sb.append("&").append(key).append("=").append(value);
		}

		// Located Party
		UserIdType lp = new UserIdType();
		lp.setAlias(this.restConnector.getAccessToken());
		sb.append("&");
		sb.append("locatedParty").append("=");
		sb.append(Utils.printUserIdType(lp));

		return sb.toString();

	}

}
