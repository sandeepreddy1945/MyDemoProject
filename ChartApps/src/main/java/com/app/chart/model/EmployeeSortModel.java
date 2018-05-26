/**
 * 
 */
package com.app.chart.model;

import java.io.Serializable;
import java.util.List;

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
public class EmployeeSortModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6998692762142268155L;
	private EmployeeDetails employeeDetails;
	private boolean headerEmployee;
	private boolean hasChildren;
	private boolean childOfSomeNode;
	private boolean childOfHeaderNode;
	private List<EmployeeDetails> childrenList;
}
