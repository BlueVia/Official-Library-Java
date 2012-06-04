package com.bluevia.commons.connector.http;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.scheme.LayeredSchemeSocketFactory;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.RequestWrapper;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;

import com.bluevia.commons.GsdpConstants;
import com.bluevia.commons.Utils;
import com.bluevia.commons.connector.GenericResponse;
import com.bluevia.commons.connector.IAuth;
import com.bluevia.commons.connector.IConnector;
import com.bluevia.commons.connector.http.multipart.BlueviaPartBase;
import com.bluevia.commons.connector.http.multipart.MultipartEntity;
import com.bluevia.commons.exception.BlueviaException;
import com.bluevia.commons.parser.ParseException;


/**
 * HttpConnector that allows to send requests via HTTP REST
 *
 * @author Telefonica R&D
 */
public class HttpConnector implements IConnector, IAuth {

	private static Logger log = Logger.getLogger(HttpConnector.class.getName());

	private static final int MAX_REDIRECTS_COUNT = 5;

	/** 
	 * Type to set the REST HttpMethod 
	 */
	private enum RestHttpMethod {POST, GET, PUT, DELETE};

	/** 
	 * Internal HTTP client. 
	 */
	protected DefaultHttpClient mHttpClient;

	/** 
	 * Internal HTTP Request.
	 */
	protected HttpUriRequest mRequest;

	/** 
	 * Enable certificate validation 
	 */
	private boolean enableCertificateValidation = true;

	/**
	 * The path to client certificate
	 */
	protected String mKeyStorePath;

	/**
	 * The password of client certificate
	 */
	protected String mKeyStorePass;

	/** 
	 * Close the HTTP connections
	 */
	public void close() {
		if (mHttpClient != null)
			mHttpClient.getConnectionManager().shutdown();
	}

	public GenericResponse create(String address, HashMap<String, String> parameters, byte[] content, 
			HashMap<String, String> headers)throws BlueviaException, IOException {

		GenericResponse res = null;


		HttpEntity httpEntity = null;
		if (content != null && headers != null){
			httpEntity = createHttpEntity(content, headers);
		}
		res = createAndExecuteRequest(address, RestHttpMethod.POST, httpEntity, parameters, headers);

		return res;
	}

	public GenericResponse create(String address, HashMap<String, String> parameters, BlueviaPartBase[] parts, HashMap<String, String> headers) throws BlueviaException, IOException {

		GenericResponse res = null;

		HttpEntity httpEntity = new MultipartEntity(parts);

		res = createAndExecuteRequest(address, RestHttpMethod.POST, httpEntity, parameters, headers);

		return res;
	}

	public GenericResponse retrieve(String feedUri) throws BlueviaException, IOException {
		return retrieve(feedUri, null);
	}

	public GenericResponse retrieve(String feedUri, HashMap<String, String> parameters) throws BlueviaException, IOException {
		return retrieve(feedUri, parameters, null);
	}

	public GenericResponse retrieve(String feedUri, HashMap<String, String> parameters, HashMap<String, String> headers) throws BlueviaException, IOException {
		GenericResponse res = createAndExecuteRequest(feedUri, RestHttpMethod.GET, null, parameters, headers);
		return res;
	}

	public GenericResponse update(String feedUri, HashMap<String, String> parameters, byte[] body, HashMap<String, String> headers)throws BlueviaException, IOException {

		GenericResponse res = null;

		HttpEntity httpEntity = null;
		if (body != null && headers != null){
			httpEntity = createHttpEntity(body, headers);
		}
		res = createAndExecuteRequest(feedUri, RestHttpMethod.PUT, httpEntity, parameters, headers);

		return res;
	}

	public GenericResponse delete(String feedUri) throws BlueviaException, IOException {
		return delete(feedUri, null);
	}

	public GenericResponse delete(String feedUri, HashMap<String, String> parameters) throws BlueviaException, IOException {
		return delete(feedUri, parameters, null);
	}

	public GenericResponse delete(String feedUri, HashMap<String, String> parameters, HashMap<String, String> headers) throws BlueviaException, IOException {
		GenericResponse res = createAndExecuteRequest(feedUri, RestHttpMethod.DELETE, null, parameters, headers);
		return res;
	}

