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

import com.telefonica.schemas.unica.rest.common.v1.ServerExceptionType;

/**
 * ServerException
 * 
 * @package com.bluevia.java
 */

/**
 * Object returned by sdk in case of Server related faults
 * 
 * <p>Java class for ServerException complex type.
 * 
 * 
 */

public class ServerException extends BlueviaException {
    private final ServerExceptionType type;
    public ServerException(ServerExceptionType se) {
        super(se.getText(),String.valueOf(se.getExceptionId()));
        this.type = se;
    }
    public ServerExceptionType getServerExceptionType() {
        return this.type;
    }
}
