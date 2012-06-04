/**
 * 
 * @category bluevia
 * @package com.bluevia.examples
 * @copyright Copyright (c) 2010 Telefonica I+D (http://www.tid.es)
 * @author Bluevia <support@bluevia.com>
 * 
 * 
 *          BlueVia is a global iniciative of Telefonica delivered by Movistar
 *          and O2. Please, check out http://www.bluevia.com and if you need
 *          more information contact us at support@bluevia.com
 */


import java.io.IOException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.bluevia.commons.client.BVBaseClient.Mode;
import com.bluevia.commons.connector.http.oauth.OAuthToken;
import com.bluevia.commons.exception.BlueviaException;
import com.bluevia.directory.client.BVDirectory;
import com.bluevia.directory.data.AccessInfo;
import com.bluevia.directory.data.PersonalInfo;
import com.bluevia.directory.data.Profile;
import com.bluevia.directory.data.TerminalInfo;
import com.bluevia.directory.data.UserInfo;
import com.bluevia.directory.data.UserInfo.DataSet;



/**
 * Directory API Class Examples
 * 
 * @package com.bluevia.examples
 */
public class DirectoryExample {

	// Logger
	private static Logger log = Logger.getLogger(DirectoryExample.class.getName());

    // API path (Mode Live/Sandbox)
	public static Mode mode = Mode.SANDBOX;

    // CREDENTIALS: 
    // User must DEFINE VALID VALUES FOR consumer token & access token  
    // Consumer Key - Consumer Token
	public static OAuthToken consumerToken = new OAuthToken("vw12012654505986", "WpOl66570544");	
    // Access Key - Access Token
	public static OAuthToken accessToken = new OAuthToken("ad3f0f598ffbc660fbad9035122eae74", "4340b28da39ec36acb4a205d3955a853");

	
	
    public static void main(String[] args) {
    	
    	// Logger
    	BasicConfigurator.configure();

        getUserInfo();
        //getProfile();
        //getAccessInfo();
        //getPersonalInfo();
        //getTerminalInfo();
    }

    /**
     * Directory API - Get UserInfo example
     * 
     */
    public static void getUserInfo() {

 
    	System.out.println("***** Directory getUserInfo");
    	BVDirectory client = null;
    	
        try {
			client = new BVDirectory(mode, 
					consumerToken.getToken(), consumerToken.getSecret(),
					accessToken.getToken(), accessToken.getSecret());
			
			//Select data sets
			UserInfo.DataSet[] dataSet = {DataSet.ACCESS_INFO, DataSet.PERSONAL_INFO};
			
			//Get the UserInfo response
			UserInfo info = client.getUserInfo(dataSet);

			/* Get information and do stuff */
			if (info!=null) {
				AccessInfo accessInfo = info.getAccessInfo();
				TerminalInfo terminalInfo = info.getTerminalInfo();	
				Profile profile = info.getProfile();	

                System.out.println(" --- UserInfoType --- ");
                if (accessInfo != null) {
                    System.out.println("UserAccessInfo");
                    System.out.println("     APN:" + accessInfo.getAPN());
                }
                if (profile != null) {
                    System.out.println("UserProfile");
                }
                if (terminalInfo != null) {
                    System.out.println("UserTerminalInfo");
                }
			}

        } catch (BlueviaException ex) {
        	log.error("BlueviaException:" + ex.getMessage());
 		} catch (IOException e) {
 			log.error("IO Exception" + e.getMessage());
		} finally {
			// Close the client
			if (client != null)	client.close();			
		}

    }

    /**
     * Directory API - Get Profile example
     * 
     */
    public static void getProfile() {

 
    	System.out.println("***** Directory getProfile");
    	BVDirectory client = null;
    	
        try {
			client = new BVDirectory(mode, 
					consumerToken.getToken(), consumerToken.getSecret(),
					accessToken.getToken(), accessToken.getSecret());
			
			//Set the fields
			Profile.Fields[] fields = {com.bluevia.directory.data.Profile.Fields.USER_TYPE, 
									   com.bluevia.directory.data.Profile.Fields.PARENTAL_CONTROL, 
									   com.bluevia.directory.data.Profile.Fields.OPERATOR_ID};
			
			//Get the Profile response
			Profile profile = client.getProfile(fields);

			/* Get information and do stuff */
			if (profile!=null) {
				
                System.out.println(" --- UserProfileType --- ");

                System.out.println("     OCB:" + profile.getOCB());
                System.out.println("     OperatorId:" + profile.getOperatorId());
			}

        } catch (BlueviaException ex) {
        	log.error("BlueviaException:" + ex.getMessage());
 		} catch (IOException e) {
 			log.error("IO Exception" + e.getMessage());
		} finally {
			// Close the client
			if (client != null)	client.close();			
		}

    }

