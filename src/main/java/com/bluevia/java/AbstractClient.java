/**
 * 
 * @category bluevia
 * @package com.bluevia.examples
 * @copyright Copyright (c) 2010 Telefonica I+D (http://www.tid.es)
 * @author Bluevia <support@bluevia.com>
 * 
 * 
 *          BlueVia is a global iniciative of Telefonica delivered by Movistar
 *          and O2. Please, check out http://www.bluevia.com and if you need
 *          more information contact us at support@bluevia.com
 */
package com.bluevia.java;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public abstract class AbstractClient {
	
	/**
	 * Working mode
	 * 
	 * - Live: In the Live environment your application uses the real network, which means 
	 * that you will be able to send real transactions to real Movistar, 
	 * O2 and Vivo customers in the applicable country.
	 * 
	 * - Test: The Test mode behave exactly like the Live mode, but the API calls are free of chargue, using a credits system. 
	 * You are required to have a Movistar, O2 or Vivo mobile number to get this monthly credits.
	 * 
	 * - Sandbox: The Sandbox environment offers you the exact same experience as the Live environment except 
	 * that no traffic is generated on the live network, meaning you can experiment and play until your heart’s content. 
	 *
	 */
	public enum Mode {LIVE, TEST, SANDBOX}
	
	public static final String BASE_ENDPOINT = "https://api.bluevia.com/services";
	
    protected JAXBContext jc;
    protected Unmarshaller u;
    protected Marshaller m;
    protected String uri;
    
    public AbstractClient(Mode mode, String pathLive, String pathSandbox){
    	
    	if (mode == null)
    		throw new IllegalArgumentException("Invalid parameter: mode cannot be null");
    	
        switch (mode){
        case LIVE:
        case TEST:
        	this.uri = BASE_ENDPOINT + pathLive;
        	break;
        case SANDBOX:
        	this.uri = BASE_ENDPOINT + pathSandbox;
        	break;
        }
    }

}
