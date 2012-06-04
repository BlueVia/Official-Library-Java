package com.bluevia.payment.data;

import com.bluevia.commons.Utils;
import com.telefonica.schemas.unica.rpc.payment.v1.MethodResponseType;

/**
 * @class PaymentResult 
 * Class representing the PaymentResult type
 *
 * This type is composed of the following fields:
 * <ul>
 * <li> trasanctionId; mandatory: String</li>
 * <li> transactionStatus; mandatory: TransactionEnumerationType</li>
 * <li> transactionStatusDescription; mandatory: String</li>
 * </ul>
 */
public class PaymentResult extends PaymentStatus {

	protected String mTransactionId;

	/**
	 * Gets the mandatory transaction identifier of the payment result	
	 * 
	 * @return the mandatory transaction identifier of the payment result
	 */
	public String getTransactionId(){
		return mTransactionId;
	}

	/**
	 * Sets the mandatory transaction identifier of the payment result	
	 * 
	 * @param transactionId transaction identifier of the payment result
	 * @return this
	 */
	public PaymentResult setTransactionId(String transactionId){
		mTransactionId = transactionId;
		return this;
	}
	
	public boolean isValid(){
		return !Utils.isEmpty(mTransactionId) && super.isValid();
	}

	public PaymentResult (MethodResponseType response) {
		setTransactionId(response.getResult().getPaymentResult().getTransactionId());
		String status = response.getResult().getPaymentResult().getTransactionStatus();
		setTransactionStatus(PaymentStatus.translateStatus(status));
		setTransactionStatusDescription(response.getResult().getPaymentResult().getTransactionStatusDescription());
	}
	
}
