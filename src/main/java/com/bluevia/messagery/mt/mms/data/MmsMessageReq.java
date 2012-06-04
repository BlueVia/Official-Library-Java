/**
 * \package com.bluevia.messagery.mt.mms.data This package contains the data classes in order to send MMS using Bluevia API.
 */
package com.bluevia.messagery.mt.mms.data;

import java.util.ArrayList;

import com.bluevia.commons.Utils;
import com.bluevia.commons.data.UserId;
import com.bluevia.messagery.data.AbstractMessage;
import com.telefonica.schemas.unica.rest.common.v1.SimpleReferenceType;
import com.telefonica.schemas.unica.rest.common.v1.UserIdType;
import com.telefonica.schemas.unica.rest.mms.v1.MessageType;

/**
 * Class representing the MmsMessageReq that will be send using the MMS Client API
 * This type is composed of the following fields:
 * <ul>
 * <li>addressList: mandatory; max occurrences: minimum 1.
 * <li>subject: optional; String
 * <li>originAddress: mandatory; String
 * </ul>
 *
 * This implementation is not synchronized
 *
 * @author Telefonica R&D
 * 
 */
public class MmsMessageReq extends AbstractMessage {

    private String mSubject;
    private String mTextMessage;

    private ArrayList<Attachment> mAttachmentList = new ArrayList<Attachment>(1);
    

    /**
     * Instantiates a new MMS message type.
     */
    public MmsMessageReq() {
        super();
    }
    
    /**
     * Instantiates a new message
     * @param address Destination address
     * @param mAttachmentList List with the attachments
     */
    public MmsMessageReq(String subject, UserId address, ArrayList<Attachment> attachmetList) {
        this(subject, address, attachmetList, null, null);
    }
    
    /**
     * Instantiates a new message
     * @param address Destination address
     * @param endpoint the endpoint to receive notifications of sent SMSs
     * @param correlator the correlator
     * @param mAttachmentList List with the attachments
     */
    public MmsMessageReq(String subject, UserId address, ArrayList<Attachment> attachmentList, String endpoint,
    		String correlator) {
        super(address, endpoint, correlator);
        mSubject = subject;
        mAttachmentList = attachmentList;
    }

    /**
     * Instantiates a new message
     * @param address Destination address
     * @param endpoint the endpoint to receive notifications of sent SMSs
     * @param correlator the correlator
     * @param mAttachmentList List with the attachments
     */
    public MmsMessageReq(String subject, String message, UserId address, ArrayList<Attachment> attachmentList, String endpoint,
    		String correlator) {
        super(address, endpoint, correlator);
        mSubject = subject;
        mAttachmentList = attachmentList;
        mTextMessage= message;
    }

    /**
     * Instantiates a new message
     * @param subject the subjec of the message
     * @param addressList Destination addresses list
     * @param mAttachmentList List with the attachments
     * @param endpoint the endpoint to receive notifications of sent SMSs
     * @param correlator the correlator
     */
    public MmsMessageReq(String subject, ArrayList<UserId> addressList, ArrayList<Attachment> attachmentList) {
        this(subject, addressList, attachmentList, null, null);
    }

    /**
     * Instantiates a new message
     * @param subject the subjec of the message
     * @param addressList Destination addresses list
     * @param endpoint the endpoint to receive notifications of sent SMSs
     * @param correlator the correlator
     * @param mAttachmentList List with the attachments
     */
    public MmsMessageReq(String subject, ArrayList<UserId> addressList, ArrayList<Attachment> attachmentList, String endpoint, 
    		String correlator) {
        super(addressList, endpoint, correlator);
        mSubject = subject;
        mAttachmentList =  attachmentList;
    }

    /**
     * Instantiates a new message
     * @param subject the subjec of the message
     * @param message the text of message
     * @param addressList Destination addresses list
     * @param endpoint the endpoint to receive notifications of sent SMSs
     * @param correlator the correlator
     * @param mAttachmentList List with the attachments
     */
    public MmsMessageReq(String subject, String message, ArrayList<UserId> addressList, ArrayList<Attachment> attachmentList, String endpoint, 
    		String correlator) {
        super(addressList, endpoint, correlator);
        mSubject = subject;
        mAttachmentList =  attachmentList;
        mTextMessage = message;
    }

