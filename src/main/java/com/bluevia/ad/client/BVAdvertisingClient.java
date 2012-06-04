/**
 * \package com.bluevia.ad.client This package contains the clients of Bluevia Advertisement Service
 */
package com.bluevia.ad.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.bluevia.ad.data.Ad;
import com.bluevia.ad.data.AdRequest;
import com.bluevia.ad.data.AdResponse;
import com.bluevia.ad.data.AdsAttribute;
import com.bluevia.ad.data.FullCreativeElement;
import com.bluevia.ad.data.Interaction;
import com.bluevia.ad.data.Resource;
import com.bluevia.ad.data.AdRequest.Type;
import com.bluevia.ad.data.simple.CreativeElement;
import com.bluevia.ad.data.simple.SimpleAdResponse;
import com.bluevia.ad.parser.url.UrlEncodedAdSerializer;
import com.bluevia.commons.Encoding;
import com.bluevia.commons.Entity;
import com.bluevia.commons.Utils;
import com.bluevia.commons.client.BVBaseClient;
import com.bluevia.commons.connector.http.oauth.IOAuth;
import com.bluevia.commons.data.JaxbEntity;
import com.bluevia.commons.exception.BlueviaException;
import com.bluevia.commons.parser.SerializeException;
import com.bluevia.commons.parser.xml.XmlConstants;
import com.bluevia.commons.parser.xml.XmlParser;
import com.telefonica.schemas.unica.rest.sgap.v1.AttributeType;
import com.telefonica.schemas.unica.rest.sgap.v1.CreativeElementType;
import com.telefonica.schemas.unica.rest.sgap.v1.InteractionType;
import com.telefonica.schemas.unica.rest.sgap.v1.SimpleAdResponseType;

/**
 *  This class provides access to the set of functions which any user could use to access
 *  the Advertising service functionality
 *
 * @author Telefonica R&D
 *
 */
public abstract class BVAdvertisingClient extends BVBaseClient {

	protected static final String FEED_AD_REQUESTS_URI = "/simple/requests";

	private static final String HEADER_X_PHONENUMBER = "X-PhoneNumber";

	private static Logger log = Logger.getLogger(BVAdvertisingClient.class.getName());

	protected void init(){
		mEncoding = Encoding.APPLICATION_URL_ENCODED;
		mParser = new XmlParser();
		mSerializer = new UrlEncodedAdSerializer();
	}

	/**
	 * Requests the retrieving of an advertisement.
	 * 
	 * @param adSpace the adSpace of the Bluevia application (mandatory)
	 * @param phoneNumber the phone number to whom the advertising is targeted
	 * @param country country where the target user is located. Must follow ISO-3166 (see http://www.iso.org/iso/country_codes.htm).
	 * (Optional: the parameter should not be included if the target user can be deduced by other means, for example, Oauth 3-legged or Oauth 2-legged with requestorID).
	 * @param targetUserId Identifier of the Target User. 
	 * (Optional: the parameter should not be included if the target user can be deduced by other means, for example, Oauth 3-legged or Oauth 2-legged with requestorID).
	 * @param adRequestId an unique id for the request. (optional: if it is not set, the SDK will generate it automatically)
	 * @param adPresentation the value is a code that represents the ad format type (optional)
	 * @param keywords the keywords the ads are related to (optional)
	 * @param protectionPolicy the adult control policy. It will be safe, low, high. It should be checked with the application SLA in the gSDP (optional)
	 * @param userAgent the user agent of the client (optional)
	 * @return the result returned by the server that contains the ad meta-data
	 * @throws BlueviaException
	 * @throws IOException 
	 */
	protected AdResponse getSimpleAd(String adSpace, String phoneNumber, String country, String targetUserId, String adRequestId, AdRequest.Type adPresentation, String[] keywords, AdRequest.ProtectionPolicyType protectionPolicy, String userAgent)
			throws BlueviaException, IOException {
		
		AdResponse adResponse = null;

		// Mandatory parameter validation
		if (Utils.isEmpty(adSpace))
			throw new BlueviaException("Bad request: adSpace cannot be null nor empty", BlueviaException.BAD_REQUEST_EXCEPTION);

		String token = null;
		if (((IOAuth)mConnector).getOauthToken() != null)
			token = ((IOAuth)mConnector).getOauthToken().getToken();
		
		AdRequest request = new AdRequest(adSpace, adRequestId, token, adPresentation,
				userAgent, keywords, protectionPolicy, country, targetUserId);

		if (!request.isValid())
			throw new BlueviaException("Bad request. Please, check function parameters",
					BlueviaException.BAD_REQUEST_EXCEPTION);

		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put(XmlConstants.PARAM_VERSION_KEY, XmlConstants.VERSION_1);

		HashMap<String, String> headers = new HashMap<String, String>();
		if (!Utils.isEmpty(phoneNumber)) {
			headers.put(HEADER_X_PHONENUMBER, phoneNumber);				
		}

		((XmlParser) mParser).setParseClass(SimpleAdResponseType.class);
		Entity response = this.create("", request, parameters, headers);

		//Check if response is instance of JaxbEntity
		if ((response == null) || (! (response instanceof JaxbEntity)))
			throw new BlueviaException("Error during request. Response received does not correspond to an SimpleAdResponse",
					BlueviaException.INTERNAL_CLIENT_ERROR);
		JaxbEntity parseEntity= (JaxbEntity) response;
		
		
		//Check if parseEntity is instance of SimpleAdResponseType
		SimpleAdResponseType simpleAdResponseType= new SimpleAdResponseType();
		if ((parseEntity.getObject() == null) || (! (parseEntity.getObject() instanceof SimpleAdResponseType)))
			throw new BlueviaException("Error during request. Response received does not correspond to an SimpleAdResponse",
					BlueviaException.INTERNAL_CLIENT_ERROR);
		simpleAdResponseType = (SimpleAdResponseType) parseEntity.getObject();

		adResponse = getAdResponseFromSimgleAdResponseType(simpleAdResponseType);
		
		return adResponse;
	}

