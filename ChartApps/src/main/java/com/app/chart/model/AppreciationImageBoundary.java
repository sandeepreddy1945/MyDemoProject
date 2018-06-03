/**
 * 
 */
package com.app.chart.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
@JsonPropertyOrder({ "type", "path", "fileName", "displayTxt" })
@ToString(includeFieldNames = true)
@JsonInclude(value = Include.NON_NULL)
@JsonRootName("AppreciationImageBoundary")
public class AppreciationImageBoundary implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5655131974285870758L;
	@JsonProperty("type")
	private String type;
	@JsonProperty("path")
	private String path;
	@JsonProperty("fileName")
	private String fileName;
	@JsonProperty("displayTxt")
	private String displayTxt;

}
