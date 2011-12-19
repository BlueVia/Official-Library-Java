/**
 * 
 * @category bluevia
 * @package com.bluevia.java.directory.client
 * @copyright Copyright (c) 2010 Telefonica I+D (http://www.tid.es)
 * @author Bluevia <support@bluevia.com>
 * 
 * 
 *          BlueVia is a global iniciative of Telefonica delivered by Movistar
 *          and O2. Please, check out http://www.bluevia.com and if you need
 *          more information contact us at support@bluevia.com
 */

package com.bluevia.java.directory;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;

import com.bluevia.java.exception.BlueviaException;
import com.bluevia.java.oauth.OAuthToken;
import com.telefonica.schemas.unica.rest.directory.v1.UserInfoType;

/**
 * UserInfo
 * 
 * @package com.bluevia.java.directory.client
 */

/**
 * <p>Java class for get User Info from Directory API
 * 
 */
public class UserInfo extends DirectoryClient {

    /**
     * Constructor
     * 
     * @param tokenConsumer
     * @param token
     * @param mode
     * @throws JAXBException
     */
    public UserInfo(OAuthToken tokenConsumer, OAuthToken token, Mode mode) throws JAXBException {

        super(tokenConsumer, token, mode);
    }

    /**
     * Get User Info from phoneNumber
     * 
     * @param alias_accessToken
     * @param dataSets
     * @return
     * @throws JAXBException
     * @throws BlueviaException
     */
    public UserInfoType get(UserSearchInfoCategories[] dataSets) throws JAXBException, BlueviaException {

        StringBuffer sb = new StringBuffer();
        if (dataSets != null) {
            for (UserSearchInfoCategories e : dataSets) {
                sb.append(e.toString()).append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        String url = this.uri + "alias:" + this.restConnector.getAccessToken() + "/UserInfo?version=v1";
        if (dataSets != null) {
            try {
                url = url + "&dataSets=" + URLEncoder.encode(sb.toString(), "UTF-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

        }
        String res = this.restConnector.get(url);

        JAXBElement<UserInfoType> e = this.u.unmarshal(new StreamSource(new StringReader(res)), UserInfoType.class);

        return e.getValue();
    }
}
