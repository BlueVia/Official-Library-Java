package com.bluevia.messagery.mo.data;

import java.util.ArrayList;

import com.bluevia.commons.Entity;
import com.bluevia.commons.data.UserId;
import com.bluevia.messagery.data.AbstractMessage;

/**
 * 
 * Class representing the AbstractReceivedMessage that will be received using the SMS/MMS Client API
 * This type is composed of the following fields:
 * <ul>
 * <li>Message: the basic info of the message.</li>
 * <li>Date</li>
 * </ul>
 *
 * This implementation is not synchronized
 *
 * @author Telefonica R&D
 *
 */
public abstract class AbstractReceivedMessage implements Entity {

	protected String mDate;
    protected AbstractMessage mMessage;
    
	/**
	 * 
	 * @param date
	 */
	public void setDate(String date) {
		mDate = date;
	}
	
	/**
	 * 
	 * @return the date-time stamp
	 */
	public String getDate()	{
		return mDate;
	}
	
	/**
     * Gets the origin address.
     *
     * @return the origin address
     */
    public String getOriginAddress() {
    	if (mMessage.getOriginAddress() != null)
    		return mMessage.getOriginAddress().getUserIdValue();
    	return null;
    }
    
    /**
     * Sets the origin address.
     *
     * @param originAddress the origin address to set
     */
    public void setUserIdOriginAddress(UserId originAddress) {
        mMessage.setOriginAddress(originAddress);
    }
    
    /**
     * Gets the destination
     *
     * @return the destination
     */
    public ArrayList<String> getDestinationList(){
    	ArrayList<String> destination = new ArrayList<String>();
    	
    	for (UserId user : mMessage.getAddressList()){
    		destination.add(user.getUserIdValue());
    	}
    	
    	return destination;
    }
    
    /**
     * Adds the address.
     *
     * @param address the address for this message
     */
    public void addAddress(UserId address) {
    	mMessage.getAddressList().add(address);
    }

	public boolean isValid(){
		if (mMessage.getAddressList() == null || mMessage.getAddressList().isEmpty())
			return false;
		for (UserId address : mMessage.getAddressList())
			if (address == null || !address.isValid())
				return false;
		if (mMessage == null || mMessage.getOriginAddress() == null)
			return false;
		else return mMessage.getOriginAddress().isValid();
	}

}
