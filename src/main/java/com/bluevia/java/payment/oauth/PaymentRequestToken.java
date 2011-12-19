package com.bluevia.java.payment.oauth;

import java.util.Hashtable;

import javax.xml.bind.JAXBException;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthProviderListener;
import oauth.signpost.http.HttpRequest;
import oauth.signpost.http.HttpResponse;

import com.bluevia.java.AbstractClient.Mode;
import com.bluevia.java.Utils;
import com.bluevia.java.oauth.OAuthToken;
import com.bluevia.java.oauth.RequestToken;
import com.bluevia.java.payment.ServiceInfo;
import com.telefonica.schemas.unica.rpc.payment.v1.PaymentInfoType;
import com.telefonica.schemas.unica.rpc.payment.v1.PaymentParamsType;

public class PaymentRequestToken extends RequestToken {

	private static final String API_NAME = "Payment";
	private static final String API_NAME_SANDBOX = "Payment_Sandbox";
	
	private static final String PAYMENT_INFO = "paymentInfo.";
	private static final String PINFO_AMOUNT = PAYMENT_INFO + "amount";
	private static final String PINFO_CURRENCY = PAYMENT_INFO + "currency";

	private static final String SERVICE_INFO = "serviceInfo.";
	private static final String SINFO_SERVICE_ID = SERVICE_INFO + "serviceID";
	private static final String SINFO_NAME = SERVICE_INFO + "name";
	private static final String SINFO_DESCRIPTION = SERVICE_INFO + "description";

	private byte[] data;
	
	private Mode apiMode;
	
	/**
	 * 
	 * @param consumer 
	 * @param mode the working mode (Live/Sandbox)
	 * @throws JAXBException
	 */
	public PaymentRequestToken(OAuthToken consumer, Mode mode) throws JAXBException {
		super(consumer);
		apiMode = mode;
	}
	
	/**
	 * Gets a RequestToken for a Payment operation
	 * @param callback the callback for the get request token. Available values are:
     * <ul>
     * 	<li>An url: if your app can receive callbacks and you want to get informed about the result of the authorization process</li>
     * 	<li>NULL: if your app cannot receive callbacks</li>
     * </ul>
	 * @param params the Payment parameters of the operation (amount and currency)
	 * @param serviceInfo the info of the service or product to be paid
	 * 
	 * @return the request token
	 */
	public OAuthToken getPaymentRequestToken(String callback, PaymentParamsType params,
			ServiceInfo serviceInfo) {
		
		if (params == null || params.getPaymentInfo() == null)
			throw new IllegalArgumentException("Payment params cannot be null");

		if (serviceInfo == null)
			throw new IllegalArgumentException("Service info cannot be null");
		
    	//Callback validation
    	if (!Utils.isEmpty(callback)){
    		if (!Utils.isUrl(callback) && !callback.equals(OAuth.OUT_OF_BAND)){
        		throw new IllegalArgumentException("Invalid parameter: callback.");
    		}
    	}
		
		data = prepareBodyParameters(params.getPaymentInfo(), serviceInfo);
		
		OAuthProviderListener listener = new OAuthProviderListener() {
			
			@Override
			public void prepareSubmission(HttpRequest request) throws Exception {}
			
			@Override
			public void prepareRequest(HttpRequest request) throws Exception {
				
				//Include apiName header
				String apiName = "";
				switch (apiMode){
				case LIVE:
					apiName = API_NAME;
					break;
				case SANDBOX:
					apiName = API_NAME_SANDBOX;
					break;
				}
				
				request.setHeader("Authorization", "OAuth " + 
						BlueviaAuthorizationHeaderSigningStrategy.OAUTH_API_NAME + "=\"" + 
						apiName + "\"");
			}
			
			@Override
			public boolean onResponseReceived(HttpRequest request, HttpResponse response) throws Exception {
				handleErrors(request, response);
				return false;
			}
		};
        
        return getRequestToken(callback, data, listener);
	}
	
	private byte[] prepareBodyParameters(PaymentInfoType paymentInfo, ServiceInfo serviceInfo){
		
		Hashtable<String, String> params = new Hashtable<String, String>();
		
		//ServiceInfo
		params.put(SINFO_SERVICE_ID, serviceInfo.getId());
		params.put(SINFO_NAME, serviceInfo.getName());
		if (!Utils.isEmpty(serviceInfo.getDescription())){
			params.put(SINFO_DESCRIPTION, serviceInfo.getDescription());
		}
		
		//Mandatory params
		params.put(PINFO_AMOUNT, String.valueOf(paymentInfo.getAmount()));
		params.put(PINFO_CURRENCY, paymentInfo.getCurrency());
		
		return Utils.toHttpQueryString(params).getBytes();
	}
	
}
