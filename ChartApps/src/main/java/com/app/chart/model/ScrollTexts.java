/**
 * 
 */
package com.app.chart.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Sandeep
 *
 */
@Getter
@Setter
@ToString
@JsonInclude(value = Include.NON_NULL)
@JsonPropertyOrder({ "scrollText" })
@JsonRootName("ScrollTexts")
public class ScrollTexts implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3676880977977745857L;
	@JsonProperty("scrollText")
	private String scrollText;
}
