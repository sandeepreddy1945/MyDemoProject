package com.app.chart.fx;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import com.app.chart.model.EmployeeDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class AddressBook extends Application {

	private TableView<Person> table = new TableView<Person>();
	private final ObservableList<Person> data = FXCollections
			.observableArrayList(/*
									 * new Person("1", "Jacob", "Smith", "jacob.smith@example.com", "s"), new
									 * Person("1", "Isabella", "Johnson", "isabella.johnson@example.com", "s"), new
									 * Person("1", "Ethan", "Williams", "ethan.williams@example.com", "s"), new
									 * Person("1", "Emma", "Jones", "emma.jones@example.com", "s"), new Person("1",
									 * "Michael", "Brown", "michael.brown@example.com", "s")
									 */);
	final HBox hb = new HBox();
	final HBox headerHb = new HBox();
	JFXComboBox<String> teamCombo;
	Properties props = new Properties();
	ObjectMapper objectMapper = new ObjectMapper();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws IOException {
		// create the files for fetching before start.
		FilesUtil.initializeFileSettings();
		initUI(stage);
	}

	private void initUI(Stage stage) throws IOException {
		Scene scene = new Scene(new Group());
		stage.setTitle("Add Members Here");
		stage.setWidth(1600);
		stage.setHeight(900);

		final Label label = new Label("Chart Address Book");
		label.setFont(new Font("Arial", 20));

		teamCombo = new JFXComboBox<>();
		teamCombo.setMinSize(300, 20);
		teamCombo.setPromptText("Select Manager/Manager's Team");

		// initalize the Team Combo Box from Main Props File
		props = FilesUtil.readMainPropertiesFile();
		teamCombo.getItems()
				.addAll(FXCollections.observableArrayList(props.keySet().toArray(new String[props.size()])));

		final JFXTextField addManagerField = new JFXTextField();
		addManagerField.setPromptText("Create/Add New Chart here");
		addManagerField.setMinSize(450, 20);

		JFXButton addChart = new JFXButton("Add Chart To List");
		headerHb.setMinWidth(1000);
		headerHb.setSpacing(10);
		headerHb.getChildren().addAll(teamCombo, addManagerField, addChart);

		table.setEditable(true);
		Callback<TableColumn, TableCell> cellFactory = new Callback<TableColumn, TableCell>() {
			public TableCell call(TableColumn p) {
				return new EditingCell();
			}
		};

		table.setPrefWidth(stage.getWidth() - 50);
		table.setPrefHeight(stage.getHeight() - 200);

		TableColumn portalCol = new TableColumn("Portal Id");
		portalCol.setMinWidth(230);
		portalCol.setCellValueFactory(new PropertyValueFactory<Person, String>("portal"));
		portalCol.setCellFactory(cellFactory);
		portalCol.setOnEditCommit(new EventHandler<CellEditEvent<Person, String>>() {
			@Override
			public void handle(CellEditEvent<Person, String> t) {
				((Person) t.getTableView().getItems().get(t.getTablePosition().getRow())).setPortal(t.getNewValue());
			}
		});

		TableColumn name = new TableColumn("Name");
		name.setMinWidth(230);
		name.setCellValueFactory(new PropertyValueFactory<Person, String>("name"));
		name.setCellFactory(cellFactory);
		name.setOnEditCommit(new EventHandler<CellEditEvent<Person, String>>() {
			@Override
			public void handle(CellEditEvent<Person, String> t) {
				((Person) t.getTableView().getItems().get(t.getTablePosition().getRow())).setName(t.getNewValue());
			}
		});

		TableColumn designation = new TableColumn("Designation");
		designation.setMinWidth(230);
		designation.setCellValueFactory(new PropertyValueFactory<Person, String>("designation"));
		designation.setCellFactory(cellFactory);
		designation.setOnEditCommit(new EventHandler<CellEditEvent<Person, String>>() {
			@Override
			public void handle(CellEditEvent<Person, String> t) {
				((Person) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setDesignation(t.getNewValue());
			}
		});

		TableColumn teamName = new TableColumn("Team Name");
		teamName.setMinWidth(240);
		teamName.setCellValueFactory(new PropertyValueFactory<Person, String>("Team"));
		teamName.setCellFactory(cellFactory);
		teamName.setOnEditCommit(new EventHandler<CellEditEvent<Person, String>>() {
			@Override
			public void handle(CellEditEvent<Person, String> t) {
				((Person) t.getTableView().getItems().get(t.getTablePosition().getRow())).setTeam(t.getNewValue());
			}
		});

		TableColumn parentName = new TableColumn("Parent Member");
		parentName.setMinWidth(240);
		parentName.setCellValueFactory(new PropertyValueFactory<Person, String>("Parent"));
		parentName.setCellFactory(cellFactory);
		parentName.setOnEditCommit(new EventHandler<CellEditEvent<Person, String>>() {
			@Override
			public void handle(CellEditEvent<Person, String> t) {
				((Person) t.getTableView().getItems().get(t.getTablePosition().getRow())).setParent(t.getNewValue());
			}
		});

		table.setStyle(" -fx-alignment: CENTER-RIGHT;\r\n" + "     /* The rest is from caspian.css */\r\n" + "\r\n"
				+ "    -fx-padding: 0.166667em; /* 2px, plus border adds 1px */\r\n" + "\r\n"
				+ "    -fx-background-color: transparent;\r\n"
				+ "    -fx-border-color: transparent -fx-table-cell-border-color transparent transparent;\r\n"
				+ "    -fx-border-width: 0.083333em; /* 1 */\r\n" + "    -fx-cell-size: 2.0em; /* 24 */\r\n"
				+ "    -fx-text-fill: -fx-text-inner-color;");
		table.setItems(data);
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table.getColumns().addAll(portalCol, name, designation, teamName, parentName);

		final JFXTextField portalTF = new JFXTextField();
		portalTF.setPromptText("Portal Id");
		portalTF.setMaxWidth(name.getPrefWidth());
		final JFXTextField nameTF = new JFXTextField();
		nameTF.setPromptText("Name");
		nameTF.setMaxWidth(name.getPrefWidth());
		final JFXTextField designationTF = new JFXTextField();
		designationTF.setMaxWidth(designation.getPrefWidth());
		designationTF.setPromptText("Designation");
		final JFXTextField teamTF = new JFXTextField();
		teamTF.setMaxWidth(teamName.getPrefWidth());
		teamTF.setPromptText("Team");
		final JFXTextField parentTF = new JFXTextField();
		parentTF.setMaxWidth(teamName.getPrefWidth());
		parentTF.setPromptText("Parent");

		final JFXButton addButton = new JFXButton("Add");
		addButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (nameTF.getText().length() > 0 && designationTF.getText().length() > 0
						&& teamTF.getText().length() > 0 && parentTF.getText().length() > 0
						&& portalTF.getText().length() > 0) {
					data.add(new Person(portalTF.getText(), nameTF.getText(), designationTF.getText(), teamTF.getText(),
							parentTF.getText()));
					nameTF.clear();
					designationTF.clear();
					teamTF.clear();
					portalTF.clear();
					parentTF.clear();
				} else {
					JFXAlert<String> alert = new JFXAlert<>();
					alert.initModality(Modality.APPLICATION_MODAL);
					alert.setOverlayClose(false);
					JFXDialogLayout layout = new JFXDialogLayout();
					layout.setHeading(new Label("Missing Fields"));
					layout.setBody(new Label("Looks Like There are Some Missing Field \n"
							+ "Please Enter Values In all The Fields To Continue ..."));
					JFXButton closeButton = new JFXButton("OK");
					closeButton.getStyleClass().add("dialog-accept");
					closeButton.setOnAction(event -> alert.hideWithAnimation());
					layout.setStyle("-fx-background-color: white;\r\n" + "    -fx-background-radius: 5.0;\r\n"
							+ "    -fx-background-insets: 0.0 5.0 0.0 5.0;\r\n" + "    -fx-padding: 10;\r\n"
							+ "    -fx-hgap: 10;\r\n" + "    -fx-vgap: 10;" + " -fx-border-color: #2e8b57;\r\n"
							+ "    -fx-border-width: 2px;\r\n" + "    -fx-padding: 10;\r\n" + "    -fx-spacing: 8;");
					layout.setActions(closeButton);
					alert.setContent(layout);
					alert.show();
				}
			}
		});

		final JFXButton delButton = new JFXButton("Delete");
		delButton.setOnAction(e -> {
			data.remove(table.getSelectionModel().getFocusedIndex());
			table.fireEvent(e);
		});

		final JFXButton saveBtn = new JFXButton("Save");
		saveBtn.setOnAction(e -> {
			onSaveActionPerfomed(addManagerField);
		});

		addChart.setOnAction(e -> {
			onAddChartAction(addManagerField);
		});

		teamCombo.setOnAction(e -> {
			onTeamComboChanged(e);
		});
		nameTF.setPrefWidth(200);
		designationTF.setPrefWidth(200);
		teamTF.setPrefWidth(200);

		nameTF.setMinWidth(180);
		designationTF.setMinWidth(180);
		teamTF.setMinWidth(180);
		parentTF.setMinWidth(180);
		portalTF.setMinWidth(180);

		hb.getChildren().addAll(portalTF, nameTF, designationTF, teamTF, parentTF, addButton, delButton, saveBtn);
		hb.setSpacing(3);

		final VBox vbox = new VBox();
		vbox.setSpacing(5);
		vbox.setPadding(new Insets(10, 0, 0, 10));
		vbox.getChildren().addAll(label, headerHb, table, hb);

		((Group) scene.getRoot()).getChildren().addAll(vbox);

		stage.setScene(scene);
		stage.show();
	}

	private void onTeamComboChanged(ActionEvent e) {
		try {
			String jsonStr = FileUtils
					.readFileToString(
							new File(FilesUtil.MAIN_APP_PATH + FilesUtil.SLASH
									+ teamCombo.getSelectionModel().getSelectedItem() + FilesUtil.SLASH + "app.json"),
							Charset.defaultCharset());
			data.clear();
			if (jsonStr != null && jsonStr.length() > 0) {

				// List<MyClass> myObjects = mapper.readValue(jsonInput, new
				// TypeReference<List<MyClass>>(){});
				// List<MyClass> myObjects = mapper.readValue(jsonInput,
				// mapper.getTypeFactory().constructCollectionType(List.class, MyClass.class));
				EmployeeDetails[] empList = objectMapper.readValue(jsonStr, EmployeeDetails[].class);

				Arrays.stream(empList).forEach(emp -> {
					Person person = new Person(emp.getPortalId(), emp.getName(), emp.getDescription(), emp.getTeam(),
							emp.getParent());
					data.add(person);
					table.fireEvent(e);
				});
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void onAddChartAction(final JFXTextField addManagerField) {
		if (addManagerField.getText().length() < 1) {
			JFXAlert<String> alert = new JFXAlert<>();
			alert.initModality(Modality.APPLICATION_MODAL);
			alert.setOverlayClose(false);
			JFXDialogLayout layout = new JFXDialogLayout();
			layout.setHeading(new Label("Missing Fields"));
			layout.setBody(new Label(
					"Chart Name cannot be empty !! \n" + "Please Enter Values In all The Fields To Continue ..."));
			JFXButton closeButton = new JFXButton("OK");
			closeButton.getStyleClass().add("dialog-accept");
			closeButton.setOnAction(event -> alert.hideWithAnimation());
			layout.setStyle("-fx-background-color: white;\r\n" + "    -fx-background-radius: 5.0;\r\n"
					+ "    -fx-background-insets: 0.0 5.0 0.0 5.0;\r\n" + "    -fx-padding: 10;\r\n"
					+ "    -fx-hgap: 10;\r\n" + "    -fx-vgap: 10;" + " -fx-border-color: #2e8b57;\r\n"
					+ "    -fx-border-width: 2px;\r\n" + "    -fx-padding: 10;\r\n" + "    -fx-spacing: 8;");
			layout.setActions(closeButton);
			alert.setContent(layout);
			alert.show();
		} else {

			try {
				// add an entry to manager.properties file for loading.
				if (!new File(FilesUtil.MAIN_APP_PATH + FilesUtil.SLASH + addManagerField.getText()).exists()) {
					FileUtils.write(new File(FilesUtil.MANAGER_PROPS_PATH),
							addManagerField.getText() + "=" + addManagerField.getText() + "\n", true);
				}
				FilesUtil.checkAndCreateDir(FilesUtil.MAIN_APP_PATH + FilesUtil.SLASH + addManagerField.getText());
				FilesUtil.checkAndCreateFile(
						FilesUtil.MAIN_APP_PATH + FilesUtil.SLASH + addManagerField.getText() + FilesUtil.SLASH
								+ "app.js",
						FilesUtil.MAIN_APP_PATH + FilesUtil.SLASH + addManagerField.getText() + FilesUtil.SLASH
								+ "app.json",
						FilesUtil.MAIN_APP_PATH + FilesUtil.SLASH + addManagerField.getText() + FilesUtil.SLASH
								+ "index.html");

				FileUtils.copyToFile(getClass().getClassLoader().getResourceAsStream("com/app/chart/html/index.html"),
						new File(FilesUtil.MAIN_APP_PATH + FilesUtil.SLASH + addManagerField.getText() + FilesUtil.SLASH
								+ "index.html"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			teamCombo.getItems().add(addManagerField.getText());
			addManagerField.clear();
		}
	}

	private void onSaveActionPerfomed(final JFXTextField addManagerField) {
		// save action
		if (teamCombo.getSelectionModel().getSelectedIndex() != -1) {
			List<EmployeeDetails> empList = new ArrayList<>();
			data.stream().forEach(p -> {
				EmployeeDetails emp = new EmployeeDetails();
				emp.setName(p.getName());
				emp.setParent(p.getParent());
				emp.setPortalId(p.getPortal());
				emp.setTeam(p.getTeam());
				emp.setDescription(p.getDesignation());
				emp.setLink("http://localhost:8020/" + emp.getPortalId());
				empList.add(emp);
			});

			try {
				FileUtils.write(
						new File(FilesUtil.MAIN_APP_PATH + FilesUtil.SLASH
								+ teamCombo.getSelectionModel().getSelectedItem() + FilesUtil.SLASH + "app.json"),
						objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(empList),
						Charset.defaultCharset(), false);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	@Getter
	@Setter
	@AllArgsConstructor
	public static class Person {

		private String portal;
		private String name;
		private String designation;
		private String team;
		private String parent;
	}

	class EditingCell extends TableCell<Person, String> {

		private TextField textField;

		public EditingCell() {
		}

		@Override
		public void startEdit() {
			if (!isEmpty()) {
				super.startEdit();
				createTextField();
				setText(null);
				setGraphic(textField);
				textField.selectAll();
			}
		}

		@Override
		public void cancelEdit() {
			super.cancelEdit();

			setText((String) getItem());
			setGraphic(null);
		}

		@Override
		public void updateItem(String item, boolean empty) {
			super.updateItem(item, empty);

			if (empty) {
				setText(null);
				setGraphic(null);
			} else {
				if (isEditing()) {
					if (textField != null) {
						textField.setText(getString());
					}
					setText(null);
					setGraphic(textField);
				} else {
					setText(getString());
					setGraphic(null);
				}
			}
		}

		private void createTextField() {
			textField = new TextField(getString());
			textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
			textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
					if (!arg2) {
						commitEdit(textField.getText());
					}
				}
			});
		}

		private String getString() {
			return getItem() == null ? "" : getItem().toString();
		}
	}
}