	/**
	 * Creates an HttpClient
	 *
	 * @return the created HTTP Client
	 */
	protected HttpClient lazyInitHttpClient() throws BlueviaException, IOException {
		if (mHttpClient == null) {
			HttpParams params = new BasicHttpParams();

			// Default connection and socket timeout of 20 seconds.
			HttpConnectionParams.setConnectionTimeout(params, 20 * 1000);
			HttpConnectionParams.setSoTimeout(params, 20 * 1000);
			HttpConnectionParams.setSocketBufferSize(params, 8192);

			//Default timeout used when retrieving a ManagedClientConnection from the ClientConnectionManager.
			//ConnManagerParams.setTimeout(params, 20 * 1000);
			HttpConnectionParams.setConnectionTimeout(params, 20 * 1000);
			// Don't handle redirects -- return them to the caller.
			HttpClientParams.setRedirecting(params, false);

			// Set the specified user agent and register standard protocols.
			//HttpProtocolParams.setUserAgent(params, getCurrentUserAgent());

			if (enableCertificateValidation)
				mHttpClient = createHttpClientWithCertificateValidation (params);
			else
				mHttpClient = createHttpClientWithNoCertificateValidation (params);

			mHttpClient.removeRequestInterceptorByClass(
					org.apache.http.protocol.RequestExpectContinue.class);

			mHttpClient.addRequestInterceptor(new CurlLogger());
		}
		return mHttpClient;
	}

	private HttpEntity createHttpEntity(byte[] body, HashMap<String, String> headers) throws IOException, ParseException {

		if (body != null) {
			try {
				log.debug("Serialized entry: " + new String(body, "UTF-8"));
			} catch (UnsupportedEncodingException uee) {
				// should not happen
				throw new IllegalStateException("UTF-8 should be supported!",
						uee);
			}
		}

		String contentType = headers.get(GsdpConstants.HEADER_CONTENT_TYPE);

		AbstractHttpEntity httpEntity = new ByteArrayEntity(body);

		httpEntity.setContentType(contentType);

		return httpEntity;
	}

