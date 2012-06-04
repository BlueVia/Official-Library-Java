package com.bluevia.commons.connector.http.oauth;

import oauth.signpost.OAuth;
import oauth.signpost.http.HttpParameters;
import oauth.signpost.http.HttpRequest;
import oauth.signpost.signature.SigningStrategy;

/**
 * Implementation of OAuth signpost SigningStrategy to include Bluevia special parameters in the sign.
 * 
 * @author Telefonica R&D
 */
public class BVAuthorizationHeaderSigningStrategy implements
		SigningStrategy {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1984135842800698120L;

	public static final String OAUTH_API_NAME = "xoauth_apiName";

	@Override
	public String writeSignature(String signature, HttpRequest request,
			HttpParameters requestParameters) {
		
		StringBuilder sb = new StringBuilder();

        sb.append("OAuth ");
        if (requestParameters.containsKey("realm")) {
            sb.append(requestParameters.getAsHeaderElement("realm"));
            sb.append(", ");
        }
        if (requestParameters.containsKey(OAuth.OAUTH_TOKEN)) {
            sb.append(requestParameters.getAsHeaderElement(OAuth.OAUTH_TOKEN));
            sb.append(", ");
        }
        if (requestParameters.containsKey(OAuth.OAUTH_CALLBACK)) {
            sb.append(requestParameters.getAsHeaderElement(OAuth.OAUTH_CALLBACK));
            sb.append(", ");
        }
        if (requestParameters.containsKey(OAuth.OAUTH_VERIFIER)) {
            sb.append(requestParameters.getAsHeaderElement(OAuth.OAUTH_VERIFIER));
            sb.append(", ");
        }
        sb.append(requestParameters.getAsHeaderElement(OAuth.OAUTH_CONSUMER_KEY));
        sb.append(", ");
        sb.append(requestParameters.getAsHeaderElement(OAuth.OAUTH_VERSION));
        sb.append(", ");
        sb.append(requestParameters.getAsHeaderElement(OAuth.OAUTH_SIGNATURE_METHOD));
        sb.append(", ");
        sb.append(requestParameters.getAsHeaderElement(OAuth.OAUTH_TIMESTAMP));
        sb.append(", ");
        sb.append(requestParameters.getAsHeaderElement(OAuth.OAUTH_NONCE));
        sb.append(", ");
        sb.append(OAuth.toHeaderElement(OAuth.OAUTH_SIGNATURE, signature));
        
        //Modification in signing strategy to include api name
        if (requestParameters.containsKey(OAUTH_API_NAME)) {
            sb.append(", ");
            sb.append(requestParameters.getAsHeaderElement(OAUTH_API_NAME));
        }

        String header = sb.toString();
        request.setHeader(OAuth.HTTP_AUTHORIZATION_HEADER, header);

        return header;
	}

}
