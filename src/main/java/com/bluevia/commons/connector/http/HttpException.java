package com.bluevia.commons.connector.http;

import java.io.InputStream;
import java.util.HashMap;

import com.bluevia.commons.exception.ConnectorException;

/**
 * This class encapsulates HTTP errors returned by the REST server
 *
 */
public class HttpException extends ConnectorException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4363860658454422142L;

	/**
	 * Error code for a BAD REQUEST.
	 */
	public static final int BAD_REQUEST = 400;

	/**
	 * Error code for a UNAUTHORIZED.
	 */
	public static final int UNAUTHORIZED = 401;

	/**
	 * Error code for a FORBIDDEN.
	 */
	public static final int FORBIDDEN = 403;

	/**
	 * Error code for a NOT FOUND.
	 */
	public static final int NOT_FOUND = 404;

	/**
	 * Error code for a REQUEST TIME OUT.
	 */
	public static final int REQUEST_TIME_OUT = 408;

	/**
	 * Error code for a CONFLICT.
	 */
	public static final int CONFLICT = 409;

	/**
	 * Error code for a GONE.
	 */
	public static final int GONE = 410;

	/**
	 * Error code for a REQUEST ENTITY TOO LARGE.
	 */
	public static final int REQUEST_ENTITY_TOO_LARGE = 413;

	/**
	 * Error code for a UNSUPORTED MEDIA TYPE.
	 */
	public static final int UNSUPORTED_MEDIA_TYPE = 415;

	/**
	 * Error code for a INTERNAL SERVER ERROR.
	 */
	public static final int INTERNAL_SERVER_ERROR = 500;

	/**
	 * Error code for a NOT IMPLEMENTED.
	 */
	public static final int NOT_IMPLEMENTED = 501;

	/**
	 * Creates an HttpException with the given message and statusCode
	 * 
	 */
	public HttpException(String message, int statusCode) {
		this(message, statusCode, null, null);
	}

	/**
	 * Creates an HttpException with the given message, statusCode and
	 * responseStream.
	 */
	public HttpException(String message, int statusCode, InputStream responseStream, HashMap<String, String> headers) {
		super(message, statusCode, new AdditionalData(responseStream, headers));
	}
	
    /**
     * Creates an HttpException with the given statusCode and
     * responseStream.
     */
    public HttpException(int statusCode, InputStream responseStream, HashMap<String, String> headers) {
      super(statusCode, new AdditionalData(responseStream, headers));
    }

}