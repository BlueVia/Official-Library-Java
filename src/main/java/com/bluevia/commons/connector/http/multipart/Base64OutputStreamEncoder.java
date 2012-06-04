package com.bluevia.commons.connector.http.multipart;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.util.EncodingUtils;

/**
 * Base64 encoder. It allows an application to transparently encode a data stream into base64.
 *
 */
class Base64OutputStreamEncoder extends OutputStream {

    private OutputStream out;

    private boolean chunked = false;

    private int chunkIndex = 0;

    private int chunkSize;

    private byte lineSeparatorBytes[];

    private ArrayList<Byte> buffer = new ArrayList<Byte>();

    private transient byte write_buffer[] = new byte[3];

    /**
     * Chunk Size default
     */
    public static final int CHUNKSIZE_DEFAULT = 76;

    /**
     * Line separator default
     */
    public static final String LINE_SEPARATOR_DEFAULT = "\r\n";

    /**
     * Line separator default in bytes
     */
    public static final byte[] LINE_SEPARATOR_BYTES_DEFAULT = EncodingUtils.getAsciiBytes(LINE_SEPARATOR_DEFAULT);


    /**
     * Constructor.
     *
     * @param out the output stream into which the encoded data is going to be written.
     * @param chunked <code>true</code> if the encoded data should be chunk to different lines.
     */
    Base64OutputStreamEncoder(OutputStream out, boolean chunked) {
        this(out, chunked, CHUNKSIZE_DEFAULT, LINE_SEPARATOR_DEFAULT);
    }

    /**
     * Constructor.
     *
     * @param out the output stream into which the encoded data is going to be written.
     * @param chunked <code>true</code> if the encoded data should be chunk to different lines.
     * @param chunksize the size of the lines into which the encoded data is going to be encoded.
     * @param lineSeparator the sequence used to separate lines.
     */
    Base64OutputStreamEncoder(OutputStream out, boolean chunked, int chunksize, String lineSeparator) {
        super();
        this.chunked = chunked;
        this.out = out;
        this.chunkSize = chunksize;
        lineSeparatorBytes = EncodingUtils.getAsciiBytes(lineSeparator);
    }


    /**
     * Constructor.
     *
     * @param out the output stream into which the encoded data is going to be written.
     */
    Base64OutputStreamEncoder(OutputStream out) {
        this(out, false);
    }


    /**
     * @see java.io.OutputStream#write(int)
     */
    @Override
    public void write(int b) throws IOException {
        buffer.add((byte)b);
        if (buffer.size() == 3) {
            copyBufferToArray(write_buffer);
            byte encoded_data[] = Base64.encodeBase64(write_buffer);
            write_encoded_data_to_output(encoded_data);
            buffer.clear();
        }
    }


    /**
     * Writes encoded data into the output stream.
     *
     * @param data the data to be written.
     *
     * @throws IOException
     */
    private void write_encoded_data_to_output(byte[] data) throws IOException {
        if (chunked) {
            for (int i=0; i<data.length; i++) {
                if (chunkIndex >= chunkSize) {
                    out.write(lineSeparatorBytes, 0, lineSeparatorBytes.length);
                    chunkIndex = 0;
                }
                out.write(data[i]);
                chunkIndex++;
            }
        }
        else {
            out.write(data);
        }
    }


    /**
     * Copies the content of current data buffer to a byte array.
     *
     * @param array the byte array into which the data is going to be copied.
     */
    private void copyBufferToArray(byte array[]) {
        for (int i=0; i<array.length; i++)
            array[i] = buffer.get(i);
    }


    /**
     * @see java.io.OutputStream#flush()
     */
    @Override
    public void flush() throws IOException {
        byte data[] = new byte[buffer.size()];
        copyBufferToArray(data);
        byte encoded_data[] = Base64.encodeBase64(data);
        write_encoded_data_to_output(encoded_data);
        out.flush();
    }

}
