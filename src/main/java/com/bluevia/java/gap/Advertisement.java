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

import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;

import com.bluevia.java.OauthRESTConnector;
import com.bluevia.java.Utils;
import com.bluevia.java.exception.BlueviaException;
import com.bluevia.java.oauth.OAuthToken;
import com.telefonica.schemas.unica.rest.sgap.v1.AdType;
import com.telefonica.schemas.unica.rest.sgap.v1.AttributeType;
import com.telefonica.schemas.unica.rest.sgap.v1.CreativeElementType;
import com.telefonica.schemas.unica.rest.sgap.v1.InteractionType;
import com.telefonica.schemas.unica.rest.sgap.v1.SimpleAdResponseType;

/**
 * Advertisement
 * 
 * @package com.bluevia.java.gap
 */

/**
 * <p>Java class for retrieve Advertisements from Advertising API
 * 
 */

public class Advertisement extends AdClient {

    /**
     * Constructor
     * 
     * @param consumer
     * @param token
     * @param mode
     * @throws JAXBException
     */
    public Advertisement(OAuthToken consumer, OAuthToken token, Mode mode) throws JAXBException {
        super(consumer, token, mode);

        if (!Utils.validateToken(token))
    		throw new IllegalArgumentException("Invalid parameter: oauth token");
    }
    
    /**
     * Constructor
     * 
     * @param consumer
     * @param token
     * @param mode
     * @throws JAXBException
     */
    public Advertisement(OAuthToken consumer, Mode mode) throws JAXBException {
        super(consumer, null, mode);
    }
    

    /**
     * Method to send Advertising
     * 
     * @param sa allowed object is {@link SimpleAd}
     * @return
     * @throws JAXBException
     * @throws BlueviaException
     */
    public CreativeElements send(SimpleAd sa) throws JAXBException, BlueviaException {

        String url = this.uri + "?version=v1";
        
        //Set adRequestId if it has not been set before
    	if (Utils.isEmpty(sa.getAdreqId()))
    		sa.composeAdRequestId(sa.getAdSpace(), restConnector.getAccessToken());
        
    	String data = sa.toHttpQueryString();

        String res;
        try {
            res = this.restConnector.postAdvertisement(url, data.getBytes());

            // res = replace(res);

            // When a portion of a document is invalid, JAXB skips that portion,
            // so
            // the end result is that the unmarshalling returns normally, yet
            // you
            // notice that a part of the content tree is missing
            // When a portion of a document is skipped, the unmarshaller
            // notifies a
            // ValidationEventHandler
            this.u.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
            JAXBElement<SimpleAdResponseType> e = this.u.unmarshal(new StreamSource(new StringReader(res)), SimpleAdResponseType.class);

            AdType adType = e.getValue().getAd();

            CreativeElements creative_elements = new CreativeElements();
            if (adType.getResource() != null && adType.getResource().getCreativeElement() != null) {

                for (CreativeElementType c : adType.getResource().getCreativeElement()) {
                    CreativeElement creative_element = new CreativeElement();
                    creative_element.setType_id(adType.getResource().getAdPresentation());
                    creative_element.setType_name(c.getType());
                    if (c.getAttribute() != null) {
                        for (AttributeType attribute : c.getAttribute()) {
                            if (attribute.getType().equals("locator") || attribute.getType().equals("adtext")) {
                                creative_element.setValue(attribute.getValue());

                            }

                        }
                        if (c.getInteraction() != null) {
                            for (InteractionType i : c.getInteraction()) {
                                if (i.getAttribute() != null) {
                                    for (AttributeType i_attribute : i.getAttribute()) {
                                        if (i_attribute.getType().equals("URL")) {
                                            creative_element.setInteraction(i_attribute.getValue());

                                        }

                                    }
                                }
                            }
                        }

                    }
                    creative_elements.setCreative_element(creative_element);

                }
            }

            return creative_elements;
        } catch (IOException e1) {
            Logger.getLogger(OauthRESTConnector.class.getName()).log(Level.SEVERE, null, e1);
        };
        return null;
    }

}
