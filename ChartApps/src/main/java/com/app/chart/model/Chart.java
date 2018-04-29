/**
 * 
 */
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
 * @author Sandeep
 *
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonPropertyOrder({ "container","rootOrientation", "nodeAlign", "animateOnInit", "animateOnInitDelay", "connectors", "animation",
		"node" })
@JsonInclude(value = Include.NON_NULL)
@JsonRootName(value = "chart")
public class Chart {

	@JsonProperty("container")
	private String container;
	@JsonProperty("rootOrientation")
	private String rootOrientation;
	@JsonProperty("nodeAlign")
	private String nodeAlign;
	@JsonProperty("animateOnInit")
	private boolean animateOnInit;
	@JsonProperty("animateOnInitDelay")
	private int animateOnInitDelay;
	@JsonProperty("connectors")
	private Connectors connectors;
	@JsonProperty("animation")
	private Animation animation;
	@JsonProperty("node")
	private Node node;

	@Getter
	@Setter
	@ToString
	@NoArgsConstructor
	@JsonPropertyOrder({ "type" })
	@JsonInclude(value = Include.NON_NULL)
	@JsonRootName(value = "connectors")
	public class Connectors {
		@JsonProperty("type")
		private String type;
	}

	@Getter
	@Setter
	@ToString
	@NoArgsConstructor
	@JsonPropertyOrder({ "nodeSpeed", "connectorsAnimation", "nodeAnimation", "connectorsSpeed" })
	@JsonInclude(value = Include.NON_NULL)
	@JsonRootName(value = "animation")
	public class Animation {
		@JsonProperty("nodeSpeed")
		private int nodeSpeed;
		@JsonProperty("connectorsAnimation")
		private String connectorsAnimation;
		@JsonProperty("nodeAnimation")
		private String nodeAnimation;
		@JsonProperty("connectorsSpeed")
		private int connectorsSpeed;
	}

	@Getter
	@Setter
	@ToString
	@NoArgsConstructor
	@JsonPropertyOrder({ "HTMLclass", "collapsable", "drawLineThrough" })
	@JsonInclude(value = Include.NON_NULL)
	@JsonRootName(value = "animation")
	public class Node {
		@JsonProperty("HTMLclass")
		private String htmlClass;
		@JsonProperty("collapsable")
		private boolean collapsable;
		@JsonProperty("drawLineThrough")
		private boolean drawLineThrough;
	}

}
