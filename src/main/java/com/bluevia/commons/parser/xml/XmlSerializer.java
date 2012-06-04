package com.bluevia.commons.parser.xml;


import java.io.ByteArrayOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBIntrospector;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.apache.log4j.Logger;

import com.bluevia.commons.Entity;
import com.bluevia.commons.data.JaxbEntity;
import com.bluevia.commons.parser.ISerializer;
import com.bluevia.commons.parser.SerializeException;


/**
 * Xml serializer for Jaxb entities.
 * 
 * @author Telefonica I+D
 * 
 */
public class XmlSerializer implements ISerializer {

	private static Logger log = Logger.getLogger(XmlSerializer.class.getName());
	
    /**
     * @throws SerializeException thrown if entity is not an instance of {@link com.bluevia.rest.SmsMessageReq.sms.data.SmsMessage SmsMessageReq}
     * @see
     * com.bluevia.rest.commons.parser.BlueviaEntitySerializer#serialize(
     * com.bluevia.rest.commons.Entity, java.io.OutputStream)
     */

	@SuppressWarnings("unchecked")
	@Override
    public ByteArrayOutputStream serialize(Entity entity) throws SerializeException {
		
		log.debug("Start serializer" );
    	if (!(entity instanceof JaxbEntity)) {
            throw new SerializeException("Attempted to parse unsupported entity type");
        }
    	
        JaxbEntity jaxbEntity= (JaxbEntity) entity;
        Object object = jaxbEntity.getObject();
        String jcInstance = jaxbEntity.getJcInstance();
        String namespace= jaxbEntity.getNamespace();
        String qname= jaxbEntity.getQname();
        
        ByteArrayOutputStream os2 = new ByteArrayOutputStream();
        try {          
	        JAXBContext jc2 = JAXBContext.newInstance(jcInstance);
	        JAXBIntrospector introspector = jc2.createJAXBIntrospector();
	        Marshaller marshaller = jc2.createMarshaller();
	        if(null == introspector.getElementName(object)) {
	            JAXBElement jaxbElement = new JAXBElement(new QName(namespace, qname), object.getClass(), object);
	            marshaller.marshal(jaxbElement, os2);
	        } else {
	        	throw new SerializeException("Attempted to parse unsupported entity type");
	        }
	        
	        log.debug("[SERIALIZED_XML]:" + os2);
	        
		} catch (JAXBException e) {
			throw new SerializeException("Attempted to parse unsupported entity type");
		}           
        return os2;
        
    }
    
}
