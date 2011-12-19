//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.11.21 at 04:22:57 PM CET 
//


package com.telefonica.schemas.unica.rpc.trusted_payment.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.telefonica.schemas.unica.rpc.common.v1.UserIdType;


/**
 * input parameters for Refund operation
 * 
 * <p>Java class for RefundParamsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RefundParamsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="endUserIdentifier" type="{http://www.telefonica.com/schemas/UNICA/RPC/common/v1}UserIdType"/>
 *         &lt;element name="transactionId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="reason" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RefundParamsType", propOrder = {
    "endUserIdentifier",
    "transactionId",
    "reason"
})
public class RefundParamsType {

    @XmlElement(required = true)
    protected UserIdType endUserIdentifier;
    @XmlElement(required = true)
    protected String transactionId;
    @XmlElement(required = true)
    protected String reason;

    /**
     * Gets the value of the endUserIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link UserIdType }
     *     
     */
    public UserIdType getEndUserIdentifier() {
        return endUserIdentifier;
    }

    /**
     * Sets the value of the endUserIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link UserIdType }
     *     
     */
    public void setEndUserIdentifier(UserIdType value) {
        this.endUserIdentifier = value;
    }

    /**
     * Gets the value of the transactionId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * Sets the value of the transactionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionId(String value) {
        this.transactionId = value;
    }

    /**
     * Gets the value of the reason property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReason() {
        return reason;
    }

    /**
     * Sets the value of the reason property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReason(String value) {
        this.reason = value;
    }

}