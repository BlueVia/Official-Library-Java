package com.bluevia.ad.data;

import com.bluevia.commons.Entity;

/** 
 * Object that represents an advertisement received from the gSDP It includes
 * the following attributes:
 * <ul>
 * <li>Resource: the returned ad resource information. It contains the meta-data related to the ad resources (images, text, etc.)</li>
 * <li>Id: Identifier of the ad selected and served by the Ad-Server. This id is
 * used for asynchronous event notification and identifies univocally the ad
 * whose associated event is reported.</li>
 * <li>Campaign: Id of a campaign associated.</li>
 * <li>Flight: Id of the flight that ad is associated.</li>
 * <li>AdPlacement: Identifier of the specific place of an ad. If several ads
 * share a WAP/screen page, the ad_placement identifies the specific place of
 * each one (at the top, at the bottom, at the middle). Its aim is to help the
 * showing of ads in the same page, or area (competitor brands must not share
 * ads in the same WP page, for instance). This parameter should be a running
 * number. First ad on the page=1, second=2, etc.</li>
 * </ul>
 *
 * @author Telefonica R&D
 * 
 * 
 */
public class Ad implements Entity {

	
    private Resource mResource;

    private String mId;

    private String mCampaign;

    private String mFlight;

    private int mAdPlacement = 0;
    
    /**
     * Gets the ad resource
     *
     * @return the ad resource
     */
    public Resource getResource() {
        return mResource;
    }

    /**
     * Sets the ad resource
     *
     * @param resource the ad resource
     */
    public void setResource(Resource resource) {
        this.mResource = resource;
    }

    /**
     * Gets the ad placement info
     *
     * @return the ad placement info
     */
    public int getAdPlacement() {
        return mAdPlacement;
    }

    /**
     * Sets the ad placement info
     *
     * @param adPlacement the ad placement info
     *
     */
    public void setAdPlacement(int adPlacement) {
        this.mAdPlacement = adPlacement;
    }

    /**
     * Gets the ad campaign
     *
     * @return the ad campaign
     */
    public String getCampaign() {
        return mCampaign;
    }

    /**
     * Sets the ad campaign
     *
     * @param campaign the ad campaign
     */
    public void setCampaign(String campaign) {
        this.mCampaign = campaign;
    }

    /**
     * Gets the ad flight
     *
     * @return the ad flight
     */
    public String getFlight() {
        return mFlight;
    }

    /**
     * Sets the ad flight
     *
     * @param flight the ad flight
     */
    public void setFlight(String flight) {
        this.mFlight = flight;
    }

    /**
     * Gets the ad id in the Ad-Server
     *
     * @return the ad id
     */
    public String getId() {
        return mId;
    }

    /**
     * Sets the ad id in the Ad-server
     *
     * @param id the ad id
     */
    public void setId(String id) {
        mId = id;
    }

	/**
	 * check if the Ad entity is valid	
	 * 
	 * @return boolean result of the check
	 */	
    public boolean isValid() {
        return ((mResource != null) && mResource.isValid());
    }
    
}
