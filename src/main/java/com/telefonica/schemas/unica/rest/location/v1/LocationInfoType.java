//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.11.22 at 04:03:12 PM CET 
//


package com.telefonica.schemas.unica.rest.location.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for LocationInfoType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LocationInfoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="coordinates" type="{http://www.telefonica.com/schemas/UNICA/REST/location/v1/}CoordinatesType"/>
 *         &lt;element name="altitude" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *         &lt;element name="accuracy" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="timestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LocationInfoType", propOrder = {
    "coordinates",
    "altitude",
    "accuracy",
    "timestamp"
})
public class LocationInfoType {

    @XmlElement(required = true)
    protected CoordinatesType coordinates;
    protected Float altitude;
    protected int accuracy;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar timestamp;

    /**
     * Gets the value of the coordinates property.
     * 
     * @return
     *     possible object is
     *     {@link CoordinatesType }
     *     
     */
    public CoordinatesType getCoordinates() {
        return coordinates;
    }

    /**
     * Sets the value of the coordinates property.
     * 
     * @param value
     *     allowed object is
     *     {@link CoordinatesType }
     *     
     */
    public void setCoordinates(CoordinatesType value) {
        this.coordinates = value;
    }

    /**
     * Gets the value of the altitude property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getAltitude() {
        return altitude;
    }

    /**
     * Sets the value of the altitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setAltitude(Float value) {
        this.altitude = value;
    }

    /**
     * Gets the value of the accuracy property.
     * 
     */
    public int getAccuracy() {
        return accuracy;
    }

    /**
     * Sets the value of the accuracy property.
     * 
     */
    public void setAccuracy(int value) {
        this.accuracy = value;
    }

    /**
     * Gets the value of the timestamp property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the value of the timestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTimestamp(XMLGregorianCalendar value) {
        this.timestamp = value;
    }

}
