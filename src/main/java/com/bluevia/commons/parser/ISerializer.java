package com.bluevia.commons.parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.bluevia.commons.Entity;

/**
 * Interface that represents the serializer object for an entity. Object implementing this interface
 * will be able to serialize an entity object into an HTTP body request containing a representation of this
 * entity object of this entity
 *
 */
public interface ISerializer {
	
    /**
     * Class to serialize an entity into an OutputStream
     * @param entity Entity to parse.
     * @return OutputStream containing the serialized content
     * @throws IOException thrown in the event of an I/O error during serialization
     * @throws SerializeException thrown if the entity or output stream are unfit for serialization
     *  or in the event of any other serialization error
     */
	ByteArrayOutputStream serialize(Entity entity) throws SerializeException;

}
