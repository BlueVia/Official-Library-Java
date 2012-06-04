package com.bluevia.payment.data;

import com.bluevia.commons.Entity;

/**
 * Class representing the information of the service to be paid 
 *
 */
public class ServiceInfo implements Entity {
	
	private String mServiceId;
	private String mServiceName;
	private String mServiceDescription;
	
	/**
	 * Constructor
	 * 
	 * @param id id of the service
	 * @param name name of the service
	 */
	public ServiceInfo(String id, String name){
		mServiceId = id;
		mServiceName = name;
	}	
	
	/**
	 * Constructor
	 * 
	 * @param id id of the service
	 * @param name name of the service
	 * @param description the description of the service
	 */
	public ServiceInfo(String id, String name, String description){
		this(id, name);
		mServiceDescription = description;
	}
	
	/**
	 * @return the serviceId
	 */
	public String getServiceId() {
		return mServiceId;
	}
	
	/**
	 * @param serviceId the serviceId to set
	 */
	public void setServiceId(String serviceId) {
		this.mServiceId = serviceId;
	}
	
	/**
	 * @return the name
	 */
	public String getServiceName() {
		return mServiceName;
	}
	
	/**
	 * @param description the description to set
	 */
	public void setServiceDescription(String description) {
		this.mServiceDescription = description;
	}
	
	/**
	 * @return the description
	 */
	public String getServiceDescription() {
		return mServiceDescription;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setServiceName(String name) {
		this.mServiceName = name;
	}

	@Override
	public boolean isValid() {
		return mServiceName != null && mServiceName.trim().length() != 0 &&
		mServiceId != null && mServiceId.trim().length() != 0;
	}

}
