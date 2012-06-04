package com.bluevia.directory.data;

import com.bluevia.commons.Entity;


/**
 * * Class to hold the information of the User Terminal Information block
 * resource from the gSDP This type is composed of the following fields:
 * <ul>
 * <li>Brand: vendor of the device</li>
 * <li>Model: model s name</li>
 * <li>Version: model s version number</li>
 * <li>MMS: yes/no field that indicates if the device supports MMS client or
 * not.
 * <li>EMS: yes/no field that indicates if the device supports EMS or not.</li>
 * <li>Smart-messaging: yes/no field that indicates if the device supports smart
 * messaging or not.</li>
 * <li>WAP: yes/no field that indicates if the device supports WAP or not.</li>
 * <li>USSDPhase: it indicates if the device supports USSD Phase 1 (only permits
 * reception of USSDs), Phase 2 (permits both reception and response to USSDs)
 * or it does not support USSD at all.</li>
 * <li>EMSmaxNumber: maximum number of consecutive SMSs.</li>
 * <li>WAP Push: It indicates whether the user's handset supports the WAP Push service. </li>
 * <li>MMS Video: It indicates whether the user's handset is able to play video received over MMS. </li>
 * <li>Video streaming: It indicates whether the user's handset supports video streaming.</li>
 * <li>Screen Resolution: screen resolution in pixels</li>
 * </ul>
 *
 * @author Telefonica R&D
 * 
 */
public class TerminalInfo implements Entity {
	
	public enum Fields { BRAND, MODEL, VERSION, MMS, EMS, SMART_MESSAGING, WAP, USSD_PHASE,
		EMS_MAX_NUMBER, WAP_PUSH, MMS_VIDEO, VIDEO_STREAMING, SCREEN_RESOLUTION};

    private String mBrand;
    private String mModel;
    private String mVersion;
    private Boolean mMMS;
    private Boolean mEMS;
    private Boolean mSmartMessaging;
    private Boolean mWAP;
    private String mUSSDPhase;
    private Integer mEMSmaxNumber;
    private Boolean mWapPush;
    private Boolean mMmsVideo;
    private Boolean mVideoStreaming;
    private String mScreenResolution;
    
    /**
     * @return the brand property
     */
    public String getBrand() {
        return mBrand;
    }

    /**
     * @param brand the brand property value to set
     */
    public void setBrand(String brand) {
        this.mBrand = brand;
    }

    /**
     * @return the model property
     */
    public String getModel() {
        return mModel;
    }

    /**
     * @param model the model property value to set
     */
    public void setModel(String model) {
        this.mModel = model;
    }

    /**
     * @return the version property
     */
    public String getVersion() {
        return mVersion;
    }

    /**
     * @param version the version property value to set
     */
    public void setVersion(String version) {
        this.mVersion = version;
    }

    /**
     * @return the screenResolution property
     */
    public String getScreenResolution() {
        return mScreenResolution;
    }

    /**
     * @param screenResolution the screenResolution property value to set
     */
    public void setScreenResolution(String screenResolution) {
        this.mScreenResolution = screenResolution;
    }

    /**
     * @return the mMS property
     */
    public boolean getMMS() {
        return mMMS;
    }

    /**
     * @param mMS the mMS property value to set
     */
    public void setMMS(boolean mMS) {
        mMMS = mMS;
    }

    /**
     * @return the eMS property
     */
    public boolean getEMS() {
        return mEMS;
    }

    /**
     * @param eMS the eMS property value to set
     */
    public void setEMS(boolean eMS) {
        mEMS = eMS;
    }

    /**
     * @return the smartMessaging property
     */
    public boolean getSmartMessaging() {
        return mSmartMessaging;
    }

    /**
     * @param smartMessaging the smartMessaging property value to set
     */
    public void setSmartMessaging(boolean smartMessaging) {
        this.mSmartMessaging = smartMessaging;
    }

    /**
     * @return the wAP property
     */
    public boolean getWAP() {
        return mWAP;
    }

    /**
     * @param wAP the wAP property value to set
     */
    public void setWAP(boolean wAP) {
        mWAP = wAP;
    }

    /**
     * @return the uSSDPhase property
     */
    public String getUSSDPhase() {
        return mUSSDPhase;
    }

    /**
     * @param uSSDPhase the uSSDPhase property value to set
     */
    public void setUSSDPhase(String uSSDPhase) {
        mUSSDPhase = uSSDPhase;
    }


    /**
     * @return the eMSmaxNumber property
     */
    public int getEMSmaxNumber() {
        return mEMSmaxNumber;
    }

    /**
     * @param eMSmaxNumber the eMSmaxNumber property value to set
     */
    public void setEMSmaxNumber(int eMSmaxNumber) {
        mEMSmaxNumber = eMSmaxNumber;
    }

    
    /**
     * @return the WAP push property
     */
    public boolean getWapPush() {
        return mWapPush;
    }

    /**
     * @param wapPush the WAP push property value to set
     */
    public void setWapPush(Boolean wapPush) {
        this.mWapPush = wapPush;
    }
    
    /**
     * @return the MMS Video property
     */
    public boolean getMmsVideo() {
        return mMmsVideo;
    }

    /**
     * @param mmsVideo the MMS Video property value to set
     */
    public void setMmsVideo(Boolean mmsVideo) {
        this.mMmsVideo = mmsVideo;
    }
    
    /**
     * @return the Video streaming property
     */
    public boolean getVideoStreaming() {
        return mVideoStreaming;
    }

    /**
     * @param videoStreaming the Video streaming property value to set
     */
    public void setVideoStreaming(Boolean videoStreaming) {
        this.mVideoStreaming = videoStreaming;
    }

    public boolean getValid() {
        return true;
    }
    
    /**
     * Returns a string representation of the values in this class
     * @return a comma-separated list of the values of the fields in this class
     */
    public String toString() {
        return "Brand: " + mBrand + ", " + 
        		"Model: " + mModel + ", " + 
        		"Version: " + mVersion + ", " + 
        		"MMS: " + mMMS + ", " + 
        		"EMS: " + mEMS + ", " + 
        		"Smart messaging: " + mSmartMessaging + ", " + 
        		"WAP: " + mWAP + ", " + 
        		"USSD Phase: " + mUSSDPhase + ", " + 
        		"EMS max number: " + mEMSmaxNumber + ", " + 
        		"WAP push: " + mWapPush + ", " + 
        		"MMS Video: " + mMmsVideo + ", " + 
        		"Video streaming: " + mVideoStreaming + ", " + 
        		"Screen resolution: " + mScreenResolution;
    }

	@Override
	public boolean isValid() {
		return true;
	}


}
