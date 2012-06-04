/**
 * \package com.bluevia.payment.client This package contains the clients of Bluevia Payment Service
 */
package com.bluevia.payment.client;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.apache.log4j.Logger;

import oauth.signpost.OAuthProviderListener;
import oauth.signpost.http.HttpRequest;
import oauth.signpost.http.HttpResponse;

import com.bluevia.commons.Encoding;
import com.bluevia.commons.Entity;
import com.bluevia.commons.GsdpConstants;
import com.bluevia.commons.Utils;
import com.bluevia.commons.connector.http.oauth.BVAuthorizationHeaderSigningStrategy;
import com.bluevia.commons.connector.http.oauth.IOAuth;
import com.bluevia.commons.connector.http.oauth.OAuthToken;
import com.bluevia.commons.connector.http.oauth.RequestToken;
import com.bluevia.commons.data.JaxbEntity;
import com.bluevia.commons.exception.BlueviaException;
import com.bluevia.commons.parser.xml.XmlConstants;
import com.bluevia.commons.parser.xml.XmlParser;
import com.bluevia.oauth.client.BVOauthClient;
import com.bluevia.payment.data.PaymentInfo;
import com.bluevia.payment.data.PaymentRequestTokenParams;
import com.bluevia.payment.data.PaymentResult;
import com.bluevia.payment.data.PaymentStatus;
import com.bluevia.payment.data.ServiceInfo;
import com.bluevia.payment.parser.PaymentSerializer;
import com.telefonica.schemas.unica.rpc.common.v1.SimpleReferenceType;
import com.telefonica.schemas.unica.rpc.payment.v1.GetPaymentStatusParamsType;
import com.telefonica.schemas.unica.rpc.payment.v1.MethodCallType;
import com.telefonica.schemas.unica.rpc.payment.v1.MethodResponseType;
import com.telefonica.schemas.unica.rpc.payment.v1.MethodType;
import com.telefonica.schemas.unica.rpc.payment.v1.PaymentInfoType;
import com.telefonica.schemas.unica.rpc.payment.v1.PaymentParamsType;
import com.telefonica.schemas.unica.rpc.payment.v1.MethodCallType.Params;


/**
 *  This class provides access to the set of functions which any user could use to access
 *  the Payment service functionality
 *
 * @author Telefonica R&D
 *
 */
public class BVPayment extends BVOauthClient {

	private static Logger log = Logger.getLogger(BVPayment.class.getName());

	private static final String FEED_PAYMENT_BASE_URI = "/RPC/Payment";

	protected static final String FEED_PAYMENT = "/payment";
	protected static final String FEED_CANCEL_AUTHORIZATION = "/cancelAuthorization";
	protected static final String FEED_GET_PAYMENT_STATUS = "/getPaymentStatus";

	protected static final String OAUTH_API_NAME = "Payment";
	protected static final String OAUTH_API_NAME_SANDBOX = "Payment_Sandbox";

	protected static final String HEADER_TIMESTAMP = "oauth_timestamp";

	/**
	 * Constructor 
	 * 
	 * @param mode
	 * @param consumerToken
	 * @param consumerSecret
	 * @throws BlueviaException
	 */
	public BVPayment(Mode mode, String consumerToken, String consumerSecret) throws BlueviaException {
		super(mode, consumerToken, consumerSecret);	//Calling super to initialize OauthClient

		if (mode == Mode.TEST)
			throw new BlueviaException("Payment API only available in LIVE and SANDBOX mode", BlueviaException.INVALID_MODE_EXCEPTION);

		mEncoding = Encoding.APPLICATION_XML;
		mParser = new XmlParser();
		mSerializer = new PaymentSerializer();

		mBaseUri += buildUri(mMode, FEED_PAYMENT_BASE_URI);	//Building base uri
	}

	/**
	 * Gets a RequestToken for a Payment operation
	 * 
	 * @param amount  the cost of the digital good being sold, expressed in the minimum fractional monetary unit of the currency reflected in the next parameter (to avoid decimal digits). 
	 * @param currency the currency of the payment, following ISO 4217 (EUR, GBP, MXN, etc.). 
	 * @param name the name of the service for the payment
	 * @param serviceId the id of the service for the payment
	 * @return the request token
	 * @throws BlueviaException
	 */
	public RequestToken getPaymentRequestToken(int amount, String currency, String name, String serviceId) throws BlueviaException {

		//Reset token 
		((IOAuth)mConnector).setOauthToken(null);

		ServiceInfo serviceInfo = new ServiceInfo(serviceId, name);
		PaymentInfo info = new PaymentInfo(amount, currency);

		PaymentRequestTokenParams request = new PaymentRequestTokenParams(serviceInfo, info);

		if (!request.isValid())
			throw new BlueviaException("Bad request: Please, check function parameters", BlueviaException.BAD_REQUEST_EXCEPTION);

		byte[] data = serializeEntity(request);


		OAuthProviderListener listener = new BVPayentOauthProviderListener();

		return super.getRequestToken(null, data, listener);
	}

