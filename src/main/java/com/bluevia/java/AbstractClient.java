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
	 * - Live: In the Live environment your application uses the real network, 
	 * which means that you will be able to send real transactions through Bluevia.
	 * 
	 * - Sandbox: The Sandbox environment offers you the exact same experience as 
	 * the Live environment except that no traffic is generated on the live network, 
	 * in order to test applications. 
	 *
	 */
	public enum Mode {LIVE, SANDBOX}
	
	public static final String BASE_ENDPOINT = "https://api.bluevia.com/services";
	
    protected JAXBContext jc;
    protected Unmarshaller u;
    protected Marshaller m;
    protected String uri;

}
