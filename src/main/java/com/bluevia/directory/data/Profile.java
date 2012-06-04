package com.bluevia.directory.data;

import com.bluevia.commons.Entity;

/**
 * 
 * Class to hold the information of the User Profile block resource from the gSDP
 * <ul>
 * <li>User Type: it indicates the billing conditions of the user (pre-paid, post-paid, corporate,
 * etc.)
 * <li>ICB: Incoming Communication Barring</li>
 * <li>OCB: Outgoing Communication Barring</li>
 * <li>Language: language, provisioned in the HLR (Home Location Register)</li>
 * <li>Parental Control: it indicates if the parental control is activated and the
 * associated control level. If it is activated, it will be necessary to check the age (e.g. using
 * the information from user profile or through other mean), but it is out of scope for this
 * API interface.
 * <li>Operator ID: It indicates the operator the user belongs to. The list of possible Operator
 * IDs is offered in described in [5].
 * <li>MMS Status: it indicates if the reception of MMS messages is activated or not.</li>
 * <li>Segment: Class the user belongs to in a social/age/geographical classification.</li>
 * </ul>
 *
 * @author Telefonica R&D
 * 
 */
public class Profile implements Entity {
	
	public enum Fields { USER_TYPE, ICB, OCB, LANGUAGE, PARENTAL_CONTROL, OPERATOR_ID, MMS_STATUS, SEGMENT };

	private String mUserType;
	private String mICB;
	private String mOCB;
	private String mLanguage;
	private String mParentalControl;
	private String mOperatorId;
	private Boolean mMmsStatus;
	private String mSegment;

	/**
	 * Gets the user type property
	 * 
	 * @return the mUserType property
	 */
	public String getUserType() {
		return mUserType;
	}


	/**
	 * Sets the user type property
	 * 
	 * @param userType the mUserType property value to set
	 */
	public void setUserType(String userType) {
		this.mUserType = userType;
	}


	/**
	 * Gets the iCB property
	 * 
	 * @return the iCB property
	 */
	public String getICB() {
		return mICB;
	}

	/**
	 * Sets the iCB property
	 * 
	 * @param iCB the iCB property value to set
	 */
	public void setICB(String iCB) {
		mICB = iCB;
	}

	/**
	 * Gets the oCB property
	 * 
	 * @return the oCB property
	 */
	public String getOCB() {
		return mOCB;
	}

	/**
	 * Sets the oCB property
	 * 
	 * @param oCB the oCB property value to set
	 */
	public void setOCB(String oCB) {
		mOCB = oCB;
	}

	/**
	 * Gets the lang property
	 * 
	 * @return the lang property
	 */
	public String getLang() {
		return mLanguage;
	}

	/**
	 * Sets the lang property
	 * 
	 * @param lang the lang property value to set
	 */
	public void setLang(String lang) {
		this.mLanguage = lang;
	}

	/**
	 * Gets the parental control property
	 * 
	 * @return the parentalControl property
	 */
	public String getParentalControl() {
		return mParentalControl;
	}

	/**
	 * Sets the parental control property
	 * 
	 * @param parentalControl the parentalControl property value to set
	 */
	public void setParentalControl(String parentalControl) {
		this.mParentalControl = parentalControl;
	}


	/**
	 * Gets the operator Id property
	 * 
	 * @return the operatorId property
	 */
	public String getOperatorId() {
		return mOperatorId;
	}

	/**
	 * Sets the operator Id property
	 * 
	 * @param operatorId the operatorId property value to set
	 */
	public void setOperatorId(String operatorId) {
		this.mOperatorId = operatorId;
	}

	/**
	 * Gets the mms status property
	 * 
	 * @return the mmsStatus property
	 */
	public Boolean getMmsStatus() {
		return mMmsStatus;
	}


	/**
	 * Sets the mms status property
	 * 
	 * @param mmsStatus the mmsStatus property value to set
	 */
	public void setMmsStatus(boolean mmsStatus) {
		this.mMmsStatus = mmsStatus;
	}


	/**
	 * Gets the segment property
	 * 
	 * @return the segment property
	 */
	public String getSegment() {
		return mSegment;
	}


	/**
	 * Sets the segment property
	 * 
	 * @param segment the segment property value to set
	 */
	public void setSegment(String segment) {
		this.mSegment = segment;
	}

	public boolean isValid() {
		return true;
	}

	/**
	 * Returns a string representation of the values in this class
	 * @return a comma-separated list of the values of the fields in this class
	 */
	public String toString(){
		return "User type: " + mUserType + ", " + 
				"ICB: " + mICB + ", " + 
				"OCB: " + mOCB + ", " + 
				"Languaje: " + mLanguage + ", " + 
				"Parental control: " + mParentalControl + ", " +
				"Operator id: " + mOperatorId + ", " +
				" MMS status: " + mMmsStatus + ", " + 
				"Segment: " + mSegment;
	}

}
