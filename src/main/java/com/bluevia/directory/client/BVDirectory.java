package com.bluevia.directory.client;

import java.io.IOException;

import com.bluevia.commons.exception.BlueviaException;
import com.bluevia.directory.data.AccessInfo;
import com.bluevia.directory.data.PersonalInfo;
import com.bluevia.directory.data.Profile;
import com.bluevia.directory.data.TerminalInfo;
import com.bluevia.directory.data.UserInfo;

/**
 * Client interface for the REST binding of the Bluevia Directory Service.
 *
 * @author Telefonica R&D
 *
 */
public class BVDirectory extends BVDirectoryClient {
	
	private static final String FEED_DIRECTORY_BASE_URI = "/REST/Directory";

	/**
	 * Constructor
	 * 
	 * @param mode
	 * @param consumerKey
	 * @param consumerSecret
	 * @param token
	 * @param tokenSecret
	 * @throws BlueviaException
	 */
	public BVDirectory(Mode mode, String consumerKey, String consumerSecret,
			String token, String tokenSecret) throws BlueviaException{
		initUntrusted(mode, consumerKey, consumerSecret, token, tokenSecret);
		
		init();
		mBaseUri += buildUri(mMode, FEED_DIRECTORY_BASE_URI);
	}
	
	/**
	 * Allows an application to get all the user context information. Applications
	 * will only be able to retrieve directory information on themselves.
	 * 
	 * @return an entity containing all blocks of user context information.
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public UserInfo getUserInfo() throws BlueviaException, IOException {
		return getUserInfo(null);
	}

	/**
	 * Allows an application to get all the user context information. Applications
	 * will only be able to retrieve directory information on themselves.
	 * Information blocks can be filtered using the data set.
	 * 
	 * @param dataSet an array of the blocks to be retrieved
	 * 
	 * @return an entity containing all blocks of user context information.
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public UserInfo getUserInfo(UserInfo.DataSet[] dataSet) throws BlueviaException, IOException {
		return getUserInfo(null, dataSet);
	}
	


	/**
	 * Retrieves the whole User Profile resource block from the directory. Applications
	 * will only be able to retrieve directory information on themselves.
	 * 
	 * @return an entity object containing the user profile information
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public Profile getProfile() throws BlueviaException, IOException {
		return getProfile(null);
	}

	/**
	 * Retrieves a subset of the User Profile resource block from the directory. Applications
	 * will only be able to retrieve directory information on themselves.
	 * 
	 * @param fields a filter object to specify which information fields are required
	 * @return an entity object containing the user personal information
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public Profile getProfile(Profile.Fields[] fields) throws BlueviaException, IOException {
		return getProfile(null, fields);
	}
	
	/**
	 * Retrieves the whole User Access Information resource block from the directory. Applications
	 * will only be able to retrieve directory information on themselves.
	 * 
	 * @return an entity object containing the user access information
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public AccessInfo getAccessInfo() throws BlueviaException, IOException {
		return getAccessInfo(null);
	}

	/**
	 * Retrieves a subset of the User Access Information resource block from the directory. Applications
	 * will only be able to retrieve directory information on themselves.
	 * 
	 * @param fields a filter object to specify which information fields are required
	 * @return an entity object containing the user access information
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public AccessInfo getAccessInfo(AccessInfo.Fields[] fields) throws BlueviaException, IOException {
		return getAccessInfo(null, fields);
	}
	
	/**
	 * Retrieves the whole User Terminal Information resource block from the directory. Applications
	 * will only be able to retrieve directory information on themselves.
	 * 
	 * @return an entity object containing the user terminal information
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public TerminalInfo getTerminalInfo() throws BlueviaException, IOException {
		return getTerminalInfo(null);
	}

	/**
	 * Retrieves a subset of the User Terminal Information resource block from the directory. Applications
	 * will only be able to retrieve directory information on themselves.
	 * 
	 * @param fields a filter object to specify which information fields are required
	 * @return an entity object containing the user terminal information
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public TerminalInfo getTerminalInfo(TerminalInfo.Fields[] fields) throws BlueviaException, IOException {
		return getTerminalInfo(null, fields);
	}

	/**
	 * Retrieves a subset of the User Personal Information resource block from the directory. Applications
	 * will only be able to retrieve directory information on themselves.
	 * 
	 * @return an entity object containing the user personal information
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public PersonalInfo getPersonalInfo() throws BlueviaException, IOException {
		return getPersonalInfo(null);
	}

	/**
	 * Retrieves a subset of the User Personal Information resource block from the directory. Applications
	 * will only be able to retrieve directory information on themselves.
	 * 
	 * @param filter a filter object to specify which information fields are required
	 * @return an entity object containing the user terminal information
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public PersonalInfo getPersonalInfo(PersonalInfo.Fields[] fields) throws BlueviaException, IOException {
		return getPersonalInfo(null, fields);
	}
	
}
