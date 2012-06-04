/**
 * \package com.bluevia.ad.data This package contains entities data types of Bluevia Advertisement Service
 */
package com.bluevia.ad.data;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.bluevia.commons.Entity;
import com.bluevia.commons.Utils;


/**
 * Internal class to hold information about the advertisement data send to the gSDP
 * It includes
 * <ul>
 * <li>adRequestId: The  adRequestId that identifies the ad request (it is compose using the adSpace and the authtoken). Both adSpace and authToken are mandatory.</li>
 * <li>adPresentation: the type of advertisement requires (image, text, image+text, etc). This parameter is optional</li>
 * <li>adSpace: adSpace parameter from the gSDP (the value is the retrieved adSpace value after the applications registration). This parameter is mandatory.</li>
 * <li>keywords: the keywords the ads are related to. This parameter is optional</li>
 * <li>protectionPolicy: the adult control policy. It will be safe, low, high. This parameter is optional</li>
 * </ul>
 *
 * @author Telefonica R&D
 *
 */
public class AdRequest implements Entity {

    /**
     * Ad Presentation Types. It is the type of ad crealive element to request. Possible values are:
     * <ul>
     *  <li> IMAGE: Banner that contains the ad image</li>
     *  <li> TEXT: Text advertisement</li>
     * </ul>
     *
     */
    public enum Type { IMAGE, TEXT};

    /**
     * Protection Policies values
     * <ul>
     *  <li> LOW: Moderately explicit content (I am youth; you can show me moderately explicit content)</li>
     *  <li> SAFE: Not rated content. (I am a kid, please, show me only safe content)</li>
     *  <li> HIGH: Explicit content. (I am an adult; I am over 18 so you can show me any content including very explicit content)</li>
     * </ul>
     *
     */
    public enum ProtectionPolicyType {LOW, SAFE, HIGH }
    
    private String mAdRequestId;

    private Type mAdPresentation = null;

    private String mAdSpace;

    private String[] mKeywords;

    private ProtectionPolicyType mProtectionPolicy = null;
    
    private String mCountry;
    
    private String mTargetUserId;
    
    private String mUserAgent;

    /**
     * Instantiates an AdRequest object with all the mandatory and optional parameters
     *
     * @param adSpace the adSpace of the application. This parameter is mandatory.
     * @param adRequestId the id for the ad request to correlate the response. This parameters is optional (if it is null the SDK will generate one).
     * @param accessToken parameter that identifies the user in the application. This parameter is mandatory.
     * @param adPresentation the type of advertisement requires (image, text, image+text, etc). This parameter is optional.
     * @param userAgent the user agent for the request. This parameters is optional.
     * @param keywords the keywords the ads are related to. This parameter is optional.
     * @param protectionPolicy the adult control policy. It will be safe, low, high. This parameter is optional.
     * @param country country country where the target user is located. Must follow ISO-3166.
     * @param targetUserId Identifier of the Target User. 
     * 
     */
    public AdRequest(String adSpace, String adRequestId, String accessToken, Type adPresentation, 
    		String userAgent, String[] keywords, ProtectionPolicyType protectionPolicy, String country, String targetUserId) {
        this(adSpace, adRequestId, accessToken);
        this.mAdSpace = adSpace;
        this.mAdPresentation = adPresentation;
        this.mKeywords = keywords;
        this.mProtectionPolicy = protectionPolicy;
        this.mCountry = country;
        this.mTargetUserId = targetUserId;
        this.mUserAgent = userAgent;
    }

    /**
     * Instantiates an AdRequest object using the mandatory parameters
     * 
     * @param adSpace the adSpace of the application. This parameter is mandatory.
     * @param adRequestId the adRequestId for this request. This parameters is optional, if it is not set, the SDK will generate one.
     * @param accessToken parameter that identifies the user in the application. This parameter is mandatory.
     *
     */
    public AdRequest(String adSpace, String adRequestId, String accessToken) {
        super();
        if (!Utils.isEmpty(adRequestId))
        	setAdRequestId(adRequestId);
        else setAdRequestId(adSpace, accessToken);
    }

    /**
     * Gets the adRequestId that identifies the ad request (it is compose using the adspace and the authtoken)
     * @return the adRequestId
     */
    public String getAdRequestId() {
        return mAdRequestId;
    }
    
