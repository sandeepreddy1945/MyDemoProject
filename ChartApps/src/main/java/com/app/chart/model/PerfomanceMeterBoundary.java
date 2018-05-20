/**
 * 
 */
package com.app.chart.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
@ToString(includeFieldNames = true)
@NoArgsConstructor
@JsonInclude(value = Include.NON_NULL)
@JsonPropertyOrder({ "totalPoints", "currentPoints", "backlogPoints" })
public class PerfomanceMeterBoundary {

	@JsonProperty("totalPoints")
	private double totalPoints;
	@JsonProperty("currentPoints")
	private double currentPoints;
	@JsonProperty("backlogPoints")
	private double backlogPoints;

}
