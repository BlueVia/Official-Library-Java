/**
 * 
 * @category opentel
 * @package com.bluevia.unica.payment
 * @copyright Copyright (c) 2010 Telefonica I+D (http://www.tid.es)
 * @author Bluevia <support@bluevia.com>
 * @version 1.3
 * 
 *          BlueVia is a global iniciative of Telefonica delivered by Movistar
 *          and O2. Please, check out http://www.bluevia.com and if you need
 *          more information contact us at support@bluevia.com
 */

package com.bluevia.java.payment;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.util.Random;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.stream.StreamSource;

import oauth.signpost.OAuth;
import oauth.signpost.http.HttpParameters;

import com.bluevia.java.Utils;
import com.bluevia.java.exception.BlueviaException;
import com.bluevia.java.oauth.OAuthToken;
import com.telefonica.schemas.unica.rpc.payment.v1.GetPaymentStatusParamsType;
import com.telefonica.schemas.unica.rpc.payment.v1.GetPaymentStatusResultType;
import com.telefonica.schemas.unica.rpc.payment.v1.MethodCallType;
import com.telefonica.schemas.unica.rpc.payment.v1.MethodCallType.Params;
import com.telefonica.schemas.unica.rpc.payment.v1.MethodResponseType;
import com.telefonica.schemas.unica.rpc.payment.v1.MethodType;
import com.telefonica.schemas.unica.rpc.payment.v1.ObjectFactory;
import com.telefonica.schemas.unica.rpc.payment.v1.PaymentParamsType;
import com.telefonica.schemas.unica.rpc.payment.v1.PaymentResultType;


public class PaymentOperation extends PaymentClient {
	
	private static final String RPC_VERSION = "v1";

	private static final String PAYMENT_URI = "/payment";
	private static final String CANCEL_AUTHORIZATION_URI = "/cancelAuthorization";
	private static final String PAYMENT_STATUS_URI = "/getPaymentStatus";

	/**
	 * 
	 * Constructor
	 * 
	 * @param consumer
	 * @param token
	 * @param mode
	 * @throws JAXBException
	 */
    public PaymentOperation(OAuthToken consumer, OAuthToken token, Mode mode) throws JAXBException {
        super(consumer, token, mode);
    }

    /**
     * Makes a payment operation. 
     * The payment parameters must fill the parameters included in the getPaymentRequestToken call.
     * 
     * @param params the Payment parameters of the operation (amount and currency)
     * @return the result of the payment.
     * @throws JAXBException
     * @throws BlueviaException
     */
    public PaymentResultType payment(PaymentParamsType params) throws JAXBException, BlueviaException {

        // build the RPC call
        ObjectFactory objectFactory = new ObjectFactory();
        MethodCallType mct = objectFactory.createMethodCallType();
        mct.setVersion(RPC_VERSION);
        mct.setMethod(MethodType.PAYMENT);
        mct.setId(generateRandomId());
        Params p = new Params();
        
        if (params.getTimestamp() == null){
            XMLGregorianCalendar timestamp = Utils.generateTimestamp();
            params.setTimestamp(timestamp);
        }
        
        p.setPaymentParams(params);
        mct.setParams(p);

        JAXBElement<MethodCallType> element = objectFactory.createMethodCall(mct);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        this.m.marshal(element, os);
        
        HttpParameters additionalParams = new HttpParameters();
        additionalParams.put(OAuth.OAUTH_TIMESTAMP, String.valueOf(
        		Utils.xmlGregorianCalendarToTimestamp(params.getTimestamp())));
        
        String url = this.uri + PAYMENT_URI;

        String res = this.rpcConnector.postHTTPS(url, os.toByteArray(), additionalParams);

        JAXBContext ctx = JAXBContext.newInstance(MethodResponseType.class);
        Unmarshaller um = ctx.createUnmarshaller();
        JAXBElement<MethodResponseType> mrt1 = um.unmarshal(new StreamSource(new StringReader(res)), MethodResponseType.class);
        
        return mrt1.getValue().getResult().getPaymentResult();
    }
    
    /**
     * Cancels the authorization of the access token previously retrieved.
     * 
     * @throws JAXBException
     * @throws BlueviaException
     */
    public void cancelAuthorization() throws JAXBException, BlueviaException {

        // build the RPC call
        ObjectFactory objectFactory = new ObjectFactory();
        MethodCallType mct = objectFactory.createMethodCallType();
        mct.setVersion(RPC_VERSION);
        mct.setMethod(MethodType.CANCEL_AUTHORIZATION);
        mct.setId(generateRandomId());
        
        JAXBElement<MethodCallType> element = objectFactory.createMethodCall(mct);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        this.m.marshal(element, os);

        String url = this.uri + CANCEL_AUTHORIZATION_URI;

        String res = this.rpcConnector.postHTTPS(url, os.toByteArray());

        JAXBElement<MethodResponseType> mrt = u.unmarshal(new StreamSource(new StringReader(res)), MethodResponseType.class);
        
    }
    
    /**
     * Gets the status of a previous payment
     * 
     * @param transactionId the transaction id of the payment.
     * @return
     * @throws JAXBException
     * @throws BlueviaException
     */
    public GetPaymentStatusResultType getStatus(String transactionId) throws JAXBException, BlueviaException {

    	GetPaymentStatusParamsType params = new GetPaymentStatusParamsType();
    	params.setTransactionId(transactionId);
    	
        // build the RPC call
        ObjectFactory objectFactory = new ObjectFactory();
        MethodCallType mct = objectFactory.createMethodCallType();
        mct.setVersion(RPC_VERSION);
        mct.setMethod(MethodType.GET_PAYMENT_STATUS);
        mct.setId(generateRandomId());
        Params p = new Params();
        p.setGetPaymentStatusParams(params);
       
        mct.setParams(p);

        JAXBElement<MethodCallType> element = objectFactory.createMethodCall(mct);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        this.m.marshal(element, os);
        
        String url = this.uri + PAYMENT_STATUS_URI;

        String res = this.rpcConnector.postHTTPS(url, os.toByteArray());

        JAXBContext ctx = JAXBContext.newInstance(MethodResponseType.class);
        Unmarshaller um = ctx.createUnmarshaller();
        JAXBElement<MethodResponseType> mrt = um.unmarshal(new StreamSource(new StringReader(res)), MethodResponseType.class);
        
        return mrt.getValue().getResult().getGetPaymentStatusResult();
    }
    
    private String generateRandomId(){
    	return String.valueOf(new Random().nextInt(999999));
    }
    
}
