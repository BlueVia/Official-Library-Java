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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import com.bluevia.java.AbstractRESTClient;
import com.bluevia.java.oauth.OAuthToken;

/**
 * DirectoryClient
 * @package com.bluevia.java.directory
 */

/**
 * <p>Java class for Directory API. This class  is used to initialize objects.
 *
 */
public class DirectoryClient extends AbstractRESTClient {

	private static String PATH_LIVE = "/REST/Directory/";
	private static String PATH_SANDBOX = "/REST/Directory_Sandbox/";

    /**
     * Constructor
     * 
     * @param tokenConsumer
     * @param token
     * @param mode
     * @throws JAXBException
     */
    public DirectoryClient(OAuthToken tokenConsumer,OAuthToken token, Mode mode) throws JAXBException {
        super(tokenConsumer, token);
    	this.jc = JAXBContext.newInstance("com.telefonica.schemas.unica.rest.directory.v1");
        this.u = jc.createUnmarshaller();
        
        switch (mode){
        case LIVE:
        	this.uri = BASE_ENDPOINT + PATH_LIVE;
        	break;
        case SANDBOX:
        	this.uri = BASE_ENDPOINT + PATH_SANDBOX;
        	break;
        }
    }
}