    /**
     * Directory API - Get AccessInfo example
     * 
     */
    public static void getAccessInfo() {

 
    	System.out.println("***** Directory getAccessInfo");
    	BVDirectory client = null;
    	
        try {
			client = new BVDirectory(mode, 
					consumerToken.getToken(), consumerToken.getSecret(),
					accessToken.getToken(), accessToken.getSecret());
			
			//Set the fields
			AccessInfo.Fields[] fields = {com.bluevia.directory.data.AccessInfo.Fields.ACCESS_TYPE, 
										  com.bluevia.directory.data.AccessInfo.Fields.APN};
			
			//Get the AccessInfo response
			AccessInfo accessInfo = client.getAccessInfo(fields);

			/* Get information and do stuff */
            if (accessInfo != null) {
                System.out.println(" --- UserAccessInfoType --- ");
                System.out.println("     APN:" + accessInfo.getAPN());
            }

        } catch (BlueviaException ex) {
        	log.error("BlueviaException:" + ex.getMessage());
 		} catch (IOException e) {
 			log.error("IO Exception" + e.getMessage());
		} finally {
			// Close the client
			if (client != null)	client.close();			
		}

    }

    /**
     * Directory API - Get UserPersonal example
     * 
     */
    public static void gePersonalInfo() {

 
    	System.out.println("***** Directory getPersonalInfo");
    	BVDirectory client = null;
    	
        try {
			client = new BVDirectory(mode, 
					consumerToken.getToken(), consumerToken.getSecret(),
					accessToken.getToken(), accessToken.getSecret());
			
			//Set the fields
			com.bluevia.directory.data.PersonalInfo.Fields[] fields = 
									{com.bluevia.directory.data.PersonalInfo.Fields.GENDER};
			
			//Get the PersonalInfo response
			PersonalInfo personalInfo = client.getPersonalInfo(fields);

			/* Get information and do stuff */
			String gender = personalInfo.getGender();
			if (gender!=null) {

                System.out.println(" --- Personal Info --- ");
                System.out.println("gender:" + gender);
			}

        } catch (BlueviaException ex) {
        	log.error("BlueviaException:" + ex.getMessage());
 		} catch (IOException e) {
 			log.error("IO Exception" + e.getMessage());
		} finally {
			// Close the client
			if (client != null)	client.close();			
		}

    }

    /**
     * Directory API - Get TerminalInfo example
     * 
     */
    public static void getTerminalInfo() {

 
    	System.out.println("***** Directory getTerminalInfo");
    	BVDirectory client = null;
    	
        try {
			client = new BVDirectory(mode, 
					consumerToken.getToken(), consumerToken.getSecret(),
					accessToken.getToken(), accessToken.getSecret());
			
			//Select data sets
            TerminalInfo.Fields[] fields = new TerminalInfo.Fields[] { 
            		com.bluevia.directory.data.TerminalInfo.Fields.BRAND,
            		com.bluevia.directory.data.TerminalInfo.Fields.MMS};
			
			//Get the UserInfo response
			TerminalInfo info = client.getTerminalInfo(fields);

			/* Get information and do stuff */
			if (info!=null) {

                System.out.println(" --- TerminalInfo --- ");
                if (info.getBrand() != null) {
                    System.out.println("Brand: " + info.getBrand());
                }
                System.out.println("MMS: " + info.getMMS());
			}

        } catch (BlueviaException ex) {
        	log.error("BlueviaException:" + ex.getMessage());
 		} catch (IOException e) {
 			log.error("IO Exception" + e.getMessage());
		} finally {
			// Close the client
			if (client != null)	client.close();			
		}

    }


}