	/**
	 * OAuthProviderListener to manage the body data in the getPaymentRequestToken operation.
	 *
	 */
	private class BVPayentOauthProviderListener implements OAuthProviderListener {

		@Override
		public void prepareSubmission(HttpRequest request) throws Exception {}

		@Override
		public void prepareRequest(HttpRequest request) throws Exception {

			//Include apiName header
			String apiName = "";
			switch (mMode){
			case LIVE:
			case TEST:
				apiName = OAUTH_API_NAME;
				break;
			case SANDBOX:
				apiName = OAUTH_API_NAME_SANDBOX;
				break;
			}

			request.setHeader("Authorization", "OAuth " + 
					BVAuthorizationHeaderSigningStrategy.OAUTH_API_NAME + "=\"" + 
					apiName + "\"");
		}

		@Override
		public boolean onResponseReceived(HttpRequest request, HttpResponse response) throws Exception {
			return false;
		}
	}

	/**
	 * Gets an access token for a Payment operation
	 * 
	 * @param oauthVerifier the OAuth verifier for the token
	 * @param token the request token previously obtained
	 * @param secret request token secret
	 * @return the access token
	 * @throws BlueviaException
	 */
	public OAuthToken getPaymentAccessToken(String oauthVerifier, String token, String secret) throws BlueviaException {
		OAuthToken accessToken = super.getAccessToken(oauthVerifier, token, secret);
		((IOAuth)mConnector).setOauthToken(accessToken);
		return accessToken;
	}


	/**
	 * 
	 * Orders an economic charge on the user's operator account.
	 * 
	 * @param amount Amount to be charged, it may be an economic amount or a number of 'virtual units' (points, tickets, etc) (mandatory).
	 * @param currency Type of currency which corresponds with the amount above, following ISO 4217 (mandatory).
	 * @return Result of the payment operation.
	 * @throws BlueviaException An exception informing about errors encountered in the request/response.
	 * @throws IOException 
	 */
	public PaymentResult payment(int amount, String currency) throws BlueviaException, IOException {
		return payment(amount, currency, null, null);
	}

	/**
	 * 
	 * Orders an economic charge on the user's operator account.
	 * @param amount Amount to be charged, it may be an economic amount or a number of 'virtual units' (points, tickets, etc) (mandatory).
	 * @param currency Type of currency which corresponds with the amount above, following ISO 4217 (mandatory).
	 * @param endpoint the endpoint to receive notifications of the payment operation
	 * @param correlator the correlator
	 * @return Result of the payment operation.
	 * @throws BlueviaException An exception informing about errors encountered in the request/response.
	 * @throws IOException 
	 */
	public PaymentResult payment(int amount, String currency, String endpoint, String correlator) throws BlueviaException, IOException {

		Entity response;

		//Timestamp
		Format formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date date = new Date();
		String timestamp = formatter.format(date);
		String millis = String.valueOf(date.getTime()/1000);

		HashMap<String, String> additionalParams = new HashMap<String, String>();
		additionalParams.put(HEADER_TIMESTAMP, millis);
		((IOAuth)mConnector).setAdditionalParameters(additionalParams);

		MethodCallType methodCallType = getMethodCallTypePayment(amount, currency, timestamp, endpoint, correlator);

		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put(GsdpConstants.HEADER_CONTENT_TYPE, getEncoding(mEncoding));

		HashMap<String, String> queryParams = new HashMap<String, String>();
		queryParams.put(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);

		JaxbEntity messageEntity= new JaxbEntity();
		messageEntity.setJcInstance(XmlConstants.XSD_PAYMENT_API_INSTANCE);
		messageEntity.setNamespace(XmlConstants.NS_PAYMENT_API_URI);
		messageEntity.setQname(XmlConstants.XSD_RPC_METHOD_CALL);
		messageEntity.setObject(methodCallType);

		((XmlParser) mParser).setParseClass(MethodResponseType.class);
		response = this.create(FEED_PAYMENT, messageEntity, queryParams, headers);

		//Check if response is instance of JaxbEntity
		if ((response == null) || (! (response instanceof JaxbEntity)))
			throw new BlueviaException("Error during request. Response received does not correspond to an MethodResponseType",
					BlueviaException.INTERNAL_CLIENT_ERROR);
		JaxbEntity parseEntity= (JaxbEntity) response;

		//Check if parseEntity is instance of MethodResponseType
		MethodResponseType methodResponseType= new MethodResponseType();
		if ((parseEntity.getObject() == null) || (! (parseEntity.getObject() instanceof MethodResponseType)))
			throw new BlueviaException("Error during request. Response received does not correspond to an MethodResponseType",
					BlueviaException.INTERNAL_CLIENT_ERROR);
		methodResponseType = (MethodResponseType) parseEntity.getObject();


		if ((methodResponseType == null) || (!(methodResponseType instanceof MethodResponseType)))
			throw new BlueviaException("Error during request. Response received does not correspond to a " + 
					PaymentResult.class.getName(), BlueviaException.INTERNAL_CLIENT_ERROR);

		PaymentResult paymentResult = new PaymentResult(methodResponseType);

		return paymentResult;
	}

