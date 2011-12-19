/**
 * 
 * @category opentel
 * @package com.bluevia.unica
 * @copyright Copyright (c) 2010 Telefonica I+D (http://www.tid.es)
 * @author Bluevia <support@bluevia.com>
 * @version 1.3
 * 
 *          BlueVia is a global iniciative of Telefonica delivered by Movistar
 *          and O2. Please, check out http://www.bluevia.com and if you need
 *          more information contact us at support@bluevia.com
 */

package com.bluevia.java;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.http.HttpParameters;

import com.bluevia.java.exception.BlueviaException;
import com.bluevia.java.oauth.OAuthToken;
import com.telefonica.schemas.unica.rpc.definition.v1.ErrorType;
import com.telefonica.schemas.unica.rpc.payment.v1.MethodResponseType;

/**
 * This class allows REST requests (POST, GET and DELETE) with AUTHORIZATION
 * headers included in the requests
 * 
 * <p>Java class for REST resquest.
 * 
 * 
 */

public class OauthRPCConnector extends AbstractOauthConnector {

    /**
     * Constructor
     * 
     * @param value allowed object is {@link OAuthToken }
     * @throws JAXBException
     */
    public OauthRPCConnector(OAuthToken token) throws JAXBException {
    	super(token);
        this.jc = JAXBContext.newInstance("com.telefonica.schemas.unica.rpc.definition.v1");
		this.u = jc.createUnmarshaller();
    }

    /**
     * POST Request
     * 
     * @param url allowed object is {@link String }
     * @param data allowed object is {@link byte[] }
     * @throws JAXBException,UnicaException
     */

    public String post(String url, byte[] data) throws JAXBException, BlueviaException {

        java.lang.System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");

        String response = null;
        try {

            URL urlObj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
            consumer.sign(conn);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/xml");

            if (data != null) {
                OutputStream os = conn.getOutputStream();
                os.write(data);
                os.flush();
            }

            response = conn.getHeaderField("Location");

            int res_code = conn.getResponseCode();

            handleErrors(res_code, conn.getErrorStream());
            conn.disconnect();
            
            
        } catch (OAuthMessageSignerException ex) {
            Logger.getLogger(OauthRPCConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OAuthExpectationFailedException ex) {
            Logger.getLogger(OauthRPCConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OAuthCommunicationException ex) {
            Logger.getLogger(OauthRPCConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(OauthRPCConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(OauthRPCConnector.class.getName()).log(Level.SEVERE, null, ex);
        }

        return response;
    }
    
    /**
     * POST Request
     * 
     * @param url allowed object is {@link String }
     * @param data allowed object is {@link byte[] }
     * @throws JAXBException,UnicaException
     */
    public String postHTTPS(String url, byte[] data) throws JAXBException, BlueviaException {
    	return postHTTPS(url, data, null);
    }
  
    /**
     * POST Request
     * 
     * @param url allowed object is {@link String }
     * @param data allowed object is {@link byte[] }
     * @param oauthAdditionalParameters TODO
     * @throws JAXBException,UnicaException
     */
    public String postHTTPS(String url, byte[] data, HttpParameters oauthAdditionalParameters) throws JAXBException, BlueviaException {

        java.lang.System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
        
        String response = null;
        
        URL urlObj = null;
        HttpsURLConnection conn = null;
        try {
            urlObj = new URL(url);
            conn = (HttpsURLConnection) urlObj.openConnection();
            
            if (oauthAdditionalParameters != null)
            	consumer.setAdditionalParameters(oauthAdditionalParameters);
            
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/xml");
            consumer.sign(conn);
            
            conn.setHostnameVerifier(new HostnameVerifier()
            {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
            }
            });
            
            if (data != null) {
                OutputStream os = conn.getOutputStream();
                os.write(data);
                os.flush();
            }

            int res_code = conn.getResponseCode();

            handleErrors(res_code, conn.getErrorStream());
            
            response = Utils.convertStreamToString(conn.getInputStream());
            
            // conn.disconnect();
            
        } catch (OAuthMessageSignerException ex) {
            Logger.getLogger(OauthRPCConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OAuthExpectationFailedException ex) {
            Logger.getLogger(OauthRPCConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OAuthCommunicationException ex) {
            Logger.getLogger(OauthRPCConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(OauthRPCConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(OauthRPCConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(OauthRPCConnector.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            conn.disconnect();
        }

        return response;
    }

    private void handleErrors(int res_code, InputStream is) throws IOException, JAXBException, BlueviaException{
    	if (res_code >= 400) {
    		String doc = Utils.convertStreamToString(is);
            JAXBElement<MethodResponseType> e = this.u.unmarshal(new StreamSource(new StringReader(doc.toString())),
                    MethodResponseType.class);
            MethodResponseType response = e.getValue();
            ErrorType error = response.getError();
            throw new BlueviaException(error.getMessage(), error.getCode());
        }
//        if (res_code >= 500) {
//        	String doc = Utils.convertStreamToString(is);
//            /* SERVER EXCEPTION */
//            JAXBElement<ServerExceptionType> e = this.u.unmarshal(new StreamSource(new StringReader(doc.toString())),
//                    ServerExceptionType.class);
//
//            throw new ServerException(e.getValue());
//        }
    }
    
}
