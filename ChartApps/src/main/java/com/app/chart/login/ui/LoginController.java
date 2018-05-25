package com.app.chart.login.ui;

import java.net.URL;
import java.util.ResourceBundle;

import com.app.chart.fx.AddressBook;
import com.app.run.main.AppMain;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

public class LoginController implements Initializable {

	@FXML
	private JFXTextField username;
	@FXML
	private JFXPasswordField password;

	@Override
	public void initialize(URL url, ResourceBundle rb) {

	}

	@FXML
	private void handleLoginButtonAction(ActionEvent event) {
		String uname = username.getText();
		String pword = password.getText();

		if (uname.equalsIgnoreCase("admin") && pword.equalsIgnoreCase("manager")) {
			closeStage();
			// loadMain();
		} else {
			username.getStyleClass().add("wrong-credentials");
			password.getStyleClass().add("wrong-credentials");
		}
	}

	@FXML
	private void handleCancelButtonAction(ActionEvent event) {
		System.exit(0);
	}

	private void closeStage() {
		((Stage) username.getScene().getWindow()).close();
		// TODO to call the UI to call main class.
	}

	/*
	 * void loadMain() { try { Parent parent =
	 * FXMLLoader.load(getClass().getResource("/library/assistant/ui/main/main.fxml"
	 * )); Stage stage = new Stage(StageStyle.DECORATED);
	 * stage.setTitle("Login Page"); stage.setScene(new Scene(parent));
	 * stage.show(); } catch (IOException ex) { ex.printStackTrace(); } }
	 */

}
