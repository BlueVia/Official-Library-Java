/**
 * 
 * @category bluevia
 * @package com.bluevia.java
 * @copyright Copyright (c) 2010 Telefonica I+D (http://www.tid.es)
 * @author Bluevia <support@bluevia.com>
 * 
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

import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.bluevia.java.exception.BlueviaException;
import com.bluevia.java.exception.ClientException;
import com.bluevia.java.exception.ServerException;
import com.bluevia.java.mms.data.MimeContent;
import com.bluevia.java.oauth.OAuthToken;
import com.telefonica.schemas.unica.rest.common.v1.ClientExceptionType;
import com.telefonica.schemas.unica.rest.common.v1.ServerExceptionType;

/**
 * This class allows REST requests (POST, GET and DELETE) with AUTHORIZATION
 * headers included in the requests
 * 
 * <p>Java class for REST resquest.
 * 
 * 
 */
public class OauthRESTConnector extends AbstractOauthConnector {

	/**
	 * Constructor
	 * 
	 * @param value allowed object is {@link OAuthToken }
	 * @throws JAXBException
	 */
	public OauthRESTConnector(OAuthToken token) throws JAXBException {
		super(token);
		this.jc = JAXBContext.newInstance("com.telefonica.schemas.unica.rest.common.v1");
		this.u = jc.createUnmarshaller();
	}

	/**
	 * GET Request
	 * 
	 * @param url allowed object is {@link String }
	 * @throws JAXBException,BlueviaException
	 */

	public String get(String url) throws JAXBException, BlueviaException {

		return get(url, "GET");
	}

	/**
	 * GET Request
	 * 
	 * @param url allowed object is {@link String }
	 * @param method allowed object is {@link String }
	 * @throws JAXBException,BlueviaException
	 * 
	 */

