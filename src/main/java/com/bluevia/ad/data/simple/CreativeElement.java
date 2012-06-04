package com.bluevia.ad.data.simple;

import com.bluevia.ad.data.AdRequest.Type;
import com.bluevia.commons.Entity;
import com.bluevia.commons.Utils;

/**
 * 
 * Object that describes a simple representation of the elements of the advertising.
 * It includes the following attributes:
 * <ul>
 * <li>Type: Category of the ad. Object. Values include image and text.</li>
 * <li>Value: The textual representation of the advertising (maybe a message for a text ad, or the url in case of an image ad).</li>
 * <li>Interaction: Tag used to invoke events, e.g: it contains the locator of click through URI. </li>
 * </ul>
 * 
 * @author Telefonica R&D
 *
 */
public class CreativeElement implements Entity {

	private Type mType;
	private String mValue;
	private String mInteraction;
	
	/**
	 * Constructor
	 * 
	 * @param type
	 * @param value
	 * @param interaction
	 */
	public CreativeElement(Type type, String value, String interaction){
		mType = type;
		mValue = value;
		mInteraction = interaction;
	}
	
	/**
	 * @return the type
	 */
	public Type getType() {
		return mType;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(Type type) {
		this.mType = type;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return mValue;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.mValue = value;
	}


	/**
	 * @return the interaction
	 */
	public String getInteraction() {
		return mInteraction;
	}

	/**
	 * @param mInteraction the interaction to set
	 */
	public void setInteraction(String interaction) {
		this.mInteraction = interaction;
	}

	@Override
	public boolean isValid() {
		return mType != null && !Utils.isEmpty(mValue) && !Utils.isEmpty(mInteraction);
	}

}
