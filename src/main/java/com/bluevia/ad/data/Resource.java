package com.bluevia.ad.data;


import java.util.ArrayList;

import com.bluevia.commons.Entity;

/**
 * 
 * Object that represents ad resource information associated It includes the
 * following attributes:
 * <ul>
 * <li>FullCreativeElement: An ad object describes the different elements that shape
 * the adsource. For instance an small banner link in line, could have two
 * objects (image & text).</li>
 * <li>AdRepresentation: Tag used by clients to get ad sources. The kind of an
 * ad sources such as banner, sponsored links, interstitials, image&link, small
 * banner link inline, top banner link, below,... Represented as in numeric form
 * (same as on the request AD_PRESENTATION).</li>
 * </ul>
 *
 * @author Telefonica R&D
 * 
 *
 */
public class Resource implements Entity {
	
    private ArrayList<FullCreativeElement> mCreativeElement;

    private String mAdRepresentation;

	/**
	 * Instantiates a new empty Resource
	 */
    public Resource() {
        super();
        mCreativeElement = new ArrayList<FullCreativeElement>();
    }

    /**
     * Gets the ad representation
     *
     * @return the ad representation
     */
    public String getAdRepresentation() {
        return mAdRepresentation;
    }

    /**
     * Sets the ad representation
     *
     * @param adRepresentation the ad representation
     */
    public void setAdRepresentation(String adRepresentation) {
        mAdRepresentation = adRepresentation;
    }

    /**
     * Gets the creative elements associated the the ad
     *
     * @return the creative elements list
     */
    public ArrayList<FullCreativeElement> getCreativeElement() {
        return (ArrayList<FullCreativeElement>) mCreativeElement;
    }

    /**
     * Adds a FullCreativeElement for the ad representation
     *
     * @param c the creative element
     * @return
     */
    public boolean addCreativeElement(FullCreativeElement c) {
        return mCreativeElement.add(c);
    }

	/**
	 * check if the Resource entity is valid	
	 * 
	 * @return boolean result of the check
	 */	
    public boolean isValid() {
        if (mAdRepresentation == null)
            return false;

        if ((mCreativeElement == null) || (mCreativeElement.size() == 0))
            return false;

        for (FullCreativeElement c : mCreativeElement) {
            if (!c.isValid()) {
                return false;
            }
        }

        return true;
    }

}
