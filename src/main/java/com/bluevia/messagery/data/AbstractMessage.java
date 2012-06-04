/**
 * \package com.bluevia.messagery This package contains the common classes in order to send SMS and MMS using Bluevia API.
 */
package com.bluevia.messagery.data;

import java.util.ArrayList;

import com.bluevia.commons.Entity;
import com.bluevia.commons.data.UserId;
import com.bluevia.commons.data.UserId.Type;

/**
 * Class representing the abstract SMS class that represents the different messages that
 * will be sent using the SMS/MMS Client API
 * This type is composed of the following fields:
 * <ul>
 * <li>addressList: mandatory; max occurrences: minimum 1.
 * <li>originAddress: mandatory; UserId
 * <li>senderName: optional; String
 * 
 * <li>endpoint: optional; String
 * <li>correlator: optional; String
 * </ul>
 *
 * This implementation is not synchronized
 *
 * @author Telefonica R&D
 * 
 */
public abstract class AbstractMessage implements Entity {

    private ArrayList<UserId> mAddressList;
    private UserId mOriginAddress;
    private String mSenderName;
    
    private String mEndpoint;
    private String mCorrelator;
    
    /**
     * Instantiates a new SMS message type.
     */
    public AbstractMessage() {
        super();
        mAddressList = new ArrayList<UserId>();
    }
    
    /**
     * Instantiates a new message with a destination address, and origin address 
     * @param address Destination address
     */
    public AbstractMessage(UserId address) {
        this();
        addAddress (address);
    }
    
    /**
     * Instantiates a new message with a destination address, and origin address 
     * @param address Destination address
     * @param endpoint Endpoint
     * @param correlator Correlator
     */
    public AbstractMessage(UserId address, String endpoint,
    		String correlator) {
        this();
        addAddress (address);
    }
    
    /**
     * Instantiates a new message
     * @param addressList List with the destination addresses
     */
    public AbstractMessage(ArrayList<UserId> addressList) {
        this();
        mAddressList = addressList;
    }

    /**
     * Instantiates a new message
     * @param addressList List with the destination addresses
     * @param endpoint Endpoint
     * @param correlator Correlator
     */
    public AbstractMessage(ArrayList<UserId> addressList, String endpoint,
    		String correlator) {
        this();
        mAddressList = addressList;
        mEndpoint = endpoint;
        mCorrelator = correlator;
    }

    /**
     * Instantiates a new message
     * @param addressList List with the destination addresses
     * @param senderName the senderName
     * @param endpoint Endpoint
     * @param correlator Correlator
     */
    public AbstractMessage(ArrayList<UserId> addressList, String senderName,
    		String endpoint, String correlator) {
        this();
        mAddressList = addressList;
        mSenderName= senderName;
        mEndpoint = endpoint;
        mCorrelator = correlator;
    }
    

    /**
     * Adds the address.
     *
     * @param address the address for this message
     */
    public void addAddress (UserId address) {
        mAddressList.add(address);
    }

    /**
     * Removes the address
     *
     * @param address the address for this message
     * @return
     */
    public boolean removeAddress (UserId address) {
        return mAddressList.remove(address);
    }

    /**
     * Clear addresses.
     */
    public void clearAddressList () {
        mAddressList.clear();
    }


    /**
     * Sets the origin address.
     *
     * @param originAddress the origin address to set
     */
    public void setOriginAddress(UserId originAddress) {
        this.mOriginAddress = originAddress;
    }
    
    /**
     * Gets the addresses.
     *
     * @return the address list
     */
    public ArrayList<UserId> getAddressList () {
        return mAddressList;
    }

    /**
     * Gets the origin address.
     *
     * @return the origin address
     */
    public UserId getOriginAddress() {
        return mOriginAddress;
    }
    
    /**
	 * @return the senderName
	 */
	public String getSenderName() {
		return mSenderName;
	}

	/**
	 * @param endpoint the senderName to set
	 */
	public void setSenderName(String senderName) {
		this.mSenderName = senderName;
	}   
    
    /**
	 * @return the endpoint
	 */
	public String getEndpoint() {
		return mEndpoint;
	}

	/**
	 * @param endpoint the endpoint to set
	 */
	public void setEndpoint(String endpoint) {
		this.mEndpoint = endpoint;
	}

	/**
	 * @return the correlator
	 */
	public String getCorrelator() {
		return mCorrelator;
	}

	/**
	 * @param correlator the correlator to set
	 */
	public void setCorrelator(String correlator) {
		this.mCorrelator = correlator;
	}

    public abstract boolean isValid();

    /**
     * Validates the origin address
     * 
     * @return the result of the validation
     */
    protected boolean validateOriginAddress() {
    	return mOriginAddress != null && 
    			mOriginAddress.isValid();
    }
    
    /**
     * Validates the origin address for received sms
     * (ALIAS and PHONENUMBER allowed)
     * 
     * @return the result of the validation
     */
    protected boolean validateReceivedOriginAddress() {
    	//Notify uri is only used in OneApi, but it is instantiated in constructor
    	return mOriginAddress != null && 
    			(mOriginAddress.getType() == Type.ALIAS || mOriginAddress.getType() == Type.PHONE_NUMBER) && 
    			mOriginAddress.isValid();
    }
    
    /**
     * Validates the destination address list
     * 
     * @return the result of the validation
     */
    protected boolean validateAddressList() {
        if ((mAddressList == null) || (mAddressList.size()<=0))
            return false;

        for (int i=0; i<mAddressList.size(); i++) {
            UserId address = mAddressList.get(i);
            if (address==null || !address.isValid())
                return false;
        }

        return true;
    }

 }
