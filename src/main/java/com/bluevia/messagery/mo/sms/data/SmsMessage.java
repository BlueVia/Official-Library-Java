package com.bluevia.messagery.mo.sms.data;

import java.util.ArrayList;

import com.bluevia.commons.data.UserId;
import com.bluevia.commons.exception.BlueviaException;
import com.bluevia.messagery.mo.data.AbstractReceivedMessage;
import com.bluevia.messagery.mt.sms.data.SmsMessageReq;


/**
 * 
 * Class representing the SmsMessage elements that will be received in ReceivedSmsList using the SMS Client API
 * This type is composed of the following fields:
 * <ul>
 * <li>mDate; String
 * </ul>
 *
 * This implementation is not synchronized
 *
 * @author Telefonica R&D
 * 
 *
 */
public final class SmsMessage extends AbstractReceivedMessage {

    /**
     * Instantiates a new empty ReceivedSMS message.
     */
    public SmsMessage(){
    	super();
    	mMessage = new SmsMessageReq();
    }
	
	/**
	 * Instantiates a new ReceivedSMS message.
	 * 
	 * @param message The body of the received message 
	 * @param addressList The list of recipients to whom the message will be sent.
     * Allowed UserId instances are those such as:
     * <ul>
     *   <li> UserId type is UserId::PHONE_NUMBER and has a phone number -following the E164 format- as 'userIdValue'.
     * </ul>
     *
     * Any other UserId instance type will cause this function throws an BlueviaException.
	 * @param dateTime The date and time when the message was sent
     * 
	 */
	public SmsMessage(String message, ArrayList<UserId> addressList, String dateTime) throws BlueviaException {
		mMessage = new SmsMessageReq(message, addressList);
		mDate = dateTime;
	}
	
	/**
	 * Instantiates a new ReceivedSMS message.
	 * 
	 * @param message The body of the received message 
	 * @param address The recipient to whom the message will be sent.
     * Allowed UserId instances are those such as:
     * <ul>
     *   <li> UserId type is UserId::PHONE_NUMBER and has a phone number -following the E164 format- as 'userIdValue'.
     * </ul>
     *
     * Any other UserId instance type will cause this function throws an BlueviaException.
	 * @param dateTime The date and time when the message was sent
     * 
	 */
	public SmsMessage(String message, UserId address, String dateTime) throws BlueviaException {
		mMessage = new SmsMessageReq(message, address);
		mDate = dateTime;
	}
	
    /**
     * Sets the message
     *
     * @param message
     */
    public void setMessage(String message) {
        ((SmsMessageReq)mMessage).setMessage(message);
    }

    /**
     * Gets the message.
     *
     * @return the message
     */
    public String getMessage() {
        return ((SmsMessageReq)mMessage).getMessage();
    }
    
	public boolean isValid() {
		return ((SmsMessageReq)mMessage).getMessage() != null && super.isValid();
	}
	
}
