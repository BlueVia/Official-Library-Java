package com.bluevia.commons.parser;

import java.io.InputStream;

import com.bluevia.commons.Entity;


/**
 * Interface that represents the parser object for an entity. Object implementing this interface
 * will be able to parse HTTP body responses containing an representation of an entity into an instance
 * object of this entity
 *
 */
public interface IParser {
	
    /**
     * @param is the InputStream containing the stream with the entity information
     * @return the Entity object
     * @throws ParseException when an error occurs converting the stream into an object
     */
    public Entity parse(InputStream is) throws ParseException;

}

