package com.bluevia.commons.parser;

import com.bluevia.commons.exception.BlueviaException;

/**
 * This class encapsulates parse errors returned 
 * during the serialization of an entity into a stream
 * Code error for ParseExceptions are always SERIALIZER_EXCEPTION
 *
 */
public class SerializeException extends BlueviaException {

    /**
     *
     */
    private static final long serialVersionUID = 5107010397481278895L;

    /**
     * Creates a ParseException
     */
    public SerializeException() {
        super(BlueviaException.SERIALIZER_EXCEPTION);
    }

    /**
     * Creates a ParseException with the given message.
     */
    public SerializeException(String detailMessage) {
        super(detailMessage, BlueviaException.SERIALIZER_EXCEPTION);
    }

    /**
     * Creates a ParseException with the given message and exception.
     */
    public SerializeException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable, BlueviaException.SERIALIZER_EXCEPTION);
    }

    /**
     * Creates a ParseException with the given exception.
     */
    public SerializeException(Throwable throwable) {
        super(throwable, BlueviaException.SERIALIZER_EXCEPTION);
    }
}
