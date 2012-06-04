package com.bluevia.ad.data;

import com.bluevia.commons.Entity;

/**
 * * Object that describes an attribute of a Creative Element or an Interaction
 * <ul>
 * <li>Type: Type of the attribute of the ad object or of interaction attribute.
 * Possible values include: adtext, locator, url.</li>
 * <li>Value: Value of the attribute of the ad object or of interaction attribute.
 * adtext, locator or url value.</li>
 * </ul>
 *
 * @author Telefonica R&D
 * 
 *
 */
public class AdsAttribute implements Entity {

    /**
     * 
     *  Type that contains the possible attributes type elements that can be received. Values includes:
     *  <ul>
     *  <li>Adtext: used for example as an attribute of a Text Creative Element, to set the text value of the ad</li>
     *  <li>Codec: used for example as an attribute of a Text Creative Element, to set the encoding of the text value of the ad</li>
     *  <li>Locator: used for example as an attribute of an Image Creative Element, to set the url to be able to get the image of the ad</li>
     *  <li>Url: used for example as an attribute of an ClickToWap interaction, to set the url to be lauch when the user clicks over the ad.</li>
     *  </ul>
     *
     */
    public enum Type {ADTEXT, LOCATOR, URL, CODEC};
    
    private Type mType = null;

    private String mValue;
    
    /**
     * Gets the attribute Type
     *
     * @return the attribute type
     */
    public Type getType() {
        return mType;
    }

    /**
     * Sets the attribute type
     *
     * @param type
     */
    public void setType(Type type) {
        this.mType = type;
    }

    /**
     * Gets attribute value
     *
     * @return the attribute value
     */
    public String getValue() {
        return mValue;
    }

    /**
     * Sets the attribute value
     *
     * @param mValue the attribute value
     */
    public void setValue(String mValue) {
        this.mValue = mValue;
    }

	/**
	 * check if the AdsAttribute entity is valid	
	 * 
	 * @return boolean result of the check
	 */	
    public boolean isValid() {
        return (mType != null);
    }
    
}
