/**
 * \package com.bluevia.commons.data This package contains basic classes for common subtypes used by other entity data of the Bluevia APIs.
 */
package com.bluevia.commons.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.bluevia.commons.Entity;
import com.bluevia.commons.Utils;
import com.bluevia.commons.parser.xml.XmlConstants;
import com.telefonica.schemas.unica.rest.common.v1.IpAddressType;
import com.telefonica.schemas.unica.rest.common.v1.OtherIdType;
import com.telefonica.schemas.unica.rest.common.v1.UserIdType;


/**
 * Class representing UserId identification
 * This class may be used to identify the user on the request.
 * For example, when sending an SMS, the recipient of the SMS may be identified using this class
 * There are 6 types of UserIds types:
  * <ul>
 * <li>PhoneNumber. Phone Number value must be set in mUserIdValue
 * <li>Any Uri. URI value must be set in mUserIdValue
 * <li>IPV4. IPV4 value must be set in mUserIdValue
 * <li>IPV6. IPV6 value must be set in mUserIdValue
 * <li>Alias. Alias value must be set in mUserIdValue
 * <li>Other Id. Id name must be set at mOtherType and Id value value must be set in mUserIdValue
 * </ul>
 *
 * This implementation is not synchronized
 *
 * @author Telefonica R&D
 * 
 *
 */

public class UserId implements Entity {

    /**
     * Enum Type to indicate the type of the value that identifies the user.
     * Available types are:
     * <ul>
     * 	<li> PHONE_NUMBER: a Telephone number.</li>
     * 	<li> ANY_URI: a Uri</li>
     * 	<li> IPV4_ADDRESS: an IP 4 address</li>
     * 	<li> IPV6_ADDRESS: an IP 6 address</li>
     * 	<li> ALIAS: an arbitrary alias</li>
     * 	<li> OTHER_ID: another identity</li>
     * <ul>
     * @author Telefonica R&D
     * 
     */
    public static enum Type {
        PHONE_NUMBER,
        ANY_URI,
        IPV4_ADDRESS,
        IPV6_ADDRESS,
        ALIAS,
        OTHER_ID,
    }

    /// @cond private
    
    private static Logger log = Logger.getLogger(UserId.class.getName());
    
    private Type mType = null;
    private String mUserIdValue = null;
    private String mOtherType = null;

    ///@endcond

    /**
     * Instantiates a new UserId type.
     */
    public UserId(){
        super();
    }


    /**
     * Instantiates a new UserId type.
     */
    public UserId(Type mType, String mUserIdValue) {
        super();
        this.mType = mType;
        this.mUserIdValue = mUserIdValue;
    }

    /**
     * Instantiates a new UserId type.
     */
    public UserId(UserIdType userIdType){
        super();
        
        if (!(Utils.isEmpty(userIdType.getPhoneNumber()))) {
        	mType= Type.PHONE_NUMBER;
            mUserIdValue = userIdType.getPhoneNumber();
       }
        if (!(Utils.isEmpty(userIdType.getAlias()))) {
        	mType= Type.ALIAS;
            mUserIdValue = userIdType.getAlias();
       }
        if (!(Utils.isEmpty(userIdType.getAnyUri()))) {
        	mType= Type.ANY_URI;
            mUserIdValue = userIdType.getAnyUri();
       }
        if ((null!=(userIdType.getIpAddress()))) {
	       if (!(Utils.isEmpty(userIdType.getIpAddress().getIpv4()))) {
	        	mType= Type.IPV4_ADDRESS;
	            mUserIdValue = userIdType.getIpAddress().getIpv4();
	       }
	        if (!(Utils.isEmpty(userIdType.getIpAddress().getIpv6()))) {
	        	mType= Type.IPV6_ADDRESS;
	            mUserIdValue = userIdType.getIpAddress().getIpv6();
	       }
        }
        if ((null!=(userIdType.getOtherId()))) {
	        if (!(Utils.isEmpty(userIdType.getOtherId().getValue()))) {
	        	mType= Type.OTHER_ID;
	            mUserIdValue = userIdType.getOtherId().getValue();
	            mOtherType= userIdType.getOtherId().getType();
	        }
        }
    }


    /**
     * Gets the UserId Type
     * @return the UserId type
     */
    public Type getType() {
        return mType;
    }


