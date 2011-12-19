/**
 * 
 * @category bluevia
 * @package com.bluevia.examples
 * @copyright Copyright (c) 2010 Telefonica I+D (http://www.tid.es)
 * @author Bluevia <support@bluevia.com>
 * 
 * 
 *          BlueVia is a global iniciative of Telefonica delivered by Movistar
 *          and O2. Please, check out http://www.bluevia.com and if you need
 *          more information contact us at support@bluevia.com
 */

package com.bluevia.java.mms.data;

import java.util.ArrayList;

import com.telefonica.schemas.unica.rest.mms.v1.MessageType;

public class ReceivedMMS {
	
	private MessageType message;
	private ArrayList<MimeContent> attachments;
	
	public ReceivedMMS(){
		attachments = new ArrayList<MimeContent>();
	}
	
	/**
	 * @return the message
	 */
	public MessageType getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(MessageType message) {
		this.message = message;
	}
	/**
	 * @return the attachments
	 */
	public ArrayList<MimeContent> getAttachments() {
		return attachments;
	}
	/**
	 * @param attachments the list of attachments to set
	 */
	public void setAttachments(ArrayList<MimeContent> attachments) {
		this.attachments = attachments;
	}
	/**
	 * @param attachment the attachment to add
	 */
	public void addAttachment(MimeContent attachment){
		if (attachments == null)
			attachments = new ArrayList<MimeContent>();
		this.attachments.add(attachment);
	}
	
	

}