    /**
     * Instantiates a new message
     * @param subject the subjec of the message
     * @param message the text of message
     * @param addressList Destination addresses list
     * @param senderName senderName
     * @param endpoint the endpoint to receive notifications of sent SMSs
     * @param correlator the correlator
     * @param mAttachmentList List with the attachments
     */
    public MmsMessageReq(String subject, String message, ArrayList<UserId> addressList, ArrayList<Attachment> attachmentList, 
    		String senderName, String endpoint, String correlator) {
        super(addressList, senderName, endpoint, correlator);
        mSubject = subject;
        mAttachmentList =  attachmentList;
        mTextMessage = message;
    }

    /**
     * Instantiates a new message
     * @param subject the subjec of the message
     * @param message the text of message
     * @param addressList Destination addresses list
     * @param originAddress origin address
     * @param senderName senderName
     * @param endpoint the endpoint to receive notifications of sent SMSs
     * @param correlator the correlator
     * @param mAttachmentList List with the attachments
     */
    public MmsMessageReq(String subject, String message, ArrayList<UserId> addressList, String originAddress, ArrayList<Attachment> attachmentList, 
    		String senderName, String endpoint, String correlator) {
        super(addressList, senderName, endpoint, correlator);
        mSubject = subject;
        mAttachmentList =  attachmentList;
        mTextMessage = message;
        this.setOriginAddress(new UserId(UserId.Type.PHONE_NUMBER, originAddress));
        
    }

    
    /**
     * Sets the subject
     *
     * @param subject
     */
    public void setSubject(String subject) {
        this.mSubject = subject;
    }

    /**
     * Gets the subject.
     *
     * @return the subject
     */
    public String getSubject() {
        return mSubject;
    }

    public String getTextMessage() {
		return mTextMessage;
	}

	public void setTextMessage(String textMessage) {
		this.mTextMessage = textMessage;
	}

	/**
     * Adds the attachment to the Mms
     *
     * @param attachment attachment to be added
     */
    public void addAttachment (Attachment attachment) {
        mAttachmentList.add(attachment);
    }

    /**
     * Removes the attachment
     *
     * @param attachment
     * @return
     */
    public boolean removeAttachment (Attachment attachment) {
        return mAttachmentList.remove(attachment);
    }

    /**
     * Clear attachement list.
     */
    public void clearAttachmentList () {
        mAttachmentList.clear();
    }

    /**
     * Gets the attachments.
     *
     * @return the attachment list
     */
    public ArrayList<Attachment> getAttachementList () {
        return mAttachmentList;
    }

    public boolean isValid() {
    	return (validateSubject() && validateOriginAddress() && validateAddressList() && validateAttachements());
    }

    private boolean validateSubject() {
        //TODO Verificar si existe algun limite de longitud en el subject
        return true;
    }

    private boolean validateAttachements() {
        //TODO Verificar si existe algun limite en el numero o tamanio de los attachements
        return true;
    }
    
    public MessageType toMessageType() {
				
		MessageType mmsType= new MessageType();
		
		ArrayList<UserIdType> addressList= new ArrayList<UserIdType>();
		for (UserId addres: this.getAddressList()) {
			addressList.add(addres.toUserIdType());
		}
		mmsType.setAddress(addressList);
		
		mmsType.setOriginAddress(this.getOriginAddress().toUserIdType());
				
		if (!Utils.isEmpty(this.getEndpoint())) {
			SimpleReferenceType reference= new SimpleReferenceType();
			reference.setCorrelator(this.getCorrelator());
			reference.setEndpoint(this.getEndpoint());
			mmsType.setReceiptRequest(reference);
		}
		
		if (!Utils.isEmpty(this.getSenderName())) {
			mmsType.setSenderName(this.getSenderName());
		}
		mmsType.setSubject(this.getSubject());		
		
		return mmsType;

    }
    
 }
