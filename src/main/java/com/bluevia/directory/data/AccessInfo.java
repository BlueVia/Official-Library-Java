package com.bluevia.directory.data;

import com.bluevia.commons.Entity;

/**
 * 
 * Class to hold the information of the User Access Information block resource from the gSDP
 * This type is composed of the following fields:
 * <ul>
 *  <li>Access Type: it indicates the access network used to get connected. Possible values are:
 *  GSM, GPRS, UMTS, HSPA, LTE, WIMAX, etc.</li>
 *  <li> APN: Access Point Name. </li>
 *  <li> Roaming: It indicates if the user is attached to an access network different from its home network.</li>
 * </ul>
 *
 * This implementation is not synchronized
 * 
 * @author Telefonica R&D
 *
 */
public class AccessInfo implements Entity {

	public enum Fields { ACCESS_TYPE, APN, ROAMING };

	private String mAccessType;
	private String mAPN;
	private Boolean mRoaming;

	/**
	 * Gets the access type property
	 * 
	 * @return the access type property
	 */
	public String getAccessType() {
		return mAccessType;
	}

	/**
	 * Sets the accessType property
	 * 
	 * @param accessType the accessType property to set
	 */
	public void setAccessType(String accessType) {
		this.mAccessType = accessType;
	}

	/**
	 * Gets the access point name property
	 * 
	 * @return the access point name property
	 */
	public String getAPN() {
		return mAPN;
	}

	/**
	 * Sets the apn property	
	 * 
	 * @param aPN the apn property to set
	 */
	public void setAPN(String aPN) {
		mAPN = aPN;
	}

	/**
	 * Gets the roaming property
	 * 
	 * @return the roaming property
	 */
	public Boolean getRoaming() {
		return mRoaming;
	}

	/**
	 * Sets the roaming property	
	 * 
	 * @param roaming the roaming property to set
	 */
	public void setRoaming(boolean roaming) {
		this.mRoaming = roaming;
	}

	public boolean isValid() {
		return true;
	}

	/**
	 * Returns a string representation of the values in this class
	 * @return a comma-separated list of the values of the fields in this class
	 */
	public String toString(){
		return "Access type: " + mAccessType + ", " + 
				"APN: " + mAPN + ", " + 
				"Roaming: " + mRoaming;
	}

}
