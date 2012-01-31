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
	
	/**
	 * 
	 * @param consumer 
	 * @param mode the working mode (Live/Sandbox)
	 * @throws JAXBException
	 */
	public PaymentRequestToken(OAuthToken consumer, Mode mode) throws JAXBException {
		super(consumer, mode);
	}
	
	/**
	 * Gets a RequestToken for a Payment operation
	 * @param callback the callback for the get request token. Available values are:
     * <ul>
     * 	<li>An url: if your app can receive callbacks and you want to get informed about the result of the authorization process</li>
     * 	<li>NULL: if your app cannot receive callbacks</li>
     * </ul>
	 * @param amount  the cost of the digital good being sold, expressed in the minimum fractional monetary unit of the currency reflected in the next parameter (to avoid decimal digits). 
	 * @param currency the currency of the payment, following ISO 4217 (EUR, GBP, MXN, etc.). 
	 * @param serviceInfo the info of the service or product to be paid
	 * @return the request token
	 */
	public OAuthToken getPaymentRequestToken(String callback, int amount,
			String currency, ServiceInfo serviceInfo) {
		
		if (amount <= 0)
			throw new IllegalArgumentException("Invalid parameter: amount");

		if (Utils.isEmpty(currency))
			throw new IllegalArgumentException("Invalid parameter: currency");

		if (serviceInfo == null)
			throw new IllegalArgumentException("Service info cannot be null");
		
		//Callback validation
    	if (!Utils.isEmpty(callback)){	//Null and empty are valid (changed by "oob")
    		
    		//Other values than URLs and "oob" and are invalid
    		//Phone number are not allowed for Payment
    		if (!Utils.isUrl(callback) && !callback.equals(OAuth.OUT_OF_BAND)){
        		throw new IllegalArgumentException("Invalid parameter: callback.");
    		}
    	}
		
		data = prepareBodyParameters(amount, currency, serviceInfo);
		
		OAuthProviderListener listener = new OAuthProviderListener() {
			
			@Override
			public void prepareSubmission(HttpRequest request) throws Exception {}
			
			@Override
			public void prepareRequest(HttpRequest request) throws Exception {
				
				//Include apiName header
				String apiName = "";
				switch (mode){
				case LIVE:
				case TEST:
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
	
	private byte[] prepareBodyParameters(int amount, String currency, ServiceInfo serviceInfo){
		
		Hashtable<String, String> params = new Hashtable<String, String>();
		
		//ServiceInfo
		params.put(SINFO_SERVICE_ID, serviceInfo.getId());
		params.put(SINFO_NAME, serviceInfo.getName());
		if (!Utils.isEmpty(serviceInfo.getDescription())){
			params.put(SINFO_DESCRIPTION, serviceInfo.getDescription());
		}
		
		//Mandatory params
		params.put(PINFO_AMOUNT, String.valueOf(amount));
		params.put(PINFO_CURRENCY, currency);
		
		return Utils.toHttpQueryString(params).getBytes();
	}
	
}
