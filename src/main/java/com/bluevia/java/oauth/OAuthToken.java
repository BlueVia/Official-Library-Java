/**
 * 
 * @category bluevia
 * @package com.bluevia.java.oauth
 * @copyright Copyright (c) 2010 Telefonica I+D (http://www.tid.es)
 * @author Bluevia <support@bluevia.com>
 * 
 * 
 *          BlueVia is a global iniciative of Telefonica delivered by Movistar
 *          and O2. Please, check out http://www.bluevia.com and if you need
 *          more information contact us at support@bluevia.com
 */
package com.bluevia.java.oauth;
/**
 * OAuthToken
 * @package com.bluevia.java.oauth 
 */

/**
 * <p>Java class to define OAuthToken (token key, token secret).
 *
 */

public class OAuthToken {

    private String token;
    private String secret;
    private String url;

    /**
     * Constructor
     * @param token (key)
     * @param tokenSecret
     */
    public OAuthToken(String tokenKey, String tokenSecret) {
        this.token = tokenKey;
        this.secret = tokenSecret;
    }

    /**
     * Get Token Key
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * Set Tolen Key
     * @param token (the token Key to set)
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Get Token Secret
     * @return the secret
     */
    public String getSecret() {
        return secret;
    }

    /**
     * Set Token Secret
     * @param secret (the token secret to set)
     */
    public void setSecret(String secret) {
        this.secret = secret;
    }

    /**
     * Get Url
     * @return
     */
    public String getUrl() {
    
        return url;
    }

    /**
     * Set Url
     * @param url
     */
    public void setUrl(String url) {
    
        this.url = url;
    }
    
}
