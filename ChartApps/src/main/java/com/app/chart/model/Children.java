/**
 * 
 */
package com.app.chart.model;

import java.util.List;

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
@JsonPropertyOrder({ "children", "childrenDropLevel" })
@JsonInclude(value = Include.NON_NULL)
@JsonRootName(value = "children")
public class Children {

	@JsonProperty("children")
	private List<ChildrenPackage> children;
	@JsonProperty("childrenDropLevel")
	private int childrenDropLevel;
}

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonPropertyOrder({ "text", "HTMLclass", "image", "children" })
@JsonInclude(value = Include.NON_NULL)
// @JsonRootName(value = "children") // no root name for this element as we
// don't display this
class ChildrenPackage {

	@JsonProperty("text")
	private Text text;
	@JsonProperty("HTMLclass")
	private String imagePath;
	@JsonProperty("image")
	private String htmlClass;
	@JsonProperty("children")
	private List<Children> children;
}
