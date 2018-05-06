/**
 * 
 */
package com.app.chart.fx;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

import com.app.chart.model.EmployeeDetails;

/**
 * @author Sandeep
 *
 */
public class BuildJavaScript {

	public static final String HEADER_MEMBER = "1";
	private static final String COMMA = ",";
	private List<EmployeeDetails> employeeDetails;
	private String jsTemplate = "%s = %s";
	public static final String jsConfigTemplate = "%s = [" + " %s " + " ]" + ";";
	private Random random = new Random();

	/**
	 * @param employeeDetails
	 */
	public BuildJavaScript(List<EmployeeDetails> employeeDetails) {
		this.employeeDetails = employeeDetails;
	}

	public String buildJsString() throws IOException {

		File file = File.createTempFile("member", "json");
		FileUtils.copyInputStreamToFile(getClass().getClassLoader().getResourceAsStream("member.json"), file);
		String fileStr = FileUtils.readFileToString(file, Charset.defaultCharset());
		/*
		 * System.out.println(String.format(fileStr, randomString(), randomString(),
		 * randomString(), randomString(), randomString(), randomString()));
		 */
		file.delete();
		return null;

	}

	public String buildMemberJsString(EmployeeDetails emp) throws IOException {
		File file = File.createTempFile("member", "json");
		FileUtils.copyInputStreamToFile(getClass().getClassLoader().getResourceAsStream("member.json"), file);
		String fileStr = FileUtils.readFileToString(file, Charset.defaultCharset());
		String memberJsonStr = String.format(fileStr, emp.getParent(), "light-gray", emp.getName(),
				emp.getDescription(), emp.getLink(), emp.getPortalId() + ".png");
		file.delete();
		return String.format(jsTemplate, emp.getName() + "_" + emp.getPortalId(), memberJsonStr);
	}

	public String buildHeadMemberJsString() throws IOException {
		File file = File.createTempFile("headMember", "json");
		FileUtils.copyInputStreamToFile(getClass().getClassLoader().getResourceAsStream("headMember.json"), file);
		String fileStr = FileUtils.readFileToString(file, Charset.defaultCharset());
		EmployeeDetails headerEmployee = employeeDetails.stream().filter(e -> e.getParent().equals(HEADER_MEMBER))
				.findFirst().get();
		String headerMemberStr = String.format(fileStr, "light-gray", headerEmployee.getName(),
				headerEmployee.getDescription(), headerEmployee.getLink(), headerEmployee.getPortalId() + ".png");
		// header Employee Template with Values filled.
		headerMemberStr = String.format(jsTemplate, headerEmployee.getName() + "_" + headerEmployee.getPortalId(),
				headerMemberStr);
		file.delete();
		return headerMemberStr;
	}

	public String buildHeadMemberJsString(EmployeeDetails headerEmployee) throws IOException {
		File file = File.createTempFile("headMember", "json");
		FileUtils.copyInputStreamToFile(getClass().getClassLoader().getResourceAsStream("headMember.json"), file);
		String fileStr = FileUtils.readFileToString(file, Charset.defaultCharset());
		String headerMemberStr = String.format(fileStr, "light-gray", headerEmployee.getName(),
				headerEmployee.getDescription(), headerEmployee.getLink(), headerEmployee.getPortalId() + ".png");
		// header Employee Template with Values filled.
		headerMemberStr = String.format(jsTemplate, headerEmployee.getName() + "_" + headerEmployee.getPortalId(),
				headerMemberStr);
		file.delete();
		return headerMemberStr;
	}

	public String buildMemberJsString(EmployeeDetails emp, int pos) throws IOException {
		File file = File.createTempFile("postHeadMember", "json");
		FileUtils.copyInputStreamToFile(getClass().getClassLoader().getResourceAsStream("postHeadMember.json"), file);
		String fileStr = FileUtils.readFileToString(file, Charset.defaultCharset());
		String memberJsonStr = String.format(fileStr, emp.getParent(), String.valueOf(pos), "light-gray", emp.getName(),
				emp.getDescription(), emp.getLink(), emp.getPortalId() + ".png");
		file.delete();
		return String.format(jsTemplate, emp.getName() + "_" + emp.getPortalId(), memberJsonStr);
	}

	public String buildChartConfigJsString() throws IOException {
		File file = File.createTempFile("chartConfig", "json");
		FileUtils.copyInputStreamToFile(getClass().getClassLoader().getResourceAsStream("chartConfig.json"), file);
		String chartJsStr = FileUtils.readFileToString(file, Charset.defaultCharset());
		return String.format(jsTemplate, "var config", chartJsStr);

	}

	public String buildPseudoJsString(EmployeeDetails parent) throws IOException {
		File file = File.createTempFile("psuedoNode", "json");
		FileUtils.copyInputStreamToFile(getClass().getClassLoader().getResourceAsStream("psuedoNode.json"), file);
		String pseudoNodeStr = FileUtils.readFileToString(file, Charset.defaultCharset());
		return String.format(jsTemplate, pseudoNodeStr, parent.getName() + "_" + parent.getPortalId());
	}

	public String buildPseudoJsString(EmployeeDetails parent, int step) throws IOException {
		File file = File.createTempFile("psuedoNodeExtended", "json");
		FileUtils.copyInputStreamToFile(getClass().getClassLoader().getResourceAsStream("psuedoNodeExtended.json"),
				file);
		String pseudoNodeStr = FileUtils.readFileToString(file, Charset.defaultCharset());
		return String.format(jsTemplate, pseudoNodeStr, parent.getName() + "_" + parent.getPortalId(), step);
	}

