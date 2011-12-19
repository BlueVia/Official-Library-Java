//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.11.22 at 04:03:12 PM CET 
//


package com.telefonica.schemas.unica.rpc.common.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * A generic IP address, including both v4 and v6. Only ONE of the child elements may be given.
 * 
 * <p>Java class for IpAddressType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IpAddressType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="ipv4" type="{http://www.telefonica.com/schemas/UNICA/RPC/common/v1}Ipv4AddressType"/>
 *         &lt;element name="ipv6" type="{http://www.telefonica.com/schemas/UNICA/RPC/common/v1}Ipv6AddressType"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IpAddressType", propOrder = {
    "ipv4",
    "ipv6"
})
public class IpAddressType {

    protected String ipv4;
    protected String ipv6;

    /**
     * Gets the value of the ipv4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIpv4() {
        return ipv4;
    }

    /**
     * Sets the value of the ipv4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIpv4(String value) {
        this.ipv4 = value;
    }

    /**
     * Gets the value of the ipv6 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIpv6() {
        return ipv6;
    }

    /**
     * Sets the value of the ipv6 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIpv6(String value) {
        this.ipv6 = value;
    }

}
