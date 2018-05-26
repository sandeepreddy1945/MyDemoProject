package com.app.chart.fx;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.slf4j.Marker;

import com.app.chart.fx.tree.OrgTreeView;
import com.app.chart.model.ChartBoardBoundary;
import com.app.chart.model.EmployeeDetails;
import com.app.chart.perfomance.dashboard.DashboardUtil;
import com.app.charts.utilities.JFXNumberField;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.cells.editors.TextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.base.GenericEditableTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jfoenix.validation.RequiredFieldValidator;

import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AddressBook {

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

	public static final String TEMP_JS = "temp.js";
	public static final String PREVIEW_JSON = "preview.json";
	public static final String TEMP_HTML = "temp.html";
	public static final String INDEX_HTML = "index.html";
	public static final String APP_JSON = "app.json";
	public static final String APP_JS = "app.js";
	private static final String EM1 = "1em";
	private static final String ERROR = "error";
	// build tree

	private final ObservableList<AddressBookTableBoundary> data = FXCollections
			.observableArrayList(/*
									 * new Person("1", "Jacob", "Smith", "jacob.smith@example.com", "s"), new
									 * Person("1", "Isabella", "Johnson", "isabella.johnson@example.com", "s"), new
									 * Person("1", "Ethan", "Williams", "ethan.williams@example.com", "s"), new
									 * Person("1", "Emma", "Jones", "emma.jones@example.com", "s"), new Person("1",
									 * "Michael", "Brown", "michael.brown@example.com", "s")
									 */);
	final TreeItem<AddressBookTableBoundary> root = new RecursiveTreeItem<>(data, RecursiveTreeObject::getChildren);

	JFXTreeTableView<AddressBookTableBoundary> tableView = new JFXTreeTableView<>(root);
	final HBox hb = new HBox();
	final HBox headerHb = new HBox();
	JFXComboBox<String> teamCombo;
	Properties props = new Properties();
	ObjectMapper objectMapper = new ObjectMapper();
	private JFXTextField addManagerField;
	private JFXTextField chartNameTF;

	private ChartBoardBoundary chartBoardBoundary;

	/*
	 * public static void main(String[] args) { launch(args); }
	 */

	private HBox mainPane = new HBox();

	// default constructor
	public AddressBook() throws IOException {
		// create the files for fetching before start.
		FilesUtil.initializeFileSettings();
		initUI();
	}

	public HBox fetchMainDisplayBox() {
		return mainPane;
	}
	// TODO to replace the save employee boundary with the ChartBoardBoundary for
	// saving purpose.

	// @Override
	public void start(Stage stage) throws IOException {
		// create the files for fetching before start.
		FilesUtil.initializeFileSettings();
		initUI(stage);
	}

	private void initUI(Stage stage) throws IOException {
		Scene scene = new Scene(new Group());
		stage.setTitle("Add Members Here");
		stage.setWidth(WIDTH);
		stage.setHeight(HEIGHT);
		chartBoardBoundary = new ChartBoardBoundary();

		tableView.setMinSize(WIDTH, HEIGHT - 450);
		tableView.setPrefSize(WIDTH, HEIGHT - 450);
		tableView.setShowRoot(false);
		tableView.setEditable(true);
		tableView.setPadding(new Insets(15));

		final Label label = new Label("Chart Address Book");
		label.setFont(new Font("Arial", 20));

		teamCombo = new JFXComboBox<>();
		teamCombo.setMinSize(300, 20);
		teamCombo.setPromptText("Select Manager/Manager's Team");

		// initalize the Team Combo Box from Main Props File
		props = FilesUtil.readMainPropertiesFile();
		teamCombo.getItems()
				.addAll(FXCollections.observableArrayList(props.keySet().toArray(new String[props.size()])));

		addManagerField = new JFXTextField();
		addManagerField.setPromptText("Create/Add New Chart here");

		final JFXButton previewBtn = new JFXButton("Create Preview Order");
		addManagerField.setMinSize(450, 20);

		// make it a required filed for entering data.
		DashboardUtil.buildRequestValidator(addManagerField);

		JFXButton addChart = new JFXButton("Add Chart To List");
		headerHb.setMinWidth(1000);
		headerHb.setSpacing(10);
		headerHb.getChildren().addAll(teamCombo, addManagerField, addChart, previewBtn);

		chartNameTF = new JFXTextField();

		chartNameTF.setPromptText("Enter Chart Header Name Here..");
		RequiredFieldValidator validator = new RequiredFieldValidator();
		validator.setMessage("This is a Required Field. Please Enter the Chart Name");
		validator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class).glyph(FontAwesomeIcon.WARNING).size(EM1)
				.styleClass(ERROR).build());
		chartNameTF.getValidators().add(validator);
		chartNameTF.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal) {
				chartNameTF.validate();
			}
		});

		chartNameTF.setMinSize(600, 30);
		chartNameTF.setPrefSize(500, 40);
		chartNameTF.setLabelFloat(true);

		final JFXNumberField portalTF = new JFXNumberField();
		portalTF.setPromptText("Portal Id");
		portalTF.setMaxWidth(230);
		final JFXTextField nameTF = new JFXTextField();
		nameTF.setPromptText("Name");
		nameTF.setMaxWidth(230);
		final JFXTextField designationTF = new JFXTextField();
		designationTF.setMaxWidth(230);
		designationTF.setPromptText("Designation");
		final JFXTextField teamTF = new JFXTextField();
		teamTF.setMaxWidth(230);
		teamTF.setPromptText("Team");
		final JFXTextField parentTF = new JFXTextField();
		parentTF.setMaxWidth(230);
		parentTF.setPromptText("Parent");

		// add validations to the txt fields
		DashboardUtil.buildRequestValidator(portalTF, nameTF, designationTF, teamTF, parentTF);

		final JFXButton addButton = new JFXButton("Add");
		addButton.setOnAction(e -> {
			addBtnAction(portalTF, nameTF, designationTF, teamTF, parentTF);
		});

		final JFXButton delButton = new JFXButton("Delete");
		delButton.setOnAction(e -> {
			data.remove(tableView.getSelectionModel().getFocusedIndex());
			tableView.fireEvent(e);
		});

		final JFXButton saveBtn = new JFXButton("Save");
		saveBtn.setOnAction(e -> {
			onSaveActionPerfomed();
		});

		addChart.setOnAction(e -> {
			onAddChartAction(addManagerField, chartNameTF);
		});

		previewBtn.setOnAction(e -> {
			onPreviewBtnAction(e);
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

		chartNameTF.setPadding(new Insets(0, 0, 5, 10));
		// tool tip for shortcut and clearing
		chartNameTF.setTooltip(new Tooltip("Press \"alt+c\" to clear this field"));
		chartNameTF.setOnKeyPressed(b -> {
			if (b.getCode().ordinal() == KeyCode.C.ordinal() && b.isAltDown()) {
				chartNameTF.clear();
			}
		});

		StackPane pane = new StackPane(chartNameTF);
		chartNameTF.setMaxSize(600, 40);
		pane.setMinSize(600, 40);
		pane.setAlignment(Pos.CENTER_LEFT);

		final VBox vbox = new VBox();
		vbox.setSpacing(25);
		vbox.setPadding(new Insets(10, 0, 0, 10));
		vbox.getChildren().addAll(label, headerHb, pane, buildDefaultTableRows(), /* table */ tableView, hb);

		((Group) scene.getRoot()).getChildren().addAll(vbox);

		stage.setScene(scene);
		stage.show();
	}

	private void initUI() throws IOException {

		chartBoardBoundary = new ChartBoardBoundary();

		tableView.setMinSize(WIDTH, HEIGHT - 300);
		tableView.setPrefSize(WIDTH, HEIGHT - 300);
		tableView.setShowRoot(false);
		tableView.setEditable(true);
		tableView.setPadding(new Insets(15));

		final Label label = new Label("Chart Address Book");
		label.setFont(new Font("Arial", 20));

		teamCombo = new JFXComboBox<>();
		teamCombo.setMinSize(300, 20);
		teamCombo.setPromptText("Select Manager/Manager's Team");

		// initalize the Team Combo Box from Main Props File
		props = FilesUtil.readMainPropertiesFile();
		teamCombo.getItems()
				.addAll(FXCollections.observableArrayList(props.keySet().toArray(new String[props.size()])));

		addManagerField = new JFXTextField();
		addManagerField.setPromptText("Create/Add New Chart here");

		final JFXButton previewBtn = new JFXButton("Create Preview Order");
		addManagerField.setMinSize(450, 20);

		// make it a required filed for entering data.
		DashboardUtil.buildRequestValidator(addManagerField);

		JFXButton addChart = new JFXButton("Add Chart To List");
		headerHb.setMinWidth(1000);
		headerHb.setSpacing(10);
		headerHb.getChildren().addAll(teamCombo, addManagerField, addChart, previewBtn);

		chartNameTF = new JFXTextField();

		chartNameTF.setPromptText("Enter Chart Header Name Here..");
		RequiredFieldValidator validator = new RequiredFieldValidator();
		validator.setMessage("This is a Required Field. Please Enter the Chart Name");
		validator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class).glyph(FontAwesomeIcon.WARNING).size(EM1)
				.styleClass(ERROR).build());
		chartNameTF.getValidators().add(validator);
		chartNameTF.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal) {
				chartNameTF.validate();
			}
		});

		chartNameTF.setMinSize(600, 30);
		chartNameTF.setPrefSize(500, 40);
		chartNameTF.setLabelFloat(true);

		final JFXNumberField portalTF = new JFXNumberField();
		portalTF.setPromptText("Portal Id");
		portalTF.setMaxWidth(230);
		final JFXTextField nameTF = new JFXTextField();
		nameTF.setPromptText("Name");
		nameTF.setMaxWidth(230);
		final JFXTextField designationTF = new JFXTextField();
		designationTF.setMaxWidth(230);
		designationTF.setPromptText("Designation");
		final JFXTextField teamTF = new JFXTextField();
		teamTF.setMaxWidth(230);
		teamTF.setPromptText("Team");
		final JFXTextField parentTF = new JFXTextField();
		parentTF.setMaxWidth(230);
		parentTF.setPromptText("Parent");

		// add validations to the txt fields
		DashboardUtil.buildRequestValidator(portalTF, nameTF, designationTF, teamTF, parentTF);

		final JFXButton addButton = new JFXButton("Add");
		addButton.setOnAction(e -> {
			addBtnAction(portalTF, nameTF, designationTF, teamTF, parentTF);
		});

		final JFXButton delButton = new JFXButton("Delete");
		delButton.setOnAction(e -> {
			data.remove(tableView.getSelectionModel().getFocusedIndex());
			tableView.fireEvent(e);
		});

		final JFXButton saveBtn = new JFXButton("Save");
		saveBtn.setOnAction(e -> {
			onSaveActionPerfomed();
		});

		addChart.setOnAction(e -> {
			onAddChartAction(addManagerField, chartNameTF);
		});

		previewBtn.setOnAction(e -> {
			onPreviewBtnAction(e);
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

		chartNameTF.setPadding(new Insets(0, 0, 5, 10));
		// tool tip for shortcut and clearing
		chartNameTF.setTooltip(new Tooltip("Press \"alt+c\" to clear this field"));
		chartNameTF.setOnKeyPressed(b -> {
			if (b.getCode().ordinal() == KeyCode.C.ordinal() && b.isAltDown()) {
				chartNameTF.clear();
			}
		});

		StackPane pane = new StackPane(chartNameTF);
		chartNameTF.setMaxSize(600, 40);
		pane.setMinSize(600, 40);
		pane.setAlignment(Pos.CENTER_LEFT);

		final VBox vbox = new VBox();
		vbox.setSpacing(25);
		vbox.setPadding(new Insets(10, 0, 0, 10));
		vbox.getChildren().addAll(label, headerHb, pane, buildDefaultTableRows(), /* table */ tableView, hb);

		mainPane.getChildren().addAll(vbox);

	}

	/**
	 * Build the table model and instantiate it to the table.
	 * 
	 * @return
	 */
	private HBox buildDefaultTableRows() {
		JFXTreeTableColumn<AddressBookTableBoundary, String> portalId = new JFXTreeTableColumn<>("Portal ID");
		portalId.setPrefWidth(250);
		portalId.setCellValueFactory((TreeTableColumn.CellDataFeatures<AddressBookTableBoundary, String> param) -> {
			if (portalId.validateValue(param)) {
				return param.getValue().getValue().getPortal();
			} else {
				return portalId.getComputedValue(param);
			}
		});

		JFXTreeTableColumn<AddressBookTableBoundary, String> name = new JFXTreeTableColumn<>("Name");
		name.setPrefWidth(450);
		name.setCellValueFactory((TreeTableColumn.CellDataFeatures<AddressBookTableBoundary, String> param) -> {
			if (name.validateValue(param)) {
				return param.getValue().getValue().getName();
			} else {
				return name.getComputedValue(param);
			}
		});

		JFXTreeTableColumn<AddressBookTableBoundary, String> designation = new JFXTreeTableColumn<>("Designation");
		designation.setPrefWidth(450);
		designation.setCellValueFactory((TreeTableColumn.CellDataFeatures<AddressBookTableBoundary, String> param) -> {
			if (designation.validateValue(param)) {
				return param.getValue().getValue().getDesignation();
			} else {
				return designation.getComputedValue(param);
			}
		});

		JFXTreeTableColumn<AddressBookTableBoundary, String> score1 = new JFXTreeTableColumn<>("Team Name");
		score1.setPrefWidth(350);
		score1.setCellValueFactory((TreeTableColumn.CellDataFeatures<AddressBookTableBoundary, String> param) -> {
			if (score1.validateValue(param)) {
				return param.getValue().getValue().getTeam();
			} else {
				return score1.getComputedValue(param);
			}
		});

		JFXTreeTableColumn<AddressBookTableBoundary, String> score2 = new JFXTreeTableColumn<>("Parent Member");
		score2.setPrefWidth(250);
		score2.setCellValueFactory((TreeTableColumn.CellDataFeatures<AddressBookTableBoundary, String> param) -> {
			if (score2.validateValue(param)) {
				return param.getValue().getValue().getParent();
			} else {
				return score2.getComputedValue(param);
			}
		});

		// column cell factories
		portalId.setCellFactory(
				(TreeTableColumn<AddressBookTableBoundary, String> param) -> new GenericEditableTreeTableCell<>(
						new TextFieldEditorBuilder()));

		portalId.setOnEditCommit((CellEditEvent<AddressBookTableBoundary, String> t) -> t.getTreeTableView()
				.getTreeItem(t.getTreeTablePosition().getRow()).getValue().getPortal().set(t.getNewValue()));
		name.setCellFactory(
				(TreeTableColumn<AddressBookTableBoundary, String> param) -> new GenericEditableTreeTableCell<>(
						new TextFieldEditorBuilder()));
		name.setOnEditCommit((CellEditEvent<AddressBookTableBoundary, String> t) -> t.getTreeTableView()
				.getTreeItem(t.getTreeTablePosition().getRow()).getValue().getName().set(t.getNewValue()));

		designation.setCellFactory(
				(TreeTableColumn<AddressBookTableBoundary, String> param) -> new GenericEditableTreeTableCell<>(
						new TextFieldEditorBuilder()));
		designation.setOnEditCommit((CellEditEvent<AddressBookTableBoundary, String> t) -> t.getTreeTableView()
				.getTreeItem(t.getTreeTablePosition().getRow()).getValue().getDesignation().set(t.getNewValue()));

		score1.setCellFactory(
				(TreeTableColumn<AddressBookTableBoundary, String> param) -> new GenericEditableTreeTableCell<>(
						new TextFieldEditorBuilder()));
		score1.setOnEditCommit((CellEditEvent<AddressBookTableBoundary, String> t) -> t.getTreeTableView()
				.getTreeItem(t.getTreeTablePosition().getRow()).getValue().getTeam().set(t.getNewValue()));

		score2.setCellFactory(
				(TreeTableColumn<AddressBookTableBoundary, String> param) -> new GenericEditableTreeTableCell<>(
						new TextFieldEditorBuilder()));
		score2.setOnEditCommit((CellEditEvent<AddressBookTableBoundary, String> t) -> t.getTreeTableView()
				.getTreeItem(t.getTreeTablePosition().getRow()).getValue().getParent().set(t.getNewValue()));

		// set colums to table
		tableView.getColumns().addAll(portalId, name, designation, score1, score2);

		HBox box = new HBox(20);
		Label sizeLbl = new Label("Table Size: ");
		Label tableSize = new Label();
		JFXTextField searchTF = new JFXTextField();
		searchTF.setPromptText("Search Text Here!!!");
		tableSize.textProperty().bind(Bindings.createStringBinding(
				() -> String.valueOf(tableView.getCurrentItemsCount()), tableView.currentItemsCountProperty()));

		searchTF.textProperty().addListener((o, oldVal, newVal) -> {
			tableView.setPredicate(userProp -> {
				final AddressBookTableBoundary user = userProp.getValue();
				return user.portal.get().contains(newVal)
						|| user.name.get().toLowerCase().contains(newVal.toLowerCase())
						|| user.designation.get().toLowerCase().contains(newVal.toLowerCase())
						|| user.team.get().toLowerCase().contains(newVal.toLowerCase())
						|| user.parent.get().contains(newVal);
			});
		});
		searchTF.setAlignment(Pos.CENTER_LEFT);
		searchTF.setLabelFloat(true);
		searchTF.setMinSize(450, 25);
		box.getChildren().addAll(searchTF, sizeLbl, tableSize);
		box.setPrefSize(WIDTH, 25);
		box.setPadding(new Insets(5));

		return box;
	}

	private void addBtnAction(final JFXTextField portalTF, final JFXTextField nameTF, final JFXTextField designationTF,
			final JFXTextField teamTF, final JFXTextField parentTF) {
		if (DashboardUtil.validateTextField(portalTF, nameTF, designationTF, teamTF, parentTF)) {
			// construct employee table add it to the table boundary.
			EmployeeDetails e = new EmployeeDetails();
			e.setPortalId(portalTF.getText());
			e.setName(nameTF.getText());
			e.setDescription(designationTF.getText());
			e.setTeam(teamTF.getText());
			e.setParent(parentTF.getText());
			data.add(constructAddressBookTableMemberBoundary(e));
			DashboardUtil.clearTextField(portalTF, nameTF, designationTF, teamTF, parentTF);
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

	private void onTeamComboChanged(ActionEvent e) {
		try {
			String jsonStr = FileUtils
					.readFileToString(
							new File(FilesUtil.MAIN_APP_PATH + FilesUtil.SLASH
									+ teamCombo.getSelectionModel().getSelectedItem() + FilesUtil.SLASH + APP_JSON),
							Charset.defaultCharset());
			data.clear();
			if (jsonStr != null && jsonStr.length() > 0) {

				// List<MyClass> myObjects = mapper.readValue(jsonInput, new
				// TypeReference<List<MyClass>>(){});
				// List<MyClass> myObjects = mapper.readValue(jsonInput,
				// mapper.getTypeFactory().constructCollectionType(List.class, MyClass.class));
				chartBoardBoundary = objectMapper.readValue(jsonStr, ChartBoardBoundary.class);
				List<EmployeeDetails> empList = chartBoardBoundary.getEmployeeDetails();
				// auto populate the header text as well.
				chartNameTF.setText(chartBoardBoundary.getHeaderTxt());

				empList.forEach(emp -> {
					data.add(constructAddressBookTableMemberBoundary(emp));
					tableView.fireEvent(e);
				});
			}
		} catch (IOException e1) {
			log.error(Marker.ANY_MARKER, "onTeamComboChanged", e1);
		}
	}

	private void onAddChartAction(final JFXTextField addManagerField, final JFXTextField chartNameTF) {
		// check if any spaces are present and report it.
		if (addManagerField.getText().length() < 1 || addManagerField.getText().contains(" ")) {
			JFXAlert<String> alert = new JFXAlert<>();
			alert.initModality(Modality.APPLICATION_MODAL);
			alert.setOverlayClose(false);
			JFXDialogLayout layout = new JFXDialogLayout();
			layout.setHeading(new Label("Missing Fields"));
			layout.setBody(new Label("Chart Name cannot be empty (Or) Cannot contain Spaces in the Name \n"
					+ "Please Enter Values as per standards in all The Fields To Continue ..."));
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

		else {

			// check for duplicate entries on the combo box by checking on equals ignore
			// case
			// as files will not be case sensitive.
			Optional<String> isItemAlreadyExists = teamCombo.getItems().stream()
					.filter(s -> s.equalsIgnoreCase(addManagerField.getText())).findFirst();

			if (isItemAlreadyExists.isPresent()) {
				popOutAlert("The Folder / Manager Name you are trying to add already exists. /n "
						+ "Please Try adding a different name . The name is just for saving purpose "
						+ " and will not be displayed any where else . \n"
						+ "Fox Example : If you have sandeep name aleready give and wanted to use the similar name for identification ."
						+ "Please use something like this sandeep1 and save.", "MPS Charts");
				return;
			} else {

				try {

					if (!(chartNameTF.getText().length() > 0)) {
						popOutAlert("The Header Name of the Chart Cannot be Empty . \n "
								+ "Please add the Title Header to be displayed in the Chart Header Name field \n "
								+ "and then click on Add Manager button.", "MPS Charts");
						return;
					}
					// add an entry to manager.properties file for loading.
					if (!new File(FilesUtil.MAIN_APP_PATH + FilesUtil.SLASH + addManagerField.getText()).exists()) {
						props.put(addManagerField.getText(), chartNameTF.getText());
						FileUtils.write(new File(FilesUtil.MANAGER_PROPS_PATH),
								addManagerField.getText() + "=" + chartNameTF.getText() + "\n",
								Charset.defaultCharset(), true);
					}
					FilesUtil.checkAndCreateDir(FilesUtil.MAIN_APP_PATH + FilesUtil.SLASH + addManagerField.getText());
					FilesUtil.checkAndCreateFile(
							FilesUtil.MAIN_APP_PATH + FilesUtil.SLASH + addManagerField.getText() + FilesUtil.SLASH
									+ APP_JS,
							FilesUtil.MAIN_APP_PATH + FilesUtil.SLASH + addManagerField.getText() + FilesUtil.SLASH
									+ APP_JSON,
							FilesUtil.MAIN_APP_PATH + FilesUtil.SLASH + addManagerField.getText() + FilesUtil.SLASH
									+ INDEX_HTML,
							FilesUtil.MAIN_APP_PATH + FilesUtil.SLASH + addManagerField.getText() + FilesUtil.SLASH
									+ TEMP_HTML,
							FilesUtil.MAIN_APP_PATH + FilesUtil.SLASH + addManagerField.getText() + FilesUtil.SLASH
									+ PREVIEW_JSON,
							FilesUtil.MAIN_APP_PATH + FilesUtil.SLASH + addManagerField.getText() + FilesUtil.SLASH
									+ TEMP_JS);

					FileUtils.copyToFile(
							getClass().getClassLoader().getResourceAsStream("com/app/chart/html/index.html"),
							new File(FilesUtil.MAIN_APP_PATH + FilesUtil.SLASH + addManagerField.getText()
									+ FilesUtil.SLASH + INDEX_HTML));

					FileUtils.copyToFile(
							getClass().getClassLoader().getResourceAsStream("com/app/chart/html/temp.html"),
							new File(FilesUtil.MAIN_APP_PATH + FilesUtil.SLASH + addManagerField.getText()
									+ FilesUtil.SLASH + TEMP_HTML));

					teamCombo.getItems().add(addManagerField.getText());
					addManagerField.clear();
				} catch (IOException e1) {
					log.error(Marker.ANY_MARKER, "onAddChartAction", e1);
				}
			}

		}
	}

	private void popOutAlert(String text, String title) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle(title);
		alert.setContentText(text);
		alert.showAndWait();
	}

	private Alert popOutAlert(String text, AlertType alertType) {
		Alert alert = new Alert(alertType);
		alert.setTitle("MPS Charts");
		alert.setContentText(text);
		alert.showAndWait();

		return alert;
	}

	private void onSaveActionPerfomed() {
		// save action
		if (teamCombo.getSelectionModel().getSelectedIndex() != -1) {

			if (chartNameTF.getText().length() > 0) {

				// check if the header name is changed . This is imp for runjson building.
				if (!props.get(teamCombo.getSelectionModel().getSelectedItem()).equals(chartNameTF.getText())) {
					Alert alert = popOutAlert("The Header Name is changed . Did you really intend to change this. \n "
							+ "Are you sure you wanted to save the new name instead of the old name . \n "
							+ "Once saved the new name will be displayed as the header for this Organization chart.",
							AlertType.CONFIRMATION);

					if (alert.getResult() == ButtonType.OK) {
						// back up the file first before saving it.
						try {
							File bckFile = new File(FilesUtil.DASHBOARD_PROPS_PATH_BCK + FilesUtil.SLASH
									+ "managerpropsbck-" + System.currentTimeMillis() + ".properties");
							OutputStream os = FileUtils.openOutputStream(bckFile, false);
							props.store(os, "Back Up File Craeted Due to Changes Made to header on :"
									+ new Date(System.currentTimeMillis()));
							os.flush();
							os.close();
						} catch (Exception ex) {
							log.error(Marker.ANY_MARKER, "onSaveActionPerfomed", ex);
						}

						props.put(teamCombo.getSelectionModel().getSelectedItem(), chartNameTF.getText());
						saveAllDetailsToFile();
					} else {
						// do nothing for now just hide the dialog and leave it alone.
					}
				} else {
					saveAllDetailsToFile();
				}

			} else {
				popOutAlert("The Header Text the chart has to display can never be Empty . \n "
						+ "Please provide a Header Text thats needs to be displayed in the "
						+ "Chart Header Text Field..", "MPS Charts");
			}
		}
	}

	private void saveAllDetailsToFile() {
		List<EmployeeDetails> empList = new ArrayList<>();
		data.stream().forEach(p -> {
			EmployeeDetails emp = new EmployeeDetails();
			emp.setName(p.getName().get());
			emp.setParent(p.getParent().get());
			emp.setPortalId(p.getPortal().get());
			emp.setTeam(p.getTeam().get());
			emp.setDescription(p.getDesignation().get());
			emp.setLink("http://localhost:8020/" + emp.getPortalId());
			empList.add(emp);
		});

		chartBoardBoundary.setEmployeeDetails(empList);
		chartBoardBoundary.setFolderName(teamCombo.getSelectionModel().getSelectedItem());
		chartBoardBoundary.setHeaderTxt(chartNameTF.getText());
		// TODO to think if this field is useful or replace it with some other data in
		// future
		chartBoardBoundary.setHeadName(chartNameTF.getText());

		try {

			// re- write the props manager file with the newer entries .so that we dont miss
			// on any updates.

			File propsFile = new File(FilesUtil.MANAGER_PROPS_PATH);
			OutputStream os = FileUtils.openOutputStream(propsFile);
			// second field is the commented filed.
			props.store(os, "This is updated recently on " + new Date(System.currentTimeMillis()));
			// flush the changes to file
			os.flush();
			os.close();
			// save the chart list in the boundary.
			FileUtils.write(
					new File(FilesUtil.MAIN_APP_PATH + FilesUtil.SLASH + teamCombo.getSelectionModel().getSelectedItem()
							+ FilesUtil.SLASH + APP_JSON),
					objectMapper/* .writerWithDefaultPrettyPrinter() */.writeValueAsString(chartBoardBoundary),
					Charset.defaultCharset(), false);
			log.info("saveAllDetailsToFile",
					objectMapper/* .writerWithDefaultPrettyPrinter() */.writeValueAsString(chartBoardBoundary));
			// prepare the js file required for the app to function
		} catch (IOException e1) {
			log.error(Marker.ANY_MARKER, "saveAllDetailsToFile", e1);
		}
	}

	private void onPreviewBtnAction(ActionEvent e) {

		if (teamCombo.getSelectionModel().getSelectedItem() == null) {
			popOutAlert(
					"Not a Valid folder selected to display the data.. \n "
							+ "Please select a valid option from the Combo Box and then make this selection.",
					"MPS Charts");
		} else {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Address Book");
			alert.setContentText("This would auto save the current table data . \n "
					+ "Please press OK to proceed or Cancel to stay on this page.");
			alert.showAndWait();

			if (alert.getResult() == ButtonType.OK) {
				// do a auto save action before proceeding further.
				this.onSaveActionPerfomed();

				// TODO for now this is not changed with chartboundary
				// need to make sure if in future this needs to be changed.
				Dialog orgPreviewDialog = new Dialog<>();
				orgPreviewDialog.setResizable(true);
				List<EmployeeDetails> empList = new ArrayList<>();
				data.stream().forEach(p -> {
					EmployeeDetails emp = new EmployeeDetails();
					emp.setName(p.getName().get());
					emp.setParent(p.getParent().get());
					emp.setPortalId(p.getPortal().get());
					emp.setTeam(p.getTeam().get());
					emp.setDescription(p.getDesignation().get());
					emp.setLink("http://localhost:8020/" + emp.getPortalId());
					empList.add(emp);
				});
				// current app dir depending on the check box..
				File appDir = new File(
						FilesUtil.MAIN_APP_PATH + FilesUtil.SLASH + teamCombo.getSelectionModel().getSelectedItem());
				OrgTreeView<EmployeeDetails> orgTreeView = new OrgTreeView<>(empList, orgPreviewDialog, appDir);
				orgTreeView.show();
			} else {
				// do nothing.
			}
		}

	}

	private AddressBookTableBoundary constructAddressBookTableMemberBoundary(EmployeeDetails member) {
		AddressBookTableBoundary m = new AddressBookTableBoundary();
		// instantiate fields
		m.setPortal(new SimpleStringProperty());
		m.setName(new SimpleStringProperty());
		m.setDesignation(new SimpleStringProperty());
		m.setParent(new SimpleStringProperty());
		m.setTeam(new SimpleStringProperty());

		// add the setters now

		m.getPortal().set(member.getPortalId());
		m.getName().set(member.getName());
		m.getDesignation().set(member.getDescription());
		m.getParent().set(String.valueOf(member.getParent()));
		m.getTeam().set(String.valueOf(member.getTeam()));
		return m;
	}

	private EmployeeDetails constructEmployeeDetailsBoundary(AddressBookTableBoundary member) {
		EmployeeDetails m = new EmployeeDetails();
		m.setPortalId(member.getPortal().get());
		m.setName(member.getName().get());
		m.setDescription(member.getDesignation().get());
		m.setParent(member.getParent().get());
		m.setTeam(member.getTeam().get());
		return m;
	}

	@Getter
	@Setter
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public static class AddressBookTableBoundary extends RecursiveTreeObject<AddressBookTableBoundary> {

		StringProperty portal;
		StringProperty name;
		StringProperty designation;
		StringProperty team;
		StringProperty parent;
		// TODO add score getters and calendar events.
	}
}