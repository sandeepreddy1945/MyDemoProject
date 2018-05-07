package com.app.chart.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author Sandeep
 *
 */
@Getter
@Setter
@NoArgsConstructor
@JsonPropertyOrder({ "portalId", "name", "description", "team", "parent", "link","pseudo" })
@JsonInclude(value = Include.NON_NULL)
public class EmployeeDetails {

	@JsonProperty("portalId")
	String portalId;
	@JsonProperty("name")
	String name;
	@JsonProperty("description")
	String description;
	@JsonProperty("team")
	String team;
	@JsonProperty("parent")
	String parent;
	@JsonProperty("link")
	String link;
	@JsonProperty("pseudo")
	boolean pseudo;

	@Getter
	@Setter
	@ToString
	@NoArgsConstructor
	@JsonPropertyOrder({ "host", "protocol", "port" })
	@JsonInclude(value = Include.NON_NULL)
	@JsonRootName(value = "children")
	public class AppDetais {
		@JsonProperty("host")
		String host;
		@JsonProperty("protocol")
		String protocol;
		@JsonProperty("port")
		String port;
	}

	/**
	 * Override to string for display in combo boxes.
	 */
	@Override
	public String toString() {
		return name + " - " + portalId;
	}
}
