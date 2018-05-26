/**
 * 
 */
package com.app.chart.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
@JsonPropertyOrder({ "teamName", "statusColor" })
public class ProjectStatusBoundary implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -5048918304347666639L;
	@JsonProperty("teamName")
	private String teamName;
	@JsonProperty("statusColor")
	private String statusColor;

}