	/**
	 * Create an HttpRequest with headers and execute it
	 * @param clientUrl endpoint to send the request
	 * @param httpMethod HTTP method (POST|GET)
	 * @param httpEntity HTTP entity to send in the request
	 * @param parameters HTTP query parameters
	 * @param headers HTTP headers
	 * @return the body of the response to this request
	 * @throws IOException
	 * @throws BlueviaException 
	 */
	private GenericResponse createAndExecuteRequest(String clientUrl, RestHttpMethod httpMethod,
			HttpEntity httpEntity, HashMap<String, String> parameters, HashMap<String, String> headers) throws IOException, BlueviaException {

		HttpResponse response = null;

		HttpClient client = lazyInitHttpClient();
		int redirectsLeft = MAX_REDIRECTS_COUNT;

		String url;
		if (parameters != null)
			url = generateQueryUrl(clientUrl, parameters);
		else url = clientUrl;

		//We must follow redirects ourselves, since we want to follow redirects even on POSTs, which
		// the HTTP library does not do.
		while (redirectsLeft > 0) {
			//Verify Uri
			URI uri = null;
			try {
				uri = new URI(url);
			} catch (URISyntaxException use) {
				log.debug("Unable to parse " + url + " as URI.", use);
				throw new IOException("Unable to parse " + url
						+ " as URI.");
			}

			//Create the request
			switch (httpMethod) {
			case POST:
				mRequest = new HttpPost(uri);
				//include the entity
				if (httpEntity != null) {
					((HttpPost)mRequest).setEntity(httpEntity);
				}

				break;
			case GET:
				mRequest = new HttpGet(uri);
				break;
			case PUT:
				mRequest = new HttpPut(uri);
				//include the entity
				if (httpEntity != null) {
					((HttpPost)mRequest).setEntity(httpEntity);
				}
				break;
			case DELETE:
				mRequest = new HttpDelete(uri);
				break;
			default:
				throw new IllegalStateException("Not expected method"); //--Should not happen
			}

			//Add request headers
			if (headers != null){
				Iterator<Entry<String, String>> headerSet = headers.entrySet().iterator();
				while (headerSet.hasNext()){
					Entry<String,String> thisHeader = headerSet.next();
					mRequest.addHeader(thisHeader.getKey(), thisHeader.getValue());
				}
			}

			//Authenticate request - abstract method
			authenticate();

			//Execute the request
			response = client.execute(mRequest);

			//Analyze the response
			StatusLine statusLine = response.getStatusLine();
			if (statusLine == null) {
				log.warn("StatusLine is null.");
				throw new NullPointerException("StatusLine is null -- should not happen.");
			}

			HashMap<String, String> responseHeaders = new HashMap<String, String>();
			log.debug(response.getStatusLine().toString());
			for (Header h : response.getAllHeaders()) {
				log.debug(h.getName() + ": " + h.getValue());
				if (responseHeaders!= null && h != null && h.getName() != null && h.getValue() != null)
					responseHeaders.put(h.getName(), h.getValue());
			}

			int status = statusLine.getStatusCode();
			HttpEntity responseHttpEntity = response.getEntity();

			InputStream content = null;
			if (responseHttpEntity != null)
				content = responseHttpEntity.getContent();

			if ((status >= 200) && (status < 300)) {
				return new GenericResponse(status, statusLine.getReasonPhrase(), content, responseHeaders);

			} else if (status == 302) { //manage redirects
				//if (responseHttpEntity != null) responseHttpEntity.consumeContent(); //Otherwise, the http lib cannot close connection
				// responseHttpEntity.consumeContent() is deprecated: 
				// Either use getContent() and call InputStream.close() on that; 
				// otherwise call writeTo(OutputStream) which is required to free the resources.
				if (responseHttpEntity != null) content.close(); //Otherwise, the http lib cannot close connection
				Header location = response.getFirstHeader("Location");
				if (location == null) {
					log.debug("Redirect requested but no Location "
							+ "specified.");
					throw new HttpException("Http 302 Response Error: Redirect requested but no Location Header found", HttpException.BAD_REQUEST_EXCEPTION);
				}

				log.debug("Following redirect to " + location.getValue());

				//Add default query parameters
				url = generateQueryUrl(location.getValue(), parameters);

			} else {
				throw new HttpException(statusLine.getReasonPhrase(), status, content, responseHeaders);
			}
			redirectsLeft --;
		}

		return null;
	}

