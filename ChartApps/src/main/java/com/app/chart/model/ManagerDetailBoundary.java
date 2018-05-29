/**
 * 
 */
package com.app.chart.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Sandeep
 *
 */
@Getter
@Setter
@JsonInclude(value = Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "portalId", "name", "designation", "folderName", "teamMembers" })
public class ManagerDetailBoundary implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2626656708493273347L;
	@JsonProperty("portalId")
	private String portalId;
	@JsonProperty("name")
	private String name;
	@JsonProperty("designation")
	private String designation;
	@JsonProperty("folderName")
	private String folderName;
	@JsonProperty("teamMembers")
	private List<TeamMember> teamMembers;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return folderName;
	}

}