	private AdResponse getAdResponseFromSimgleAdResponseType(
			SimpleAdResponseType simpleAdResponseType) throws SerializeException {

		AdResponse adResponse= new AdResponse();

		adResponse.setId(simpleAdResponseType.getId());
		
		Ad ad= new Ad();
		ad.setAdPlacement(simpleAdResponseType.getAd().getAdPlacement().intValue());
		ad.setCampaign(simpleAdResponseType.getAd().getCampaign());
		ad.setFlight(simpleAdResponseType.getAd().getFlight());
		ad.setId(simpleAdResponseType.getAd().getId());
		log.debug("[Ad id]:" + simpleAdResponseType.getAd().getId());
			Resource resource= new Resource();
			resource.setAdRepresentation(simpleAdResponseType.getAd().getResource().getAdPresentation());
			log.debug("[Resource Ad Representation]:" + simpleAdResponseType.getAd().getResource().getAdPresentation());

			List<CreativeElementType> creativeElementTypeList= simpleAdResponseType.getAd().getResource().getCreativeElement();
			for (CreativeElementType element: creativeElementTypeList) {
				FullCreativeElement ce= new FullCreativeElement();
				log.debug("[Element Type]:" + element.getType());
	            if (XmlConstants.XSD_AD_CREATIVE_ELEMENT_ATTR_TYPE_IMAGE_VALUE.equals(element.getType())) {
	            	ce.setType(FullCreativeElement.Type.IMAGE);
	            } else if (XmlConstants.XSD_AD_CREATIVE_ELEMENT_ATTR_TYPE_TEXT_VALUE.equals(element.getType())) {
	            	ce.setType(FullCreativeElement.Type.TEXT);
	            } else if (XmlConstants.XSD_AD_CREATIVE_ELEMENT_ATTR_TYPE_SOUND_VALUE.equals(element.getType())) {
	            	ce.setType(FullCreativeElement.Type.SOUND);
	            } else if (XmlConstants.XSD_AD_CREATIVE_ELEMENT_ATTR_TYPE_ZIP_VALUE.equals(element.getType())) {
	            	ce.setType(FullCreativeElement.Type.ZIP);
	            } else if (XmlConstants.XSD_AD_CREATIVE_ELEMENT_ATTR_TYPE_PAGE_VALUE.equals(element.getType())) {
	            	ce.setType(FullCreativeElement.Type.PAGE);
	            } else {
	                throw new SerializeException("AdPresentation Type not supported");
	            }
	        		
				for (AttributeType at: element.getAttribute()) {
					AdsAttribute adsatt= new AdsAttribute();
					log.debug("[Ads Attributte Type]:" + at.getType());
		            if (XmlConstants.XSD_AD_ATTRIBUTE_ATTR_TYPE_URL_VALUE.equals(at.getType())) {
		            	adsatt.setType(AdsAttribute.Type.URL);
		            } else if (XmlConstants.XSD_AD_ATTRIBUTE_ATTR_TYPE_ADTEXT_VALUE.equals(at.getType())) {
		            	adsatt.setType(AdsAttribute.Type.ADTEXT);
		            } else if (XmlConstants.XSD_AD_ATTRIBUTE_ATTR_TYPE_LOCATOR_VALUE.equals(at.getType())) {
		            	adsatt.setType(AdsAttribute.Type.LOCATOR);
		            } else if (XmlConstants.XSD_AD_ATTRIBUTE_ATTR_TYPE_CODEC_VALUE.equals(at.getType())) {
		            	adsatt.setType(AdsAttribute.Type.CODEC);
		            } else {
		                throw new SerializeException("AdsAttribute Type not supported");
		            }
					adsatt.setValue(at.getValue());
					ce.addAdsAttribute(adsatt);	
				}
				for (InteractionType interaction: element.getInteraction()) {
					Interaction ia= new Interaction();
					log.debug("[Interaction Type]:" + interaction.getType());
		            if (XmlConstants.XSD_AD_INTERACTION_ATTR_TYPE_CLICK2WAP_VALUE.equals(interaction.getType())) {
		            	ia.setType(Interaction.Type.CLICK_2_WAP);
		            } else {
		                throw new SerializeException("AdsAttribute Type not supported");
		            }
					for (AttributeType at: interaction.getAttribute()) {
						AdsAttribute adsatt= new AdsAttribute();
						log.debug("[Interaction Ad Attribute Type]:" + at.getType());
			            if (XmlConstants.XSD_AD_ATTRIBUTE_ATTR_TYPE_URL_VALUE.equals(at.getType())) {
			            	adsatt.setType(AdsAttribute.Type.URL);
			            } else if (XmlConstants.XSD_AD_ATTRIBUTE_ATTR_TYPE_ADTEXT_VALUE.equals(at.getType())) {
			            	adsatt.setType(AdsAttribute.Type.ADTEXT);
			            } else if (XmlConstants.XSD_AD_ATTRIBUTE_ATTR_TYPE_LOCATOR_VALUE.equals(at.getType())) {
			            	adsatt.setType(AdsAttribute.Type.LOCATOR);
			            } else if (XmlConstants.XSD_AD_ATTRIBUTE_ATTR_TYPE_CODEC_VALUE.equals(at.getType())) {
			            	adsatt.setType(AdsAttribute.Type.CODEC);
			            } else {
			                throw new SerializeException("AdsAttribute Type not supported");
			            }
						adsatt.setValue(at.getValue());
						ia.addAttribute(adsatt);
					}
					ce.addInteraction(ia);
				}
				resource.addCreativeElement(ce);
			}
		ad.setResource(resource);	
		adResponse.setAd(ad);	
		adResponse.setVersion(simpleAdResponseType.getVersion().intValue());
		log.debug("[Ad Version]:" + simpleAdResponseType.getVersion().intValue());
		
		return adResponse;
	}