	/**
	 * Disables the validation of certificates for SSL connections.
	 */
	private DefaultHttpClient createHttpClientWithNoCertificateValidation(HttpParams params)  throws IOException {
		// Create a specific protocol socket factory, for https, based on java default
		// socket factories.
		LayeredSchemeSocketFactory sslSocketFactory = null;
		
		if (Utils.isEmpty(mKeyStorePath))
			sslSocketFactory = new SSLForNonValidCertsSocketFactory();
		else sslSocketFactory = new SSL2wayForNonValidCertsSocketFactory(mKeyStorePath, mKeyStorePass);

		SchemeRegistry sr = new SchemeRegistry();
		sr.register(new Scheme("https", 8443, sslSocketFactory));
		sr.register(new Scheme("https", 443, sslSocketFactory));
		sr.register(new Scheme("http",  8080, PlainSocketFactory.getSocketFactory()));
		sr.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));

		ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(sr);
		return new DefaultHttpClient(manager, params);
	}

	/**
	 * Enables the validation of certificates for SSL connections.
	 * @throws IOException 
	 */
	private DefaultHttpClient createHttpClientWithCertificateValidation(HttpParams params) throws IOException {
		// Create a specific protocol socket factory, for https, based on java default
		// socket factories.

		LayeredSchemeSocketFactory sslSocketFactory = null;
		if (Utils.isEmpty(mKeyStorePath))
			sslSocketFactory = SSLSocketFactory.getSocketFactory();
		else sslSocketFactory = createSSLSocketFactoryWithKeystore();
		
		SchemeRegistry sr = new SchemeRegistry();
		sr.register(new Scheme("https", 8443,  sslSocketFactory));
		sr.register(new Scheme("https", 443, sslSocketFactory));
		sr.register(new Scheme("http", 8080, PlainSocketFactory.getSocketFactory()));
		sr.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));

		ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(sr);
		return new DefaultHttpClient(manager, params);
	}

	private SSLSocketFactory createSSLSocketFactoryWithKeystore() throws IOException {
		
		SSLSocketFactory sslSocketFactory = null;

		try {

			KeyStore keystore = KeyStore.getInstance(GsdpConstants.KEYSTORE_TYPE_PKCS12);
			
			FileInputStream fis = new FileInputStream(new File(mKeyStorePath));
			keystore.load(fis, mKeyStorePass.toCharArray());
			
			sslSocketFactory = new SSLSocketFactory(keystore, mKeyStorePass);

		} catch (GeneralSecurityException e) {
			log.error("Error in certificate. " + e.getMessage(), e);
			throw new IOException(e);		
		}

		return sslSocketFactory;
	}

	/**
	 * Generates the url query string including the parameters
	 * @param url the base url
	 * @return the url including the query parameters
	 * @throws UnsupportedEncodingException 
	 */
	private String generateQueryUrl(String url, HashMap<String, String> params) throws UnsupportedEncodingException {
		StringBuffer res = new StringBuffer(url);
		res.append(url.indexOf('?') >= 0 ? '&' : '?');

		Set<String> keys = params.keySet();
		int i = 0;
		for (String name : keys) {
			if (i > 0) {
				res.append('&');
			}
			res.append(encodeUri(name)).append('=');
			res.append(encodeUri(params.get(name)));
			i++;
		}
		return res.toString();
	}

	/**
	 * Encodes the uri in UTF-8
	 * 
	 * @param uri
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static String encodeUri(String uri) throws UnsupportedEncodingException {
		String encodedUri;
		try {
			encodedUri = URLEncoder.encode(uri, "UTF-8");
		} catch (UnsupportedEncodingException uee) {
			// should not happen.
			log.error("[QueryParams]" + 
					"UTF-8 not supported -- should not happen.  "
					+ "Using default encoding.", uee);
			encodedUri = URLEncoder.encode(uri, "UTF-8");
		}
		return encodedUri;
	}

	@Override
	public void authenticate() throws BlueviaException {
		//No authentication for default HTTPConnector, do nothing
	}

	/**
	 * Logs cURL commands equivalent to requests.
	 */
	private class CurlLogger implements HttpRequestInterceptor {
		public CurlLogger() {
			super();
		}

		public void process(HttpRequest request, HttpContext context)
				throws org.apache.http.HttpException, IOException {
			log.info(toCurl((HttpUriRequest) request, true));
		}
	}

	/**
	 * Generates a cURL command equivalent to the given request.
	 */
	private static String toCurl(HttpUriRequest request, boolean logAuthToken) throws IOException {
		StringBuilder builder = new StringBuilder();

		builder.append("curl ");

		for (Header header: request.getAllHeaders()) {
			if (!logAuthToken
					&& (header.getName().equals("Authorization") ||
							header.getName().equals("Cookie"))) {
				continue;
			}
			builder.append("--header \"");
			builder.append(header.toString().trim());
			builder.append("\" ");
		}

		URI uri = request.getURI();

		// If this is a wrapped request, use the URI from the original
		// request instead. getURI() on the wrapper seems to return a
		// relative URI. We want an absolute URI.
		if (request instanceof RequestWrapper) {
			HttpRequest original = ((RequestWrapper) request).getOriginal();
			if (original instanceof HttpUriRequest) {
				uri = ((HttpUriRequest) original).getURI();
			}
		}

		builder.append("\"");
		builder.append(uri);
		builder.append("\"");

		if (request instanceof HttpEntityEnclosingRequest) {
			HttpEntityEnclosingRequest entityRequest =
					(HttpEntityEnclosingRequest) request;
			HttpEntity entity = entityRequest.getEntity();
			if (entity != null && entity.isRepeatable()) {
				//if (entity.getContentLength() < 1024) {
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				entity.writeTo(stream);
				String entityString = stream.toString();

				// TODO: Check the content type, too.
				builder.append(" --data-ascii \"")
				.append(entityString)
				.append("\"");
				//} else {
				//    builder.append(" [TOO MUCH DATA TO INCLUDE]");
				//}
			}
		}

		return builder.toString();
	}

}