	/**
	 * Merchant can use this operation to invalidate the access token of the session.
	 * If the payment has been made before, it remains valid, but no more getPaymentStatus operations will be enabled.
	 * 
	 * @throws IOException 
	 * @throw BlueviaException An exception informing about errors encountered in the request/response.
	 */
	public void cancelAuthorization() throws BlueviaException, IOException {

		MethodCallType methodCallType = getMethodCallTypeCancel();

		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put(GsdpConstants.HEADER_CONTENT_TYPE, getEncoding(mEncoding));

		HashMap<String, String> queryParams = new HashMap<String, String>();
		queryParams.put(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);

		JaxbEntity messageEntity= new JaxbEntity();
		messageEntity.setJcInstance(XmlConstants.XSD_PAYMENT_API_INSTANCE);
		messageEntity.setNamespace(XmlConstants.NS_PAYMENT_API_URI);
		messageEntity.setQname(XmlConstants.XSD_RPC_METHOD_CALL);
		messageEntity.setObject(methodCallType);

		((XmlParser) mParser).setParseClass(MethodResponseType.class);
		Entity response = this.create(FEED_CANCEL_AUTHORIZATION, messageEntity, queryParams, headers);

		//Check if response is instance of JaxbEntity
		if ((response == null) || (! (response instanceof JaxbEntity)))
			throw new BlueviaException("Error during request. Response received does not correspond to an MethodResponseType",
					BlueviaException.INTERNAL_CLIENT_ERROR);
		JaxbEntity parseEntity= (JaxbEntity) response;

		//Check if parseEntity is instance of MethodResponseType
		MethodResponseType methodResponseType= new MethodResponseType();
		if ((parseEntity.getObject() == null) || (! (parseEntity.getObject() instanceof MethodResponseType)))
			throw new BlueviaException("Error during request. Response received does not correspond to an MethodResponseType",
					BlueviaException.INTERNAL_CLIENT_ERROR);
		methodResponseType = (MethodResponseType) parseEntity.getObject();


		if ((methodResponseType == null) || (!(methodResponseType instanceof MethodResponseType)))
			throw new BlueviaException("Error during request. Response received does not correspond to a " + 
					PaymentResult.class.getName(), BlueviaException.INTERNAL_CLIENT_ERROR);

	}

