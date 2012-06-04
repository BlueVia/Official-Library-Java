package com.bluevia.payment.data;

import com.bluevia.commons.Entity;

public class PaymentRequestTokenParams implements Entity {

	private ServiceInfo mServiceInfo;
	private PaymentInfo mPaymentInfo;
	
	public PaymentRequestTokenParams(ServiceInfo serviceInfo, PaymentInfo paymentInfo){
		mServiceInfo = serviceInfo;
		mPaymentInfo = paymentInfo;
	}
	
	/**
	 * @return the paymentInfo
	 */
	public PaymentInfo getPaymentInfo() {
		return mPaymentInfo;
	}
	
	/**
	 * @param paymentInfo the paymentInfo to set
	 */
	public void setPaymentInfo(PaymentInfo paymentInfo) {
		this.mPaymentInfo = paymentInfo;
	}
	
	/**
	 * @return the serviceInfo
	 */
	public ServiceInfo getServiceInfo() {
		return mServiceInfo;
	}
	
	/**
	 * @param serviceInfo the serviceInfo to set
	 */
	public void setServiceInfo(ServiceInfo serviceInfo) {
		this.mServiceInfo = serviceInfo;
	}
	
	@Override
	public boolean isValid() {
		return mPaymentInfo != null && mPaymentInfo.isValid() && 
			mServiceInfo != null && mServiceInfo.isValid();
			
	}
	
}
