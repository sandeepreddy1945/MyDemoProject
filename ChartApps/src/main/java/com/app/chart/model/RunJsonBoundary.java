/**
 * 
 */
package com.app.chart.model;

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
@JsonPropertyOrder({ "type", "type", "isHeaderApplicable", "displayTxt" })
@ToString(includeFieldNames = true)
@JsonInclude(value = Include.NON_NULL)
public class RunJsonBoundary {

	@JsonProperty("type")
	private String type;
	@JsonProperty("type")
	private String path;
	@JsonProperty("isHeaderApplicable")
	private boolean isHeaderApplicable;
	@JsonProperty("displayTxt")
	private String displayTxt;
}
