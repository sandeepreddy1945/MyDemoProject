/**
 * 
 */
package com.app.chart.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Sandeep
 *
 */
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(value = Include.NON_NULL)
@JsonPropertyOrder({ "red", "green", "blue", "opacity" })
@ToString(includeFieldNames = true)
public class CustomColor implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1082536919349441979L;
	@JsonProperty("red")
	double red;
	@JsonProperty("green")
	double green;
	@JsonProperty("blue")
	double blue;
	@JsonProperty("opacity")
	double opacity;

	public Color fecthColor() {
		return new Color(red, green, blue, opacity);
	}

	/**
	 * @param color
	 */
	public CustomColor(Color color) {
		this.red = color.getRed();
		this.green = color.getGreen();
		this.blue = color.getBlue();
		this.opacity = color.getOpacity();
	}
}
