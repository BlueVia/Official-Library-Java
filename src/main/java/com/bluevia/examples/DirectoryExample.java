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
package com.bluevia.examples;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import com.bluevia.java.AbstractClient.Mode;
import com.bluevia.java.directory.AccessInfo;
import com.bluevia.java.directory.AccessInfoFields;
import com.bluevia.java.directory.Profile;
import com.bluevia.java.directory.ProfileFields;
import com.bluevia.java.directory.TerminalInfo;
import com.bluevia.java.directory.TerminalInfoFields;
import com.bluevia.java.directory.UserInfo;
import com.bluevia.java.directory.UserSearchInfoCategories;
import com.bluevia.java.exception.BlueviaException;
import com.bluevia.java.oauth.OAuthToken;
import com.telefonica.schemas.unica.rest.directory.v1.UserAccessInfoType;
import com.telefonica.schemas.unica.rest.directory.v1.UserInfoType;
import com.telefonica.schemas.unica.rest.directory.v1.UserProfileType;
import com.telefonica.schemas.unica.rest.directory.v1.UserTerminalInfoType;

/**
 * Directory API Class Examples
 * 
 * @package com.bluevia.examples
 */
public class DirectoryExample {
    
	// API path (Mode Live/Sandbox)
    public static Mode mode = Mode.SANDBOX;

    // CREDENTIALS: 
    // User must DEFINE VALID VALUES FOR consumer token & access token
    
    // Consumer Key - Consumer Token
    public static OAuthToken consumer = new OAuthToken("vw12012654505986", "WpOl66570544");
    
    // Access Token - Access Token Secret
    public static OAuthToken accesstoken = new OAuthToken("ad3f0f598ffbc660fbad9035122eae74", "4340b28da39ec36acb4a205d3955a853");

    public static void main(String[] args) {

    	getUserInfo();
    	//getUserProfile();
    	//getUserAccessInfo();
    	//getUserTerminalInfo();
    }

    /**
     * Directory API - Get User Info example
     * 
     */
    public static void getUserInfo() {

        try {
            System.out.println("***** AppTestDirectory getUserInfo");

            UserInfo ui = new UserInfo(consumer, accesstoken, mode);

            /*
             * CALL with other filters UserSearchInfoCategories[] dataSets = new
             * UserSearchInfoCategories[] { 
             * UserSearchInfoCategories.AccessInfo,
             * UserSearchInfoCategories.TerminalInfo };
             */
            UserSearchInfoCategories[] dataSets = new UserSearchInfoCategories[] { UserSearchInfoCategories.AccessInfo, UserSearchInfoCategories.Profile };

            UserInfoType utt = ui.get(dataSets);

            /*
             * Also you can define hashMap HashMap<String, String> hashMap = new
             * HashMap<String, String>(); * hashMap.put("alias",
             * "9fed3d081d3ca2fa35464746579daf53"); * UserInfoType utt =
             * ui.get(hashMap, dataSets);
             */

            if (utt != null) {
                System.out.println(" --- UserInfoType --- ");
                if (utt.getUserAccessInfo() != null) {
                    System.out.println("UserAccessInfo");
                    System.out.println("     Apn:" + utt.getUserAccessInfo().getValue().getApn().getValue());
                }
                if (utt.getUserProfile() != null) {
                    System.out.println("UserProfile");
                }
                if (utt.getUserTerminalInfo() != null) {
                    System.out.println("UserTerminalInfo");
                }

            }

        } catch (BlueviaException ex) {
            Logger.getLogger(DirectoryExample.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JAXBException ex) {
            Logger.getLogger(DirectoryExample.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

  

   

    /**
     * Directory API - Get User Profile example
     * 
     * @param
     * @return
     * @throws
     */
    public static void getUserProfile() {

        try {

            System.out.println("***** AppTestDirectory getUserProfile");

            Profile ui = new Profile(consumer, accesstoken, mode);

            ProfileFields[] dataSets = new ProfileFields[] { ProfileFields.parentalControl, ProfileFields.operatorId };

            UserProfileType utt = ui.get(dataSets);

            if (utt != null) {
                System.out.println(" --- UserProfileType --- ");

                System.out.println("     Ocb:" + utt.getOcb());
                System.out.println("     OperatorId:" + utt.getOperatorId());

            }

        } catch (BlueviaException ex) {
            Logger.getLogger(DirectoryExample.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JAXBException ex) {
            Logger.getLogger(DirectoryExample.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Directory API - Get User AccessInfo example
     * 
     * @param
     * @return
     * @throws
     */
    public static void getUserAccessInfo() {

        try {

            System.out.println("***** AppTestDirectory getUserAccessInfo");

            AccessInfo ui = new AccessInfo(consumer, accesstoken, mode);

            AccessInfoFields[] dataSets = new AccessInfoFields[] { AccessInfoFields.apn };

            UserAccessInfoType utt = ui.get(dataSets);

            if (utt != null) {
                System.out.println(" --- UserAccessInfoType --- ");
                System.out.println("     Apn:" + utt.getApn().getValue());
            }

        } catch (BlueviaException ex) {
            Logger.getLogger(DirectoryExample.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JAXBException ex) {
            Logger.getLogger(DirectoryExample.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Directory API - Get User TerminalInfo example
     * 
     * @param
     * @return
     * @throws
     */
    public static void getUserTerminalInfo() {

        try {

            System.out.println("***** AppTestDirectory getUserTerminalInfo");

            TerminalInfo ui = new TerminalInfo(consumer, accesstoken, mode);

            
            TerminalInfoFields[] dataSets = new TerminalInfoFields[] { TerminalInfoFields.brand, TerminalInfoFields.mms };
            
            UserTerminalInfoType utt = ui.get(dataSets);

            if (utt != null) {
                System.out.println(" --- UserTerminalInfoType --- ");

                System.out.println("     Brand:" + utt.getBrand().getValue());

            }

        } catch (BlueviaException ex) {
            Logger.getLogger(DirectoryExample.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JAXBException ex) {
            Logger.getLogger(DirectoryExample.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
