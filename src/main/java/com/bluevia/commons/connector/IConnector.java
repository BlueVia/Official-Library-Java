package com.bluevia.commons.connector;

import java.io.IOException;
import java.util.HashMap;

import com.bluevia.commons.connector.http.multipart.BlueviaPartBase;
import com.bluevia.commons.exception.BlueviaException;

/**
 * Interface that will be implemented by REST clients that will allow to
 * manage REST requests and responses
 *
 * @author Telefonica R&D
 */
public interface IConnector {
	
	/**
	 * Creates a request using REST to the gSDP server in order to create an entity
	 * using POST method
	 * 
	 * @param address the uri to create the entity remotely via REST
	 * @param parameters The query parameters for the request
	 * @param content the body of the request
	 * @param headers headers of the request
	 * @return the response of the operation
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public GenericResponse create(String address, HashMap<String, String> parameters, byte[] content, HashMap<String, String> headers) throws BlueviaException, IOException;

	/**
	 * Creates a request using HTTP REST to the server in order to create a multipart entity
	 * 
	 * @param address the uri to create the entity remotely via REST
	 * @param parameters the query parameters for the request
	 * @param parts the multiparts
	 * @param headers headers of the request
	 * @return the response of the operation
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public GenericResponse create(String address, HashMap<String, String> parameters, BlueviaPartBase[] parts, HashMap<String, String> headers) throws BlueviaException, IOException;
	
	/**
	 * Creates a request using REST to the gSDP server in order to retrieve an entity from the server
	 *
	 * @param address the uri to create the entity remotely via REST
	 * @return the response of the operation
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public GenericResponse retrieve(String address) throws BlueviaException, IOException;

	/**
	 * Creates a request using REST to the gSDP server in order to retrieve an entity from the server
	 *
	 * @param address the uri to create the entity remotely via REST
	 * @param parameters the parameters in order to do the filtering
	 * @return the response of the operation
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public GenericResponse retrieve(String address, HashMap<String, String> parameters) throws BlueviaException, IOException;
	
	/**
	 * Creates a request using REST to the gSDP server in order to retrieve an entity from the server
	 *
	 * @param address the uri to create the entity remotely via REST
	 * @param parameters the parameters in order to do the filtering
	 * @param headers headers of the request
	 * @return the response of the operation
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public GenericResponse retrieve(String address, HashMap<String, String> parameters, HashMap<String, String> headers) throws BlueviaException, IOException;

	/**
	 * Creates a request using REST to the gSDP server in order to create an entity
	 * 
	 * @param address the uri to create the entity remotely via REST
	 * @param parameters The query parameters for the request
	 * @param body the body of the request
	 * @param headers headers of the request
	 * @return the response of the operation
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public GenericResponse update(String address, HashMap<String, String> parameters, byte[] body, HashMap<String, String> headers) throws BlueviaException, IOException;

	/**
	 * Creates a request using REST to the gSDP server in order to retrieve an entity from the server
	 *
	 * @param address the uri to create the entity remotely via REST
	 * @return the response of the operation
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public GenericResponse delete(String address) throws BlueviaException, IOException;

	/**
	 * Creates a request using REST to the gSDP server in order to retrieve an entity from the server
	 *
	 * @param address the uri to create the entity remotely via REST
	 * @param parameters the parameters in order to do the filtering
	 * @return the response of the operation
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public GenericResponse delete(String address, HashMap<String, String> parameters) throws BlueviaException, IOException;
	
	/**
	 * Creates a request using REST to the gSDP server in order to retrieve an entity from the server
	 *
	 * @param address the uri to create the entity remotely via REST
	 * @param parameters the parameters in order to do the filtering
	 * @param headers headers of the request
	 * @return the response of the operation
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public GenericResponse delete(String address, HashMap<String, String> parameters, HashMap<String, String> headers) throws BlueviaException, IOException;

	
	/**
	 * Close and free all connector resources
	 */
	public void close();
}