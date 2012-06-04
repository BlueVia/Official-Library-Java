/**
 * \package com.bluevia.messagery.mt.data This package contains the common data classes in order to send SMS and MMS using Bluevia API.
 */
package com.bluevia.messagery.mt.data;

import com.bluevia.commons.Entity;
import com.bluevia.commons.data.UserId;
import com.bluevia.commons.exception.BlueviaException;

/**
 *
 * Class representing the delivery status of a previous sent message either for SMS or MMS
 *
 * @author Telefonica R&D
 *
 */
public class DeliveryInfo implements Entity {

	/**
	 * 
	 * Enum representing the values of the status of a particular SMS sent
	 * Possible values are:
	 * <ul>
	 * 	<li>DELIVERED_TO_NETWORK: The message was delivered to network</li>
	 * 	<li>DELIVERY_UNCERTAIN: It is not possible to ascertain whether the message was delivered</li>
	 *  <li>DELIVERY_IMPOSSIBLE: It is not possible to deliver the message</li>
	 *  <li>MESSAGE_WAITING: The message is waiting to be delivered</li>
	 *  <li>DELIVERED_TO_TERMINAL: The message was successfully delivered to the recipient terminal</li>
	 *  <li>DELIVERY_NOTIFICATION_NOT_SUPPORTED: Delivery notification is not supported by the network</li>
	 * </ul>
	 * @author Telefonica R&D
	 * 
	 *
	 */
	public static enum Status {
		DELIVERED_TO_NETWORK,
		DELIVERY_UNCERTAIN,
		DELIVERY_IMPOSSIBLE,
		MESSAGE_WAITING,
		DELIVERED_TO_TERMINAL,
		DELIVERY_NOTIFICATION_NOT_SUPPORTED
	}

	private Status mStatus = null;
	private UserId mAddress = null;
	private String mDescription = null;

	/**
	 * 
	 * Instantiates a new SMS status single type.
	 *
	 */
	public DeliveryInfo(){

	}

	/**
	 * 
	 * Instantiates a new SMS status single type.
	 * @param address the address of the sender
	 * @param status the status of the message 
	 */
	public DeliveryInfo (UserId address, Status status){
		this(address, status, null);
	}

	/**
	 * 
	 * Instantiates a new SMS status single type.
	 * @param address the address of the sender
	 * @param status the status of the message 
	 * @param description description of the status
	 */
	public DeliveryInfo (UserId address, Status status, String description){
		this.mAddress=address;
		this.mStatus=status;

		if (description != null)
			this.mDescription = description;
		else description = statusDescription(status);
	}

	/**
	 * Gets the address associated to the status
	 * @return the address
	 */
	public String getAddress(){
		return this.mAddress.getUserIdValue();
	}

	/**
	 * Gets the address associated to the status
	 * @return the address
	 */
	public UserId getUserIdAddress(){
		return this.mAddress;
	}

	/**
	 * Gets the delivery status of the SMS sent to the address part of the object
	 * @return the status
	 */
	public Status getStatus(){
		return this.mStatus;
	}

	/**
	 * Gets the delivery status description of the SMS sent to the address part of the object
	 * @return the description
	 */
	public String getDescription(){
		return this.mDescription;
	}

	/**
	 * Sets the status of the delivery
	 * @param status
	 */
	public void setStatus(Status status){
		this.mStatus=status;

		//Set default description
		if (mDescription == null)
			mDescription = statusDescription(status);
	}

	/**
	 * Sets the address of the delivery status
	 * @param address
	 */
	public void setUserIdAddress(UserId address){
		this.mAddress=address;
	}

	/**
	 * Sets the description of the delivery status
	 * @param description
	 */
	public void setDescription(String description) {
		this.mDescription=description;
	}

	public boolean isValid() {
		if ((mStatus != null) && (mAddress!=null) &&
				(mAddress.isValid()))
			return true;
		return false;
	}

	/**
	 * Translates a string into the corresponding enumerated type
	 * @param raw A string with the human readable representation of the
	 * enumerated type
	 * @return The corresponding enumerated type
	 * @throws BlueviaClientException Thrown when the raw parameter does not correspond
	 * to any enumerated type 
	 */
	public static Status translate(String raw) throws BlueviaException {
		if (raw.equals("DeliveredToNetwork") || raw.equals("DELIVERED_TO_NETWORK"))
			return Status.DELIVERED_TO_NETWORK;
		else if (raw.equals("DeliveryUncertain") || raw.equals("DELIVERY_UNCERTAIN"))
			return Status.DELIVERY_UNCERTAIN;
		else if (raw.equals("DeliveryImpossible") || raw.equals("DELIVERY_IMPOSSIBLE"))
			return Status.DELIVERY_IMPOSSIBLE;
		else if (raw.equals("MessageWaiting") || raw.equals("MESSAGE_WAITING"))
			return Status.MESSAGE_WAITING;
		else if (raw.equals("DeliveredToTerminal") || raw.equals("DELIVERED_TO_TERMINAL"))
			return Status.DELIVERED_TO_TERMINAL;
		else if (raw.equals("DeliveryNotificationNotSupported") || raw.equals("DELIVERY_NOTIFICATION_NOT_SUPPORTED"))
			return Status.DELIVERY_NOTIFICATION_NOT_SUPPORTED;
		else throw new BlueviaException("Internal client error- Unrecognized delivery status: "+raw, BlueviaException.INTERNAL_CLIENT_ERROR);

	}

	private String statusDescription(Status status){
		switch (status){
		case DELIVERED_TO_NETWORK:
			return "The message has been delivered to the network. Another state could be available " +
			"later to inform if the message was finally delivered to the handset.";
		case DELIVERY_UNCERTAIN:
			return "Delivery status unknown.";
		case DELIVERY_IMPOSSIBLE:
			return "Unsuccessful delivery; the message could not be delivered before it expired.";
		case MESSAGE_WAITING:
			return "The message is still queued for delivery. This is a temporary state, pending transition to another state.";
		case DELIVERED_TO_TERMINAL:
			return "The message has been successful delivered to the handset.";
		case DELIVERY_NOTIFICATION_NOT_SUPPORTED:
			return "Unable to provide delivery status information because it is not supported by the network.";
		default :
			return null;
		}
	}
}
