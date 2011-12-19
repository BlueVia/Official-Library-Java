//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.11.22 at 04:03:12 PM CET 
//


package com.telefonica.schemas.unica.rest.sms.v1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ReceivedSMSType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ReceivedSMSType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="receivedSMS" type="{http://www.telefonica.com/schemas/UNICA/REST/sms/v1/}SMSMessageType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReceivedSMSType", propOrder = {
    "receivedSMS"
})
public class ReceivedSMSType {

    protected List<SMSMessageType> receivedSMS;

    /**
     * Gets the value of the receivedSMS property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the receivedSMS property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReceivedSMS().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SMSMessageType }
     * 
     * 
     */
    public List<SMSMessageType> getReceivedSMS() {
        if (receivedSMS == null) {
            receivedSMS = new ArrayList<SMSMessageType>();
        }
        return this.receivedSMS;
    }

    /**
     * Sets the value of the receivedSMS property.
     * 
     * @param receivedSMS
     *     allowed object is
     *     {@link SMSMessageType }
     *     
     */
    public void setReceivedSMS(List<SMSMessageType> receivedSMS) {
        this.receivedSMS = receivedSMS;
    }

}
