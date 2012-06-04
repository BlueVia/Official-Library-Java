/**
 * \package com.bluevia.directory This package contains the classes in order to get Directory User Info using Bluevia API.
 * \package com.bluevia.directory.client This package contains REST client to get Directory User Info using Bluevia API.
 */
package com.bluevia.directory.client;

import java.io.IOException;
import java.util.HashMap;

import com.bluevia.commons.Encoding;
import com.bluevia.commons.Entity;
import com.bluevia.commons.client.BVBaseClient;
import com.bluevia.commons.connector.http.oauth.IOAuth;
import com.bluevia.commons.data.JaxbEntity;
import com.bluevia.commons.data.UserId;
import com.bluevia.commons.data.UserId.Type;
import com.bluevia.commons.exception.BlueviaException;
import com.bluevia.commons.parser.xml.XmlConstants;
import com.bluevia.commons.parser.xml.XmlParser;
import com.bluevia.directory.data.AccessInfo;
import com.bluevia.directory.data.FilterUtils;
import com.bluevia.directory.data.PersonalInfo;
import com.bluevia.directory.data.Profile;
import com.bluevia.directory.data.TerminalInfo;
import com.bluevia.directory.data.UserInfo;
import com.telefonica.schemas.unica.rest.common.v1.FlagType;
import com.telefonica.schemas.unica.rest.directory.v1.MmsStatusType;
import com.telefonica.schemas.unica.rest.directory.v1.UserAccessInfoType;
import com.telefonica.schemas.unica.rest.directory.v1.UserInfoType;
import com.telefonica.schemas.unica.rest.directory.v1.UserPersonalInfoType;
import com.telefonica.schemas.unica.rest.directory.v1.UserProfileType;
import com.telefonica.schemas.unica.rest.directory.v1.UserTerminalInfoType;

/**
 * 
 * Client interface for the REST binding of the Bluevia Directory Service.
 * 
 * @author Telefonica R&D
 *
 */
public abstract class BVDirectoryClient extends BVBaseClient {

	/**
	 * Data set key used to add the filters to UserInfo requets
	 */
	private static final String DATASETS_KEY = "dataSets";

	/**
	 * Filter Key used to add the filters on the query get request
	 */
	private static final String FILTER_KEY = "fields";

	private static final String USER_INFO_FEED_PATH = "/UserInfo";
	private static final String PERSONAL_INFO_FEED = USER_INFO_FEED_PATH + "/UserPersonalInfo";
	private static final String USER_PROFILE_FEED = USER_INFO_FEED_PATH + "/UserProfile";
	private static final String ACCESS_INFO_FEED = USER_INFO_FEED_PATH + "/UserAccessInfo";
	private static final String TERMINAL_INFO_FEED = USER_INFO_FEED_PATH + "/UserTerminalInfo";

	protected void init(){
		mEncoding = Encoding.APPLICATION_XML;
		mParser = new XmlParser();
		mSerializer = null;		//No serializer
	}

