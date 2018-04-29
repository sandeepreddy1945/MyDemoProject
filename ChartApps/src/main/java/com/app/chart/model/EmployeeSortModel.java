/**
 * 
 */
package com.app.chart.model;

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
public class EmployeeSortModel {

	private EmployeeDetails employeeDetails;
	private boolean headerEmployee;
	private boolean hasChildren;
	private boolean childOfSomeNode;
	private boolean childOfHeaderNode;
	private List<EmployeeDetails> childrenList;
}
