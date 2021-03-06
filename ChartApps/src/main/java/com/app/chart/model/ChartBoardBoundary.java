/**
 * 
 */
package com.app.chart.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Sandeep Reddy Battula
 *
 */
@Getter
@Setter
@ToString
@JsonInclude(value = Include.NON_NULL)
@JsonPropertyOrder({ "folderName", "headName", "headerTxt", "employeeDetails" })
public class ChartBoardBoundary implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3945559430317795818L;
	@JsonProperty("folderName")
	private String folderName;
	@JsonProperty("headName")
	private String headName;
	@JsonProperty("headerTxt")
	private String headerTxt;
	@JsonProperty("employeeDetails")
	private List<EmployeeDetails> employeeDetails;
}