	/**
	 * Retrieves the status of a previous payment operation
	 * 
	 * @param transactionId the id of the transaction
	 * @return the status of the payment
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	public PaymentStatus getPaymentStatus(String transactionId) throws BlueviaException, IOException{

		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put(GsdpConstants.HEADER_CONTENT_TYPE, getEncoding(mEncoding));

		HashMap<String, String> queryParams = new HashMap<String, String>();
		queryParams.put(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);

		Entity response = null;
		MethodCallType methodCallType = getMethodCallTypeStatus(transactionId);

		JaxbEntity messageEntity= new JaxbEntity();
		messageEntity.setJcInstance(XmlConstants.XSD_PAYMENT_API_INSTANCE);
		messageEntity.setNamespace(XmlConstants.NS_PAYMENT_API_URI);
		messageEntity.setQname(XmlConstants.XSD_RPC_METHOD_CALL);
		messageEntity.setObject(methodCallType);

		((XmlParser) mParser).setParseClass(MethodResponseType.class);
		response = this.create(FEED_GET_PAYMENT_STATUS, messageEntity, queryParams, headers);

		//Check if response is instance of JaxbEntity
		if ((response == null) || (! (response instanceof JaxbEntity)))
			throw new BlueviaException("Error during request. Response received does not correspond to an MethodResponseType",
					BlueviaException.INTERNAL_CLIENT_ERROR);
		JaxbEntity parseEntity= (JaxbEntity) response;

		//Check if parseEntity is instance of MethodResponseType
		MethodResponseType methodResponseType= new MethodResponseType();
		if ((parseEntity.getObject() == null) || (! (parseEntity.getObject() instanceof MethodResponseType)))
			throw new BlueviaException("Error during request. Response received does not correspond to an MethodResponseType",
					BlueviaException.INTERNAL_CLIENT_ERROR);
		methodResponseType = (MethodResponseType) parseEntity.getObject();


		if ((methodResponseType == null) || (!(methodResponseType instanceof MethodResponseType)))
			throw new BlueviaException("Error during request. Response received does not correspond to a " + 
					PaymentResult.class.getName(), BlueviaException.INTERNAL_CLIENT_ERROR);

		PaymentStatus paymentStatus= new PaymentStatus();
		paymentStatus.setTransactionStatus(PaymentStatus.TransactionStatusType.valueOf(methodResponseType.getResult().getGetPaymentStatusResult().getTransactionStatus()));
		paymentStatus.setTransactionStatusDescription(methodResponseType.getResult().getGetPaymentStatusResult().getTransactionStatusDescription());

		return paymentStatus;
	}

	/**
	 * Sets the access token of the session
	 * This functions is used to change the token of the session to be able to get the
	 * payment status of an old operation, or cancel the authorization of a token.
	 * 
	 * @param accessToken
	 */
	public void setAccessToken(OAuthToken accessToken){
		((IOAuth)mConnector).setOauthToken(accessToken);
	}

	private MethodCallType getMethodCallTypePayment(int amount, String currency, String timestamp, String endpoint, String correlator) throws BlueviaException {
		MethodCallType methodCallType= new MethodCallType();

		String mId= String.valueOf(new Random().nextInt(99999));
		methodCallType.setId(mId);

		methodCallType.setMethod(MethodType.PAYMENT);

		Params params= new Params();
		PaymentParamsType ppt= new PaymentParamsType();
		PaymentInfoType pit= new PaymentInfoType();
		pit.setAmount(new BigDecimal(Float.toString(amount)));
		pit.setCurrency(currency);
		ppt.setPaymentInfo(pit);
		if (endpoint!=null && !(Utils.isEmpty(correlator))) {
			SimpleReferenceType simple= new SimpleReferenceType();
			simple.setCorrelator(correlator);
			simple.setEndpoint(endpoint);
			ppt.setReceiptRequest(simple);
		}
		try {
			ppt.setTimestamp(DatatypeFactory.newInstance().newXMLGregorianCalendar(timestamp));
		} catch (DatatypeConfigurationException e) {
			log.error("Invalid timestamp: " + e.getMessage());
			throw new BlueviaException(e.getMessage(), e, BlueviaException.INTERNAL_CLIENT_ERROR);
		}
		params.setPaymentParams(ppt);

		methodCallType.setParams(params);

		methodCallType.setVersion(XmlConstants.VERSION_1);

		return methodCallType;
	}

	private MethodCallType getMethodCallTypeCancel() {
		MethodCallType methodCallType= new MethodCallType();
		String mId= String.valueOf(new Random().nextInt(99999));
		methodCallType.setId(mId);

		methodCallType.setId(mId);
		methodCallType.setMethod(MethodType.CANCEL_AUTHORIZATION);		
		methodCallType.setVersion(XmlConstants.VERSION_1);

		return methodCallType;
	}


	private MethodCallType getMethodCallTypeStatus(String transactionId) {
		MethodCallType methodCallType= new MethodCallType();

		String mId= String.valueOf(new Random().nextInt(99999));
		methodCallType.setId(mId);

		methodCallType.setMethod(MethodType.GET_PAYMENT_STATUS);
		Params params= new Params();
		GetPaymentStatusParamsType gpsp= new GetPaymentStatusParamsType();
		gpsp.setTransactionId(transactionId);
		params.setGetPaymentStatusParams(gpsp);					
		methodCallType.setParams(params);

		methodCallType.setVersion(XmlConstants.VERSION_1);

		return methodCallType;
	}

}
