package com.bluevia.commons.connector;

import com.bluevia.commons.exception.BlueviaException;

/**
 * Interface that will be implemented by REST clients that will allow to
 * authenticate requests.
 *
 */
public interface IAuth {

	/**
	 * Authenticates the request
	 */
	public void authenticate() throws BlueviaException;
	
}