	public String buildChartConfigJsStr() {
		StringBuilder sb = new StringBuilder();
		sb.append("config");
		appendComma(sb);
		// append header second now
		return jsTemplate;

	}

	private void appendComma(StringBuilder sb) {
		sb.append(COMMA);
	}

	/**
	 * 
	 * Procedure followed first filter the header guy -> Then check for his siblings
	 * -> Then Loop on the Siblings and Check for their's.
	 * 
	 * @throws IOException
	 */
	private void reArrangeMembers() throws IOException {
		List<EmployeeDetails> details = new ArrayList<>();
		int totalChartEmpCount = employeeDetails.size();
		int childrenDropLvl = 1;
		StringBuilder sb = new StringBuilder();
		StringBuilder configOrderBuider = new StringBuilder();// TODO at the end format it for getting = symbol
		// first add header member
		EmployeeDetails headerEmployee = employeeDetails.stream().filter(e -> e.getParent().equals(HEADER_MEMBER))
				.findFirst().get();
		// first of all append the config file .
		sb.append(buildChartConfigJsString());
		sb.append(COMMA);
		configOrderBuider.append("config");
		configOrderBuider.append(COMMA);

		// the next header
		sb.append(buildHeadMemberJsString(headerEmployee));
		sb.append(COMMA);
		configOrderBuider.append(headerEmployee.getPortalId());
		configOrderBuider.append(COMMA);

		// here comes the children.
		// check if all inherit the header parent.Single hierarcy chart.
		if (employeeDetails.size() == fetchNumberOfChildrenForMember(headerEmployee)) {
			// then try adding hierarchial root for parent only using pseudo

			// first all the children of the header except for the parent.
			List<EmployeeDetails> details2 = employeeDetails.stream().filter(e -> !e.getParent().equals(HEADER_MEMBER))
					.collect(Collectors.toList());
			int nodeCount = 0;
			String randomGeneratedStr = null;
			details2.stream().forEach(e -> {
				try {
					buildJsArrayForSingleHierarchy(sb, configOrderBuider, nodeCount, e, randomGeneratedStr);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			});
		} else {
			// the sibilings of parent has got children
		}

	}

	private void buildJsArrayForSingleHierarchy(StringBuilder sb, StringBuilder configOrderBuider, int nodeCount,
			EmployeeDetails e, String randomGeneratedStr) throws IOException {
		if (nodeCount % 3 == 0) {
			// means elements are present here from the list are added up with pseudo one
			// left and 2 under pseudo and one right element.

			sb.append(buildMemberJsString(e));
			sb.append(COMMA);
			configOrderBuider.append(e.getPortalId());
			configOrderBuider.append(COMMA);
			// make it null for identification purpose
			randomGeneratedStr = null;

		} else {
			// here comes the middle man pseudo and give him two people in the list to carry
			// away
			if (randomGeneratedStr == null) {
				randomGeneratedStr = String.valueOf(random.nextInt(999999));
				sb.append(COMMA);
				configOrderBuider.append(randomGeneratedStr);
				configOrderBuider.append(COMMA);
			}
		}

		nodeCount++;
	}

	/**
	 * Aim Here is to check if a node is having more than 3 children. If So help it
	 * to connect to parent node as a small hierarchy using pseudo . Detect the
	 * parent based on count and adjust it with pseudo where ever required .
	 * 
	 * @param emp
	 */
	private String reArrangeInternal(EmployeeDetails emp, StringBuilder sb, List<EmployeeDetails> empList) {

		List<EmployeeDetails> details = empList.stream().filter(e -> e.getParent().equals(emp.getPortalId()))
				.collect(Collectors.toList());
		if (details.size() > 3) {
			details.stream();
		}
		return null;

	}

	/**
	 * Fetches the number of children for the given member.
	 * 
	 * @param emp
	 * @return
	 */
	private int fetchNumberOfChildrenForMember(EmployeeDetails emp) {
		return employeeDetails.stream().filter(e -> e.getParent().equals(emp.getPortalId()))
				.collect(Collectors.toList()).size();
	}

	private String fetchHeaderEmployeePortalId() {
		return employeeDetails.stream().filter(e -> e.getParent().equals(HEADER_MEMBER)).findFirst().get()
				.getPortalId();
	}

	private EmployeeDetails fetchHeaderEmployee() {
		return employeeDetails.stream().filter(e -> e.getParent().equals(HEADER_MEMBER)).findFirst().get();
	}

	private List<EmployeeDetails> fetchHeaderEmployeeChildren() {
		return employeeDetails.stream().filter(e -> !e.getParent().equals(HEADER_MEMBER)).collect(Collectors.toList());
	}

	public void reArrangeListBasedOnChildNodes() {
		List<EmployeeDetails> clonedNode = Collections.emptyList();

		// add header employee
		clonedNode.add(fetchHeaderEmployee());
		// as already we have one element added to it
		int size = 1;

		List<EmployeeDetails> headerChildrenList = fetchHeaderEmployeeChildren();

		// now add header children and iterate through their children
		while (size != employeeDetails.size()) {

			size++;
		}
	}
}