	/**
	 * Allows an application to get all the user context information.
	 * @param user the user whose information is going to be retrieved
	 * @param dataSet an array of the blocks to be retrieved
	 * 
	 * @return an entity containing all blocks of user context information.
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	protected UserInfo getUserInfo(UserId user, UserInfo.DataSet[] dataSet) throws BlueviaException, IOException {

		//UserId
		UserId userId = null;

		if (user == null)
			userId = new UserId(Type.ALIAS, ((IOAuth)mConnector).getOauthToken().getToken());
		else userId = user;

		if (dataSet != null && dataSet.length < 2)
			throw new BlueviaException("Bad request: data set must contain two elements at least.", BlueviaException.BAD_REQUEST_EXCEPTION);

		//Build the status feed uri
		String feedUri = "/" + userId.getStringType() + ":" +  userId.getUserIdValue() + USER_INFO_FEED_PATH;

		// Add version parameter
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);

		if (dataSet != null){
			parameters.put(DATASETS_KEY, FilterUtils.buildUserInfoFilter(dataSet));
		}

		// Set parse class
		((XmlParser) mParser).setParseClass(UserInfoType.class);
		
		Entity response = retrieve(feedUri, parameters);

		//Check if response is instance of JaxbEntity
		if (response == null || !(response instanceof JaxbEntity))
			throw new BlueviaException("Error during request. Response received does not correspond to a " + JaxbEntity.class.getName(),
					BlueviaException.INTERNAL_CLIENT_ERROR);

		JaxbEntity parseEntity = (JaxbEntity) response;

		if (parseEntity.getObject() == null || !(parseEntity.getObject() instanceof UserInfoType))
			throw new BlueviaException("Error during request. Response received does not correspond to an " + UserInfoType.class.getName(),
					BlueviaException.INTERNAL_CLIENT_ERROR);

		UserInfoType userInfoType = (UserInfoType) parseEntity.getObject(); 

		//Set the response
		UserInfo userInfo = fromUserInfoTypeToUserInfo(userInfoType);

		if (!userInfo.isValid()){
			throw new BlueviaException("The " + UserInfo.class.getName() + 
					" received is empty, incomplete or not valid", BlueviaException.INTERNAL_CLIENT_ERROR);
		}

		return userInfo;
	}

	/**
	 * Retrieves a subset of the User Profile resource block from the directory. 
	 * 
	 * @param user the user whose information is going to be retrieved
	 * @param fields a filter object to specify which information fields are required
	 * @return an entity object containing the user personal information
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	protected Profile getProfile(UserId user, Profile.Fields[] fields) throws BlueviaException, IOException {

		//UserId
		UserId userId = null;

		if (user == null)
			userId = new UserId(Type.ALIAS, ((IOAuth)mConnector).getOauthToken().getToken());
		else userId = user;

		//Build the status feed uri
		String feedUri = "/" + userId.getStringType() + ":" +  userId.getUserIdValue() + USER_PROFILE_FEED;

		// Add version parameter
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);

		if (fields != null){
			parameters.put(FILTER_KEY, "'" + FilterUtils.buildProfileFilter(fields) + "'");
		}
		
		// Set parse class
		((XmlParser) mParser).setParseClass(UserProfileType.class);

		Entity response = retrieve(feedUri, parameters);

		//Check if response is instance of JaxbEntity
		if (response == null || !(response instanceof JaxbEntity))
			throw new BlueviaException("Error during request. Response received does not correspond to a " + JaxbEntity.class.getName(),
					BlueviaException.INTERNAL_CLIENT_ERROR);

		JaxbEntity parseEntity = (JaxbEntity) response;

		if (parseEntity.getObject() == null || !(parseEntity.getObject() instanceof UserProfileType))
			throw new BlueviaException("Error during request. Response received does not correspond to an " + UserProfileType.class.getName(),
					BlueviaException.INTERNAL_CLIENT_ERROR);

		UserProfileType userProfileType = (UserProfileType) parseEntity.getObject(); 

		//Set the response
		Profile profile = fromUserProfileTypeToProfile(userProfileType);

		if (!profile.isValid()){
			throw new BlueviaException("The " + Profile.class.getName() + 
					" received is empty, incomplete or not valid", BlueviaException.INTERNAL_CLIENT_ERROR);
		}

		return profile;
	}

	/**
	 * Retrieves a subset of the User Access Information resource block from the directory. 
	 * @param user the user whose information is going to be retrieved
	 * @param fields a filter object to specify which information fields are required
	 * @return an entity object containing the user access information
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	protected AccessInfo getAccessInfo(UserId user, AccessInfo.Fields[] fields) throws BlueviaException, IOException {

		//UserId
		UserId userId = null;

		if (user == null)
			userId = new UserId(Type.ALIAS, ((IOAuth)mConnector).getOauthToken().getToken());
		else userId = user;

		//Build the status feed uri
		String feedAccessInfoUri = "/" + userId.getStringType() + ":" + userId.getUserIdValue() + ACCESS_INFO_FEED;

		// Add version parameter
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);

		if (fields != null){
			parameters.put(FILTER_KEY, "'" + FilterUtils.buildAccessInfoFilter(fields) + "'");
		}
		
		// Set parse class
		((XmlParser) mParser).setParseClass(UserAccessInfoType.class);

		Entity response = retrieve(feedAccessInfoUri, parameters);

		//Check if response is instance of JaxbEntity
		if (response == null || !(response instanceof JaxbEntity))
			throw new BlueviaException("Error during request. Response received does not correspond to a " + JaxbEntity.class.getName(),
					BlueviaException.INTERNAL_CLIENT_ERROR);

		JaxbEntity parseEntity = (JaxbEntity) response;

		if (parseEntity.getObject() == null || !(parseEntity.getObject() instanceof UserAccessInfoType))
			throw new BlueviaException("Error during request. Response received does not correspond to an " + UserAccessInfoType.class.getName(),
					BlueviaException.INTERNAL_CLIENT_ERROR);

		UserAccessInfoType accessInfoType = (UserAccessInfoType) parseEntity.getObject(); 

		//Set the response
		AccessInfo accessInfo = fromUserAccessInfoTypeToAccessInfo(accessInfoType);

		if (!accessInfo.isValid()){
			throw new BlueviaException("The " + AccessInfo.class.getName() + 
					" received is empty, incomplete or not valid", BlueviaException.INTERNAL_CLIENT_ERROR);
		}

		return accessInfo;
	}

	/**
	 * Retrieves a subset of the User Terminal Information resource block from the directory. 
	 * 
	 * @param user the user whose information is going to be retrieved
	 * @param fields a filter object to specify which information fields are required
	 * @return an entity object containing the user terminal information
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	protected TerminalInfo getTerminalInfo(UserId user, TerminalInfo.Fields[] fields) throws BlueviaException, IOException {

		//UserId
		UserId userId = null;

		if (user == null)
			userId = new UserId(Type.ALIAS, ((IOAuth)mConnector).getOauthToken().getToken());
		else userId = user;

		//Build the status feed uri
		String feedUri = "/" + userId.getStringType() + ":" + userId.getUserIdValue() + TERMINAL_INFO_FEED;

		// Add version parameter
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);

		if (fields != null){
			parameters.put(FILTER_KEY, "'" + FilterUtils.buildTerminalInfoFilter(fields) + "'");
		}
		
		// Set parse class
		((XmlParser) mParser).setParseClass(UserTerminalInfoType.class);

		Entity response = retrieve(feedUri, parameters);

		//Check if response is instance of JaxbEntity
		if (response == null || !(response instanceof JaxbEntity))
			throw new BlueviaException("Error during request. Response received does not correspond to a " + JaxbEntity.class.getName(),
					BlueviaException.INTERNAL_CLIENT_ERROR);

		JaxbEntity parseEntity = (JaxbEntity) response;

		if (parseEntity.getObject() == null || !(parseEntity.getObject() instanceof UserTerminalInfoType))
			throw new BlueviaException("Error during request. Response received does not correspond to an " + UserTerminalInfoType.class.getName(),
					BlueviaException.INTERNAL_CLIENT_ERROR);

		UserTerminalInfoType terminalInfoType = (UserTerminalInfoType) parseEntity.getObject(); 

		//Set the response
		TerminalInfo terminalInfo = fromUserTerminalInfoTypeToTerminalInfo(terminalInfoType);

		if (!terminalInfo.isValid()){
			throw new BlueviaException("The " + TerminalInfo.class.getName() + 
					" received is empty, incomplete or not valid", BlueviaException.INTERNAL_CLIENT_ERROR);
		}

		return terminalInfo;
	}

	/**
	 * Retrieves a subset of the User Personal Information resource block from the directory.
	 * 
	 * @param user the user whose information is going to be retrieved
	 * @param filter a filter object to specify which information fields are required
	 * @return an entity object containing the user terminal information
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	protected PersonalInfo getPersonalInfo(UserId user, PersonalInfo.Fields[] fields) throws BlueviaException, IOException {

		//UserId
		UserId userId = null;

		if (user == null)
			userId = new UserId(Type.ALIAS, ((IOAuth)mConnector).getOauthToken().getToken());
		else userId = user;

		//Build the status feed uri
		String feedUri = "/" + userId.getStringType() + ":" + userId.getUserIdValue() + PERSONAL_INFO_FEED;

		// Add version parameter
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);

		if (fields != null){
			parameters.put(FILTER_KEY, "'" + FilterUtils.buildPersonalInfoFilter(fields) + "'");
		}

		// Set parse class
		((XmlParser) mParser).setParseClass(UserPersonalInfoType.class);
		
		Entity response = retrieve(feedUri, parameters);

		//Check if response is instance of JaxbEntity
		if (response == null || !(response instanceof JaxbEntity))
			throw new BlueviaException("Error during request. Response received does not correspond to a " + JaxbEntity.class.getName(),
					BlueviaException.INTERNAL_CLIENT_ERROR);

		JaxbEntity parseEntity = (JaxbEntity) response;

		if (parseEntity.getObject() == null || !(parseEntity.getObject() instanceof UserPersonalInfoType))
			throw new BlueviaException("Error during request. Response received does not correspond to an " + UserPersonalInfoType.class.getName(),
					BlueviaException.INTERNAL_CLIENT_ERROR);

		UserPersonalInfoType personalInfoType = (UserPersonalInfoType) parseEntity.getObject(); 

		//Set the response
		PersonalInfo personalInfo = fromUserPersonalInfoTypeToPersonalInfo(personalInfoType);

		if (!personalInfo.isValid()){
			throw new BlueviaException("The " + PersonalInfo.class.getName() + 
					" received is empty, incomplete or not valid", BlueviaException.INTERNAL_CLIENT_ERROR);
		}

		return personalInfo;
	}
	
	private static boolean fromFlagTypeToBoolean(FlagType flag){
		if (flag == null)
			throw new IllegalArgumentException("FlagType should not be null. This should not happen");
		if (flag == FlagType.YES){
			return true;
		} else return false;
	}

	private AccessInfo fromUserAccessInfoTypeToAccessInfo(UserAccessInfoType in){
		
		if (in == null)
			return null;
		
		AccessInfo accessInfo = new AccessInfo();
		
		if (in.getAccessType() != null)
			accessInfo.setAccessType(in.getAccessType().getValue());
		if (in.getApn() != null)
			accessInfo.setAPN(in.getApn().getValue());
		if (in.getRoaming() != null)
			accessInfo.setRoaming(fromFlagTypeToBoolean(in.getRoaming().getValue()));
		
		return accessInfo;
	}

	private PersonalInfo fromUserPersonalInfoTypeToPersonalInfo(UserPersonalInfoType in){
		
		if (in == null)
			return null;
		
		PersonalInfo personalInfo = new PersonalInfo();
		if (in.getGender() != null)
			personalInfo.setGender(in.getGender().getValue().value());
		
		return personalInfo;
	}

	private TerminalInfo fromUserTerminalInfoTypeToTerminalInfo(UserTerminalInfoType in){
		
		if (in == null)
			return null;
		
		TerminalInfo terminalInfo = new TerminalInfo();
		if (in.getBrand() != null)
			terminalInfo.setBrand(in.getBrand().getValue());
		if (in.getEmn() != null)
			terminalInfo.setEMS(fromFlagTypeToBoolean(in.getEmn().getValue()));
		if (in.getEmsMaxNumber() != null)
			terminalInfo.setEMSmaxNumber(Integer.valueOf(in.getEmsMaxNumber().getValue()).intValue());
		if (in.getMms() != null)
			terminalInfo.setMMS(fromFlagTypeToBoolean(in.getMms().getValue()));
		if (in.getMmsVideo() != null)
			terminalInfo.setMmsVideo(fromFlagTypeToBoolean(in.getMmsVideo().getValue()));
		if (in.getModel() != null)
			terminalInfo.setModel(in.getModel().getValue());
		if (in.getScreenResolution() != null)
			terminalInfo.setScreenResolution(in.getScreenResolution().getValue());
		if (in.getSmartMessaging() != null)
			terminalInfo.setSmartMessaging(fromFlagTypeToBoolean(in.getSmartMessaging().getValue()));
		if (in.getUssdPhase() != null)
			terminalInfo.setUSSDPhase(in.getUssdPhase().getValue());
		if (in.getVersion() != null)
			terminalInfo.setVersion(in.getVersion().getValue());
		if (in.getVideoStreaming() != null)
			terminalInfo.setVideoStreaming(fromFlagTypeToBoolean(in.getVideoStreaming().getValue()));
		if (in.getWap() != null)
			terminalInfo.setWAP(fromFlagTypeToBoolean(in.getWap().getValue()));
		if (in.getWapPush() != null)
			terminalInfo.setWapPush(fromFlagTypeToBoolean(in.getWapPush().getValue()));
		
		return terminalInfo;
	}

	private Profile fromUserProfileTypeToProfile(UserProfileType in){
		
		if (in == null)
			return null;
		
		Profile profile = new Profile();
		
		if (in.getIcb() != null)
			profile.setICB(in.getIcb().getValue());
		if (in.getLanguage() != null)
			profile.setLang(in.getLanguage().getValue());
		if (in.getMmsStatus() != null){
			MmsStatusType mmsStatus = in.getMmsStatus().getValue();
			if (mmsStatus == MmsStatusType.ACTIVATED)
				profile.setMmsStatus(true);
			else if (mmsStatus == MmsStatusType.DEACTIVATED)
				profile.setMmsStatus(false);
		}
		if (in.getOcb() != null)
			profile.setOCB(in.getOcb().getValue());
		if (in.getOperatorId() != null)
			profile.setOperatorId(in.getOperatorId().getValue());
		if (in.getParentalControl() != null)
			profile.setParentalControl(in.getParentalControl().getValue());
		if (in.getSegment() != null)
			profile.setSegment(in.getSegment().getValue());
		if (in.getUserType() != null)
			profile.setUserType(in.getUserType().getValue());
		
		return profile;
	}

	private UserInfo fromUserInfoTypeToUserInfo(UserInfoType in){
		
		if (in == null)
			return null;
		
		UserInfo userInfo = new UserInfo();
		
		if (in.getUserAccessInfo() != null)
			userInfo.setAccessInfo(fromUserAccessInfoTypeToAccessInfo(in.getUserAccessInfo().getValue()));
		
		if (in.getUserPersonalInfo() != null)
			userInfo.setPersonalInfo(fromUserPersonalInfoTypeToPersonalInfo(in.getUserPersonalInfo().getValue()));
		
		if (in.getUserProfile() != null)
			userInfo.setProfile(fromUserProfileTypeToProfile(in.getUserProfile().getValue()));
		
		if (in.getUserTerminalInfo() != null)
			userInfo.setTerminalInfo(fromUserTerminalInfoTypeToTerminalInfo(in.getUserTerminalInfo().getValue()));
		
		return userInfo;
	}
}
