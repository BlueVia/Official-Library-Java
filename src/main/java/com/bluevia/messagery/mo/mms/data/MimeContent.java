package com.bluevia.messagery.mo.mms.data;

import com.bluevia.commons.Entity;
import com.bluevia.commons.Utils;

/**
 * Class representing a Mime Content (an attachment) of a received MMS.
 * The content is an Object which can be a String or a byte[].
 * 
 * <ul>
 * <li>Name: name of the attachment, if exists</li>
 * <li>Content: the content of the attachments. The object will be a String for text attachemtns or InputStream for binary attachents.</li>
 * <li>ContentType: the content type of the attachemnt</li>
 * <li>ContentEncoding: the content encoding of the attachment</li>
 * </ul>
 * 
 * @author Telefonica R&D
 * 
 */
public class MimeContent implements Entity {

	private String contentType;
    private Object content;
    private String contentEncoding;
    private String name;

    /**
     * @return the content of the MimeContent. This can be a String or a byte[]
     */
    public Object getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(Object content) {
        this.content = content;
    }

    /**
     * @return the contentEncoding
     */
    public String getEncoding() {
        return contentEncoding;
    }

    /**
     * @param contentEncoding the contentEncoding to set
     */
    public void setEncoding(String contentEncoding) {
        this.contentEncoding = contentEncoding;
    }

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String fileName) {
		this.name = fileName;
	}

    /**
     * @return the contentType
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * @param contentType the contentType to set
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

	@Override
	public boolean isValid() {
		return !Utils.isEmpty(contentType) && content != null;
	}

}
