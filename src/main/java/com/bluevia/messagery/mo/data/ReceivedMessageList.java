package com.bluevia.messagery.mo.data;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.bluevia.commons.Entity;
import com.bluevia.commons.data.UserId;
import com.bluevia.messagery.mo.mms.data.MmsMessageInfo;
import com.bluevia.messagery.mo.mms.data.MmsMessageInfo.AttachmentInfo;
import com.bluevia.messagery.mo.sms.data.SmsMessage;
import com.telefonica.schemas.unica.rest.mms.v1.MessageReferenceType;
import com.telefonica.schemas.unica.rest.mms.v1.MessageURIType;
import com.telefonica.schemas.unica.rest.mms.v1.ReceivedMessagesType;
import com.telefonica.schemas.unica.rest.sms.v1.ReceivedSMSType;
import com.telefonica.schemas.unica.rest.sms.v1.SMSMessageType;

/**
 * 
 * Class representing the ReceivedMessageList that will be received using the SMS/MMS Client API
 * This type is composed of the following fields:
 * <ul>
 * <li>List</li>
 * </ul>
 *
 * This implementation is not synchronized
 *
 * @author Telefonica R&D
 *
 */
public class ReceivedMessageList implements Entity {

    private ArrayList<AbstractReceivedMessage> mList;

	private static Logger log = Logger.getLogger(ReceivedMessageList.class.getName());

	/**
     *  Instantiates a new ReceivedSmsList
     */
    public ReceivedMessageList(){
    	super();
    	mList = new ArrayList<AbstractReceivedMessage>();
    }

    /**
     *  Instantiates a new ReceivedSmsList
     */
    public ReceivedMessageList(ReceivedSMSType receivedSMSType){
    	super();
    	mList = new ArrayList<AbstractReceivedMessage>();

    	SmsMessage message= new SmsMessage();
    	
    	for (SMSMessageType messageType: receivedSMSType.getReceivedSMS()) {
    		
    		log.debug("[Message]:" + messageType.getMessage());
    		
    	   	UserId originAddress= new UserId(messageType.getOriginAddress());
        	message.setUserIdOriginAddress(originAddress);
        	message.setDate(messageType.getDateTime().toString());
        	message.setMessage(messageType.getMessage());
        	mList.add(message);
    	}
     }


    
    /**
     *  Instantiates a new ReceivedSmsList
     */
    public ReceivedMessageList(ReceivedMessagesType receivedMessageType){
    	super();
    	mList = new ArrayList<AbstractReceivedMessage>();

    	MmsMessageInfo message= new MmsMessageInfo();
    	
    	for (MessageReferenceType messageType: receivedMessageType.getReceivedMessages()) {
    		
    		log.debug("[Message Identifier]:" + messageType.getMessageIdentifier());
    		
    	   	UserId originAddress= new UserId(messageType.getOriginAddress());
        	message.setDate(messageType.getDateTime().toString());
           	message.setMessageIdentifier(messageType.getMessageIdentifier());
        	message.setSubject(messageType.getSubject());
        	message.setUserIdOriginAddress(originAddress);

        	for (MessageURIType aurl :messageType.getAttachmentURL()) {
           		AttachmentInfo info= message.new AttachmentInfo();
        		info.setContentType(aurl.getContentType());
        		info.setUrl(aurl.getHref());
        		message.addAttachmentInfo(info);
        		log.debug("[HRef URL]:" + aurl.getHref());
        	}

         	mList.add(message);
    	}
     }


    /**
     * @see com.bluevia.rest.commons.Entity#isValid(ApiMode)
     */
    public boolean isValid() {
		for (AbstractReceivedMessage message : mList){
			if (!message.isValid())
				return false;
		}
		return true;
	}
    /**
     * Adds a new single received Message
     * @param element
     * @return
     */
    public boolean add(AbstractReceivedMessage element){
        return mList.add(element);
    }

    /**
     * Return the received Message list for all recipient addresses
     * @param <E>
     * @return the list
     */
    @SuppressWarnings("unchecked")
	public <E> ArrayList<E> getList()  {
        return (ArrayList<E>)mList.clone();
    }

}
