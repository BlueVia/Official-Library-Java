package com.bluevia.commons.connector.http.multipart;

import org.apache.commons.httpclient.methods.multipart.PartBase;
import org.apache.http.util.EncodingUtils;

import com.bluevia.commons.exception.BlueviaException;
import com.bluevia.messagery.mt.mms.data.Attachment;

import java.io.IOException;
import java.io.OutputStream;

/**
 * The multipart related classes are based on the internal ANDROID classes
 * com.android.internal.http.multipart, which are not part of the Android API.
 * If ANDROID change these classes in future versions, we should change these classes
 * in order to include the multipart classes into the library, to make it independent
 * of the ANDROID internal classes
 * 
 * The base class for Http Multipart classes.
 *
 *
 */
public abstract class BlueviaPartBase extends PartBase {

    public static final String UTF8_CHARSET = "UTF-8";

    public static final String BASE64_CONTENT_TRANSFER_ENCODING = "base64";
    public static final String _7BIT_CONTENT_TRANSFER_ENCODING = "7bit";
    public static final String _8BIT_CONTENT_TRANSFER_ENCODING = "8bit";

    public static final String TEXT_PLAIN = "text/plain";
    public static final String APPLICATION_XML = "application/xml";
    public static final String APPLICATION_JSON = "application/json";
    public static final String APPLICATION_URL_ENCODED = "application/x-www-form-urlencoded";

    public static final String IMAGE_JPEG = "image/jpeg";
    public static final String IMAGE_BMP = "image/bmp";
    public static final String IMAGE_GIF = "image/gif";
    public static final String IMAGE_PNG = "image/png";

    public static final String AUDIO_MIDI = "audio/midi";
    public static final String AUDIO_WAV = "audio/wav";
    public static final String AUDIO_MPEG = "audio/mpeg";
    public static final String AUDIO_MP3 = "audio/mp3";
    public static final String AUDIO_AMR = "audio/amr";

    public static final String VIDEO_AVI = "video/avi";
    public static final String VIDEO_MP4 = "video/mp4";
    public static final String VIDEO_3GPP = "video/3gpp";

    /* Content Disposition */
    protected static final String CONTENT_DISPOSITION = "Content-Disposition: ";

    protected static final String DEFAULT_CONTENT_DISPOSITION = "form-data";

    protected static final String NAME = "; name=";

    protected static final byte[] NAME_BYTES =  EncodingUtils.getAsciiBytes(NAME);

    /**
     * 
     * Content dispostion as a byte array
    *
     */
    protected static final byte[] CONTENT_DISPOSITION_BYTES =
      EncodingUtils.getAsciiBytes(CONTENT_DISPOSITION);

    /**
     *  Name of the file part.
     */
    private String name;

    private String contentDisposition;
    
    /**
     * Main constructor.
     *
     * @param name the name of the part.
     * @param contentType the content type.
     * @param charSet the character encoding.
     * @param transferEncoding the transfer encoding.
     */
    public BlueviaPartBase(String name, String contentType, String charSet,
            String transferEncoding) {
        super(name, contentType, charSet, transferEncoding);
        this.name = name;
    }

    /**
     * Main constructor.
     *
     * @param name the name of the part.
     * @param contentType the content type.
     * @param charSet the character encoding.
     * @param transferEncoding the transfer encoding.
     * @throws BlueviaException 
     */
    public BlueviaPartBase(Attachment attach) throws BlueviaException {
    	// TODO check encoding and charset!!!
        super(attach.getFilePath(), attach.getStringContentType(), UTF8_CHARSET, _8BIT_CONTENT_TRANSFER_ENCODING);
        this.name = attach.getFilePath();
    }


    /**
     * Overrides com.android.internal.http.multipart.PartBase#setName(java.lang.String)
     * @see com.android.internal.http.multipart.PartBase#setServiceName(java.lang.String)
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Overrides com.android.internal.http.multipart.PartBase#getName()
     * @see com.android.internal.http.multipart.PartBase#getServiceName()
     */
    @Override
    public String getName() {
        return this.name;
    }
    
    /**
     * Returns the Content-Disposition Http Header value for that part
     * @return the Content Disposition Header
     */
    public String getContentDisposition() {
        if (contentDisposition!=null){
            return contentDisposition;
        }
        else{
            return DEFAULT_CONTENT_DISPOSITION;
        }
    }


    /**
     * Sets the Content-Disposition Http Header Value for that part
     * @param newContentDisposition
     */
    public void setContentDisposition(String newContentDisposition) {
        contentDisposition = newContentDisposition;
    }

    @Override
    protected long lengthOfData() throws IOException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    protected void sendData(OutputStream out) throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    protected void sendDispositionHeader(OutputStream out) throws IOException {
        out.write(CONTENT_DISPOSITION_BYTES);
        out.write(EncodingUtils.getAsciiBytes(getContentDisposition()));
        if (getName() != null) {
            out.write(NAME_BYTES);
            out.write(QUOTE_BYTES);
            out.write(EncodingUtils.getAsciiBytes(getName()));
            out.write(QUOTE_BYTES);
        }
    }
}

