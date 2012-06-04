package com.bluevia.commons;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.bluevia.commons.Entity;

/**
 * Utils class
 * 
 * @author Telefonica I+D
 */
public class Utils {

	private static Logger log = Logger.getLogger(Utils.class.getName());

	/**
	 * Checks if the parameter is an empty String
	 * 
	 * @param value the String to validate
	 * @return true if the String is null or empty, false otherwise
	 */
	public static boolean isEmpty(String value) {
		return (value == null || value.trim().length() == 0);
	}

	/**
	 * Checks if a Entity is valid
	 * 
	 * @param entity the Entity to check
	 * @return true if the Entity is valid, false otherwise
	 */
	public static boolean isValid(Entity entity){
		return (entity != null && entity.isValid());
	}
	
	/**
	 * Checks if the parameter is a phone number (E164 format)
	 * 
	 * @param value the phone number to validate
	 * @return true if the value is a valid phone number, false otherwise
	 */
	public static boolean isPhoneNumber(String value){
		boolean isValid = false;
		log.debug("Starting isPhoneNumber " + value);
		if (value != null) {
			// Expression to validate XSD pattern <xsd:pattern value="[0-9]+"/> applied to
			// UseridType phoneNumber (E164 Type)
			// A phone number, is a maximum of 15 digits following the E164 format
			String expression ="\\d{1,15}$";
			Pattern pattern = Pattern.compile(expression);
			Matcher matcher = pattern.matcher(value);
			if(matcher.matches()){
				isValid = true;
			} else {
				log.error( "Phone Number Address " + value + " does not follow phone pattern");
			}
		} else {
			log.error("Phone Number Address must not be null");
		}
		log.debug("isPhoneNumber: " + isValid);
		return isValid;
	}

	/**
	 * Converts the InputStream to a String
	 * Assumes UTF-8 encoding
	 * 
	 * @param is
	 * @return 
	 * @throws IOException
	 */
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

}