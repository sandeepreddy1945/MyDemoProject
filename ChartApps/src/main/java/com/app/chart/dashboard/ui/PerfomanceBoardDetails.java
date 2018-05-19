/**
 * 
 */
package com.app.chart.dashboard.ui;

import java.sql.Date;
import java.time.Month;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.assertj.core.util.Arrays;

import com.app.chart.model.ManagerDetailBoundary;
import com.app.chart.model.PerfomanceBoardBoundary;
import com.app.chart.model.TeamMember;
import com.app.chart.perfomance.dashboard.DashboardUtil;
import com.app.charts.utilities.JFXNumberField;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
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

/**
 * @author Sandeep
 *
 */
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

	private List<PerfomanceBoardBoundary> perfomanceBoardDetails = new LinkedList<>();
	private JFXTextField boardHeader;

	private ObservableList<TeamMemberTableBoundary> members = FXCollections
			.observableArrayList(/* constructTestBoundary() */);
	// build tree
	final TreeItem<TeamMemberTableBoundary> root = new RecursiveTreeItem<>(members, RecursiveTreeObject::getChildren);

	JFXTreeTableView<TeamMemberTableBoundary> tableView = new JFXTreeTableView<>(root);
	private JFXDatePicker datePickerFX;

	private ObjectMapper mapper = new ObjectMapper();

	/**
	 * 
	 */
	public PerfomanceBoardDetails() {
		super(5);

		initUI();
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

		getChildren().add(mainBox);

	}

	private HBox constructTopBox() {
		HBox box = new HBox(20);
		boardHeader = new JFXTextField();
		boardHeader.setPromptText("Enter the Team Name");
		boardHeader.setMinSize(300, 10);
		managerCBX.setMinSize(300, 15);
		DashboardUtil.buildRequestValidator(boardHeader);

		JFXButton addManager = new JFXButton("Add Manager");
		addManager.setOnAction(e -> {
			displayAddManagerDialog(boardHeader.getText());
		});
		addManager.setAlignment(Pos.CENTER_RIGHT);

		box.getChildren().addAll(boardHeader, managerCBX, addManager);

		box.setMinSize(WIDTH, 70);
		box.setPrefSize(WIDTH, 90);
		box.setPadding(new Insets(15));

		return box;
	}

	private void displayAddManagerDialog(String teamName) {
		JFXAlert<String> alert = new JFXAlert<>();
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.setOverlayClose(false);
		JFXDialogLayout layout = new JFXDialogLayout();
		layout.setHeading(new Text("Add Manager Detail"));

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

		okBtn.setOnAction(e -> {
			if (DashboardUtil.validateTextField(portalId, managerName, designation) && teamName != null
					&& teamName.length() > 0) {
				// build manager object
				// for now the team folder name will the teamName given replaced without spaces.
				ManagerDetailBoundary detailBoundary = new ManagerDetailBoundary(Float.valueOf(portalId.getText()),
						managerName.getText(), designation.getText(), teamName.replaceAll(" ", ""), new ArrayList<>());
				managerCBX.getItems().add(detailBoundary);
				// hide window on successful call
				alert.hideWithAnimation();
			}

			if (teamName.length() == 0) {
				popOutAlert("Team Name is Required Before Adding a Manager. \n"
						+ "Please Add the Team Name first to continue..", "Missing fields");
			}
		});

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
			if (e.getCode() == KeyCode.ENTER) {
				okBtn.fire();
			}
		});

		cancelBtn.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				okBtn.fire();
			}
		});
		layout.setActions(okBtn, cancelBtn);
		alert.setContent(layout);
		alert.show();

	}

	private void popOutAlert(String text, String title) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle(title);
		alert.setContentText(text);
		alert.showAndWait();
	}

	private VBox constructMiddleBox() {
		VBox middleBox = new VBox(25);

		JFXNumberField portalID = new JFXNumberField();
		portalID.setPromptText("Enter Portal Id");
		JFXTextField name = new JFXTextField();
		name.setPromptText("Enter Name");
		JFXTextField designation = new JFXTextField();
		designation.setPromptText("Enter Designation");
		datePickerFX = new JFXDatePicker();
		datePickerFX.setPromptText("Time Intreval");

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
		datePickerFX.setMinSize(250, 15);

		DashboardUtil.buildRequestValidator(descriptionTA);

		JFXButton saveMember = new JFXButton("Add Member");
		saveMember.setOnAction(e -> {

			if (DashboardUtil.validateTextField(portalID, name, designation, month1Score, month2Score, month3Score,
					valueAddTF, qualityTF, onTimeTF) && datePickerFX.getValue() != null) {

				Optional<StringProperty> checkOptional = members.stream().map(TeamMemberTableBoundary::getPortalId)
						.filter(f -> f.get().equals(portalID.getText())).findFirst();

				if (checkOptional.isPresent()) {
					popOutAlert("The Member you are trying to add Already Exits in the List. \n "
							+ "Please Add a new employee .\n" + "Tip: If Required use the search filter on Table to \n "
							+ "search for alread added Employee", "Member Alread Added !!");
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

					if (managerCBX.getSelectionModel().getSelectedItem().getTeamMembers() == null)
						managerCBX.getSelectionModel().getSelectedItem().setTeamMembers(new ArrayList<>());
					managerCBX.getSelectionModel().getSelectedItem().getTeamMembers().add(member);

					// clear all the text fields once the data is added to the table .
					DashboardUtil.clearTextField(portalID, name, designation, month1Score, month2Score, month3Score,
							valueAddTF, qualityTF, onTimeTF);
					// fire table view change.
					tableView.fireEvent(e);
				}
			}
		});

		// set some right padding to align it little bit near the box.
		saveMember.setPadding(new Insets(0, 45, 0, 0));

		HBox box1 = new HBox(10);
		box1.getChildren().addAll(portalID, name, designation, datePickerFX);

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

	private void setBoundsForTxtFields(JFXTextField... fields) {
		Arrays.asList(fields).stream().forEach(f -> {
			((JFXTextField) f).setMinSize(350, 10);
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

		saveBtn.setOnAction(e -> {

			members.stream().forEach(m -> {

				PerfomanceBoardBoundary boardBoundary = new PerfomanceBoardBoundary();
				boardBoundary.setHeaderTxt(boardHeader.getText());
				boardBoundary.setManagerDetailBoundary(managerCBX.getSelectionModel().getSelectedItem());
				if (boardBoundary.getTeamMembers() == null)
					boardBoundary.setTeamMembers(new LinkedList<>());
				int currentMonthDetails = datePickerFX.getValue().getMonthValue();
				TeamMember member = constructTeamMemberBoundary(m);

				member.setIntreval1(
						datePickerFX.getValue().getMonth().name() + " - " + datePickerFX.getValue().getYear());
				member.setIntreval2(
						Month.of(currentMonthDetails + 1).name() + " - " + datePickerFX.getValue().getYear());
				member.setIntreval3(
						Month.of(currentMonthDetails + 2).name() + " - " + datePickerFX.getValue().getYear());
				member.setLink("http://localhost:8020/" + member.getPortalId());
				boardBoundary.getTeamMembers().add(member);

				// do this as local date is immutable and difficult for parsing it.
				boardBoundary.setInitalDate(new java.util.Date(Date.valueOf(datePickerFX.getValue()).getTime()));

				if (managerCBX.getSelectionModel().getSelectedItem().getTeamMembers() == null)
					managerCBX.getSelectionModel().getSelectedItem().setTeamMembers(new ArrayList<>());
				managerCBX.getSelectionModel().getSelectedItem().getTeamMembers().add(member);
				perfomanceBoardDetails.add(boardBoundary);

				try {
					String str = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(perfomanceBoardDetails);
					System.out.println(str);
				} catch (JsonProcessingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// TODO need to add JSON Model for this table list.
			});

		});

		delBtn.setOnAction(e -> {
			members.remove(tableView.getSelectionModel().getSelectedItem());
			tableView.fireEvent(e);
		});

		previewBtn.setOnAction(e -> {
			// TODO need to add Implementation for this.
		});

		container.getChildren().addAll(saveBtn, delBtn, previewBtn);
		container.setAlignment(Pos.BOTTOM_RIGHT);

		box.getChildren().add(container);

		box.setMinSize(WIDTH, 100);
		box.setPrefSize(WIDTH, 100);
		box.setAlignment(Pos.BOTTOM_RIGHT);

		box.setPadding(new Insets(5, 30, 30, 0));
		return box;
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
