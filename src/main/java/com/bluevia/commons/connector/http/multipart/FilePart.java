package com.bluevia.commons.connector.http.multipart;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;

import org.apache.commons.httpclient.methods.multipart.FilePartSource;
import org.apache.commons.httpclient.methods.multipart.PartSource;
import org.apache.http.util.EncodingUtils;

/**
 * This class implements a part of a Multipart post object that consists of a file.
 * Two different types of files are supported:
 * <ul>
 *  <li>text files, for the following content types:
 *      <ul>
 *          <li>text/plain
 *          <li>application/xml
 *          <li>application/json
 *      </ul>
 *  <li>binary files, for the following content types:
 *      <ul>
 *          <li>image/jpeg
 *          <li>image/bmp
 *          <li>image/gif
 *          <li>image/png
 *          <li>audio/midi
 *          <li>audio/wav
 *          <li>audio/mpeg
 *          <li>audio/amr
 *          <li>video/avi
 *          <li>video/x-ms-asf
 *          <li>video/mpeg
 *          <li>video/mpeg4
 *          <li>video/3gpp
 *      </ul>
 * </ul>
 *
 * 
 */
public class FilePart extends BlueviaPartBase {

	/**
     * Default content encoding of file attachments.
     */
    //public static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";

    /**
     * Default charset of file attachments.
     */
    public static final String DEFAULT_CHARSET = "ISO-8859-1";

    /**
     * Default transfer encoding of file attachments.
     */
    public static final String DEFAULT_TRANSFER_ENCODING = "binary";
    
    /**
     * Table containing supported text type content types and their corresponding charsets.
     */
    protected static HashMap<String, String> TEXT_CONTENT_TYPES_TABLE = new HashMap<String, String>();

    /**
     * Table containing supported binary type content types and their corresponding charsets.
     */
    protected static HashMap<String, String> BINARY_CONTENT_TYPES_TABLE = new HashMap<String, String>();

    /**
     * Fill the content types conversion table (content type to charsets)
     *
     */
    static {
        TEXT_CONTENT_TYPES_TABLE.put(TEXT_PLAIN, TEXT_PLAIN);
        TEXT_CONTENT_TYPES_TABLE.put(APPLICATION_XML, APPLICATION_XML);
        TEXT_CONTENT_TYPES_TABLE.put(APPLICATION_JSON, APPLICATION_JSON);
        TEXT_CONTENT_TYPES_TABLE.put(APPLICATION_URL_ENCODED, APPLICATION_URL_ENCODED);

        BINARY_CONTENT_TYPES_TABLE.put(IMAGE_JPEG, IMAGE_JPEG);
        BINARY_CONTENT_TYPES_TABLE.put(IMAGE_BMP, IMAGE_BMP);
        BINARY_CONTENT_TYPES_TABLE.put(IMAGE_GIF, IMAGE_GIF);
        BINARY_CONTENT_TYPES_TABLE.put(IMAGE_PNG, IMAGE_PNG);

        BINARY_CONTENT_TYPES_TABLE.put(AUDIO_MIDI, AUDIO_MIDI);
        BINARY_CONTENT_TYPES_TABLE.put(AUDIO_WAV, AUDIO_WAV);
        BINARY_CONTENT_TYPES_TABLE.put(AUDIO_MPEG, AUDIO_MPEG);
        BINARY_CONTENT_TYPES_TABLE.put(AUDIO_MP3, AUDIO_MP3);
        BINARY_CONTENT_TYPES_TABLE.put(AUDIO_AMR, AUDIO_AMR);

        BINARY_CONTENT_TYPES_TABLE.put(VIDEO_AVI, VIDEO_AVI);
        BINARY_CONTENT_TYPES_TABLE.put(VIDEO_MP4, VIDEO_MP4);
        BINARY_CONTENT_TYPES_TABLE.put(VIDEO_3GPP, VIDEO_3GPP);
    }

    /**
     * Attachment's file name.
     */
    protected static final String FILE_NAME = "; filename=";

    /**
     * Attachment's file name as a byte array.
     */
    private static final byte[] FILE_NAME_BYTES = EncodingUtils.getAsciiBytes(FILE_NAME);

    /**
     * Source of the file part.
     */
    private PartSource source;

    /**
     * Main constructor.
     *
     * @param name the name of the part.
     * @param file the file to be sent.
     * @param contentType the content type.
     *
     * @throws FileNotFoundException
     */
    public FilePart(String name, File file, String contentType) throws FileNotFoundException {
        //super(name, contentType == null ? DEFAULT_CONTENT_TYPE : contentType, DEFAULT_CHARSET, DEFAULT_TRANSFER_ENCODING);
    	super(name, contentType, DEFAULT_CHARSET, DEFAULT_TRANSFER_ENCODING);

    	if (!isAvailable(contentType))
    		throw new IllegalArgumentException("Content-type is not available");
    	
        source = new FilePartSource(file.getName(), file);

        if (isText(contentType)) {
            setCharSet(UTF8_CHARSET);
            setTransferEncoding(_8BIT_CONTENT_TRANSFER_ENCODING);
        } else {
            setCharSet(null);
            setTransferEncoding(BASE64_CONTENT_TRANSFER_ENCODING);
        }
    }

