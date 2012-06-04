package com.bluevia.payment.data;

import com.bluevia.commons.Entity;

/**
 * @class PaymentStatus 
 * Class representing the PaymentStatus type
 *
 * This type is composed of the following fields:
 * <ul>
 * <li> transactionStatus; mandatory: TransactionStatusType</li>
 * <li> transactionStatusDescription; mandatory: String</li>
 * </ul>
 */
public class PaymentStatus implements Entity {

	/**
	 * @enum PaymentResult::TransactionStatusType
	 * Information about the final status of payment transaction. 
	 * @var PaymentResult::TransactionStatusType PaymentResult::SUCCESS:
	 * Transaction has had successful result
	 * @var PaymentResult::TransactionStatusType PaymentResult::FAILURE:
	 * Transaction result has been a failure
	 * @var PaymentResult::TransactionStatusType PaymentResult::PENDING:
	 * Transaction is pending
	 */
	public enum TransactionStatusType { SUCCESS, FAILURE, PENDING };	

	private static String SUCCESS_ST = "Success";
	private static String FAILURE_ST = "Failure";
	private static String PENDING_ST = "Pending";

	protected TransactionStatusType mTransactionStatus;
	protected String mTransactionStatusDescription;

	/**
	 * Gets the mandatory transaction status of the payment result
	 * 
	 * @return the transaction status of the payment session
	 */
	public TransactionStatusType getTransactionStatus(){
		return mTransactionStatus;
	}

	/**
	 * Sets the mandatory transaction status of the payment result	
	 * 
	 * @param transactionStatus the mandatory transaction status of the payment result
	 */
	public void setTransactionStatus(TransactionStatusType transactionStatus){
		mTransactionStatus = transactionStatus;
	}

	/**
	 * Sets the mandatory transaction status description of the payment result	
	 * 
	 * @param  transactionStatusDescription the transaction status description of the payment result
	 * @return this
	 */
	public void setTransactionStatusDescription(String transactionStatusDescription){
		mTransactionStatusDescription = transactionStatusDescription;
	}

	/**
	 * Gets the mandatory transaction status description of the payment result	
	 * 
	 * @return the transaction status description of the payment result
	 */
	public String getTransactionStatusDescription(){
		return mTransactionStatusDescription;
	}

	public boolean isValid() {
		return mTransactionStatus != null;
	}

	public static TransactionStatusType translateStatus(String status){
		if (status.equalsIgnoreCase(SUCCESS_ST))
			return TransactionStatusType.SUCCESS;
		else if (status.equalsIgnoreCase(FAILURE_ST))
			return TransactionStatusType.FAILURE;
		else if (status.equalsIgnoreCase(PENDING_ST))
			return TransactionStatusType.PENDING;
		return null;
	}

}
