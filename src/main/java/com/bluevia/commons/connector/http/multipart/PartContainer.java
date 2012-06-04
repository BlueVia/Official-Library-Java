package com.bluevia.commons.connector.http.multipart;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.httpclient.methods.multipart.PartBase;

/**
 * This class implements a part of a Multipart post object that consists of a multipart part.
 * 
 */
public class PartContainer extends BlueviaPartBase {

    private PartBase parts[];

    /**
     * Main constructor.
     *
     * @param name the name of the part.
     * @param parts the different PartBase parts that will include the container.
     */
    public PartContainer(String name, PartBase parts[]) {
        super(name, "multipart/mixed", null, null);
        setContentType("multipart/mixed; boundary=" + new String(getPartBoundary()));
        this.parts = parts;
    }

    /**
     * Return the length of the data.
     *
     * @return The length.
     * @throws IOException if an IO problem occurs
     */
    @Override
    protected long lengthOfData() throws IOException {
        long res = getLengthOfParts(parts);
        return res;
    }

    /**
     * Write the data in "source" to the specified stream.
     *
     * @param out The output stream.
     * @throws IOException if an IO problem occurs.
     */
    @Override
    protected void sendData(OutputStream out) throws IOException {
        sendParts(out, parts);
    }
    
}