    /**
     * Main constructor
     *
     * @param name the name of the part.
     * @param partSource the part source
     * @param contentType the content type.
     * @throws FileNotFoundException
     */
    public FilePart(String name, PartSource partSource, String contentType) throws FileNotFoundException {
        //super(name, contentType == null ? DEFAULT_CONTENT_TYPE : contentType, DEFAULT_CHARSET, DEFAULT_TRANSFER_ENCODING);
    	super(name, contentType, DEFAULT_CHARSET, DEFAULT_TRANSFER_ENCODING);
    	
    	if (!isAvailable(contentType))
    		throw new IllegalArgumentException("Content-type is not available");

        if (isText(contentType)) {
            setCharSet(UTF8_CHARSET);
            setTransferEncoding(_8BIT_CONTENT_TRANSFER_ENCODING);
        } else {
            setCharSet(null);
            setTransferEncoding(BASE64_CONTENT_TRANSFER_ENCODING);
        }

        if (partSource == null) {
            throw new IllegalArgumentException("Source may not be null");
        }
        this.source = partSource;
    }
    
    /**
     * Returns if the content type passed as a parameter refers to text.
     *
     * @param contentType the content type to be analyzed.
     *
     * @return <code>true</code> if the content type passed as a parameter refers to text;
     *         <code>false</code> otherwise.
     */
    private boolean isText(String contentType) {
        return (TEXT_CONTENT_TYPES_TABLE.containsKey(contentType));
    }


    /**
     * Returns if the content type of current part refers to text.
     *
     * @return <code>true</code> if the content type of current part refers to text;
     *         <code>false</code> otherwise.
     */
    private boolean isText() {
        return isText(getContentType());
    }

    /**
     * Returns if the content type is available by gSDP
     * 
     * @param contentType the content type to be analyzed.
     * 
     * @return <code>true</code> if the content type is available;
     *         <code>false</code> otherwise.
     */
    public boolean isAvailable(String contentType){
    	return TEXT_CONTENT_TYPES_TABLE.containsKey(contentType)
    		|| BINARY_CONTENT_TYPES_TABLE.containsKey(contentType);
    }
    
    /**
     * Returns if the content type is available by gSDP
     * 
     * @return <code>true</code> if the content type is available;
     *         <code>false</code> otherwise.
     */
    public boolean isAvailable(){
    	return isAvailable(getContentType());
    }
    

    /**
     * Write the disposition header to the output stream
     * @param out The output stream
     * @throws IOException If an IO problem occurs
     * @see Part#sendDispositionHeader(OutputStream)
     */
    protected void sendDispositionHeader(OutputStream out)
    throws IOException {
        super.sendDispositionHeader(out);
        String filename = this.source.getFileName();
        if (filename != null) {
            out.write(FILE_NAME_BYTES);
            out.write(QUOTE_BYTES);
            out.write(EncodingUtils.getAsciiBytes(filename));
            out.write(QUOTE_BYTES);
        }
    }


    /**
     * Return the length of the data.
     *
     * @return The length.
     * @throws IOException if an IO problem occurs
     */
    @Override
    protected long lengthOfData() throws IOException {
        if (!isText())
            return getBase64EncodedSize();
        return source.getLength();
    }


    /**
     * Write the data in "source" to the specified stream.
     *
     * @param outputStream The output stream.
     * @throws IOException if an IO problem occurs.
     */
    @Override
    protected void sendData(OutputStream outputStream) throws IOException {
        if (lengthOfData() == 0) {
            // This file contains no data, so there is nothing to send.
            // we don't want to create a zero length buffer as this will
            // cause an infinite loop when reading.
            return;
        }

        if (isText()) {
            // Text data.
            String buff;
            LineNumberReader in = new LineNumberReader(new InputStreamReader(source.createInputStream()));
            PrintWriter out = new PrintWriter(outputStream);
            try {
                while ((buff = in.readLine()) != null) {
                    out.println(buff);
                }
            } finally {
                out.flush();
                // we're done with the stream, close it
                in.close();
            }
        }
        else {
            // Binary data.
            byte buff[] = new byte[3000];
            InputStream instream = source.createInputStream();
            Base64OutputStreamEncoder base64_out = new Base64OutputStreamEncoder(outputStream, true);
            try {
                int len;
                while ((len = instream.read(buff)) >= 0) {
                    base64_out.write(buff, 0, len);
                }
                base64_out.flush();
            } finally {
                outputStream.flush();
                // we're done with the stream, close it
                instream.close();
            }
        }
    }


    /**
     * Returns the source of the file part.
     *
     * @return The source.
     */
    protected PartSource getSource() {
        return this.source;
    }

    private long getBase64EncodedSize() {
        long source_length = source.getLength();

        long encoded_base64_length = (source_length / 3) * 4;
        if ((source_length % 3) > 0)
            encoded_base64_length += 4;

        long num_rows = encoded_base64_length / Base64OutputStreamEncoder.CHUNKSIZE_DEFAULT;
        if (encoded_base64_length % Base64OutputStreamEncoder.CHUNKSIZE_DEFAULT == 0)
            num_rows--;

        long res = encoded_base64_length + (num_rows * Base64OutputStreamEncoder.LINE_SEPARATOR_DEFAULT.length());
        return res;
    }
}
