/**
 * 
 */
package com.app.chart.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Sandeep
 *
 */
@Getter
@Setter
@NoArgsConstructor
@JsonPropertyOrder({ "type", "path", "isHeaderApplicable", "displayTxt" })
@ToString(includeFieldNames = true)
@JsonInclude(value = Include.NON_NULL)
public class RunJsonBoundary implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7781037217259637667L;
	@JsonProperty("type")
	private String type;
	@JsonProperty("path")
	private String path;
	@JsonProperty("isHeaderApplicable")
	private boolean isHeaderApplicable;
	@JsonProperty("displayTxt")
	private String displayTxt;
}
