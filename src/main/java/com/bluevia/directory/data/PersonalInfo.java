package com.bluevia.directory.data;

import com.bluevia.commons.Entity;


/**
 * 
 * Class to hold the information of the User Personal Information block resource from the gSDP
 * This type is composed of the following fields:
 * <ul>
 *  <li>Gender: the gender of the user</li>
 * </ul>
 *
 * This implementation is not synchronized
 * 
 * @author Telefonica R&D
 *
 */
public class PersonalInfo implements Entity {
	
	public enum Fields { GENDER };

	private String mGender;
	
	/**
	 * @return the gender
	 */
	public String getGender() {
		return mGender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.mGender = gender;
	}

	@Override
	public boolean isValid() {
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Gender: " + mGender;
	}
	
	

}
