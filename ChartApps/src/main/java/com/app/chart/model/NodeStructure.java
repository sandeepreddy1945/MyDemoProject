/**
 * 
 */
package com.app.chart.model;

import java.io.Serializable;

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
@JsonPropertyOrder({ "text", "image", "children", "HTMLclass" })
@JsonInclude(value = Include.NON_NULL)
@JsonRootName(value = "nodeStructure")
public class NodeStructure implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8899123200147690015L;
	@JsonProperty("text")
	private Text text;
	@JsonProperty("image")
	private String imagePath;
	@JsonProperty("HTMLclass")
	private String hTMLclass;
}
