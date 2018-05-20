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
@JsonPropertyOrder({ "totalSprintPoints", "currentSprintPoints", "backlogSprintPoints" })
public class CurrentSprintBoundary {

	@JsonProperty("totalSprintPoints")
	private double totalSprintPoints;
	@JsonProperty("currentSprintPoints")
	private double currentSprintPoints;
	@JsonProperty("backlogSprintPoints")
	private double backlogSprintPoints;
}
