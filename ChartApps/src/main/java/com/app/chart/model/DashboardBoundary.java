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
public class DashboardBoundary implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2011019201699820135L;
	private String folderName;
	private String headerTxt;
	private String managerName;
	private String managerPortalId;
	private boolean isDiffMode;
}
