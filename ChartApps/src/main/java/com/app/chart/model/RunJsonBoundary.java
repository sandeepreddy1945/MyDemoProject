/**
 * 
 */
package com.app.chart.model;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@NoArgsConstructor
@JsonPropertyOrder({})
@ToString(includeFieldNames = true)
@JsonInclude(value = Include.NON_NULL)
public class RunJsonBoundary {

	private String displayName;
	private String type;
	private String path;
	private boolean isHeaderApplicable;
}
