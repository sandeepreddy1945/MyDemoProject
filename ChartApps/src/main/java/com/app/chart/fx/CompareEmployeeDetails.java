/**
 * 
 */
package com.app.chart.fx;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.app.chart.model.EmployeeDetails;

/**
 * @author Sandeep
 * @param <T>
 *
 */
public class CompareEmployeeDetails<EmployeeSortModel> implements Comparator<EmployeeSortModel> {

	public static final String HEADER_MEMBER = "1";
	private List<EmployeeDetails> empList;
	private List<EmployeeSortModel> empSortList;

	@Override
	public int compare(EmployeeSortModel o1, EmployeeSortModel o2) {

		return 0;
	}

	/**
	 * @param empList
	 */
	public CompareEmployeeDetails(List<EmployeeDetails> empList) {
		this.empList = empList;
		this.empSortList = Collections.emptyList();
	}

	public void initialize() {

		int count = 0;
		int size = empList.size();
		while (count != size) {

		}
	}

	private int fetchNumberOfChildrenForMember(EmployeeDetails emp) {
		return empList.stream().filter(e -> e.getParent().equals(emp.getPortalId())).collect(Collectors.toList())
				.size();
	}

	private String fetchHeaderEmployeePortalId() {
		return empList.stream().filter(e -> e.getParent().equals(HEADER_MEMBER)).findFirst().get().getPortalId();
	}

	private EmployeeDetails fetchHeaderEmployee() {
		return empList.stream().filter(e -> e.getParent().equals(HEADER_MEMBER)).findFirst().get();
	}

	private List<EmployeeDetails> fetchHeaderEmployeeChildren() {
		return empList.stream().filter(e -> !e.getParent().equals(HEADER_MEMBER)).collect(Collectors.toList());
	}

	private List<EmployeeDetails> fetchOnlyHeaderEmployeeChildren() {
		return empList.stream().filter(e -> e.getParent().equals(fetchHeaderEmployeePortalId()))
				.collect(Collectors.toList());
	}

	private List<EmployeeDetails> fetchChildrenForMember(EmployeeDetails emp) {
		return empList.stream().filter(e -> e.getParent().equals(emp.getPortalId())).collect(Collectors.toList());
	}

	private void buildEmpSortModel(EmployeeDetails empD, boolean isHeaderEmp, boolean hasChildren,
			boolean childOfSomeNode, boolean isHeaderNodeChild, List<EmployeeDetails> childrenList) {

	}

}
