package com.bluevia.messagery.mt.data;


import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.bluevia.commons.Entity;
import com.bluevia.commons.data.UserId;
import com.bluevia.commons.data.UserId.Type;
import com.bluevia.commons.exception.BlueviaException;
import com.telefonica.schemas.unica.rest.mms.v1.MessageDeliveryStatusType;
import com.telefonica.schemas.unica.rest.sms.v1.DeliveryInformationType;
import com.telefonica.schemas.unica.rest.sms.v1.SMSDeliveryStatusType;

/**
 * Internal class representing a list of DeliveryInfo
 *
 * @author Telefonica R&D
 * 
 */
public class DeliveryInfoList implements Entity {

	private static Logger log = Logger.getLogger(DeliveryInfoList.class.getName());

	private ArrayList<DeliveryInfo> mMessageDeliveryStatus;
    
    /**
     * Instantiates a new SmsDeliveryStatus
     */
    public DeliveryInfoList() {
        super();

        mMessageDeliveryStatus = new ArrayList<DeliveryInfo>();
    }

    
    /**
     * @param deliveryStatusType
     * @returnthe DeliveryInfoList Entity for response (MMS)
     * @throws BlueviaException
     */
    public DeliveryInfoList(MessageDeliveryStatusType deliveryStatusType) throws BlueviaException {

    	super();
    	log.debug("Creating DeliveryInfoList for MMS");
    	
    	mMessageDeliveryStatus = new ArrayList<DeliveryInfo>();
    	
	    List<com.telefonica.schemas.unica.rest.mms.v1.DeliveryInformationType> ldit= deliveryStatusType.getMessageDeliveryStatus();
	    for (com.telefonica.schemas.unica.rest.mms.v1.DeliveryInformationType dit :ldit) {
		    DeliveryInfo deliveryInfo = new DeliveryInfo();
	    	deliveryInfo.setDescription(dit.getDescription());
	    	deliveryInfo.setStatus(DeliveryInfo.translate(dit.getDeliveryStatus()));
	    	UserId userId= new UserId();
	    	userId.setType(Type.PHONE_NUMBER);
	    	userId.setUserIdValue(dit.getAddress().getPhoneNumber());
	    	deliveryInfo.setUserIdAddress(userId);
	    	mMessageDeliveryStatus.add(deliveryInfo);
	    }


    }
    
    /**
     * @param deliveryStatusType
     * @returnthe DeliveryInfoList Entity for response (SMS)
     * @throws BlueviaException
     */
    public DeliveryInfoList(SMSDeliveryStatusType deliveryStatusType) throws BlueviaException {

    	super();
    	
    	log.debug("Creating DeliveryInfoList for SMS");
    	mMessageDeliveryStatus = new ArrayList<DeliveryInfo>();
    	
	    List<DeliveryInformationType> ldit= deliveryStatusType.getSmsDeliveryStatus();
	    for (DeliveryInformationType dit :ldit) {
		    DeliveryInfo deliveryInfo = new DeliveryInfo();
	    	deliveryInfo.setDescription(dit.getDescription());
	    	deliveryInfo.setStatus(DeliveryInfo.translate(dit.getDeliveryStatus()));
	    	UserId userId= new UserId();
	    	userId.setType(Type.PHONE_NUMBER);
	    	userId.setUserIdValue(dit.getAddress().getPhoneNumber());
	    	deliveryInfo.setUserIdAddress(userId);
	    	mMessageDeliveryStatus.add(deliveryInfo);
	    }


    }

    /**
     * @see com.bluevia.rest.commons.Entity#isValid()
     */
    public boolean isValid() {
    	for (DeliveryInfo mds : mMessageDeliveryStatus){
    		if (!mds.isValid())	return false;	
    	}
        return true;
    }

    /**
     * Adds a new single SMS delivery status for a particular recipient address
     * @param element
     * @return
     */
    public boolean add(DeliveryInfo element){
        return this.mMessageDeliveryStatus.add(element);
    }

    /**
     * Return the Sms Delivery status list for all recipient addresses
     * @return the delivery status list
     */
    @SuppressWarnings("unchecked")
	public ArrayList<DeliveryInfo> getDeliveryStatusList ()  {
        return (ArrayList<DeliveryInfo>) mMessageDeliveryStatus.clone();
    }

}


