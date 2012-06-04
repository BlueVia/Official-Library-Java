package com.bluevia.commons.connector;

import java.io.InputStream;
import java.util.HashMap;

import com.bluevia.commons.connector.http.AdditionalData;

/**
 * Class that represents the response of the IConnector methods.
 *
 * @author Telefonica R&D
 */
public class GenericResponse {
	
	private int mStatus;
	private String mMessage;
	private AdditionalData mAdditionalData;
	
	public GenericResponse(int status, String message, AdditionalData additionalData){
		mStatus = status;
		mMessage = message;
		mAdditionalData = additionalData;
	}
	
	public GenericResponse(int status, String message, InputStream body, HashMap<String, String> headers){
		mStatus = status;
		mMessage = message;
		mAdditionalData = new AdditionalData(body, headers);
	}

	/**
	 * Returns the status of the response
	 * 
	 * @return the status of the response
	 */
	public int getStatus(){
		return mStatus;
	}
	
	/**
	 * Returns the message of the response
	 * 
	 * @return the message of the response
	 */
	public String getMessage(){
		return mMessage;
	}
	
	/**
	 * Returns additional data of the response
	 * 
	 * @return additional data of the response
	 */
	public AdditionalData getAdditionalData(){
		return mAdditionalData;
	}
	
}
