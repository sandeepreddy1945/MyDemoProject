/**
 * 
 */
package com.app.chart.dashboard.ui;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.slf4j.Marker;

import com.app.chart.fx.FilesUtil;
import com.app.chart.model.CurrentSprintBoundary;
import com.app.chart.model.ManagerDetailBoundary;
import com.app.chart.model.PerfomanceBoardBoundary;
import com.app.chart.model.PerfomanceMeterBoundary;
import com.app.chart.model.TeamMember;
import com.app.chart.perfomance.dashboard.DashboardUtil;
import com.app.chart.perfomance.dashboard.ui.DashboardUI;
import com.app.charts.utilities.JFXNumberField;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.cells.editors.TextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.base.GenericEditableTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellEditEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Screen;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Sandeep
 *
 */
@Slf4j
public class PerfomanceBoardDetails extends HBox {

	/**
	 * Visual Bounds of the Screen.
	 */
	public static Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
	public static double MIN_X = visualBounds.getMinX();
	public static double MIN_Y = visualBounds.getMinY();
	public static double MAX_X = visualBounds.getMaxX();
	public static double MAX_Y = visualBounds.getMaxY();
	public static double WIDTH = visualBounds.getWidth() - 100;
	public static double HEIGHT = visualBounds.getHeight() - 100;

	private VBox mainBox = new VBox(10);
	private JFXComboBox<ManagerDetailBoundary> managerCBX = new JFXComboBox<>();

	private List<PerfomanceBoardBoundary> perfomanceBoardDetails = null;
	private JFXTextField boardHeader;

	private ObservableList<TeamMemberTableBoundary> members = FXCollections
			.observableArrayList(/* constructTestBoundary() */);
	// build tree
	final TreeItem<TeamMemberTableBoundary> root = new RecursiveTreeItem<>(members, RecursiveTreeObject::getChildren);

	JFXTreeTableView<TeamMemberTableBoundary> tableView = new JFXTreeTableView<>(root);
	private JFXDatePicker datePickerFX;

	private ObjectMapper mapper = new ObjectMapper();
	private String jsonDataContent;
	private final File dataFile;

	/**
	 * 
	 */
	public PerfomanceBoardDetails(File dataFile) {
		super(5);
		this.dataFile = dataFile;
		// before init of ui just read the file and save the data for usage.
		readDataFromFile();

		initUI();
	}

	public PerfomanceBoardDetails() {
		super(5);
		this.dataFile = new File(FilesUtil.DASHBOARD_CONTENT_DATA);
		// before init of ui just read the file and save the data for usage.
		readDataFromFile();

		initUI();
	}

