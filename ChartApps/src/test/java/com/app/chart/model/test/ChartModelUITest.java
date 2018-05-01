/**
 * 
 */
package com.app.chart.model.test;

import java.util.Random;

import com.app.chart.fx.tree.OrgTreeView;
import com.app.chart.model.EmployeeDetails;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;

/**
 * @author Sandeep
 *
 */
public class ChartModelUITest extends Application {

	ObservableList<EmployeeDetails> emplList = FXCollections.observableArrayList();
	Random random = new Random();

	/*
	 * (non-Javadoc)
	 * 
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		for (int i = 0; i < 10; i++) {
			emplList.add(generateEmployeesValues());
		}
		EmployeeDetails emp = generateEmployeesValues();
		emp.setParent("1");
		emplList.add(emp);
		// OrgTreeView<EmployeeDetails> orgTreeView = new OrgTreeView<>(emplList,
		// primaryStage);

		Dialog dialog = new Dialog<>();
		OrgTreeView<EmployeeDetails> orgTreeView = new OrgTreeView<>(emplList, dialog, null);
		orgTreeView.show();
	}

	private EmployeeDetails generateEmployeesValues() {
		EmployeeDetails details = new EmployeeDetails();
		details.setDescription(randomString());
		details.setLink(randomString());
		details.setName(randomString());
		details.setParent(randomString());
		details.setPortalId(randomString());
		details.setTeam(randomString());
		details.setPseudo(false);
		return details;

	}

	private String randomString() {
		return String.valueOf("asdfg" + random.nextInt(99999));
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);

	}

}
