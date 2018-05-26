/**
 * 
 */
package com.app.chart.model;

import java.io.Serializable;

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
public class ManagerBoundary implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3650417112370564854L;
	private String folderName;
	private String headerTxt;
	
}
