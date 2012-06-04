package com.bluevia.commons.data;

import com.bluevia.commons.Entity;

/**
 * Entity to represent error messages received from the server.
 *
 */
public class StringEntity implements Entity {

	private String text;

	public StringEntity(String text){
		this.text = text;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public boolean isValid() {
		return true;
	}

}