    /**
     * Sets the Userid type
     * @param type the type of the UserId
     */
    public void setType(Type type) {
        this.mType = type;
    }


    /**
     * Gets the UserId value for the type set
     * @return the UserId value
     */
    public String getUserIdValue() {
        return mUserIdValue;
    }


    /**
     * Sets the UserId value for the type set
     * @param userIdValue the UserId value
     */
    public void setUserIdValue(String userIdValue) {
        this.mUserIdValue = userIdValue;
    }


    /**
     * When the type use id "Other Type", The Type value must be set using this method
     * This method gets the value of the other type name.
     * @return the Other Type name
     */
    public String getOtherType() {
        return mOtherType;
    }

    /**
     * When the type use id "Other Type", The Type value must be set using this method
     *  * This method sets the other type name.
     * @param otherType
     */
    public void setOtherType(String otherType) {
        this.mOtherType = otherType;
    }

    public boolean isValid() {

        if (mType == null)
            return false;


        switch (mType) {
            case PHONE_NUMBER:
            	log.debug("validando phonenumber");
                return validateAsPhoneNumber();
            case IPV4_ADDRESS:
                return validateAsIPv4();
            case IPV6_ADDRESS:
                return validateAsIPv6();
            case ANY_URI:
                return validateAnyURI();
            case ALIAS:
                return validateAlias();
            case OTHER_ID:
                return validateOtherType();
        }
        return false;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof UserId && o != null) {
            UserId userId = (UserId) o;

            return (userId.getType().equals(this.mType)
                    && userId.mUserIdValue.equals(this.mUserIdValue)
                    && (this.mType == Type.OTHER_ID ? userId.getOtherType().equals(mOtherType) : true));
        }
        return super.equals(o);
    }
    
    @Override
    public int hashCode(){
    	final int PRIME = 31;
    	int hash = 1;
    	hash = PRIME * hash + mType.ordinal();
    	hash = PRIME * hash + mUserIdValue.hashCode();
    	if (mType == Type.OTHER_ID && mOtherType != null)
    		hash = PRIME * hash + mOtherType.hashCode();
    	return hash;
    }

    /**
     * Returns the corresponding String representation for each UserId type.
     * 
     * @return the corresponding String representation for each UserId type.
     */
    public String getStringType(){
    	switch(mType){
    	case ALIAS:
    		return XmlConstants.XSD_USERIDTYPE_ALIAS;
    	case ANY_URI: 
    		return XmlConstants.XSD_USERIDTYPE_ANYURI;
    	case IPV4_ADDRESS:
    		return XmlConstants.XSD_USERIDTYPE_IPADDRESS_IPV4;
    	case IPV6_ADDRESS:
    		return XmlConstants.XSD_USERIDTYPE_IPADDRESS_IPV6;
    	case PHONE_NUMBER:
   			return XmlConstants.XSD_USERIDTYPE_PHONENUMBER;
    	case OTHER_ID:
    		return XmlConstants.XSD_USERIDTYPE_OTHERTYPE;
    	}
    	return null;
    }
    
    public UserIdType toUserIdType() {
    	UserIdType userIdType = new UserIdType();
    	switch(mType){
    	case ALIAS:
        	userIdType.setAlias(this.getUserIdValue());
        	break;
    	case ANY_URI: 
    		userIdType.setAnyUri(this.getUserIdValue());
        	break;
        case IPV4_ADDRESS:
    		IpAddressType ip4= new IpAddressType();
    		ip4.setIpv4(this.getUserIdValue());
    		userIdType.setIpAddress(ip4);
        	break;
    	case IPV6_ADDRESS:
    		IpAddressType ip6= new IpAddressType();
    		ip6.setIpv6(this.getUserIdValue());
    		userIdType.setIpAddress(ip6);
        	break;
    	case PHONE_NUMBER:
    		userIdType.setPhoneNumber(this.getUserIdValue());
        	break;
    	case OTHER_ID:
    		OtherIdType other= new OtherIdType();
    		other.setType(this.getOtherType());
    		other.setValue(this.getUserIdValue());
    		userIdType.setOtherId(other);
        	break;
    	}
    	return (userIdType);
    }
    
    /// @cond private
    
    private boolean validateAsPhoneNumber() {
        boolean isValid = false;
        log.debug("Starting validateAsPhoneNumber");
        if (mUserIdValue != null) {
            // Expression to validate XSD pattern <xsd:pattern value="[0-9]+"/> applied to
            // UseridType phoneNumber (E164 Type)
            // A phone number, is a maximum of 15 digits following the E164 format
            String expression ="\\d{1,15}$";
            Pattern pattern = Pattern.compile(expression);
            Matcher matcher = pattern.matcher(mUserIdValue);
            if(matcher.matches()){
                isValid = true;
            } else {
                log.error("Phone Number Address " + mUserIdValue + " does not follow phone pattern");
            }
        } else {
            log.error("Phone Number Address must not be null");
        }
        log.debug("ValidateAsPhonenumber: " + isValid);
        return isValid;
    }

    private boolean validateAsIPv4() {
        boolean isValid = false;

        // We use the BlueVia expression to validate IPv4 addresses, although is wrong
        // TODO Let see if BlueVia mantain the expression or finds a better one
        String expression = "((0|(1[0-9]{0,2})|(2(([0-4][0-9]?)|(5[0-5]?)|([6-9]?)))|([3-9][0-9]?))\\.){3}(0|(1[0-9]{0,2})|(2(([0-4][0-9]?)|(5[0-5]?)|([6-9]?)))|([3-9][0-9]?))$";

        // Other better regex used on Internet to validate ipv4 address
        //String expression ="((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]\\d|\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]\\d|\\d)$";

        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(mUserIdValue);
        if(matcher.matches()){
            isValid = true;
        } else {
            log.error("IPv4 Address " + mUserIdValue + " does not follow IPv4 pattern");
        }
        return isValid;
    }


   /*
    * This is the best way to validate all IPv4 address because using a good pattern is really difficult
    * However, the BlueVia XML use a "wrong" pattern to validate IPv4 address. We are using this pattern for
    * compatibility, because if we use other pattern they can check the XML and say it is wrong
    private boolean validateAsIPv4(String mUserIdValue) {
        try {
            java.net.Inet4Address.getByName(mUserIdValue);
            return true;
        } catch(Exception e) {
            return false;
        }
    }*/


    private boolean validateAsIPv6() {
        boolean isValid = false;


        // We use the BlueVia expression to validate IPv6 addresses, although is wrong
        // TODO Let see if BlueVia mantain the expression or finds a better one
        // Valid addresses: 1080:0:0:0:8:800:200C:417A 1080::8:800:200C:417A ::FFFF:129.144.52.38 ::129.144.52.38 "::FFFF:d",
        // Invalid addresses ::FFFF:d.d.d  ::FFFF:d.d  ::d.d.d ::d.d

        String expression = "(([0-9a-fA-F]+:){7}[0-9a-fA-F]+)|(([0-9a-fA-F]+:)*[0-9a-fA-F]+)?::(([0-9a-fA-F]+:)*[0-9a-fA-F]+)?";
        // Other regex used on Internet to validate ipv6 address
        //String expression = "\\A(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}\\z";

        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(mUserIdValue);
        if(matcher.matches()){
            isValid = true;
        } else {
            log.error("IPv6 Address " + mUserIdValue + " does not follow IPv6 pattern");
        }
        return isValid;
    }


    /*
     * This is the best way to validate all IPv6 address because using a good pattern is really difficult
     * However, the BlueVia XML use a "wrong" pattern to validate IPv6 address. We are using this pattern for
     * compatibility, because if we use other pattern they can check the XML and say it is wrong
    private boolean validateAsIPv6() {
        try {
            java.net.Inet6Address.getByName(mUserIdValue);
            return true;
        } catch(Exception e) {
            return false;
        }
    }*/



    private boolean validateAnyURI () {

        //URI validation is done using Exceptions because using a pattern is difficult
        // However there may be problems with encoded non ASCII characters, because these special characters does not need to be escaped
        // in XML while in java URI they need to be escaped

        try {
          java.net.URI.create (mUserIdValue);
        } catch (Exception ex) {
           return false;
        }
        return true;

     }


    private boolean validateAlias () {
        return ( (mUserIdValue != null) && (mUserIdValue.length() > 0));
    }

    private boolean validateOtherType () {
        return ( (mOtherType != null) && (mOtherType.length() > 0) &&
                 (mUserIdValue != null) && (mUserIdValue.length() > 0) );
    }

    ///@endcond

}
