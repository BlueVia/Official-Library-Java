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

import java.io.NotSerializableException;
/**
 * Useful generic exception for known system situations.
 */
@SuppressWarnings("serial")

public class BlueviaException extends Exception{
    
    private String code = null;

    /**
     * Constructor.
     */
    public BlueviaException() {
        super();
    }

    /**
     * Constructor.
     */
    public BlueviaException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor.
     */
    public BlueviaException(String message, String code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    /**
     * Constructor.
     */
    public BlueviaException(final String message) {
        super(message);
    }

    /**
     * Constructor.
     */
    public BlueviaException(String message, String code) {
        super(message);
        this.code = code;
    }

    /**
     * Constructor.
     */
    public BlueviaException(final Throwable cause) {
        super(cause);
    }

    /**
     * @return Returns the code.
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code
     *            The code to set.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Method for avoiding uncontrolled serialization of this class.
     */
    private void writeObject(final java.io.ObjectOutputStream s)
            throws NotSerializableException {
        throw new NotSerializableException(
                "Serialization is not allowed for this object. If serialization"
                        + " is needed manage explicitly the serialVersionUID"
                        + " field and remove this method and the 'serial'"
                        + " suppressed warning.");
    }

 
}
