/**
 * 
 */
package com.app.chart.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
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
@ToString
@NoArgsConstructor
@JsonPropertyOrder({ "parent", "pseudo" })
@JsonInclude(value = Include.NON_NULL)
@JsonRootName(value = "pseudonode")
public class PseudoNode implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 407197495828758354L;
	@JsonProperty("parent")
	private String parent;
	@JsonProperty("pseudo")
	private boolean pseudo;
}
