package com.bluevia.ad.data;


import java.util.ArrayList;

import com.bluevia.commons.Entity;

/**
 * 
 * Object that describes the different elements that shape the adsource. For
 * instance an small banner link in line, could have two objects (image & text).
 * It includes the following attributes:
 * <ul>
 * <li>Type: Category of the ad. Object. Values include image, text, sound, zip,
 * page.</li>
 * <li>Attribute: An ad object may have different attributes for example the location.</li>
 * <li>Interaction: Tag used to invoke events, e.g: it contains the locator of
 * click through URI. When using isOfflineClient, there would be as many
 * interaction elements as needed in order to receive all the information the
 * offline client will need (without ad-server connectivity) for performing the
 * interaction.</li>
 * </ul>
 *
 * @author Telefonica R&D
 * 
 *
 */
public class FullCreativeElement implements Entity {

    /**
     * 
     *  Type that contains the possible Creative Elements that can be received. Values includes:
     *  <ul>
     *  <li>Image: Contains the information about the image of the ad. This information should contain an url locator attribute (to be able to get the image) and maybe some "click" interactions </li>
     *  <li>Text:  Contains information about the text of the ad. This information should contain the text value and maybe some "click" interactions </li>
     *  <li>Sound:  Contains information about the audio of the ad. This information should contain an url locator attribute and maybe some "click" interactions </li>
     *  <li>Zip:  Contains information about the zip of the ad. This information should contain an url locator attribute and maybe some "click" interactions </li>
     *  <li>Page:  Contains information about the page of the ad. This information should contain an url locator attribute and maybe some "click" interactions </li>
     *  </ul>
     *
     */
    public enum Type{IMAGE, TEXT, SOUND, ZIP, PAGE};
    
    private Type mType = null;

    private ArrayList<AdsAttribute> mAdsAttribute;

    private ArrayList<Interaction> mInteraction;

	/**
	 * Instantiates a new empty FullCreativeElement
	 */
    public FullCreativeElement() {
        super();
        mAdsAttribute = new ArrayList<AdsAttribute>();
        mInteraction = new ArrayList<Interaction>();
    }

    /**
     * Gets the Creative Element type. Values include image, text, sound, zip,
     * page
     *
     * @return the creative element type
     */
    public Type getType() {
        return mType;
    }

    /**
     * Sets the Creative Element type. Values include image, text, sound, zip,
     * page
     *
     * @param type the creative element type
     */
    public void setType(Type type) {
        this.mType = type;
    }

    /**
     * Gets the ad creative element attribute list
     *
     * @return the ad creative element attribute list
     */
    public ArrayList<AdsAttribute> getAdsAttribute() {
        return mAdsAttribute;
    }

    /**
     * Sets an attribute for the ad creative element
     *
     * @param a the attribute to add
     */
    public void addAdsAttribute(AdsAttribute a) {
        this.mAdsAttribute.add(a);
    }

    /**
     * Gets the ad creative element interaction list
     *
     * @return the ad creative element interaction list
     */
    public ArrayList<Interaction> getInteraction() {
        return mInteraction;
    }

    /**
     * Sets an interaction for the ad creative element
     *
     * @param i the interaction to add
     */
    public void addInteraction(Interaction i) {
        this.mInteraction.add(i);
    }

	/**
	 * check if the FullCreativeElement entity is valid	
	 * 
	 * @return boolean result of the check
	 */	
    public boolean isValid() {
        if (mType == null)
            return false;

        if ((mAdsAttribute == null) || (mAdsAttribute.size() == 0))
            return false;

        for (AdsAttribute c : mAdsAttribute) {
            if (!c.isValid()) {
                return false;
            }
        }

        if ((mInteraction != null) && (mInteraction.size() >0)) {
            for (Interaction i: mInteraction) {
                if (!i.isValid())
                    return false;
            }
        }

        return true;
    }

}
