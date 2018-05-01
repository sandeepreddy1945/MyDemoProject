/**
 * 
 */
package com.app.chart.fx.tree;

import com.app.chart.model.EmployeeDetails;
import com.app.chart.model.PseudoNode;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Sandeep
 *
 */
@Getter
@Setter
@ToString(includeFieldNames = true)
public class TreeViewHelper<T> {
	private EmployeeDetails employeeDetails;
	private PseudoNode pseudoNode;

	/**
	 * 
	 * @param details
	 */
	public TreeViewHelper(EmployeeDetails details) {
		this.employeeDetails = details;
	}

	/**
	 * 
	 * @param node
	 */
	public TreeViewHelper(PseudoNode node) {
		this.pseudoNode = node;
	}

}