	private void readDataFromFile() {
		try {
			if (dataFile != null && dataFile.exists()
					&& FileUtils.readFileToString(dataFile, Charset.defaultCharset()).length() > 0) {
				String fileData = FileUtils.readFileToString(dataFile, Charset.defaultCharset());
				System.out.println(fileData);
				mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);

				List<PerfomanceBoardBoundary> dataList = mapper.readValue(fileData,
						mapper.getTypeFactory().constructCollectionType(List.class, PerfomanceBoardBoundary.class));
				dataList.forEach(System.out::println);

				// set the list with details
				this.jsonDataContent = fileData;
				this.perfomanceBoardDetails = dataList;

			} else {
				// instntiate the perfomance list as not data is present.
				perfomanceBoardDetails = new ArrayList<>();
			}
		} catch (IOException e) {

			log.error(Marker.ANY_MARKER, "readDataFromFile", e);
		}

	}

	private void initUI() {

		// construct the table first.
		tableView.setMinSize(WIDTH, HEIGHT - 550);
		tableView.setPrefSize(WIDTH, HEIGHT - 450);
		tableView.setShowRoot(false);
		tableView.setEditable(true);
		tableView.setPadding(new Insets(15));
		mainBox.getChildren().addAll(constructTopBox(), constructMiddleBox(), constructTable(), tableView,
				constructBottomBox());

		// check and load the data from the file if exists
		checkAndLoadData();

		// listen to the changes on the combobox to change around the data.
		managerCBX.setOnAction(this::listenToComboBoxChanges);
		managerCBX.setLabelFloat(true);

		getChildren().add(mainBox);

	}

	private void checkAndLoadData() {
		if (perfomanceBoardDetails.size() > 0) {
			// just add the details to the manager and this listen for data on the combo
			// box.
			perfomanceBoardDetails.stream().forEach(p -> {
				managerCBX.getItems().add(p.getManagerDetailBoundary());
			});
		} else {
			// for now no implementation
			log.info("Intial Start of creating data.");
		}

	}

	private void listenToComboBoxChanges(ActionEvent e) {
		if (managerCBX.getSelectionModel().getSelectedItem() == null) {
			// then do nothing
		} else {
			// make a check on the folder name as it is the only unique name available.
			PerfomanceBoardBoundary p = perfomanceBoardDetails.stream().filter(
					b -> b.getFolderName().equals(managerCBX.getSelectionModel().getSelectedItem().getFolderName()))
					.findFirst().get();

			boardHeader.setText(p.getHeaderTxt());

			// clear all the members available and then add
			members.clear();

			// add all the members to the table here.
			members.addAll(constructTableMemberBoundary(p.getTeamMembers()));
			// fire teh table with new data available.
			tableView.fireEvent(e);
		}
	}

	private HBox constructTopBox() {
		HBox box = new HBox(20);
		boardHeader = new JFXTextField();
		boardHeader.setPromptText("Enter the Team Name");
		boardHeader.setMinSize(300, 10);
		managerCBX.setMinSize(300, 15);
		managerCBX.setPromptText("Dashboard List Available");
		DashboardUtil.buildRequestValidator(boardHeader);
		boardHeader.setTooltip(new Tooltip("Press \"alt+c\" to clear this field"));
		boardHeader.setOnKeyPressed(b -> {
			if (b.getCode().ordinal() == KeyCode.C.ordinal() && b.isAltDown()) {
				boardHeader.clear();
			}
		});

		datePickerFX = new JFXDatePicker();
		datePickerFX.setPromptText("Time Intreval");
		datePickerFX.setMinSize(250, 15);

		JFXButton addManager = new JFXButton("Add Manager");
		addManager.setOnAction(e -> {
			// TODO to add changes to files accordingly.
			displayAddManagerDialog(boardHeader.getText());
		});

		addManager.setOnKeyPressed(e -> {
			if (e.getCode().ordinal() == KeyCode.ENTER.ordinal()) {
				addManager.fire();
			}
		});

		JFXButton addSunburstChart = new JFXButton("Add Release Graph");
		addSunburstChart.setOnAction(e -> {
			if (managerCBX.getSelectionModel().getSelectedItem() == null) {
				popOutAlert("Not a Valid Team Name Selected To Add The Graph . \n "
						+ "Select a Valid Team Name and then proceed.", "Perfomance Chart");
			} else {
				PerfomanceBoardBoundary pbb = perfomanceBoardDetails.parallelStream().filter(
						p -> p.getFolderName().equals(managerCBX.getSelectionModel().getSelectedItem().getFolderName()))
						.findFirst().get();
				Dialog sunBurstEditor = new Dialog<>();
				sunBurstEditor.setResizable(true);
				ReleaseBoardDetails boardDetails = new ReleaseBoardDetails(pbb.getFolderName(), pbb, sunBurstEditor);

				sunBurstEditor.show();
			}
		});

		addSunburstChart.setOnKeyPressed(e -> {
			if (e.getCode().ordinal() == KeyCode.ENTER.ordinal()) {
				addManager.fire();
			}
		});

		JFXButton teamPerfomanceMeter = new JFXButton("Team Perfomance Meter");
		teamPerfomanceMeter.setOnAction(e -> {
			if (managerCBX.getSelectionModel().getSelectedItem() == null) {
				popOutAlert("Not a Valid Team Name Selected To Add The Graph . \n "
						+ "Select a Valid Team Name and then proceed.", "Perfomance Chart");
			} else {
				displayAddPerfomanceMeter(managerCBX.getSelectionModel().getSelectedItem().getFolderName());
			}
		});

		teamPerfomanceMeter.setOnKeyPressed(e -> {
			if (e.getCode().ordinal() == KeyCode.ENTER.ordinal()) {
				addManager.fire();
			}
		});

		JFXButton currentSprintACBtn = new JFXButton("Add Current Sprint Details");
		currentSprintACBtn.setOnAction(e -> {
			if (managerCBX.getSelectionModel().getSelectedItem() == null) {
				popOutAlert("Not a Valid Team Name Selected To Add The Graph . \n "
						+ "Select a Valid Team Name and then proceed.", "Perfomance Chart");
			} else {
				displayAddSprintData(managerCBX.getSelectionModel().getSelectedItem().getFolderName());
			}
		});
		addManager.setAlignment(Pos.CENTER_RIGHT);

		box.getChildren().addAll(boardHeader, managerCBX, datePickerFX, addManager, addSunburstChart,
				teamPerfomanceMeter, currentSprintACBtn);

		box.setMinSize(WIDTH, 90);
		box.setPrefSize(WIDTH, 110);
		box.setPadding(new Insets(15));

		return box;
	}

	private void displayAddSprintData(String teamName) {

		JFXAlert<String> alert = new JFXAlert<>();
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.setOverlayClose(false);
		JFXDialogLayout layout = new JFXDialogLayout();
		layout.setHeading(new Text("Add Perfomance Meter Stats"));
		layout.setMinSize(500, 200);

		HBox box = new HBox(10);
		JFXNumberField totalPoints = new JFXNumberField();
		totalPoints.setPromptText("Total Sprint Points");
		JFXNumberField currentPoints = new JFXNumberField();
		currentPoints.setPromptText("Current Sprint Points");
		JFXNumberField backlogPoints = new JFXNumberField();
		backlogPoints.setPromptText("Backlog Sprint Points");

		// adding validator for all these fields
		DashboardUtil.buildRequestValidator(totalPoints, currentPoints, backlogPoints);
		box.getChildren().addAll(totalPoints, currentPoints, backlogPoints);
		layout.setBody(box);

		JFXButton okBtn = new JFXButton("OK");
		okBtn.getStyleClass().add("dialog-accept");

		okBtn.setOnAction(e -> onSprintDatAddOk(teamName, alert, totalPoints, currentPoints, backlogPoints));

		layout.setStyle("-fx-background-color: white;\r\n" + "    -fx-background-radius: 5.0;\r\n"
				+ "    -fx-background-insets: 0.0 5.0 0.0 5.0;\r\n" + "    -fx-padding: 10;\r\n"
				+ "    -fx-hgap: 10;\r\n" + "    -fx-vgap: 10;" + " -fx-border-color: #2e8b57;\r\n"
				+ "    -fx-border-width: 2px;\r\n" + "    -fx-padding: 10;\r\n" + "    -fx-spacing: 8;");
		JFXButton cancelBtn = new JFXButton("Cancel");
		cancelBtn.setOnAction(e -> {
			alert.hideWithAnimation();
		});
		// add enter button listener on the button
		okBtn.setOnKeyPressed(e -> {
			if (e.getCode().ordinal() == KeyCode.ENTER.ordinal()) {
				okBtn.fire();
			}
		});

		cancelBtn.setOnKeyPressed(e -> {
			if (e.getCode().ordinal() == KeyCode.ENTER.ordinal()) {
				okBtn.fire();
			}
		});
		layout.setActions(okBtn, cancelBtn);
		alert.setContent(layout);
		alert.show();

	}

	private void onSprintDatAddOk(String teamName, JFXAlert<String> alert, JFXNumberField totalPoints,
			JFXNumberField currentPoints, JFXNumberField backlogPoints) {

		if (DashboardUtil.validateTextField(totalPoints, currentPoints, backlogPoints) && teamName != null
				&& teamName.length() > 0) {
			CurrentSprintBoundary sprintBoundary = new CurrentSprintBoundary();
			sprintBoundary.setTotalSprintPoints(Double.valueOf(totalPoints.getText()));
			sprintBoundary.setCurrentSprintPoints(Double.valueOf(currentPoints.getText()));
			sprintBoundary.setBacklogSprintPoints(Double.valueOf(backlogPoints.getText()));

			Optional<PerfomanceBoardBoundary> details = perfomanceBoardDetails.parallelStream().filter(
					p -> p.getFolderName().equals(managerCBX.getSelectionModel().getSelectedItem().getFolderName()))
					.findFirst();
			if (details.isPresent()) {
				details.get().setCurrentSprintBoundary(sprintBoundary);
			}
			alert.hideWithAnimation();
		}

	}

	private void displayAddPerfomanceMeter(String teamName) {

		JFXAlert<String> alert = new JFXAlert<>();
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.setOverlayClose(false);
		JFXDialogLayout layout = new JFXDialogLayout();
		layout.setHeading(new Text("Add Perfomance Meter Stats"));
		layout.setMinSize(500, 200);

		HBox box = new HBox(10);
		JFXNumberField totalPoints = new JFXNumberField();
		totalPoints.setPromptText("Total Points");
		JFXNumberField currentPoints = new JFXNumberField();
		currentPoints.setPromptText("Current Points");
		JFXNumberField backlogPoints = new JFXNumberField();
		backlogPoints.setPromptText("Backlog Points");

		// adding validator for all these fields
		DashboardUtil.buildRequestValidator(totalPoints, currentPoints, backlogPoints);
		box.getChildren().addAll(totalPoints, currentPoints, backlogPoints);
		layout.setBody(box);

		JFXButton okBtn = new JFXButton("OK");
		okBtn.getStyleClass().add("dialog-accept");

		okBtn.setOnAction(e -> onPerfomanceMeterOkAction(teamName, alert, totalPoints, currentPoints, backlogPoints));

		layout.setStyle("-fx-background-color: white;\r\n" + "    -fx-background-radius: 5.0;\r\n"
				+ "    -fx-background-insets: 0.0 5.0 0.0 5.0;\r\n" + "    -fx-padding: 10;\r\n"
				+ "    -fx-hgap: 10;\r\n" + "    -fx-vgap: 10;" + " -fx-border-color: #2e8b57;\r\n"
				+ "    -fx-border-width: 2px;\r\n" + "    -fx-padding: 10;\r\n" + "    -fx-spacing: 8;");
		JFXButton cancelBtn = new JFXButton("Cancel");
		cancelBtn.setOnAction(e -> {
			alert.hideWithAnimation();
		});
		// add enter button listener on the button
		okBtn.setOnKeyPressed(e -> {
			if (e.getCode().ordinal() == KeyCode.ENTER.ordinal()) {
				okBtn.fire();
			}
		});

		cancelBtn.setOnKeyPressed(e -> {
			if (e.getCode().ordinal() == KeyCode.ENTER.ordinal()) {
				okBtn.fire();
			}
		});
		layout.setActions(okBtn, cancelBtn);
		alert.setContent(layout);
		alert.show();

	}

	private void onPerfomanceMeterOkAction(String teamName, JFXAlert<String> alert, JFXNumberField totalPoints,
			JFXNumberField currentPoints, JFXNumberField backlogPoints) {

		if (DashboardUtil.validateTextField(totalPoints, currentPoints, backlogPoints) && teamName != null
				&& teamName.length() > 0) {
			PerfomanceMeterBoundary meterBoundary = new PerfomanceMeterBoundary();
			meterBoundary.setTotalPoints(Double.valueOf(totalPoints.getText()));
			meterBoundary.setCurrentPoints(Double.valueOf(currentPoints.getText()));
			meterBoundary.setBacklogPoints(Double.valueOf(backlogPoints.getText()));

			Optional<PerfomanceBoardBoundary> details = perfomanceBoardDetails.parallelStream().filter(
					p -> p.getFolderName().equals(managerCBX.getSelectionModel().getSelectedItem().getFolderName()))
					.findFirst();
			if (details.isPresent()) {
				details.get().setPerfomanceMeterBoundary(meterBoundary);
			}
			alert.hideWithAnimation();
		}
	}

	private void displayAddManagerDialog(String teamName) {
		JFXAlert<String> alert = new JFXAlert<>();
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.setOverlayClose(false);
		JFXDialogLayout layout = new JFXDialogLayout();
		layout.setHeading(new Text("Add Manager Detail"));
		layout.setMinSize(500, 200);

		HBox box = new HBox(10);
		JFXNumberField portalId = new JFXNumberField();
		portalId.setPromptText("Enter Portal Id");
		JFXTextField managerName = new JFXTextField();
		managerName.setPromptText("Enter Name");
		JFXTextField designation = new JFXTextField();
		designation.setPromptText("Enter Designation");

		// adding validator for all these fields
		DashboardUtil.buildRequestValidator(portalId, managerName, designation);
		box.getChildren().addAll(portalId, managerName, designation);
		layout.setBody(box);

		JFXButton okBtn = new JFXButton("OK");
		okBtn.getStyleClass().add("dialog-accept");

		okBtn.setOnAction(e -> onDialogOkAction(teamName, alert, portalId, managerName, designation));

		layout.setStyle("-fx-background-color: white;\r\n" + "    -fx-background-radius: 5.0;\r\n"
				+ "    -fx-background-insets: 0.0 5.0 0.0 5.0;\r\n" + "    -fx-padding: 10;\r\n"
				+ "    -fx-hgap: 10;\r\n" + "    -fx-vgap: 10;" + " -fx-border-color: #2e8b57;\r\n"
				+ "    -fx-border-width: 2px;\r\n" + "    -fx-padding: 10;\r\n" + "    -fx-spacing: 8;");
		JFXButton cancelBtn = new JFXButton("Cancel");
		cancelBtn.setOnAction(e -> {
			alert.hideWithAnimation();
		});
		// add enter button listener on the button
		okBtn.setOnKeyPressed(e -> {
			if (e.getCode().ordinal() == KeyCode.ENTER.ordinal()) {
				okBtn.fire();
			}
		});

		cancelBtn.setOnKeyPressed(e -> {
			if (e.getCode().ordinal() == KeyCode.ENTER.ordinal()) {
				okBtn.fire();
			}
		});
		layout.setActions(okBtn, cancelBtn);
		alert.setContent(layout);
		alert.show();

	}

	private void onDialogOkAction(String teamName, JFXAlert<String> alert, JFXNumberField portalId,
			JFXTextField managerName, JFXTextField designation) {
		if (datePickerFX.getValue() == null) {
			popOutAlert("Please Select a Valid Date from the Calendar to Continue ..", "Perfomance Board");
			return;
		}

		if (DashboardUtil.validateTextField(portalId, managerName, designation) && teamName != null
				&& teamName.length() > 0) {
			// build manager object
			// for now the team folder name will the teamName given replaced without spaces.
			ManagerDetailBoundary detailBoundary = new ManagerDetailBoundary(Integer.valueOf(portalId.getText()),
					managerName.getText(), designation.getText(), teamName.replaceAll(" ", ""), new ArrayList<>());
			// main list checkng starts here.. check if the folder already exists.
			Optional<String> isFolderExists = perfomanceBoardDetails.stream()
					.map(PerfomanceBoardBoundary::getFolderName).filter(f -> f.equals(teamName.replaceAll(" ", "")))
					.findFirst();

			if (isFolderExists.isPresent()) {
				popOutAlert("The Perfomance Board You are trying to Add already Exists. \n "
						+ "Please select the Option from Team List To Continue..", "Perofomance Board");
			} else {
				// add to main list as it is a new entry.
				PerfomanceBoardBoundary perfomanceBoardBoundary = new PerfomanceBoardBoundary();
				perfomanceBoardBoundary.setFolderName(teamName.replaceAll(" ", ""));
				perfomanceBoardBoundary.setHeaderTxt(teamName);
				perfomanceBoardBoundary.setManagerDetailBoundary(detailBoundary);
				perfomanceBoardBoundary
						.setInitalDate(new java.util.Date(Date.valueOf(datePickerFX.getValue()).getTime()));
				perfomanceBoardBoundary.setTeamMembers(new ArrayList<>());
				perfomanceBoardDetails.add(perfomanceBoardBoundary);
				managerCBX.getItems().add(detailBoundary);
				// dont clear it ..need to think on this hndling TODO
				// datePickerFX.getEditor().clear();
				// hide window on successful call
				alert.hideWithAnimation();
			}
		}

		if (teamName.length() == 0) {
			popOutAlert("Team Name is Required Before Adding a Manager. \n"
					+ "Please Add the Team Name first to continue..", "Missing fields");
		}
	}

	private void popOutAlert(String text, String title) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle(title);
		alert.setContentText(text);
		alert.showAndWait();
	}

	private Alert popOutAlert(String text, String title, AlertType alertType) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setContentText(text);
		alert.showAndWait();

		return alert;
	}

	private VBox constructMiddleBox() {
		VBox middleBox = new VBox(25);

		JFXNumberField portalID = new JFXNumberField();
		portalID.setPromptText("Enter Portal Id");
		JFXTextField name = new JFXTextField();
		name.setPromptText("Enter Name");
		JFXTextField designation = new JFXTextField();
		designation.setPromptText("Enter Designation");

		JFXNumberField month1Score = new JFXNumberField();
		month1Score.setPromptText("Enter 1st Month Points");
		JFXNumberField month2Score = new JFXNumberField();
		month2Score.setPromptText("Enter 2nd Month Points");
		JFXNumberField month3Score = new JFXNumberField();
		month3Score.setPromptText("Enter 3rd Month Points");

		JFXNumberField valueAddTF = new JFXNumberField();
		valueAddTF.setPromptText("Enter Value Add Points");
		JFXNumberField qualityTF = new JFXNumberField();
		qualityTF.setPromptText("Enter Quality Points");
		JFXNumberField onTimeTF = new JFXNumberField();
		onTimeTF.setPromptText("Enter On Time Points");

		JFXTextArea descriptionTA = new JFXTextArea();
		descriptionTA.setLabelFloat(true);
		descriptionTA.setPromptText("Enter Description About The Employee !!");

		// validate all the fields
		DashboardUtil.buildRequestValidator(portalID, name, designation, month1Score, month2Score, month3Score,
				valueAddTF, qualityTF, onTimeTF);

		setBoundsForTxtFields(portalID, name, designation, month1Score, month2Score, month3Score, valueAddTF, qualityTF,
				onTimeTF);

		DashboardUtil.buildRequestValidator(descriptionTA);

		JFXButton saveMember = new JFXButton("Add Member");
		saveMember.setOnAction(e -> addTeamMemberDataToTable(portalID, name, designation, month1Score, month2Score,
				month3Score, valueAddTF, qualityTF, onTimeTF, e));

		// set some right padding to align it little bit near the box.
		saveMember.setPadding(new Insets(0, 45, 0, 0));

		HBox box1 = new HBox(10);
		box1.getChildren().addAll(portalID, name, designation);

		HBox box2 = new HBox(10);
		box2.getChildren().addAll(month1Score, month2Score, month3Score);

		HBox box3 = new HBox(10);
		box3.getChildren().addAll(valueAddTF, qualityTF, onTimeTF);

		// for now this is not used , as we will not be requiring it.
		// So Skipped adding it to the layout
		HBox box4 = new HBox(10);
		box4.getChildren().add(descriptionTA);

		HBox box5 = new HBox(10);
		box5.setAlignment(Pos.BOTTOM_RIGHT);
		box5.getChildren().add(saveMember);

		Text lblText = new Text("Add Member Details Here");
		lblText.setFont(Font.font("Roboto", FontWeight.BOLD, 20));
		lblText.setUnderline(true);
		lblText.setTextAlignment(TextAlignment.LEFT);

		middleBox.getChildren().addAll(lblText, box1, box2, box3, box5);

		middleBox.setMinSize(WIDTH, 250);
		middleBox.setPrefSize(WIDTH, 250);
		middleBox.setPadding(new Insets(15));

		return middleBox;

	}

	private void addTeamMemberDataToTable(JFXNumberField portalID, JFXTextField name, JFXTextField designation,
			JFXNumberField month1Score, JFXNumberField month2Score, JFXNumberField month3Score,
			JFXNumberField valueAddTF, JFXNumberField qualityTF, JFXNumberField onTimeTF, ActionEvent e) {
		if (managerCBX.getSelectionModel().getSelectedItem() == null) {
			popOutAlert("Please select a Valid Dashboard Name from the Dashboard List Box to Continue...",
					"Select Team Name");
			return;
		}
		if (DashboardUtil.validateTextField(portalID, name, designation, month1Score, month2Score, month3Score,
				valueAddTF, qualityTF, onTimeTF) && datePickerFX.getValue() != null) {

			Optional<StringProperty> checkOptional = members.stream().map(TeamMemberTableBoundary::getPortalId)
					.filter(f -> f.get().equals(portalID.getText())).findFirst();

			if (checkOptional.isPresent()) {
				popOutAlert("The Member you are trying to add Already Exits in the List. \n "
						+ "Please Add a new employee .\n" + "Tip: If Required use the search filter on Table to \n "
						+ "search for already added Employee", "Member Alread Added !!");
			} else {

				int currentMonthDetails = datePickerFX.getValue().getMonthValue();
				TeamMember member = new TeamMember();
				member.setPortalId(portalID.getText());
				member.setName(name.getText());
				member.setDescription(designation.getText());
				member.setScore1(Integer.valueOf(month1Score.getText()));
				member.setScore2(Integer.valueOf(month2Score.getText()));
				member.setScore3(Integer.valueOf(month3Score.getText()));
				member.setQuality(Integer.valueOf(qualityTF.getText()));
				member.setValueAdd(Integer.valueOf(valueAddTF.getText()));
				member.setOnTime(Integer.valueOf(onTimeTF.getText()));
				member.setLink("http://localhost:8020/" + portalID.getText());
				// adding 3 months data here - For now Date is rules out.
				member.setIntreval1(
						datePickerFX.getValue().getMonth().name() + " - " + datePickerFX.getValue().getYear());
				member.setIntreval2(
						Month.of(currentMonthDetails + 1).name() + " - " + datePickerFX.getValue().getYear());
				member.setIntreval3(
						Month.of(currentMonthDetails + 2).name() + " - " + datePickerFX.getValue().getYear());

				// add it to the table view List
				members.add(constructTableMemberBoundary(member));

				// not to do this here .. would be wrong TODO to check this later.
				/*
				 * if (managerCBX.getSelectionModel().getSelectedItem().getTeamMembers() ==
				 * null) managerCBX.getSelectionModel().getSelectedItem().setTeamMembers(new
				 * ArrayList<>());
				 * managerCBX.getSelectionModel().getSelectedItem().getTeamMembers().add(member)
				 * ;
				 */

				// clear all the text fields once the data is added to the table .
				DashboardUtil.clearTextField(portalID, name, designation, month1Score, month2Score, month3Score,
						valueAddTF, qualityTF, onTimeTF);
				// fire table view change.
				tableView.fireEvent(e);
			}
		}
	}

	private void setBoundsForTxtFields(JFXTextField... fields) {
		Arrays.asList(fields).stream().forEach(f -> {
			((JFXTextField) f).setMinSize(400, 10);
		});
	}

	@SuppressWarnings("unlikely-arg-type")
	private HBox constructBottomBox() {
		HBox box = new HBox(10);
		box.setAlignment(Pos.BOTTOM_CENTER);

		HBox container = new HBox(10);
		JFXButton saveBtn = new JFXButton("Save");
		JFXButton delBtn = new JFXButton("Delete");
		JFXButton previewBtn = new JFXButton("Preview");
		// display manager and board details after reload
		JFXButton displayDetails = new JFXButton("Overview Board Details");

		JFXButton editManager = new JFXButton("Edit Manager/Header Details");
		editManager.setOnAction(this::editManagerHeaderDetails);

		saveBtn.setOnAction(this::saveAllTheDetails);

		delBtn.setOnAction(e -> {
			if (tableView.getSelectionModel().getSelectedItem() == null) {
				popOutAlert(
						"No a Valid Table Row Selected to Delete ." + "Pleae Select a Row from the Table to delete.",
						"Performance Board");
			} else {
				members.remove(tableView.getSelectionModel().getSelectedIndex());
				tableView.fireEvent(e);
			}
		});

		previewBtn.setOnAction(e -> {
			Dialog dashboardUIPreview = new Dialog<>();
			dashboardUIPreview.setResizable(true);

			if (managerCBX.getSelectionModel().getSelectedItem() == null) {
				popOutAlert("Not a Valid Team Selected for Preview .Please select a Valid Team To Display",
						"Perfomance Charts");
				return;
			}

			Optional<PerfomanceBoardBoundary> details = perfomanceBoardDetails.parallelStream().filter(
					p -> p.getFolderName().equals(managerCBX.getSelectionModel().getSelectedItem().getFolderName()))
					.findFirst();
			if (details.isPresent()) {
				PerfomanceBoardBoundary p = details.get();
				try {
					DashboardUI dashboardUI = new DashboardUI(p.getTeamMembers(), p.getHeaderTxt(),
							p.getManagerDetailBoundary(), p.getSunburstBoundary(), p.getPerfomanceMeterBoundary(),
							p.getCurrentSprintBoundary(), dashboardUIPreview);
					// dashboardUIPreview.show();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else {
				popOutAlert("Not a Valid Team Selected for Preview .Please select a Valid Team To Display",
						"Perfomance Charts");
			}

		});

		displayDetails.setOnAction(this::displayOverViewDetails);

		container.getChildren().addAll(editManager, displayDetails, saveBtn, delBtn, previewBtn);

		// container.setAlignment(Pos.BOTTOM_RIGHT);

		box.getChildren().add(container);

		box.setMinSize(WIDTH, 100);
		box.setPrefSize(WIDTH, 100);
		box.setAlignment(Pos.BOTTOM_RIGHT);

		box.setPadding(new Insets(5, 30, 30, 0));
		return box;
	}

	private void editManagerHeaderDetails(ActionEvent ae) {

		if (managerCBX.getSelectionModel().getSelectedItem() == null) {
			popOutAlert("Please select a Manager to Edit the Details. \n"
					+ "After Selecting the manager continue to edit the details.", "Edit Manager");
			return;
		} else {

			JFXAlert<String> alert = new JFXAlert<>();
			alert.initModality(Modality.APPLICATION_MODAL);
			alert.setOverlayClose(false);
			JFXDialogLayout layout = new JFXDialogLayout();
			layout.setHeading(new Text("Edit Manager Detail"));
			layout.setMinSize(1000, 200);

			HBox box = new HBox(10);
			JFXNumberField portalId = new JFXNumberField();
			portalId.setPromptText("Enter Portal Id");
			JFXTextField managerName = new JFXTextField();
			managerName.setPromptText("Enter Name");
			managerName.setMinWidth(250);
			JFXTextField designation = new JFXTextField();
			designation.setPromptText("Enter Designation");
			designation.setMinWidth(200);
			JFXTextField headerTF = new JFXTextField();
			headerTF.setPromptText("Enter The New Header Name");
			headerTF.setMinWidth(300);

			// pre populate the data for this scenario as it is edit.
			ManagerDetailBoundary m = managerCBX.getSelectionModel().getSelectedItem();
			portalId.setText(String.valueOf(m.getPortalId()));
			managerName.setText(m.getName());
			designation.setText(m.getDesignation());
			// pre-populate the data from the board header text field
			headerTF.setText(boardHeader.getText());

			// adding validator for all these fields
			DashboardUtil.buildRequestValidator(portalId, managerName, designation, headerTF);
			box.getChildren().addAll(portalId, managerName, designation, headerTF);
			layout.setBody(box);

			JFXButton okBtn = new JFXButton("Save Changes");
			okBtn.getStyleClass().add("dialog-accept");

			okBtn.setOnAction(b -> onEditManagerSaveAction(boardHeader.getText(), alert, portalId, managerName,
					designation, headerTF));

			layout.setStyle("-fx-background-color: white;\r\n" + "    -fx-background-radius: 5.0;\r\n"
					+ "    -fx-background-insets: 0.0 5.0 0.0 5.0;\r\n" + "    -fx-padding: 10;\r\n"
					+ "    -fx-hgap: 10;\r\n" + "    -fx-vgap: 10;" + " -fx-border-color: #2e8b57;\r\n"
					+ "    -fx-border-width: 2px;\r\n" + "    -fx-padding: 10;\r\n" + "    -fx-spacing: 8;");
			JFXButton cancelBtn = new JFXButton("Cancel");
			cancelBtn.setOnAction(e -> {
				alert.hideWithAnimation();
			});
			// add enter button listener on the button
			okBtn.setOnKeyPressed(e -> {
				if (e.getCode().ordinal() == KeyCode.ENTER.ordinal()) {
					okBtn.fire();
				}
			});

			cancelBtn.setOnKeyPressed(e -> {
				if (e.getCode().ordinal() == KeyCode.ENTER.ordinal()) {
					okBtn.fire();
				}
			});

			JFXButton deleteManagerBtn = new JFXButton("Delete Manager");
			deleteManagerBtn.setOnAction(event -> deleteManagerAction(event, alert));

			layout.setActions(okBtn, deleteManagerBtn, cancelBtn);
			alert.setContent(layout);
			alert.show();

		}
	}

	/**
	 * Delete a manager / board details completely.
	 * 
	 * @param e
	 */
	private void deleteManagerAction(ActionEvent e, JFXAlert jfxAlert) {
		// TODO add confirmation dialog here.
		Alert alert = popOutAlert("Are you sure you want to delete the Manager ? \n "
				+ "Deleting the manager would delete the enitre manager's data \n "
				+ "and also the members associated with them permanently . \n "
				+ "Action once done the data will be deleted and cannot be reterived back . \n"
				+ "This Action will performa an Auto Save.", "Delete Manager/Board", AlertType.CONFIRMATION);

		if (alert.getResult() == ButtonType.OK) {
			ManagerDetailBoundary m = managerCBX.getSelectionModel().getSelectedItem();
			Optional<PerfomanceBoardBoundary> opbb = perfomanceBoardDetails.parallelStream()
					.filter(p -> p.getManagerDetailBoundary().getFolderName().equals(m.getFolderName())).findFirst();

			int size = perfomanceBoardDetails.size();
			if (opbb.isPresent()) {

				// create a back file of the data and then delete the entries .
				// to make sure we donot lose any data that is important to us.
				try {
					String contentData = mapper.writerWithDefaultPrettyPrinter()
							.writeValueAsString(perfomanceBoardDetails);
					System.out.println(contentData);

					// never append the file always overwrite it as we are storing all the details
					// in one single file.

					// back up all the data that is going to be delted for safe purpose,.
					FileUtils.writeStringToFile(
							new File(FilesUtil.DASHBOARD_CONTENT_PATH_BCK + FilesUtil.SLASH + "DashboardBackup-"
									+ System.currentTimeMillis() + ".json"),
							contentData, Charset.defaultCharset(), false);
				} catch (Exception ex) {
					log.error(Marker.ANY_MARKER, "deleteManagerAction", e);
				}

				// remove the detail from main boundary.
				perfomanceBoardDetails.remove(opbb.get());
				managerCBX.getItems().remove(managerCBX.getSelectionModel().getSelectedItem());
				managerCBX.getSelectionModel().clearSelection();
				managerCBX.fireEvent(e);
				boardHeader.clear();
				if (size > 1) {
					managerCBX.getSelectionModel().select(0);
				} else {
					// no more entries present.
					members.clear();
					tableView.fireEvent(e);
				}

				// auto save the details.
				saveAllTheDetails(null);
				jfxAlert.hideWithAnimation();
			}
		} else {
			// do nothing for now.
		}
	}

	private void onEditManagerSaveAction(String text, JFXAlert<String> alert, JFXNumberField portalId,
			JFXTextField managerName, JFXTextField designation, JFXTextField headerTF) {
		if (DashboardUtil.validateTextField(portalId, managerName, designation, headerTF)) {
			// let them edit the changes they wanted..
			ManagerDetailBoundary m = managerCBX.getSelectionModel().getSelectedItem();
			// prior to edit fetch board boundary to re-edit it.
			// now edit the performance board list with the updated manager.
			Optional<PerfomanceBoardBoundary> opbb = perfomanceBoardDetails.parallelStream()
					.filter(p -> p.getManagerDetailBoundary().getFolderName().equals(m.getFolderName())).findFirst();
			// edit the details
			m.setPortalId(Integer.valueOf(portalId.getText()));
			m.setName(managerName.getText());
			m.setDesignation(designation.getText());
			// rechange the header name and folder name.
			boardHeader.setText(headerTF.getText());
			m.setFolderName(headerTF.getText().replaceAll(" ", ""));
			// adjust the manager with the new changes.
			managerCBX.getItems().remove(managerCBX.getSelectionModel().getSelectedIndex());
			managerCBX.getSelectionModel().clearSelection();
			managerCBX.getItems().add(m);

			// just to be null safe
			if (opbb.isPresent()) {
				PerfomanceBoardBoundary pbb = opbb.get();
				pbb.setManagerDetailBoundary(m);
				pbb.setFolderName(m.getFolderName());
				pbb.setHeaderTxt(headerTF.getText());
			}

			// after adding it to the list re-change the selection model of combo box.
			managerCBX.getSelectionModel().select(m);

			// just save all the details as we have the manager changed.
			// save so that we will not have any issues further.
			saveAllTheDetails(null);

		}
		// hide the dialog animation once done.
		alert.hideWithAnimation();
	}

	private void saveAllTheDetails(ActionEvent e) {
		// clear all the employee fields before adding them again.
		Optional<PerfomanceBoardBoundary> details = perfomanceBoardDetails.parallelStream()
				.filter(p -> p.getFolderName().equals(managerCBX.getSelectionModel().getSelectedItem().getFolderName()))
				.findFirst();
		if (details.isPresent()) {
			if (details.get().getTeamMembers() != null) {
				details.get().getTeamMembers().clear();
			} else {
				details.get().setTeamMembers(new ArrayList<>());
			}
		}

		members.stream().forEach(this::loopOnTableData);

		// TODO need to add JSON Model for this table list.
		try {
			// For now skipping the pretty view to conserve diskspace.
			String contentData = mapper
					/* .writerWithDefaultPrettyPrinter() */.writeValueAsString(perfomanceBoardDetails);
			log.info("saveAllTheDetails", contentData);

			// never append the file always overwrite it as we are stroing all the details
			// in one single file.
			FileUtils.writeStringToFile(new File(FilesUtil.DASHBOARD_CONTENT_DATA), contentData,
					Charset.defaultCharset(), false);
			mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);

			List<PerfomanceBoardBoundary> list1 = mapper.readValue(contentData,
					mapper.getTypeFactory().constructCollectionType(List.class, PerfomanceBoardBoundary.class));
			// can be used as alternative to the above method to read json back.
			/*
			 * 
			 * List<PerfomanceBoardBoundary> myObjects = mapper.readValue(str, new
			 * TypeReference<List<PerfomanceBoardBoundary>>() { });
			 * myObjects.forEach(System.out::println);
			 * 
			 * PerfomanceBoardBoundary[] list =
			 * mapper.reader().forType(PerfomanceBoardBoundary[].class) .readValue(str);
			 * Arrays.stream(list).forEach(System.out::println);
			 */
		} catch (IOException e1) {
			log.error(Marker.ANY_MARKER, "saveAllTheDetails", e1);
		}
	}

	private void loopOnTableData(TeamMemberTableBoundary m) {

		TeamMember member = constructTeamMemberBoundary(m);
		// if time not set just leave it alone with old date.
		if (datePickerFX.getValue() != null) {
			int currentMonthDetails = datePickerFX.getValue().getMonthValue();

			member.setIntreval1(datePickerFX.getValue().getMonth().name() + " - " + datePickerFX.getValue().getYear());
			member.setIntreval2(Month.of(currentMonthDetails + 1).name() + " - " + datePickerFX.getValue().getYear());
			member.setIntreval3(Month.of(currentMonthDetails + 2).name() + " - " + datePickerFX.getValue().getYear());
		} else {
			LocalDate localDate = LocalDate.now();
			int currentMonthDetails = localDate.getMonthValue();
			member.setIntreval1(LocalDate.now().getMonth().name() + " - " + localDate.getYear());
			member.setIntreval2(Month.of(currentMonthDetails + 1).name() + " - " + localDate.getYear());
			member.setIntreval3(Month.of(currentMonthDetails + 2).name() + " - " + localDate.getYear());
		}
		member.setLink("http://localhost:8020/" + member.getPortalId());

		if (managerCBX.getSelectionModel().getSelectedItem().getTeamMembers() == null)
			managerCBX.getSelectionModel().getSelectedItem().setTeamMembers(new ArrayList<>());
		managerCBX.getSelectionModel().getSelectedItem().getTeamMembers().add(member);
		// this is wrong need to work on this.
		Optional<PerfomanceBoardBoundary> o = perfomanceBoardDetails.stream()
				.filter(p -> p.getFolderName().equals(managerCBX.getSelectionModel().getSelectedItem().getFolderName()))
				.findFirst();

		if (o.isPresent()) {

			if (perfomanceBoardDetails.stream().filter(p -> {
				String folderName = managerCBX.getSelectionModel().getSelectedItem().getFolderName();
				return p.getFolderName().equals(folderName);
			}).findFirst().get().getTeamMembers() == null)
				// check if the list of team members is instantiated or not.
				perfomanceBoardDetails.parallelStream()
						.filter(p -> p.getFolderName()
								.equals(managerCBX.getSelectionModel().getSelectedItem().getFolderName()))
						.findFirst().get().setTeamMembers(new ArrayList<>());

			perfomanceBoardDetails.parallelStream()
					.filter(p -> p.getFolderName()
							.equals(managerCBX.getSelectionModel().getSelectedItem().getFolderName()))
					.findFirst().get().getTeamMembers().add(member);

			// set the system date time if not specified.
			perfomanceBoardDetails.parallelStream()
					.filter(p -> p.getFolderName()
							.equals(managerCBX.getSelectionModel().getSelectedItem().getFolderName()))
					.findFirst().get().setInitalDate(datePickerFX.getValue() == null ? new java.util.Date()
							: new java.util.Date(Date.valueOf(datePickerFX.getValue()).getTime()));

		} else {
			// add to main list as it is a new entry.
			PerfomanceBoardBoundary perfomanceBoardBoundary = new PerfomanceBoardBoundary();
			perfomanceBoardBoundary.setFolderName(boardHeader.getText().replaceAll(" ", ""));
			perfomanceBoardBoundary.setHeaderTxt(boardHeader.getText());
			perfomanceBoardBoundary.setManagerDetailBoundary(managerCBX.getSelectionModel().getSelectedItem());
			perfomanceBoardBoundary.setInitalDate(datePickerFX.getValue() == null ? new java.util.Date()
					: new java.util.Date(Date.valueOf(datePickerFX.getValue()).getTime()));
			perfomanceBoardBoundary.setTeamMembers(new ArrayList<>());
			perfomanceBoardDetails.add(perfomanceBoardBoundary);
		}
		// perfomanceBoardDetails.add(boardBoundary);
	}

	private void displayOverViewDetails(ActionEvent e) {
		if (managerCBX.getSelectionModel().getSelectedItem() == null) {
			popOutAlert("No Valid Team Selected to display Detais. \n "
					+ "Select the Team Name in the Combo Box Available at the top .", "Not Valid Choice");
		} else {
			Optional<String> isFileDetailsExist = perfomanceBoardDetails.stream()
					.map(PerfomanceBoardBoundary::getFolderName)
					.filter(f -> f.equals(managerCBX.getSelectionModel().getSelectedItem().getFolderName()))
					.findFirst();

			if (isFileDetailsExist.isPresent()) {
				// get the details of board
				PerfomanceBoardBoundary p = perfomanceBoardDetails.stream().filter(
						f -> f.getFolderName().equals(managerCBX.getSelectionModel().getSelectedItem().getFolderName()))
						.findFirst().get();
				StringBuilder displayText = new StringBuilder();
				displayText.append("Board Name : " + p.getHeaderTxt());
				displayText.append("\n");
				displayText.append("Identifier Name : " + p.getFolderName());
				displayText.append("\n");
				displayText.append("Manager Name : " + p.getManagerDetailBoundary().getName());
				displayText.append("\n");
				displayText.append("Manager Portal Id : " + p.getManagerDetailBoundary().getPortalId());
				displayText.append("\n");
				displayText.append("Initial Date Added : " + p.getInitalDate().toString());

				popOutAlert(displayText.toString(), "Team Overview Information", AlertType.INFORMATION);
			} else {
				popOutAlert(
						"The Details for this folder doesn't exist . \n "
								+ "Please add the details and save them to display the details overview.",
						"MPS Perfomance Board");
			}
		}
	}

	@SuppressWarnings("unchecked")
	private HBox constructTable() {

		JFXTreeTableColumn<TeamMemberTableBoundary, String> portalId = new JFXTreeTableColumn<>("Portal ID");
		portalId.setPrefWidth(170);
		portalId.setCellValueFactory((TreeTableColumn.CellDataFeatures<TeamMemberTableBoundary, String> param) -> {
			if (portalId.validateValue(param)) {
				return param.getValue().getValue().getPortalId();
			} else {
				return portalId.getComputedValue(param);
			}
		});

		JFXTreeTableColumn<TeamMemberTableBoundary, String> name = new JFXTreeTableColumn<>("Name");
		name.setPrefWidth(290);
		name.setCellValueFactory((TreeTableColumn.CellDataFeatures<TeamMemberTableBoundary, String> param) -> {
			if (name.validateValue(param)) {
				return param.getValue().getValue().getName();
			} else {
				return name.getComputedValue(param);
			}
		});

		JFXTreeTableColumn<TeamMemberTableBoundary, String> designation = new JFXTreeTableColumn<>("Designation");
		designation.setPrefWidth(290);
		designation.setCellValueFactory((TreeTableColumn.CellDataFeatures<TeamMemberTableBoundary, String> param) -> {
			if (designation.validateValue(param)) {
				return param.getValue().getValue().getDescription();
			} else {
				return designation.getComputedValue(param);
			}
		});

		JFXTreeTableColumn<TeamMemberTableBoundary, String> score1 = new JFXTreeTableColumn<>("1st Month Score");
		score1.setPrefWidth(170);
		score1.setCellValueFactory((TreeTableColumn.CellDataFeatures<TeamMemberTableBoundary, String> param) -> {
			if (score1.validateValue(param)) {
				return param.getValue().getValue().getScore1();
			} else {
				return score1.getComputedValue(param);
			}
		});

		JFXTreeTableColumn<TeamMemberTableBoundary, String> score2 = new JFXTreeTableColumn<>("2nd Month Score");
		score2.setPrefWidth(170);
		score2.setCellValueFactory((TreeTableColumn.CellDataFeatures<TeamMemberTableBoundary, String> param) -> {
			if (score2.validateValue(param)) {
				return param.getValue().getValue().getScore2();
			} else {
				return score2.getComputedValue(param);
			}
		});

		JFXTreeTableColumn<TeamMemberTableBoundary, String> score3 = new JFXTreeTableColumn<>("3rd Month Score");
		score3.setPrefWidth(170);
		score3.setCellValueFactory((TreeTableColumn.CellDataFeatures<TeamMemberTableBoundary, String> param) -> {
			if (score3.validateValue(param)) {
				return param.getValue().getValue().getScore3();
			} else {
				return score3.getComputedValue(param);
			}
		});

		JFXTreeTableColumn<TeamMemberTableBoundary, String> valueAdd = new JFXTreeTableColumn<>("Value Add");
		valueAdd.setPrefWidth(170);
		valueAdd.setCellValueFactory((TreeTableColumn.CellDataFeatures<TeamMemberTableBoundary, String> param) -> {
			if (valueAdd.validateValue(param)) {
				return param.getValue().getValue().getValueAdd();
			} else {
				return valueAdd.getComputedValue(param);
			}
		});

		JFXTreeTableColumn<TeamMemberTableBoundary, String> quality = new JFXTreeTableColumn<>("Quality Points");
		quality.setPrefWidth(170);
		quality.setCellValueFactory((TreeTableColumn.CellDataFeatures<TeamMemberTableBoundary, String> param) -> {
			if (quality.validateValue(param)) {
				return param.getValue().getValue().getQuality();
			} else {
				return quality.getComputedValue(param);
			}
		});

		JFXTreeTableColumn<TeamMemberTableBoundary, String> onTime = new JFXTreeTableColumn<>("OnTime Score");
		onTime.setPrefWidth(170);
		onTime.setCellValueFactory((TreeTableColumn.CellDataFeatures<TeamMemberTableBoundary, String> param) -> {
			if (onTime.validateValue(param)) {
				return param.getValue().getValue().getOnTime();
			} else {
				return onTime.getComputedValue(param);
			}
		});

		// column cell factories
		portalId.setCellFactory(
				(TreeTableColumn<TeamMemberTableBoundary, String> param) -> new GenericEditableTreeTableCell<>(
						new TextFieldEditorBuilder()));
		portalId.setOnEditCommit((CellEditEvent<TeamMemberTableBoundary, String> t) -> t.getTreeTableView()
				.getTreeItem(t.getTreeTablePosition().getRow()).getValue().getPortalId().set(t.getNewValue()));
		name.setCellFactory(
				(TreeTableColumn<TeamMemberTableBoundary, String> param) -> new GenericEditableTreeTableCell<>(
						new TextFieldEditorBuilder()));
		name.setOnEditCommit((CellEditEvent<TeamMemberTableBoundary, String> t) -> t.getTreeTableView()
				.getTreeItem(t.getTreeTablePosition().getRow()).getValue().getName().set(t.getNewValue()));

		designation.setCellFactory(
				(TreeTableColumn<TeamMemberTableBoundary, String> param) -> new GenericEditableTreeTableCell<>(
						new TextFieldEditorBuilder()));
		designation.setOnEditCommit((CellEditEvent<TeamMemberTableBoundary, String> t) -> t.getTreeTableView()
				.getTreeItem(t.getTreeTablePosition().getRow()).getValue().getDescription().set(t.getNewValue()));

		score1.setCellFactory(
				(TreeTableColumn<TeamMemberTableBoundary, String> param) -> new GenericEditableTreeTableCell<>(
						new TextFieldEditorBuilder()));
		score1.setOnEditCommit((CellEditEvent<TeamMemberTableBoundary, String> t) -> t.getTreeTableView()
				.getTreeItem(t.getTreeTablePosition().getRow()).getValue().getScore1().set(t.getNewValue()));

		score2.setCellFactory(
				(TreeTableColumn<TeamMemberTableBoundary, String> param) -> new GenericEditableTreeTableCell<>(
						new TextFieldEditorBuilder()));
		score2.setOnEditCommit((CellEditEvent<TeamMemberTableBoundary, String> t) -> t.getTreeTableView()
				.getTreeItem(t.getTreeTablePosition().getRow()).getValue().getScore2().set(t.getNewValue()));

		score3.setCellFactory(
				(TreeTableColumn<TeamMemberTableBoundary, String> param) -> new GenericEditableTreeTableCell<>(
						new TextFieldEditorBuilder()));
		score3.setOnEditCommit((CellEditEvent<TeamMemberTableBoundary, String> t) -> t.getTreeTableView()
				.getTreeItem(t.getTreeTablePosition().getRow()).getValue().getScore3().set(t.getNewValue()));

		valueAdd.setCellFactory(
				(TreeTableColumn<TeamMemberTableBoundary, String> param) -> new GenericEditableTreeTableCell<>(
						new TextFieldEditorBuilder()));
		valueAdd.setOnEditCommit((CellEditEvent<TeamMemberTableBoundary, String> t) -> t.getTreeTableView()
				.getTreeItem(t.getTreeTablePosition().getRow()).getValue().getValueAdd().set(t.getNewValue()));

		quality.setCellFactory(
				(TreeTableColumn<TeamMemberTableBoundary, String> param) -> new GenericEditableTreeTableCell<>(
						new TextFieldEditorBuilder()));
		quality.setOnEditCommit((CellEditEvent<TeamMemberTableBoundary, String> t) -> t.getTreeTableView()
				.getTreeItem(t.getTreeTablePosition().getRow()).getValue().getQuality().set(t.getNewValue()));

		onTime.setCellFactory(
				(TreeTableColumn<TeamMemberTableBoundary, String> param) -> new GenericEditableTreeTableCell<>(
						new TextFieldEditorBuilder()));
		onTime.setOnEditCommit((CellEditEvent<TeamMemberTableBoundary, String> t) -> t.getTreeTableView()
				.getTreeItem(t.getTreeTablePosition().getRow()).getValue().getOnTime().set(t.getNewValue()));

		// set colums to table
		tableView.getColumns().addAll(portalId, name, designation, score1, score2, score3, valueAdd, quality, onTime);

		HBox box = new HBox(20);
		Label sizeLbl = new Label("Table Size: ");
		Label size = new Label();
		JFXTextField searchTF = new JFXTextField();
		searchTF.setPromptText("Search Text Here!!!");
		size.textProperty().bind(Bindings.createStringBinding(() -> String.valueOf(tableView.getCurrentItemsCount()),
				tableView.currentItemsCountProperty()));

		searchTF.textProperty().addListener((o, oldVal, newVal) -> {
			tableView.setPredicate(userProp -> {
				final TeamMemberTableBoundary user = userProp.getValue();
				return user.portalId.get().contains(newVal) || user.name.get().contains(newVal)
						|| user.description.get().contains(newVal) || user.score1.get().contains(newVal)
						|| user.score2.get().contains(newVal) || user.score3.get().contains(newVal)
						|| user.valueAdd.get().contains(newVal) || user.quality.get().contains(newVal)
						|| user.onTime.get().contains(newVal);
			});
		});
		searchTF.setAlignment(Pos.CENTER_LEFT);
		searchTF.setMinSize(450, 15);
		box.getChildren().addAll(searchTF, sizeLbl, size);
		box.setPrefSize(WIDTH, 20);
		box.setPadding(new Insets(15));

		return box;
	}

	private List<TeamMemberTableBoundary> constructTableMemberBoundary(List<TeamMember> members) {
		List<TeamMemberTableBoundary> mtb = new ArrayList<>();
		// construct the table readle list .
		members.stream().forEach(m -> {
			mtb.add(constructTableMemberBoundary(m));
		});
		return mtb;
	}

	private TeamMemberTableBoundary constructTableMemberBoundary(TeamMember member) {
		TeamMemberTableBoundary m = new TeamMemberTableBoundary();
		// instantiate fields
		m.setPortalId(new SimpleStringProperty());
		m.setName(new SimpleStringProperty());
		m.setDescription(new SimpleStringProperty());
		m.setScore1(new SimpleStringProperty());
		m.setScore2(new SimpleStringProperty());
		m.setScore3(new SimpleStringProperty());
		m.setValueAdd(new SimpleStringProperty());
		m.setOnTime(new SimpleStringProperty());
		m.setQuality(new SimpleStringProperty());

		// add the setters now

		m.getPortalId().set(member.getPortalId());
		m.getName().set(member.getName());
		m.getDescription().set(member.getDescription());
		m.getScore1().set(String.valueOf(member.getScore1()));
		m.getScore2().set(String.valueOf(member.getScore2()));
		m.getScore3().set(String.valueOf(member.getScore3()));
		m.getValueAdd().set(String.valueOf(member.getValueAdd()));
		m.getQuality().set(String.valueOf(member.getQuality()));
		m.getOnTime().set(String.valueOf(member.getQuality()));
		return m;
	}

	private TeamMember constructTeamMemberBoundary(TeamMemberTableBoundary member) {
		TeamMember m = new TeamMember();
		m.setPortalId(member.getPortalId().get());
		m.setName(member.getName().get());
		m.setDescription(member.getDescription().get());
		m.setScore1(Integer.valueOf(member.getScore1().get()));
		m.setScore2(Integer.valueOf(member.getScore2().get()));
		m.setScore3(Integer.valueOf(member.getScore3().get()));
		m.setValueAdd(Integer.valueOf(member.getValueAdd().get()));
		m.setOnTime(Integer.valueOf(member.getOnTime().get()));
		m.setQuality(Integer.valueOf(member.getQuality().get()));
		return m;
	}

	// TODO remove in mere future added for testing purpose
	private TeamMemberTableBoundary constructTestBoundary() {
		TeamMemberTableBoundary m = new TeamMemberTableBoundary();
		// instantiate fields
		m.setPortalId(new SimpleStringProperty());
		m.setName(new SimpleStringProperty());
		m.setDescription(new SimpleStringProperty());
		m.setScore1(new SimpleStringProperty());
		m.setScore2(new SimpleStringProperty());
		m.setScore3(new SimpleStringProperty());
		m.setValueAdd(new SimpleStringProperty());
		m.setOnTime(new SimpleStringProperty());
		m.setQuality(new SimpleStringProperty());

		Random r = new Random();
		m.getPortalId().set(String.valueOf(r.nextInt(9999)));
		m.getName().set(String.valueOf("ADSDSDS" + r.nextInt(9999)));
		m.getDescription().set(String.valueOf("ADSDSDS" + r.nextInt(9999)));
		m.getScore1().set(String.valueOf(r.nextInt(9999)));
		m.getScore2().set(String.valueOf(r.nextInt(9999)));
		m.getScore3().set(String.valueOf(r.nextInt(9999)));
		m.getValueAdd().set(String.valueOf(r.nextInt(9999)));
		m.getQuality().set(String.valueOf(r.nextInt(9999)));
		m.getOnTime().set(String.valueOf(r.nextInt(9999)));
		return m;
	}

	@Getter
	@Setter
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public static class TeamMemberTableBoundary extends RecursiveTreeObject<TeamMemberTableBoundary> {

		StringProperty portalId;
		StringProperty name;
		StringProperty description;
		StringProperty score1;
		StringProperty score2;
		StringProperty score3;
		StringProperty valueAdd;
		StringProperty quality;
		StringProperty onTime;
		// TODO add score getters and calendar events.
	}

}
