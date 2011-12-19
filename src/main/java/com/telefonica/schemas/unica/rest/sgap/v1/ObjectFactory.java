//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.11.22 at 04:03:12 PM CET 
//


package com.telefonica.schemas.unica.rest.sgap.v1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.telefonica.schemas.unica.rest.sgap.v1 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _AdResponse_QNAME = new QName("http://www.telefonica.com/schemas/UNICA/REST/sgap/v1/", "adResponse");
    private final static QName _AdRequest_QNAME = new QName("http://www.telefonica.com/schemas/UNICA/REST/sgap/v1/", "adRequest");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.telefonica.schemas.unica.rest.sgap.v1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SimpleAdResponseType }
     * 
     */
    public SimpleAdResponseType createSimpleAdResponseType() {
        return new SimpleAdResponseType();
    }

    /**
     * Create an instance of {@link SimpleAdRequestType }
     * 
     */
    public SimpleAdRequestType createSimpleAdRequestType() {
        return new SimpleAdRequestType();
    }

    /**
     * Create an instance of {@link ResourceType }
     * 
     */
    public ResourceType createResourceType() {
        return new ResourceType();
    }

    /**
     * Create an instance of {@link CreativeElementType }
     * 
     */
    public CreativeElementType createCreativeElementType() {
        return new CreativeElementType();
    }

    /**
     * Create an instance of {@link AttributeType }
     * 
     */
    public AttributeType createAttributeType() {
        return new AttributeType();
    }

    /**
     * Create an instance of {@link InteractionType }
     * 
     */
    public InteractionType createInteractionType() {
        return new InteractionType();
    }

    /**
     * Create an instance of {@link AdType }
     * 
     */
    public AdType createAdType() {
        return new AdType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SimpleAdResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.telefonica.com/schemas/UNICA/REST/sgap/v1/", name = "adResponse")
    public JAXBElement<SimpleAdResponseType> createAdResponse(SimpleAdResponseType value) {
        return new JAXBElement<SimpleAdResponseType>(_AdResponse_QNAME, SimpleAdResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SimpleAdRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.telefonica.com/schemas/UNICA/REST/sgap/v1/", name = "adRequest")
    public JAXBElement<SimpleAdRequestType> createAdRequest(SimpleAdRequestType value) {
        return new JAXBElement<SimpleAdRequestType>(_AdRequest_QNAME, SimpleAdRequestType.class, null, value);
    }

}
