//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.11.21 at 04:22:57 PM CET 
//


package com.telefonica.schemas.unica.rpc.trusted_payment.v1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.telefonica.schemas.unica.rpc.trusted_payment.v1 package. 
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

    private final static QName _MethodCall_QNAME = new QName("http://www.telefonica.com/schemas/UNICA/RPC/trusted_payment/v1", "methodCall");
    private final static QName _MethodResponse_QNAME = new QName("http://www.telefonica.com/schemas/UNICA/RPC/trusted_payment/v1", "methodResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.telefonica.schemas.unica.rpc.trusted_payment.v1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link MethodResponseType }
     * 
     */
    public MethodResponseType createMethodResponseType() {
        return new MethodResponseType();
    }

    /**
     * Create an instance of {@link MethodCallType }
     * 
     */
    public MethodCallType createMethodCallType() {
        return new MethodCallType();
    }

    /**
     * Create an instance of {@link PaymentInfoType }
     * 
     */
    public PaymentInfoType createPaymentInfoType() {
        return new PaymentInfoType();
    }

    /**
     * Create an instance of {@link RefundParamsType }
     * 
     */
    public RefundParamsType createRefundParamsType() {
        return new RefundParamsType();
    }

    /**
     * Create an instance of {@link PaymentParamsType }
     * 
     */
    public PaymentParamsType createPaymentParamsType() {
        return new PaymentParamsType();
    }

    /**
     * Create an instance of {@link PaymentResultType }
     * 
     */
    public PaymentResultType createPaymentResultType() {
        return new PaymentResultType();
    }

    /**
     * Create an instance of {@link MethodResponseType.Result }
     * 
     */
    public MethodResponseType.Result createMethodResponseTypeResult() {
        return new MethodResponseType.Result();
    }

    /**
     * Create an instance of {@link MethodCallType.Params }
     * 
     */
    public MethodCallType.Params createMethodCallTypeParams() {
        return new MethodCallType.Params();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MethodCallType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.telefonica.com/schemas/UNICA/RPC/trusted_payment/v1", name = "methodCall")
    public JAXBElement<MethodCallType> createMethodCall(MethodCallType value) {
        return new JAXBElement<MethodCallType>(_MethodCall_QNAME, MethodCallType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MethodResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.telefonica.com/schemas/UNICA/RPC/trusted_payment/v1", name = "methodResponse")
    public JAXBElement<MethodResponseType> createMethodResponse(MethodResponseType value) {
        return new JAXBElement<MethodResponseType>(_MethodResponse_QNAME, MethodResponseType.class, null, value);
    }

}
