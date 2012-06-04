/**
 * \package com.bluevia.messagery.mt.mms.data This package contains the data classes in order to send MMS using Bluevia API.
 */
package com.bluevia.messagery.mt.mms.data;

import com.bluevia.commons.exception.BlueviaException;

/**
 * Class representing a mms attachment information. It includes the path of the file
 * and the Content-type.
 * 
 * The value of the Content-type must be chosen from the valid content-type enum list
 *
 */
public class Attachment {

	/**
	 * Availables Content-type values.
	 *
	 */
	public enum ContentType {TEXT_PLAIN, 
		IMAGE_JPEG, IMAGE_BMP, IMAGE_GIF, IMAGE_PNG,
		AUDIO_AMR, AUDIO_MIDI, AUDIO_MP3, AUDIO_MPEG, AUDIO_WAV,
		VIDEO_MP4, VIDEO_AVI, VIDEO_3GPP};

	private static final String TEXT_PLAIN_ST = "text/plain";
	private static final String IMAGE_JPEG_ST = "image/jpeg";
	private static final String IMAGE_BMP_ST = "image/bmp";
	private static final String IMAGE_GIF_ST = "image/gif";
	private static final String IMAGE_PNG_ST = "image/png";
	private static final String AUDIO_AMR_ST = "audio/amr";
	private static final String AUDIO_MIDI_ST = "audio/midi";
	private static final String AUDIO_MP3_ST = "audio/mp3";
	private static final String AUDIO_MPEG_ST = "audio/mpeg";
	private static final String AUDIO_WAV_ST = "audio/wav";
	private static final String VIDEO_MP4_ST = "video/mp4";
	private static final String VIDEO_AVI_ST = "video/avi";
	private static final String VIDEO_3GPP_ST = "video/3gpp";
	
	private ContentType contentType;
	private String filePath;
	
	/**
	 * 
	 * @param path the path of the file
	 * @param contentType the content-type of the file
	 */
	public Attachment(String path, ContentType contentType){
		this.filePath = path;
		this.contentType = contentType;
	}
	
	/**
	 * @return the contentType
	 */
	public ContentType getContentType() {
		return contentType;
	}
	
	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}
	
	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}
	
	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	/**
	 * 
	 * @return returns the content-type of the attachment as a String
	 * @throws BlueviaException 
	 */
	public String getStringContentType() throws BlueviaException{
		switch (contentType){
		case TEXT_PLAIN:
			return TEXT_PLAIN_ST;
		case IMAGE_JPEG:
			return IMAGE_JPEG_ST;
		case IMAGE_BMP:
			return IMAGE_BMP_ST;
		case IMAGE_GIF:
			return IMAGE_GIF_ST;
		case IMAGE_PNG:
			return IMAGE_PNG_ST;
		case AUDIO_AMR:
			return AUDIO_AMR_ST;
		case AUDIO_MIDI:
			return AUDIO_MIDI_ST;
		case AUDIO_MP3:
			return AUDIO_MP3_ST;
		case AUDIO_MPEG:
			return AUDIO_MPEG_ST;
		case AUDIO_WAV:
			return AUDIO_WAV_ST;
		case VIDEO_MP4:
			return VIDEO_MP4_ST;
		case VIDEO_AVI:
			return VIDEO_AVI_ST;
		case VIDEO_3GPP:
			return VIDEO_3GPP_ST;
		default:
			throw new BlueviaException("Not supported content type: " + contentType, BlueviaException.BAD_REQUEST_EXCEPTION );
		}
	}
	
	
	
}
