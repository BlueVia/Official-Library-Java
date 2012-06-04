/**
 * @package com.bluevia.payment.data This package contains entity data types of Bluevia Payment Management Service
 */
package com.bluevia.payment.data;

/**
* @author Telefonica R&D
* 
*/
import com.bluevia.commons.Entity;
import com.bluevia.commons.Utils;

/**
 * @class PaymentInfo
 * Class representing the PaymentInfo type
 *
 * This type is composed of the following fields:
 * <ul>
 * <li> amount; mandatory: Integer</li>
 * <li> currency; mandatory: String</li>
 * </ul>
 */
public class PaymentInfo implements Entity {
	
	protected int mAmount;
	protected String mCurrency;
	
	public PaymentInfo(int amount, String currency){
		mAmount = amount;
		mCurrency = currency;
	}
	
	/**
	 * Gets the mandatory amount of the payment session	
	 * 
	 * @return the amount of the payment session
	 */
	public int getAmount(){
		return mAmount;
	}

	/**
	 * Sets the mandatory amount of the payment session	
	 * 
	 * @param amount the amount of the payment session
	 */
	public void setAmount(int amount){
		mAmount = amount;
	}

	/**
	 * Gets the mandatory currency of the payment session	
	 * 
	 * @return the currency of the payment session
	 */
	public String getCurrency(){
		return mCurrency;
	}

	/**
	 * Sets the mandatory currency of the payment session	
	 * 
	 * @param currency the currency of the payment session
	 */
	public void setCurrency(String currency){
		mCurrency = currency;
	}

	@Override
	public boolean isValid() {
		return !Utils.isEmpty(mCurrency) && mAmount >= 0;	//0 is valid
	}

	
	
}
