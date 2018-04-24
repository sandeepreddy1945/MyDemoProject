/**
 * 
 */
package com.app.chart.model;

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
@JsonPropertyOrder({ "val", "href", "target" })
@JsonInclude(value = Include.NON_NULL)
@JsonRootName(value = "contact")
public class Contact {

	@JsonProperty("val")
	private String val;
	@JsonProperty("href")
	private String href;
	@JsonProperty("target")
	private String target;
}
