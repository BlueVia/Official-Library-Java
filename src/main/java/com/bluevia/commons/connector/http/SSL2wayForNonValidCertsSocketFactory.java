package com.bluevia.commons.connector.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.LayeredSchemeSocketFactory;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.bluevia.commons.GsdpConstants;

/**
 * Class to create a Socket Factory that do not do the validation of the certificate
 * This class is done in order to test HTTPS connections without validating the
 * test certificates.
 * This class should not be used in a production environment
 *
 *
 */
class SSL2wayForNonValidCertsSocketFactory implements LayeredSchemeSocketFactory {

    private SSLContext sslcontext = null;
    
    private static String path = null;
    private static String pass = null;
    
    public SSL2wayForNonValidCertsSocketFactory(String path, String pass){
    	this.path = path;
    	this.pass = pass;
    }

    private static SSLContext createEasySSLContext() throws IOException {
        try {
        	
			KeyStore keystore = KeyStore.getInstance(GsdpConstants.KEYSTORE_TYPE_PKCS12);
			
			FileInputStream fis = new FileInputStream(new File(path));
			keystore.load(fis, pass.toCharArray());
			
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			kmf.init(keystore, pass.toCharArray());
        	
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(kmf.getKeyManagers(), new TrustManager[] {
                new X509TrustManager() {
                    public void checkClientTrusted(X509Certificate[] chain, String authType)
                            throws CertificateException {
                    }

                    public void checkServerTrusted(X509Certificate[] chain, String authType)
                            throws CertificateException {
                    }

                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }
            }, null);
            return context;
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    private SSLContext getSSLContext() throws IOException {
        if (this.sslcontext == null) {
            this.sslcontext = createEasySSLContext();
        }
        return this.sslcontext;
    }

    /**
     * @see org.apache.http.conn.scheme.SocketFactory#connectSocket(java.net.Socket,
     *      java.lang.String, int, java.net.InetAddress, int,
     *      org.apache.http.params.HttpParams)
     */
    public Socket connectSocket(Socket sock, String host, int port, InetSocketAddress localAddress, HttpParams params) throws IOException, UnknownHostException,
            ConnectTimeoutException {
        int connTimeout = HttpConnectionParams.getConnectionTimeout(params);
        int soTimeout = HttpConnectionParams.getSoTimeout(params);

        InetSocketAddress remoteAddress = new InetSocketAddress(host, port);
        SSLSocket sslsock = (SSLSocket) ((sock != null) ? sock : createSocket());

        InetSocketAddress isa = null;
        if (localAddress == null)
        	isa = new InetSocketAddress(0);
        else isa = localAddress;
        
        sslsock.bind(isa);

        sslsock.connect(remoteAddress, connTimeout);
        sslsock.setSoTimeout(soTimeout);
        return sslsock;

    }

    /**
     * @see org.apache.http.conn.scheme.SocketFactory#createSocket()
     */
    public Socket createSocket() throws IOException {
        return getSSLContext().getSocketFactory().createSocket();
    }

    /**
     * @see org.apache.http.conn.scheme.SocketFactory#createSocket()
     */
    public Socket createSocket(HttpParams params) throws IOException {
    	// TODO: Check this method. Previous factory was deprecated and remplaced with SchemeSockeFactory
        return getSSLContext().getSocketFactory().createSocket();
    }

    /**
     * @see org.apache.http.conn.scheme.SocketFactory#isSecure(java.net.Socket)
     */
    public boolean isSecure(Socket socket) throws IllegalArgumentException {
        return true;
    }

    /**
     * @see org.apache.http.conn.scheme.LayeredSocketFactory#createSocket(java.net.Socket,
     *      java.lang.String, int, boolean)
     */
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
            throws IOException, UnknownHostException {
        return getSSLContext().getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    // -------------------------------------------------------------------
    // javadoc in org.apache.http.conn.scheme.SocketFactory says :
    // Both Object.equals() and Object.hashCode() must be overridden
    // for the correct operation of some connection managers
    // -------------------------------------------------------------------

	@Override
	public Socket connectSocket(Socket sock, InetSocketAddress remoteAddress,
			InetSocketAddress localAddress, HttpParams params)
			throws IOException, UnknownHostException, ConnectTimeoutException {
		return connectSocket(sock, remoteAddress.getHostName(), remoteAddress.getPort(), localAddress, params);
	}

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        return ((obj != null) && obj.getClass().equals(this.getClass()));
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return SSL2wayForNonValidCertsSocketFactory.class.hashCode();
    }

	@Override
	public Socket createLayeredSocket(Socket socket, String target, int port,
			boolean autoClose) throws IOException, UnknownHostException {
		return createSocket(socket, target, port, autoClose);
	}

}