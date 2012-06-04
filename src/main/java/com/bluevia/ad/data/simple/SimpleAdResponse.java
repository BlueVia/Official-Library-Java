package com.bluevia.ad.data.simple;

import java.util.ArrayList;

import com.bluevia.commons.Entity;
import com.bluevia.commons.Utils;

/**
 * 
 * Object that describes a simple representation of the advertising.
 * It includes the following attributes:
 * <ul>
 * <li>Request Id: The id of the request for the advertising.</li>
 * <li>Advertising list: A list of CreativeElement objects representing the advertising.</li>
 * </ul>
 * 
 * @author Telefonica R&D
 *
 */
public class SimpleAdResponse implements Entity {

	private String mRequestId;
	private ArrayList<CreativeElement> mAdvertisingList = new ArrayList<CreativeElement>();
	
	/**
	 * Gets the request id
	 * 
	 * @return the request id
	 */
	public String getRequestId(){
		return mRequestId;
	}
	
	/**
	 * Sets the request id
	 * 
	 * @param requestId the request id
	 */
	public void setRequestId(String requestId){
		mRequestId = requestId;
	}
	
	/**
	 * Returns the advertising list
	 * 
	 * @return the advertising list
	 */
	public ArrayList<CreativeElement> getAdvertisingList() {
		return mAdvertisingList;
	}

	/**
	 * @param advertisingList the advertising list to set
	 */
	public void setAdvertisingList(ArrayList<CreativeElement> advertisingList) {
		this.mAdvertisingList = advertisingList;
	}
	
	/**
	 * @param advertisingList the advertising list to set
	 */
	public void addAdvertising(CreativeElement creativeElement) {
		if (mAdvertisingList == null)
			mAdvertisingList = new ArrayList<CreativeElement>();
		mAdvertisingList.add(creativeElement);
	}

	@Override
	public boolean isValid() {
		
		for (CreativeElement e : mAdvertisingList)
			if (!e.isValid())
				return false;
		
		return !Utils.isEmpty(mRequestId);
	}

}
