package com.bluevia.messagery.mo.mms.data;

import java.util.ArrayList;

import com.bluevia.commons.Entity;

/**
 * Class representing a received MMS message
 * This type is composed of the following fields:
 * <ul>
 * <li>MessageInfo: the object containing the metadata of the MMS</li>
 * <li>Attachments: list of the attachemnts (MimeContent object)</li>
 * </ul>
 *
 * This implementation is not synchronized
 *
 * @author Telefonica R&D
 * 
 */
public class MmsMessage implements Entity {
	
	private MmsMessageInfo mMessageInfo;
	private ArrayList<MimeContent> mAttachments;
	
	/**
	 * Constructor
	 */
	public MmsMessage(){
		mAttachments = new ArrayList<MimeContent>();
	}
	
	public void setMessageInfo(MmsMessageInfo message){
		this.mMessageInfo = message;
	}
	
	/**
	 * @return the messageIdentifier
	 */
	public String getMessageId() {
		return mMessageInfo.getMessageId();
	}
	
	/**
	 * @return the subject
	 */
	public String getSubject(){
		return mMessageInfo.getSubject();
	}
	
	/**
	 * 
	 * @return the date-time stamp
	 */
	public String getDate()	{
		return mMessageInfo.getDate();
	}
	
	/**
     * Gets the origin address.
     *
     * @return the origin address
     */
    public String getOriginAddress() {
        return mMessageInfo.getOriginAddress();
    }
    
    /**
     * Gets the destination
     *
     * @return the destination
     */
    public ArrayList<String> getDestinationList(){
    	return mMessageInfo.getDestinationList();
    }
	
	/**
	 * @return the mAttachments
	 */
	public ArrayList<MimeContent> getAttachments() {
		return mAttachments;
	}
	
	/**
	 * @param mAttachments the list of mAttachments to set
	 */
	public void setAttachments(ArrayList<MimeContent> attachments) {
		this.mAttachments = attachments;
	}
	
	/**
	 * @param attachment the attachment to add
	 */
	public void addAttachment(MimeContent attachment){
		if (mAttachments == null)
			mAttachments = new ArrayList<MimeContent>();
		this.mAttachments.add(attachment);
	}

	@Override
	public boolean isValid() {
		
		for (MimeContent mime : mAttachments){
			if (!mime.isValid())
				return false;
		}
		
		return mMessageInfo != null && mMessageInfo.isValid();
	}
	
	

}
