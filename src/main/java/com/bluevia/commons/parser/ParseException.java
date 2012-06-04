package com.bluevia.commons.parser;

import com.bluevia.commons.exception.BlueviaException;

/**
 * This class encapsulates parse errors returned during the parse of the streams
 * into an entity
 * Code error for ParseExceptions are always PARSER_EXCEPTION
 *
 */
public class ParseException extends BlueviaException {

    /**
     *
     */
    private static final long serialVersionUID = 5107010397481278895L;

    /**
     * Creates a ParseException
     */
    public ParseException() {
        super(BlueviaException.PARSER_EXCEPTION);
    }

    /**
     * Creates a ParseException with the given message.
     */
    public ParseException(String detailMessage) {
        super(detailMessage, BlueviaException.PARSER_EXCEPTION);
    }

    /**
     * Creates a ParseException with the given message and exception.
     */
    public ParseException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable, BlueviaException.PARSER_EXCEPTION);
    }

    /**
     * Creates a ParseException with the given exception.
     */
    public ParseException(Throwable throwable) {
        super(throwable, BlueviaException.PARSER_EXCEPTION);
    }
}