	/**
	 * Returns a simple ad response with the usefull information
	 * 
	 * @param response
	 * @return
	 */
	protected SimpleAdResponse simplifyResponse(AdResponse response){

		if (response == null)
			return null;

		SimpleAdResponse simpleResponse = new SimpleAdResponse();

		//Ad request id
		simpleResponse.setRequestId(response.getId());

		//Elements
		Resource resource = response.getAd().getResource();
		if (resource == null)
			return simpleResponse;

		for (FullCreativeElement e : resource.getCreativeElement()){
			
			if (e.getType() != null && 
					e.getAdsAttribute() != null && !e.getAdsAttribute().isEmpty() && 
					e.getInteraction() != null && !e.getInteraction().isEmpty() &&
					e.getInteraction().get(0).getAttribute() != null && !e.getInteraction().get(0).getAttribute().isEmpty()) {

				//Set type
				Type type = translateType(e.getType());

				//Set value
				String value = null;
				for (AdsAttribute attr : e.getAdsAttribute()){
					if (attr.getType() == AdsAttribute.Type.ADTEXT || attr.getType() == AdsAttribute.Type.LOCATOR)
						value = attr.getValue();
				}
				
				String interaction = null;
				for (AdsAttribute attr : e.getInteraction().get(0).getAttribute()){
					if (attr.getType() == AdsAttribute.Type.URL){
						interaction = attr.getValue();
					}
				}

				simpleResponse.addAdvertising(new CreativeElement(type, value, interaction));
			}
					
		}

		return simpleResponse;
	}

	/**
	 * Translates from FullCreativeElement.Type to AdRequest.Type
	 * 
	 * @param type the FullCreativeElement.Type representation
	 * @return the AdRequest.Type representation of the type received
	 */
	private AdRequest.Type translateType(FullCreativeElement.Type type){
		switch (type) {
		case IMAGE:
			return Type.IMAGE;
		case TEXT:
			return Type.TEXT;
		default:
			return null;
		}
	}
	
}
