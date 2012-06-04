/**
 * \package com.bluevia.messagery.mt.sms.data This package contains the data classes in order to send SMS using Bluevia API.
 */
package com.bluevia.messagery.mt.sms.data;

import java.util.ArrayList;

import com.bluevia.commons.data.UserId;
import com.bluevia.messagery.data.AbstractMessage;


/**
 * Class representing abstract SMS class that represents the different SMS messages that
 * will be send using the SMS Client API
 * This type is composed of the following fields:
 *
 * This implementation is not synchronized
 *
 * @author Telefonica R&D
 * 
*/
public abstract class Sms extends AbstractMessage {

    /**
     * Enum class to implement sms format attribute
     */
    public enum SmsFormat { EMS, SMART_MESSAGING };

    /**
     * Instantiates a new SMS message type.
     */
    public Sms() {
        super();
    }
    
    /**
     * Instantiates a new SMS message type.
     * 
     * @param address Destination address
     */
    public Sms(UserId address) {
        super(address);
    }
    
    /**
     * Instantiates a new SMS message type.
     * 
     * @param address Destination address
     * @param endpoint Endpoint
     * @param correlator Correlator
     */
    public Sms(UserId address, String endpoint, String correlator) {
        super(address, endpoint, correlator);
    }

    /**
     * Instantiates a new SMS message type.
     * 
     * @param addressList List with the destination addresses
     */
    public Sms(ArrayList<UserId> addressList) {
        super (addressList);
    }
    
    /**
     * Instantiates a new SMS message type.
     * 
     * @param addressList List with the destination addresses
     * @param endpoint Endpoint
     * @param correlator Correlator
     */
    public Sms(ArrayList<UserId> addressList, String endpoint, String correlator) {
        super (addressList, endpoint, correlator);
    }

    public abstract boolean isValid();



 }
