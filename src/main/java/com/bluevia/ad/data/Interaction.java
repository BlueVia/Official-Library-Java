package com.bluevia.ad.data;


import java.util.ArrayList;

import com.bluevia.commons.Entity;

/**
 * 
 * Object used to hold creative element invoke events, e.g: it contains the
 * locator of click through URI. When using isOfflineClient, there would be as
 * many interaction elements as needed in order to receive all the information
 * the offline client will need (without ad-server connectivity) for performing
 * the interaction It includes the following attributes:
 * <ul>
 * <li>Type: Category of interactivity. Values include: Click2wap.</li>
 * <li>Attributes: An ad interaction may have different attributes (such as
 * URL in click2wap).</li>
 * </ul>
 *
 * @author Telefonica R&D
 * 
 *
 */
public class Interaction implements Entity {

    /**
     * 
     * Type that contains the possible interaction type elements that can be received. Values includes: Click2wap interaction, so when the user clicks over the ad, it should open an Url via the device browser
     *\
     */
    public enum Type {CLICK_2_WAP};
    
    private Type mType;

    private ArrayList<AdsAttribute> mAttribute;

	/**
	 * Instantiates a new empty Interaction
	 */
    public Interaction() {
        super();
        mAttribute = new ArrayList<AdsAttribute>();
    }
    
    /**
     * Gets the type of interactivity. Values include: Click2wap
     *
     * @return the type of interactivity
     */
    public Type getType() {
        return mType;
    }

    /**
     * Sets the type of interactivity.
     *
     * @param type the type of interactivity
     */
    public void setType(Type type) {
        this.mType = type;
    }

    /**
     * Gets the attributes for the interaction
     *
     * @return the attribute list for the interaction
     */
    public ArrayList<AdsAttribute> getAttribute() {
        return mAttribute;
    }

    /**
     * Adds an attribute for the interaction
     *
     * @param a The attribute for the interaction
     * @return
     */
    public boolean addAttribute(AdsAttribute a) {
        return this.mAttribute.add(a);
    }

	/**
	 * check if the Interaction entity is valid	
	 * 
	 * @return boolean result of the check
	 */	
    public boolean isValid() {
        if (mType == null)
            return false;

        if ((mAttribute == null) || (mAttribute.size() == 0))
            return false;

        for (AdsAttribute c : mAttribute) {
            if (!c.isValid()) {
                return false;
            }
        }

        return true;
    }

}
