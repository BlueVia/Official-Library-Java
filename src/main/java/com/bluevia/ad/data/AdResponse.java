package com.bluevia.ad.data;

import com.bluevia.commons.Entity;

/**
 * Class to hold information about the advertisement data received from the gSDP
 * It includes
 * <ul>
 * <li>Ad: Element that includes information for ad (one, for correct
 * responses). Note: only one may be present in AdResponse.</li>
 * <li>Id: Unique request identification (BlueVia  appID + timestamp).</li>
 * <li>Version: Current version of the advertising protocol.</li>
 * </ul>
 *
 * @author Telefonica R&D
 * 
 *
 */
public final class AdResponse implements Entity {
	
    private String mId;

    private int mVersion;

    private Ad mAd;

    /**
     * Gets the Ad object value
     *
     * @return the Ad object included in the response
     */
    public Ad getAd() {
        return mAd;
    }

    /**
     * Sets the Ad object
     *
     * @param ad the Ad object
     */
    public void setAd(Ad ad) {
        this.mAd = ad;
    }

    /**
     * Gets the advertising protocol version
     *
     * @return the advertising protocol version
     */
    public int getVersion() {
        return mVersion;
    }

    /**
     * Set the advertising protocol version
     *
     * @param version the advertising protocol version
     */
    public void setVersion(int version) {
        this.mVersion = version;
    }

    /**
     * Gets the Unique request identification
     *
     * @return the unique request identification
     */
    public String getId() {
        return mId;
    }

    /**
     * Sets the unique request identification
     *
     * @param id the unique request identification
     */
    public void setId(String id) {
        this.mId = id;
    }
    
	/**
	 * check if the AdResponse entity is valid	
	 * 
	 * @return boolean result of the check
	 */	
    public boolean isValid() {
        return ((mId != null) && ((mAd == null) || (mAd.isValid())));
    }

}
