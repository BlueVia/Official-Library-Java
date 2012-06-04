package com.bluevia.commons.connector.http.multipart;

import org.apache.http.util.EncodingUtils;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Simple string part for a multipart post. It uses as default "UTF-8" as
 * charset and "8bit" as content transfer encoding.
 *
 */
public class StringPart extends BlueviaPartBase {

	/**
     * Default content encoding of string parameters.
     */
    public static final String DEFAULT_CONTENT_TYPE = TEXT_PLAIN;

    /**
     * Default charset of string parameters.
     */
    public static final String DEFAULT_CHARSET = UTF8_CHARSET;

    /**
     * Default transfer encoding of string parameters
     */
    public static final String DEFAULT_TRANSFER_ENCODING = _8BIT_CONTENT_TRANSFER_ENCODING;

    /**
     * Attachment's file name.
     */
    protected static final String FILE_NAME = "; filename=";

    /**
     * Attachment's file name as a byte array.
     */
    private static final byte[] FILE_NAME_BYTES = EncodingUtils.getAsciiBytes(FILE_NAME);

    /**
     * Contents of this StringPart.
     */
    private byte[] content;

    /**
     * The String value of this part.
     */
    private String value;

    /**
     * Name of the filename part. 
     */
    private String filename;

    /**
     * Constructor.
     *
     * @param name The name of the part
     * @param value the string to post
     * the {@link #DEFAULT_CHARSET default} is used
     */
    public StringPart(String name, String value) {

        super(name, DEFAULT_CONTENT_TYPE, DEFAULT_CHARSET, DEFAULT_TRANSFER_ENCODING);
        if (value == null) {
            throw new IllegalArgumentException("Value may not be null");
        }
        if (value.indexOf(0) != -1) {
            // See RFC 2048, 2.8. "8bit Data"
            throw new IllegalArgumentException("NULs may not be present in string parts");
        }
        this.value = value;
    }


    /**
     * Gets the content in bytes.  Bytes are lazily created to allow the charset to be changed
     * after the part is created.
     *
     * @return the content in bytes
     */
    private byte[] getContent() {
        if (content == null) {
            content = EncodingUtils.getBytes(value, getCharSet());
        }
        return content;
    }


    /**
     * Writes the data to the given OutputStream.
     * @param out the OutputStream to write to
     * @throws IOException if there is a write error
     */
    @Override
    protected void sendData(OutputStream out) throws IOException {
        out.write(getContent());
    }


    /**
     * Return the length of the data.
     * @return The length of the data.
     * @throws IOException If an IO problem occurs
     * @see org.apache.commons.httpclient.methods.multipart.Part#lengthOfData()
     */
    protected long lengthOfData() throws IOException {
        return getContent().length;
    }


    /**
     * Sets the filename
     * @param filename the filename for the String
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * Gets the filename
     * @return the filenam for the string
     */
    public String getFilename() {
        return this.filename;
    }


    /**
     * @see BlueviaPartBase.android.rest.commons.connector.http.multipart.AndroidPartBase#sendDispositionHeader(java.io.OutputStream)
     */
    @Override
    protected void sendDispositionHeader(OutputStream out) throws IOException {
        super.sendDispositionHeader(out);
        String filename = getFilename();
        if (filename != null) {
            out.write(FILE_NAME_BYTES);
            out.write(QUOTE_BYTES);
            out.write(EncodingUtils.getAsciiBytes(filename));
            out.write(QUOTE_BYTES);
        }
    }



}