	public String get(String url, String method) throws JAXBException, BlueviaException {

		HttpURLConnection conn = null;
		String res = "";
		try {

			conn = this.getConnection(url);
			conn.setRequestMethod(method);
			conn.setRequestProperty("Accept", "application/xml");
			consumer.sign(conn);
			int res_code = conn.getResponseCode();

			//Handle error responses
			handleErrors(res_code, conn.getErrorStream());

			if (res_code != 204) {
				res = Utils.convertStreamToString(conn.getInputStream());
			}

			return res;

		} catch (OAuthMessageSignerException ex) {
			Logger.getLogger(OauthRESTConnector.class.getName()).log(Level.SEVERE, null, ex);
		} catch (OAuthExpectationFailedException ex) {
			Logger.getLogger(OauthRESTConnector.class.getName()).log(Level.SEVERE, null, ex);
		} catch (OAuthCommunicationException ex) {
			Logger.getLogger(OauthRESTConnector.class.getName()).log(Level.SEVERE, null, ex);
		} catch (MalformedURLException ex) {
			Logger.getLogger(OauthRESTConnector.class.getName()).log(Level.SEVERE, null, ex);

		} catch (IOException ex) {
			Logger.getLogger(OauthRESTConnector.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			if (conn != null)
				conn.disconnect();
		}
		return null;
	}

	public MimeMultipart getMms(String url) throws JAXBException, BlueviaException {

		HttpURLConnection conn = null;
		MimeMultipart mime = null;
		try {

			conn = this.getConnection(url);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/xml");
			consumer.sign(conn);
			int res_code = conn.getResponseCode();

			//Handle error responses
			handleErrors(res_code, conn.getErrorStream());

			if (res_code != 204) {
				InputStream is = conn.getInputStream();
				mime = new MimeMultipart(new ByteArrayDataSource(is, conn.getContentType()));
			}

			return mime;

		} catch (OAuthMessageSignerException ex) {
			Logger.getLogger(OauthRESTConnector.class.getName()).log(Level.SEVERE, null, ex);
		} catch (OAuthExpectationFailedException ex) {
			Logger.getLogger(OauthRESTConnector.class.getName()).log(Level.SEVERE, null, ex);
		} catch (OAuthCommunicationException ex) {
			Logger.getLogger(OauthRESTConnector.class.getName()).log(Level.SEVERE, null, ex);
		} catch (MalformedURLException ex) {
			Logger.getLogger(OauthRESTConnector.class.getName()).log(Level.SEVERE, null, ex);

		} catch (IOException ex) {
			Logger.getLogger(OauthRESTConnector.class.getName()).log(Level.SEVERE, null, ex);
		} catch (MessagingException ex) {
			Logger.getLogger(OauthRESTConnector.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			if (conn != null)
				conn.disconnect();
		}
		return null;
	}
	
	public MimeContent getAttachment(String url) throws JAXBException, BlueviaException {

		HttpURLConnection conn = null;
		
		try {
			
			conn = this.getConnection(url);
			
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/xml");
			consumer.sign(conn);
			int res_code = conn.getResponseCode();

			//Handle error responses
			handleErrors(res_code, conn.getErrorStream());

			if (res_code != 204) {
				String ct = conn.getContentType();
				String cte = conn.getHeaderField("Content-Transfer-Encoding");
				InputStream is = conn.getInputStream();
				
				if (ct.contains("xml") || ct.contains("smil") || ct.contains("text"))
					return Utils.buildMimeContent(ct, cte, Utils.convertStreamToString(is), true);
				else return Utils.buildMimeContent(ct, cte, is, true);
			}

		} catch (IOException e) {
			Logger.getLogger(OauthRESTConnector.class.getName()).log(Level.SEVERE, null, e);
		} catch (OAuthMessageSignerException e) {
			Logger.getLogger(OauthRESTConnector.class.getName()).log(Level.SEVERE, null, e);
		} catch (OAuthExpectationFailedException e) {
			Logger.getLogger(OauthRESTConnector.class.getName()).log(Level.SEVERE, null, e);
		} catch (OAuthCommunicationException e) {
			Logger.getLogger(OauthRESTConnector.class.getName()).log(Level.SEVERE, null, e);
		} catch (MessagingException e) {
			Logger.getLogger(OauthRESTConnector.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (conn != null)
				conn.disconnect();
		}
		return null;
	}

	/**
	 * POST Request
	 * 
	 * @param url allowed object is {@link String }
	 * @param data allowed object is {@link byte[] }
	 * @throws JAXBException,BlueviaException
	 */
	public String post(String url, byte[] data) throws JAXBException, BlueviaException {
		
		HttpURLConnection conn = null;
		String response = null;
		try {

			conn = this.getConnection(url);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/xml");
			consumer.sign(conn);
			if (data != null) {
				OutputStream os = conn.getOutputStream();
				os.write(data);
				os.flush();
			}

			response = conn.getHeaderField("Location");

			int res_code = conn.getResponseCode();

			//Handle error responses
			handleErrors(res_code, conn.getErrorStream());

			return response;
		} catch (OAuthMessageSignerException ex) {
			Logger.getLogger(OauthRESTConnector.class.getName()).log(Level.SEVERE, null, ex);
		} catch (OAuthExpectationFailedException ex) {
			Logger.getLogger(OauthRESTConnector.class.getName()).log(Level.SEVERE, null, ex);
		} catch (OAuthCommunicationException ex) {
			Logger.getLogger(OauthRESTConnector.class.getName()).log(Level.SEVERE, null, ex);
		} catch (MalformedURLException ex) {
			Logger.getLogger(OauthRESTConnector.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			ex.printStackTrace();
			Logger.getLogger(OauthRESTConnector.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			if (conn != null)
				conn.disconnect();
		}

		return null;
	}

	/**
	 * POST Request (Only for Advertisement API)
	 * 
	 * @param url allowed object is {@link String }
	 * @param data allowed object is {@link byte[] }
	 * @throws JAXBException,BlueviaException
	 */

	public String postAdvertisement(String url, byte[] data) throws JAXBException, BlueviaException, IOException {

		String response = null;
		
		try {
			OAuthConsumer consumerCommon = new CommonsHttpOAuthConsumer(consumer.getConsumerKey(), consumer.getConsumerSecret());
			consumerCommon.setTokenWithSecret(consumer.getToken(), consumer.getTokenSecret());
			
            // create an HTTP request to a protected resource
            HttpPost request = new HttpPost(url);
            
            request.addHeader("Content-Type", "application/x-www-form-urlencoded");
            request.addHeader("Accept", "application/xml");
            request.addHeader("Accept-Charset", "utf-8");
            
            ByteArrayEntity entity = new ByteArrayEntity(data);
        	entity.setContentType("application/x-www-form-urlencoded");
        	request.setEntity(entity);
        	
        	consumerCommon.sign(request);
        	
    		HttpClient httpClient = new DefaultHttpClient();
        	
        	HttpResponse httpResponse = httpClient.execute(request);
        	
        	int res_code = httpResponse.getStatusLine().getStatusCode();
        	
        	if (httpResponse.getEntity() != null){
	        	InputStream is = httpResponse.getEntity().getContent();
	        	handleErrors(res_code, is);
	        	response = Utils.convertStreamToString(is);
	        	is.close();
        	}
			return response;
			
		} catch (OAuthMessageSignerException ex) {
			Logger.getLogger(OauthRESTConnector.class.getName()).log(Level.SEVERE, null, ex);
			throw new BlueviaException("OAuthMessageSignerException:" + ex.getLocalizedMessage());
		} catch (OAuthExpectationFailedException ex) {
			Logger.getLogger(OauthRESTConnector.class.getName()).log(Level.SEVERE, null, ex);
		} catch (OAuthCommunicationException ex) {
			Logger.getLogger(OauthRESTConnector.class.getName()).log(Level.SEVERE, null, ex);
		} catch (MalformedURLException ex) {
			Logger.getLogger(OauthRESTConnector.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;

	}

	public String postMMS(String url, byte[] data) throws JAXBException, BlueviaException {

		HttpURLConnection conn = null;
		String response = null;
		try {

			conn = this.getConnection(url);

			conn.setDoOutput(true);
			conn.setRequestMethod("POST");

			conn.setRequestProperty("Accept", "application/xml");
			conn.setRequestProperty("Content-Type", " multipart/form-data; boundary=asdfa487");
			consumer.sign(conn);

			OutputStream os = conn.getOutputStream();
			os.write(data);
			os.flush();

			response = conn.getHeaderField("Location");
			int res_code = conn.getResponseCode();

			//Handle error responses
			handleErrors(res_code, conn.getErrorStream());
			
		} catch (OAuthMessageSignerException ex) {
			Logger.getLogger(OauthRESTConnector.class.getName()).log(Level.SEVERE, null, ex);
		} catch (OAuthExpectationFailedException ex) {
			Logger.getLogger(OauthRESTConnector.class.getName()).log(Level.SEVERE, null, ex);
		} catch (OAuthCommunicationException ex) {
			Logger.getLogger(OauthRESTConnector.class.getName()).log(Level.SEVERE, null, ex);
		} catch (MalformedURLException ex) {
			Logger.getLogger(OauthRESTConnector.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			ex.printStackTrace();
			Logger.getLogger(OauthRESTConnector.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			if (conn != null)
				conn.disconnect();
		}
		return response;
	}

	private void handleErrors(int res_code, InputStream is) throws IOException, JAXBException, BlueviaException {
		if (res_code >= 400 && res_code < 500) {
			String doc = Utils.convertStreamToString(is);
			/* CLIENT EXCEPTION */
			JAXBElement<ClientExceptionType> e = this.u.unmarshal(new StreamSource(new StringReader(doc)),
					ClientExceptionType.class);
			throw new ClientException(e.getValue());
		}
		if (res_code >= 500) {
			String doc = Utils.convertStreamToString(is);
			/* SERVER EXCEPTION */
			JAXBElement<ServerExceptionType> e = this.u.unmarshal(new StreamSource(new StringReader(doc)),
					ServerExceptionType.class);
			throw new ServerException(e.getValue());
		}
	}
	private HttpURLConnection getConnection(String url) throws IOException {

		URL urlObj = new URL(url);
		if (Utils.isHttps(url)) {

			HttpsURLConnection conn = null;
			conn = (HttpsURLConnection) urlObj.openConnection();
			conn.setHostnameVerifier(new HostnameVerifier() {

				public boolean verify(String hostname, SSLSession session) {

					return true;
				}
			});
			return conn;
		} else {

			HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
			return conn;
		}
	}
}
