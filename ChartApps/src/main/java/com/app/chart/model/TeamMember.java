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
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Sandeep
 *
 */
@Getter
@Setter
@NoArgsConstructor
@JsonPropertyOrder({ "portalId", "name", "description", "team", "parent", "link", "score1", "score2", "score3",
		"intreval1", "intreval2", "intreval3", "valueAdd", "quality", "onTime", "extraDescription" })
@JsonInclude(value = Include.NON_NULL)
public class TeamMember implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6191758893719953530L;
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
	@JsonProperty("score1")
	int score1;
	@JsonProperty("score2")
	int score2;
	@JsonProperty("score3")
	int score3;
	@JsonProperty("intreval1")
	String intreval1;
	@JsonProperty("intreval2")
	String intreval2;
	@JsonProperty("intreval3")
	String intreval3;
	@JsonProperty("valueAdd")
	int valueAdd;
	@JsonProperty("quality")
	int quality;
	@JsonProperty("onTime")
	int onTime;
	@JsonProperty("extraDescription")
	String extraDescription;
	// TODO add score getters and calendar events.
}
