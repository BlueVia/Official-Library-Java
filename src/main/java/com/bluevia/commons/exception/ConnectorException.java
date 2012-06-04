package com.bluevia.commons.exception;


import com.bluevia.commons.connector.http.AdditionalData;


/**
 * This class encapsulates errors returned by the REST server
 *
 */
public class ConnectorException extends BlueviaException {

	/**
	 */
	private static final long serialVersionUID = 5791050574131117948L;
	
	private AdditionalData additionalData;

	/**
	 * Creates an ConnectorException with the given message and statusCode
	 * 
	 */
	public ConnectorException(String message, int statusCode) {
		this(message, statusCode, null);
	}

	/**
	 * Creates an ConnectorException with the given message, statusCode and
	 * responseStream.
	 */
	public ConnectorException(String message, int statusCode, AdditionalData additionalData) {
		super(message, statusCode);
		this.additionalData = additionalData;
	}

	/**
	 * Creates an ConnectorException with the given statusCode and
	 * responseStream.
	 */
	public ConnectorException(int statusCode, AdditionalData additionalData) {
		super(statusCode);
		this.additionalData = additionalData;
	}

	/**
	 * Returns additional data of the response
	 * 
	 * @return additional data of the response
	 */
	public AdditionalData getAdditionalData(){
		return additionalData;
	}

}