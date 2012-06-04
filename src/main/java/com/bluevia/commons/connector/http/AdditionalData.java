package com.bluevia.commons.connector.http;

import java.io.InputStream;
import java.util.HashMap;

public class AdditionalData {
	
	private InputStream mBody;
	private HashMap<String, String> mHeaders;
	
	public AdditionalData(InputStream body, HashMap<String, String> headers){
		mBody = body;
		mHeaders = headers;
	}
	
	/**
	 * Returns the content of the response
	 * 
	 * @return the content of the response
	 */
	public InputStream getBody(){
		return mBody;
	}
	
	/**
	 * Returns the headers of the response
	 * 
	 * @return the headers of the response
	 */
	public HashMap<String, String> getHeaders(){
		return mHeaders;
	}
}
