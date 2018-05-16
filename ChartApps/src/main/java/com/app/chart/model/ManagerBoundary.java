/**
 * 
 */
package com.app.chart.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Sandeep
 *
 */
@Getter
@Setter
@ToString
@JsonInclude(value = Include.NON_NULL)
public class ManagerBoundary {

	private String folderName;
	private String headerTxt;
	
}
