package com.bluevia.commons;

/**
 * Common interface for all entities that are sent or received
 * 
 * @author Telefonica I+D
 *
 */
public interface Entity {

	/**
     * Validates the entity data
     * 
	 * @return boolean result of the check
     */
    public boolean isValid();
}