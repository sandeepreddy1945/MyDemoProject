/**
 * 
 */
package com.app.charts.utilities;

import com.jfoenix.controls.JFXTextField;

/**
 * @author Sandeep
 *
 */
public class JFXNumberField extends JFXTextField {

	/**
	 * 
	 */
	public JFXNumberField() {
	}

	/**
	 * @param text
	 */
	public JFXNumberField(String text) {
		super(text);
	}

	/**
	 * These methods control the alpha characters from being entered.
	 */

	@Override
	public void replaceText(int start, int end, String text) {
		if (text.matches("[0-9]*")) {
			super.replaceText(start, end, text);
		}
	}

	@Override
	public void replaceSelection(String text) {
		if (text.matches("[0-9]*")) {
			super.replaceSelection(text);
		}
	}

}
