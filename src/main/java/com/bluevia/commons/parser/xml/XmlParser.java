package com.bluevia.commons.parser.xml;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;

import com.bluevia.commons.Entity;
import com.bluevia.commons.data.JaxbEntity;
import com.bluevia.commons.parser.IParser;
import com.bluevia.commons.parser.ParseException;

/** 
 * XML implementation of {@link com.bluevia.android.commons.parser.IParser IParser}
 * Class that represents the parser object for any XML entity.
 * Using this class you will be able to parse XML documents containing
 * a representation of any Jaxb entity into an instance
 * object of this entity related to parseClass parameter set
 *
 * @author Telefonica R&D

 */
public class XmlParser implements IParser {

	private static Logger log = Logger.getLogger(XmlParser.class.getName());

	private Class<?> parseClass= null;
	
	public Class<?> getParseClass() {
		return parseClass;
	}

	public void setParseClass(Class<?> c) {
		this.parseClass = c;
	}

	public Entity parse(InputStream is) throws ParseException {
		
		log.debug("Start parser" );
		JaxbEntity parseEntity= new JaxbEntity();

		// Pull parser to do xml processing
		if (is == null) {
			return null;
		}
		if (parseClass == null) {
			return null;
		}
		log.debug("[parseClass]:" + parseClass);

	       try {          
	    	   
	    	   
		        //Class<?>[] classList= {parseClass};
		        JAXBContext jc = JAXBContext.newInstance(parseClass);
		        Unmarshaller unmarshaller = jc.createUnmarshaller();
	        	JAXBElement<?> jaxbElement= unmarshaller.unmarshal(new StreamSource(is), parseClass);
		        log.debug("[XML_PARSED]:" + jaxbElement.getValue());
		        log.debug("[NamespaceURI]:" + jaxbElement.getName().getNamespaceURI());
		        log.debug("[LocalPart]:" + jaxbElement.getName().getLocalPart());
		        log.debug("[DeclaredType]:" + jaxbElement.getDeclaredType());

		        parseEntity.setObject(jaxbElement.getValue());     		        
		        
			} catch (JAXBException e) {
				throw new ParseException("Attempted to parse unsupported entity type");
			}           

		return parseEntity;

	}

	  public static String convertStreamToString(InputStream is) throws Exception {
		    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		    StringBuilder sb = new StringBuilder();
		    String line = null;
		    while ((line = reader.readLine()) != null) {
		      sb.append(line + "\n");
		    }
		    is.close();
		    return sb.toString();
		  }

}
