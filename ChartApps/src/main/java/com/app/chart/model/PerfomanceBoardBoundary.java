/**
 * 
 */
package com.app.chart.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
@ToString(includeFieldNames = true)
@JsonInclude(value = Include.NON_NULL)
@NoArgsConstructor
@JsonPropertyOrder({ "headerTxt", "managerDetailBoundary", "teamMembers", "initalDate", "folderName",
		"sunburstBoundary", "perfomanceMeterBoundary", "currentSprintBoundary", "appreciationImageBoundaries" })
public class PerfomanceBoardBoundary implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3522145124433203757L;
	@JsonProperty("headerTxt")
	private String headerTxt;
	@JsonProperty("managerDetailBoundary")
	private ManagerDetailBoundary managerDetailBoundary;
	@JsonProperty("teamMembers")
	private List<TeamMember> teamMembers;
	@JsonProperty("initalDate")
	private Date initalDate;
	@JsonProperty("folderName")
	private String folderName;
	@JsonProperty("sunburstBoundary")
	private SunburstBoundary sunburstBoundary;
	@JsonProperty("perfomanceMeterBoundary")
	private PerfomanceMeterBoundary perfomanceMeterBoundary;
	@JsonProperty("currentSprintBoundary")
	private CurrentSprintBoundary currentSprintBoundary;
	@JsonProperty("appreciationImageBoundaries")
	private List<AppreciationImageBoundary> appreciationImageBoundaries;

}