    /**
     * Sets the adRequestId
     * @param adRequestId the adRequestId to set.
     */
    public void setAdRequestId(String adRequestId) {
        this.mAdRequestId = adRequestId;
    }

    /**
     * Generates a new adRequestId
     * @param adSpace the adSpace of the application. This parameter is mandatory.
     * @param accessToken parameter that identifies the user in the application. This parameter is mandatory.
     */
    public void setAdRequestId(String adSpace, String accessToken) {
        this.mAdRequestId = getComposedAdRequestId(adSpace, accessToken);
    }

    /**
     * Gets the required ad presentation
     * @return the required ad presentation
     */
    public Type getAdPresentation() {
        return mAdPresentation;
    }

    /**
     * Sets the required ad presentation
     * @param adPresentation the required ad presentation
     */
    public void setAdPresentation(Type adPresentation) {
        this.mAdPresentation = adPresentation;
    }

    /**
     * Gets the adSpace used to generate the ad request
     * @return the adSpace
     */
    public String getAdSpace() {
        return mAdSpace;
    }

    /**
     * Sets the ad Space
     * @param adSpace the required ad space
     */
    public void setAdSpace(String adSpace) {
        this.mAdSpace = adSpace;
    }
    
    /**
     * Sets the keywords related to the required ad
     * @return the keywords related to the required ad
     */
    public String[] getKeywords() {
        return mKeywords;
    }

    /**
     * Get the keywords related to the required ad
     * @param keywords he keywords related to the required ad
     */
    public void setKeywords(String[] keywords) {
        this.mKeywords = keywords;
    }

    /**
     * Get the protection policy related to the required ad
     * @return the protection policy related to the required ad
     */
    public ProtectionPolicyType getProtectionPolicy() {
        return mProtectionPolicy;
    }

    /**
     * Sets the protection policy related to the required ad
     * @param protectionPolicy the protection policy related to the required ad
     */
    public void setProtectionPolicy(ProtectionPolicyType protectionPolicy) {
        this.mProtectionPolicy = protectionPolicy;
    }

    /**
     * Gets the country related to the required ad
     * @return the country related to the required ad
     */
    public String getCountry(){
    	return mCountry;
    }
    
    /**
     * Sets the country related to the required ad
     * @param country country value (must follow ISO-3166)
     */
    public void setCountry(String country){
    	mCountry = country;
    }
    
    /**
     * Gets the target user id related to the required ad
     * @return the target user id related to the required ad
     */
    public String getTargetUserId(){
    	return mTargetUserId;
    }
    
    /**
     * Sets the target user id related to the required ad
     * @param targetUserId the target user id related to the required ad
     */
    public void setTargetUserId(String targetUserId){
    	mTargetUserId = targetUserId;
    }
    
    /**
     * Gets the user agent
     * @return the user agent
     */
    public String getUserAgent(){
    	return mUserAgent;
    }
    
    /**
     * Sets the user agent
     * @param user agent the user agent
     */
    public void setUserAgent(String userAgent){
    	mUserAgent = userAgent;
    }
    
	/**
	 * check if the AdRequest entity is valid	
	 * 
	 * @return boolean result of the check
	 */	
    public boolean isValid() {
        return (mAdRequestId != null) && (mAdRequestId.trim().length() >0) && (mAdSpace != null) && (mAdSpace.trim().length() >0);
    }

    /**
     * Gets the composed ad request id
     * @param adSpace the adSpace of the application.
     * @param accessToken parameter that identifies the user in the application.
     * @return the composed ad request id
     */
    private String getComposedAdRequestId(String adSpace, String accessToken) {
        if (adSpace == null)
            return null;
        
        StringBuilder sb = new StringBuilder();
        if (adSpace.length() > 10)
        	sb.append(adSpace.substring(0, 10));
        else sb.append(adSpace);
        	
        
        long millis = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhh:mm:ss");
        String timestamp = sdf.format(new Date(millis));
        sb.append(timestamp);
        
        if (accessToken != null){
        	if (accessToken.length() > 10)
        		sb.append(accessToken.substring(0, 10));
            else sb.append(accessToken);        
        }
        
        return sb.toString();
    }
}
