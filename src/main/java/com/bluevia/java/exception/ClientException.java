/**
 * 
 * @category bluevia
 * @package com.bluevia.java
 * @copyright Copyright (c) 2010 Telefonica I+D (http://www.tid.es)
 * @author Bluevia <support@bluevia.com>
 * 
 * 
 *          BlueVia is a global iniciative of Telefonica delivered by Movistar
 *          and O2. Please, check out http://www.bluevia.com and if you need
 *          more information contact us at support@bluevia.com
 */

package com.bluevia.java.exception;

import com.telefonica.schemas.unica.rest.common.v1.ClientExceptionType;

/**
 * ClientException
 * 
 * @package com.bluevia.java
 */

/**
 * Object returned by sdk in case of Client related faults
 * 
 * <p>Java class for ClientExceptionType complex type.
 * 
 * 
 */

public class ClientException extends BlueviaException {
    private final ClientExceptionType type;
    /**
     * Constructor
     * 
     * @param ce
     *     allowed object is
     *     {@link ClientExceptionType }
     *   
     */
    
    public ClientException(ClientExceptionType ce) {
        
        super(ce.getText(),String.valueOf(ce.getExceptionId()));
        this.type = ce;
        
    }
    
    /**
     * Get ClientExceptionType
     * 
     * @return ClientExceptionType
     *   
     */
    public ClientExceptionType getClientExceptionType() {
        return this.type;
    }
}
