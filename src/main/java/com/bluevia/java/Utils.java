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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.UUID;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.bluevia.java.oauth.OAuthToken;
import com.telefonica.schemas.unica.rest.common.v1.UserIdType;

/**
 * Utils class
 * 
 * <p>Java class for utils methods.
 * 
 * 
 */

public class Utils {

	public static String guessType(String file) {

		return "text/plain";
	}

	public static String guessType(byte[] file) {

		return "text/plain";
	}
	
	// Returns the contents of the file in a byte array.
	public static byte[] getBytesFromFile(File file) throws IOException {
	    InputStream is = new FileInputStream(file);

	    // Get the size of the file
	    long length = file.length();

	    // You cannot create an array using a long type.
	    // It needs to be an int type.
	    // Before converting to an int type, check
	    // to ensure that file is not larger than Integer.MAX_VALUE.
	    if (length > Integer.MAX_VALUE) {
	        // File is too large
	    }

	    // Create the byte array to hold the data
	    byte[] bytes = new byte[(int)length];

	    // Read in the bytes
	    int offset = 0;
	    int numRead = 0;
	    while (offset < bytes.length
	           && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
	        offset += numRead;
	    }

	    // Ensure all the bytes have been read in
	    if (offset < bytes.length) {
	        throw new IOException("Could not completely read file "+file.getName());
	    }

	    // Close the input stream and return bytes
	    is.close();
	    return bytes;
	}

	public static void insertAttach(DataOutputStream out, File file, String contentType, String separator) throws IOException {

		byte[] content = getBytesFromFile(file);

		out.writeBytes("--" + separator);
		out.writeBytes("\r\n");
		out.writeBytes("Content-Disposition: attachment; fileName=\"" + file.getName());
		out.writeBytes("\"\r\n");

		if (contentType.contains("text")) {
			out.writeBytes("Content-Type: " + contentType);
			out.writeBytes(";charset=\"UTF-8\"");
			out.writeBytes("\r\n");
			out.writeBytes("Content-Transfer-Encoding: 8bit");
			out.writeBytes("\r\n");
			out.writeBytes("\r\n");
			out.write(content);
		} else {
			out.writeBytes("Content-Type: ");
			out.writeBytes(contentType);
			out.writeBytes("\r\n");
			out.writeBytes("Content-Transfer-Encoding: 8bit");
			//out.writeBytes("Content-Transfer-Encoding: base64");
			out.writeBytes("\r\n");
			out.writeBytes("\r\n");
			out.write(content);
			//out.write(Base64.encodeBase64(content));
		}
		
		out.writeBytes("\r\n");

	}

	public static String generateBoundary() {
		String part1 = String.valueOf(UUID.randomUUID());
		String part2 = new StringBuffer(part1).reverse().toString();
		return part1 + part2;
	}

	public static boolean isEmpty(String value) {
		return (value == null || value.trim().isEmpty());
	}
	
	public static boolean isNumber(String value){
		try {
            Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
	}

	public static boolean validateToken(OAuthToken token){
		return token != null &&
			!Utils.isEmpty(token.getToken()) &&
			!Utils.isEmpty(token.getSecret());
	}
	
	public static boolean isUrl(String url){
		return(url.toLowerCase().startsWith("http"));
	}

	public static boolean isHttps(String url){
		return(url.toLowerCase().startsWith("https"));
	}

	public static String convertStreamToString(InputStream is) throws IOException {
		if (is != null) {
			Writer writer = new StringWriter();

			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(
						new InputStreamReader(is, "UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}
			return writer.toString();
		} else {        
			return "";
		}
	}
	
	public static String printUserIdType(UserIdType userId){
		
		if (!isEmpty(userId.getAlias())){
			return "alias:" + userId.getAlias();
		} else if (!isEmpty(userId.getPhoneNumber())){
			return "phoneNumber:" + userId.getPhoneNumber();
		} else if (!isEmpty(userId.getAnyUri())){
			return "anyUri:" + userId.getAnyUri();
		} else if (userId.getIpAddress() != null){
			if (!isEmpty(userId.getIpAddress().getIpv4())){
				return "ipv4:" + userId.getIpAddress().getIpv4();
			} else if (!isEmpty(userId.getIpAddress().getIpv6())){
				return "ipv6:" + userId.getIpAddress().getIpv6();
			} 
		} else if (userId.getOtherId() != null){
			return userId.getOtherId().getType() + ":" + userId.getOtherId().getValue();
		}
		
		return null;
	}
	
	public static String toHttpQueryString(Hashtable<String, String> ht){
		StringBuffer sb = new StringBuffer();
		Enumeration<String> keys = ht.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            String value = (String) ht.get(key);
            sb.append(key).append("=").append(value).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        
        return sb.toString();
	}
	
	public static XMLGregorianCalendar generateTimestamp(){
        GregorianCalendar gcal = new GregorianCalendar();
        try {
            return  DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }
	
	public static long xmlGregorianCalendarToTimestamp(XMLGregorianCalendar calendar){
		return calendar.toGregorianCalendar().getTimeInMillis()/1000;
	}
}
