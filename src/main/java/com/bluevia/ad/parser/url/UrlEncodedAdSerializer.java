package com.bluevia.ad.parser.url;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import com.bluevia.ad.data.AdRequest;
import com.bluevia.commons.Entity;
import com.bluevia.commons.Utils;
import com.bluevia.commons.parser.ISerializer;
import com.bluevia.commons.parser.SerializeException;


/**
 * URL encoded implementation of {@link com.bluevia.commons.parser.ISerializer ISerializer}
 * Class that represents the parser object for any Ad entity.
 * Using this class you will be able to serialize and AdRequet entity into a URL encoded body
 * to send to the gSDP via a HTTP request.
 *
 * @author Telefonica R&D
 * 
 */
public class UrlEncodedAdSerializer implements ISerializer {

    private static final String AD_REQUEST_ID_HEADER_NAME="ad_request_id";	
    private static final String AD_SPACE_HEADER_NAME="ad_space";
    private static final String USER_AGENT_HEADER_NAME ="user_agent";
    private static final String AD_PRESENTATION_HEADER_NAME ="ad_presentation";
    private static final String AD_KEYWORDS_HEADER_NAME="keywords";
    private static final String AD_PROTECTION_POLICY_HEADER_NAME="protection_policy";
    private static final String AD_COUNTRY_HEADER_NAME="country";
    private static final String AD_TARGET_USER_ID_HEADER_NAME="target_user_id";

    private static final String AD_PRESENTATION_BANNER_VALUE = "0101";	
    private static final String AD_PRESENTATION_TEXT_VALUE = "0104";

    private static final String AD_PROTECTION_POLICY_LOW_VALUE="1";
    private static final String AD_PROTECTION_POLICY_SAFE_VALUE ="2";
    private static final String AD_PROTECTION_POLICY_HIGH_VALUE = "3";

    private static final String USER_AGENT_NONE = "none";
    
    /**
	 * This function serializes an entity into an OutputStream.
	 * 
	 * @param entity the entity to serialize
     * @returns the outputstream to write the serialization result to
     * @throws SerializeException thrown if the entity or output stream are unfit for serialization
	 * 
	 */
    public ByteArrayOutputStream serialize(Entity entity) throws SerializeException {
        
    	ByteArrayOutputStream out = new ByteArrayOutputStream();
    	
    	if (entity == null)
            throw new SerializeException("Can not serialize null entity ");

        if (! (entity instanceof AdRequest))
            throw new SerializeException("Entity class does not support serializing this entity class");

        AdRequest adRequest = (AdRequest) entity;
        List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>(6);
        
        //Mandatory
        nameValuePairs.add(new BasicNameValuePair(AD_REQUEST_ID_HEADER_NAME, adRequest.getAdRequestId()));
        nameValuePairs.add(new BasicNameValuePair(AD_SPACE_HEADER_NAME, adRequest.getAdSpace()));
        
        if (Utils.isEmpty(adRequest.getUserAgent()))
        	nameValuePairs.add(new BasicNameValuePair(USER_AGENT_HEADER_NAME, USER_AGENT_NONE));
        else nameValuePairs.add(new BasicNameValuePair(USER_AGENT_HEADER_NAME, adRequest.getUserAgent()));

        //Optional
        if (adRequest.getAdPresentation() != null)
            nameValuePairs.add(new BasicNameValuePair(AD_PRESENTATION_HEADER_NAME, getAdPresentationCode(adRequest.getAdPresentation())));
        
        if (adRequest.getProtectionPolicy() != null)
        	nameValuePairs.add(new BasicNameValuePair(AD_PROTECTION_POLICY_HEADER_NAME, getProtectionPolicyCode(adRequest.getProtectionPolicy())));
        
        
        String composedKeywords = getComposedKeywords (adRequest.getKeywords());
        if (composedKeywords != null)
            nameValuePairs.add(new BasicNameValuePair(AD_KEYWORDS_HEADER_NAME, composedKeywords));

        if (adRequest.getCountry() != null)
        	nameValuePairs.add((new BasicNameValuePair(AD_COUNTRY_HEADER_NAME, adRequest.getCountry())));

        if (adRequest.getTargetUserId() != null)
           	nameValuePairs.add((new BasicNameValuePair(AD_TARGET_USER_ID_HEADER_NAME, adRequest.getTargetUserId())));
        
        //Finally it is not required by the gSDP
        //nameValuePairs.add(new BasicNameValuePair(AD_PROTOCOL_VERSION, "2"));

        UrlEncodedFormEntity formEncodedEntity;
		try {
			formEncodedEntity = new UrlEncodedFormEntity(nameValuePairs);
	        formEncodedEntity.writeTo(out);
		} catch (IOException e) {
			throw new SerializeException(e);
		}
		return out;
    }

    /**
     * Gets the Ad Presentation Code. 
     *
     * @param type Ad Presentation type. Values included: IMAGE, TEXT
     * @return the Ad presentation Code
     * @throws SerializeException
     */
    private String getAdPresentationCode (AdRequest.Type type) throws SerializeException {
        switch (type) {
            case IMAGE:
                return AD_PRESENTATION_BANNER_VALUE;
            case TEXT:
                return AD_PRESENTATION_TEXT_VALUE;
            default:
                throw new SerializeException("AdPresentation Type not supported");
        }
    }
    

    /**
     * Gets the Protection Policy Code. 
     *
     * @param type Protection Policy type. Values included: LOW, SAFE, HIGH
     * @return the Protection Policy Code
     * @throws SerializeException
     */
    private String getProtectionPolicyCode (AdRequest.ProtectionPolicyType type) throws SerializeException {
        switch (type) {
            case LOW:
                return AD_PROTECTION_POLICY_LOW_VALUE;
            case SAFE:
                return AD_PROTECTION_POLICY_SAFE_VALUE;
            case HIGH:
                return AD_PROTECTION_POLICY_HIGH_VALUE;
            default:
                throw new SerializeException("PortectionPolicy Type not supported");
        }
    }

    /**
     * Gets the Composed Keywords. 
     *
     * @param keywords keyword string
     * @return the Composed Keywords
     */
    private String getComposedKeywords(String[] keywords) {
        String composedKeywords = null;
        if ((keywords != null) && (keywords.length > 0)) {
            StringBuffer aux = new StringBuffer(keywords[0]);

            for (int i = 1; i < keywords.length; i++) {
                aux.append("|").append(keywords[i]);
            }

            composedKeywords = aux.toString();
        }
        return composedKeywords;
    }
}

