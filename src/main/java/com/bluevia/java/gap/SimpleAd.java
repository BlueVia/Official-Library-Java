/**
 * 
 * @category bluevia
 * @package com.bluevia.java.gap
 * @copyright Copyright (c) 2010 Telefonica I+D (http://www.tid.es)
 * @author Bluevia <support@bluevia.com>
 * 
 * 
 *          BlueVia is a global iniciative of Telefonica delivered by Movistar
 *          and O2. Please, check out http://www.bluevia.com and if you need
 *          more information contact us at support@bluevia.com
 */

package com.bluevia.java.gap;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import com.bluevia.java.Utils;

/**
 * SimpleAd
 * 
 * @package com.bluevia.java.gap
 */

/**
 * <p>Java class to create the params request URL to send Simple Advertising
 * 
 * 
 */
public class SimpleAd {

    private String adreqId;
    private String adPresentation;
    private String adPresentationSize;
    private String adSpace;
    private String userAgent;
    private String keywords;
    private String protection_policy;
    private String country;
    private String target_user_id;
    
    /**
     * Get the params request URL (i.e : param1=value1&param2=value2....)
     * 
     * @return String
     */
    public String toHttpQueryString() {
    	
        Hashtable<String, String> ht = new Hashtable<String, String>();

        if (this.getAdreqId() != null) {
            ht.put("ad_request_id", this.getAdreqId());
        }

        if (this.getAdPresentation() != null) {
            ht.put("ad_presentation", this.getAdPresentation());
        }

        if (this.getAdPresentationSize() != null) {
            ht.put("ad_presentation_size", this.getAdPresentationSize());
        }

        if (this.getAdSpace() != null) {
            ht.put("ad_space", this.getAdSpace());
        }

        if (this.getUserAgent() != null) {
            ht.put("user_agent", this.getUserAgent());
        }

        if (this.getKeywords() != null) {
            ht.put("keywords", this.getKeywords());
        }

        if (this.getProtection_policy() != null) {
            ht.put("protection_policy", this.getProtection_policy());
        }

        if (this.getCountry() != null) {
            ht.put("country", this.getCountry());
        }
        if (this.getTarget_user_id() != null) {
            ht.put("target_user_id", this.getTarget_user_id());
        }

        return Utils.toHttpQueryString(ht);
    }
    
    /**
     * Get AdreqId
     * 
     * @return adreqId
     */
    public String getAdreqId() {

        return adreqId;
    }

    /**
     * Set AdreqId
     * 
     * @param adreqId the adreqId to set
     */
    public void setAdreqId(String adreqId) {

        this.adreqId = adreqId;
    }

    /**
     * Get AdPresentation
     * 
     * @return
     */
    public String getAdPresentation() {

        return adPresentation;
    }

    /**
     * Set AdPresentation
     * 
     * @param adPresentation
     */

    public void setAdPresentation(String adPresentation) {

        this.adPresentation = adPresentation;
    }

    /**
     * Get AdPresentationSize
     * 
     * @return
     */

    public String getAdPresentationSize() {

        return adPresentationSize;
    }

    /**
     * Set AdPresentationSize
     * 
     * @param adPresentationSize
     */

    public void setAdPresentationSize(String adPresentationSize) {

        this.adPresentationSize = adPresentationSize;
    }

    /**
     * Get userAgent
     * 
     * @return the userAgent
     */
    public String getUserAgent() {

        return userAgent;
    }

    /**
     * Set userAgent
     * 
     * @param userAgent the userAgent to set
     */
    public void setUserAgent(String userAgent) {

        this.userAgent = userAgent;
    }

    /**
     * Get AdSpace
     * 
     * @return the adSpace
     */
    public String getAdSpace() {

        return adSpace;
    }

    /**
     * Set AdSpace
     * 
     * @param adSpace the adSpace to set
     */
    public void setAdSpace(String adSpace) {

        this.adSpace = adSpace;
    }

    /**
     * Get Keywords
     * 
     * @return the keywords
     */
    public String getKeywords() {

        return keywords;
    }

    /**
     * Set Keywords
     * 
     * @param keywords the keywords to set
     */
    public void setKeywords(String keywords) {

        this.keywords = keywords;
    }

    /**
     * Get Protection_policy
     * 
     * @return protection_policy
     */
    public String getProtection_policy() {

        return protection_policy;
    }

    /**
     * Set Protection_policy
     * 
     * @param protectionPolicy
     */
    public void setProtection_policy(String protectionPolicy) {

        protection_policy = protectionPolicy;
    }

    /**
     * Get county
     * 
     * @return
     */
    public String getCountry() {

        return country;
    }

    /**
     * Set country
     * 
     * @param country
     */
    public void setCountry(String country) {

        this.country = country;
    }

    /**
     * Get targer_user_id
     * 
     * @return
     */
    public String getTarget_user_id() {

        return target_user_id;
    }

    /**
     * Set targer_user_id
     * 
     * @param targetUserId
     */
    public void setTarget_user_id(String targetUserId) {

        target_user_id = targetUserId;
    }
    
    public void composeAdRequestId(String adSpace, String accessToken) {
        if (adSpace == null)
            setAdreqId(null);
        
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
        
        setAdreqId(sb.toString());
    }

}
