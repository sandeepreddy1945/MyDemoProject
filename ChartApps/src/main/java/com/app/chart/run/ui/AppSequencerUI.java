/**
 * 
 */
package com.app.chart.run.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

import com.app.chart.fx.FilesUtil;
import com.app.chart.model.PerfomanceBoardBoundary;
import com.app.chart.model.RunJsonBoundary;
import com.app.chart.perfomance.dashboard.DashboardUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
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
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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
public class AppSequencerUI extends HBox {

	private LinkedList<RunJSonTableBoundary> memberLinkedList = new LinkedList<>();

	private ObservableList<RunJSonTableBoundary> members = FXCollections.observableArrayList(memberLinkedList);
	// build tree
	final TreeItem<RunJSonTableBoundary> root = new RecursiveTreeItem<>(members, RecursiveTreeObject::getChildren);

	JFXTreeTableView<RunJSonTableBoundary> tableView = new JFXTreeTableView<>(root);

	private List<PerfomanceBoardBoundary> perfomanceBoardBoundaries;

	public static Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
	public static double WIDTH = visualBounds.getWidth() - 100;
	public static double HEIGHT = visualBounds.getHeight() - 50;
	private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");

	VBox mainBox = new VBox(10);
	private ObjectMapper mapper = new ObjectMapper();
	private List<RunJsonBoundary> runJsonBoundaries;
	private JFXTreeTableView<SearchOptionTableBoundary> searchOptionTabView;

	public AppSequencerUI(File runJsonFile) {
		super(10);
		initUI();
	}

	public AppSequencerUI(String runJsonFile) {
		super(10);

		initUI();
	}

	public AppSequencerUI(List<RunJsonBoundary> runJsonBoundaries) {
		super(10);

		this.runJsonBoundaries = runJsonBoundaries;
		initUI();
	}

	private void initUI() {

		// construct the table first.
		tableView.setMinSize(WIDTH, HEIGHT - 180);
		tableView.setPrefSize(WIDTH, HEIGHT - 180);
		tableView.setShowRoot(false);
		tableView.setEditable(true);
		tableView.setPadding(new Insets(15));

		// add drag and drop listener on the table
		addTableDragDropListener();

		HBox bottomPanel = buildBottomPanel();

		mainBox.getChildren().addAll(initTableView(), tableView, bottomPanel);

		getChildren().add(mainBox);
	}

	private HBox buildBottomPanel() {
		HBox box = new HBox(10);
		JFXButton addEntry = new JFXButton("Add Entry");
		JFXButton editEntry = new JFXButton("Edit Entry");
		JFXButton deleteRow = new JFXButton("Delete Row");
		JFXButton saveBtn = new JFXButton("Save");

		// listeners to buttons

		addEntry.setOnAction(this::addEntryAction);
		editEntry.setOnAction(this::editEntryAction);
		deleteRow.setOnAction(this::deleteRowEntryAction);
		saveBtn.setOnAction(this::saveDetailsAction);

		// set position to right
		box.setAlignment(Pos.BOTTOM_RIGHT);
		box.setPadding(new Insets(0, 40, 0, 0));
		// add all the buttons to box
		box.getChildren().addAll(addEntry, editEntry, deleteRow, saveBtn);
		return box;
	}

	private void addEntryAction(ActionEvent e) {
		displayAddEntryBox();
	}

	private void editEntryAction(ActionEvent e) {
		if (tableView.getSelectionModel().getSelectedItem() != null) {
			RunJSonTableBoundary runJSonTableBoundary = tableView.getSelectionModel().getSelectedItem().getValue();
			if (runJSonTableBoundary.getType().get().equalsIgnoreCase(DisplayBoardConstants.image.name())
					|| runJSonTableBoundary.getType().get().equalsIgnoreCase(DisplayBoardConstants.customer.name())) {
				displayEditEntryDialog(runJSonTableBoundary);
			} else {
				popOutAlert(
						"Only an Image Chart Can be Edited for now. \n"
								+ "If it really requires to edit the Dashboard (or) Organizational Chart"
								+ " please remove the entry from the table and add it as a fresh entry .",
						"MPS Charts", AlertType.INFORMATION);
			}
		}
	}

	/**
	 * TODO to reduce the similar piece of code
	 * 
	 * @param value
	 */
	private void displayEditEntryDialog(RunJSonTableBoundary boundary) {

		ObservableList<String> optionsList = FXCollections.observableArrayList(DisplayBoardConstants.chart.name(),
				DisplayBoardConstants.dashboard.name(), DisplayBoardConstants.image.name(),
				DisplayBoardConstants.customer.name());
		JFXComboBox<String> optionsBox = new JFXComboBox<>(optionsList);
		JFXComboBox<String> headerCBX = new JFXComboBox<>(FXCollections.observableArrayList("Y", "N"));

		optionsBox.setPromptText("Select The Type Here");
		headerCBX.setPromptText("Is Header Display Req");

		JFXAlert<String> alert = new JFXAlert<>();
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.setOverlayClose(false);
		JFXDialogLayout layout = new JFXDialogLayout();
		layout.setHeading(new Text("Add Perfomance Meter Stats"));
		layout.setMinSize(1200, 200);

		HBox box = new HBox(10);
		JFXTextField pathTF = new JFXTextField();
		pathTF.setPromptText("Path");
		JFXTextField headerTxtTF = new JFXTextField();
		headerTxtTF.setPromptText("Enter Header Txt");

		// dimension settings
		optionsBox.setMinWidth(200);
		headerCBX.setMinWidth(250);

		pathTF.setMinWidth(300);
		headerTxtTF.setMinWidth(300);

		box.getChildren().addAll(optionsBox, pathTF, headerCBX, headerTxtTF);
		layout.setBody(box);

		// preload the values into the fields
		optionsBox.getSelectionModel().select(boundary.type.get());
		// disable the combo box as we don't want to re-edit it.
		optionsBox.setDisable(true);

		headerCBX.getSelectionModel().select(boundary.isHeaderApplicable.get());
		headerTxtTF.setText(boundary.displayTxt.get());
		pathTF.setText(boundary.path.get());
		// disable the path as well if req to edit delete this and add a new entry
		// instead.
		pathTF.setDisable(true);

		JFXButton okBtn = new JFXButton("Add Record To Table");
		okBtn.getStyleClass().add("dialog-accept");

		optionsBox.setOnAction(e -> {
			if (optionsBox.getSelectionModel().getSelectedItem().equals(DisplayBoardConstants.dashboard.name())) {
				pathTF.setText(FilesUtil.DASHBOARD_CONTENT_DATA_FILE);
				pathTF.setDisable(true);
				headerCBX.getSelectionModel().select(0);
				headerCBX.setDisable(true);
				headerTxtTF.setDisable(true);
			} else if (optionsBox.getSelectionModel().getSelectedItem().equals(DisplayBoardConstants.chart.name())) {
				// adding validator for all these fields
				DashboardUtil.buildRequestValidator(pathTF, headerTxtTF);
				headerCBX.getSelectionModel().select(0);
				headerCBX.setDisable(true);
				// the header text should be loaded from the files it self no edit option here
				// again.
				headerTxtTF.setDisable(true);
				pathTF.clear();
				pathTF.setDisable(false);
			} else if (optionsBox.getSelectionModel().getSelectedItem().equals(DisplayBoardConstants.image.name())) {
				DashboardUtil.buildRequestValidator(pathTF, headerTxtTF);
				headerCBX.setDisable(false);
				headerTxtTF.setDisable(false);
				pathTF.clear();
				pathTF.setDisable(false);
			} else if (optionsBox.getSelectionModel().getSelectedItem().equals(DisplayBoardConstants.customer.name())) {
				DashboardUtil.buildRequestValidator(pathTF, headerTxtTF);
				headerCBX.setDisable(false);
				headerTxtTF.setDisable(false);
				pathTF.clear();
				pathTF.setDisable(false);
			}
		});

		okBtn.setOnAction(e -> {
			// now add the record obtained to the main table .
			// path is the main part of the app so check for it.
			if (pathTF.getText().length() > 0 && optionsBox.getSelectionModel().getSelectedItem() != null) {
				String fileName = pathTF.getText();
				boolean isHeaderReq = headerCBX.getSelectionModel().getSelectedItem() == null ? Boolean.FALSE
						: stringToBoolean(headerCBX.getSelectionModel().getSelectedItem());
				String isHedeaderReqTxt = booleanToString(isHeaderReq);
				String headerText = headerTxtTF.getText();
				String type = optionsBox.getSelectionModel().getSelectedItem();

				// as this is a edit sequence for the table first remove the old row and the
				// re-add it back again.
				members.remove(boundary);

				// add it at the same row where it was removed.
				members.add(tableView.getSelectionModel().getSelectedIndex(),
						constructTableMemberBoundary(type, fileName, isHedeaderReqTxt, headerText));
				// fire the table away with the changes.
				tableView.fireEvent(e);

				// hide the popup
				alert.hideWithAnimation();
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
			if (e.getCode().ordinal() == KeyCode.ENTER.ordinal()) {
				okBtn.fire();
			}
		});

		cancelBtn.setOnKeyPressed(e -> {
			if (e.getCode().ordinal() == KeyCode.ENTER.ordinal()) {
				cancelBtn.fire();
			}
		});

		JFXButton searchBtn = new JFXButton("Search Option");
		searchBtn.setOnAction(e -> {
			try {
				// run the dialog only after a valid selection is made.
				if (optionsBox.getSelectionModel().getSelectedItem() != null)
					buildSearchOptionDialog(optionsBox.getSelectionModel().getSelectedItem(), pathTF, headerTxtTF);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});

		// only edit on header Txt and isHeaderReq fields no search option provided to
		// the user.
		// TODO to check if this can be improved in future.
		searchBtn.setDisable(true);

		layout.setActions(searchBtn, okBtn, cancelBtn);
		alert.setContent(layout);
		alert.show();

	}

	private void deleteRowEntryAction(ActionEvent e) {
		if (tableView.getSelectionModel().getSelectedItem() != null) {
			members.remove(tableView.getSelectionModel().getSelectedItem().getValue());
			tableView.fireEvent(e);
		}
	}

	private void saveDetailsAction(ActionEvent e) {

	}

	private void displayAddEntryBox() {

		ObservableList<String> optionsList = FXCollections.observableArrayList(DisplayBoardConstants.chart.name(),
				DisplayBoardConstants.dashboard.name(), DisplayBoardConstants.image.name(),
				DisplayBoardConstants.customer.name());
		JFXComboBox<String> optionsBox = new JFXComboBox<>(optionsList);
		JFXComboBox<String> headerCBX = new JFXComboBox<>(FXCollections.observableArrayList("Y", "N"));

		optionsBox.setPromptText("Select The Type Here");
		headerCBX.setPromptText("Is Header Display Req");

		JFXAlert<String> alert = new JFXAlert<>();
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.setOverlayClose(false);
		JFXDialogLayout layout = new JFXDialogLayout();
		layout.setHeading(new Text("Add Perfomance Meter Stats"));
		layout.setMinSize(1200, 200);

		HBox box = new HBox(10);
		JFXTextField pathTF = new JFXTextField();
		pathTF.setPromptText("Path");
		JFXTextField headerTxtTF = new JFXTextField();
		headerTxtTF.setPromptText("Enter Header Txt");

		// dimension settings
		optionsBox.setMinWidth(200);
		headerCBX.setMinWidth(250);

		pathTF.setMinWidth(300);
		headerTxtTF.setMinWidth(300);

		box.getChildren().addAll(optionsBox, pathTF, headerCBX, headerTxtTF);
		layout.setBody(box);

		JFXButton okBtn = new JFXButton("Add Record To Table");
		okBtn.getStyleClass().add("dialog-accept");

		optionsBox.setOnAction(e -> {
			if (optionsBox.getSelectionModel().getSelectedItem().equals(DisplayBoardConstants.dashboard.name())) {
				pathTF.setText(FilesUtil.DASHBOARD_CONTENT_DATA_FILE);
				pathTF.setDisable(true);
				headerCBX.getSelectionModel().select(0);
				headerCBX.setDisable(true);
				headerTxtTF.setDisable(true);
			} else if (optionsBox.getSelectionModel().getSelectedItem().equals(DisplayBoardConstants.chart.name())) {
				// adding validator for all these fields
				DashboardUtil.buildRequestValidator(pathTF, headerTxtTF);
				headerCBX.getSelectionModel().select(0);
				headerCBX.setDisable(true);
				// the header text should be loaded from the files it self no edit option here
				// again.
				headerTxtTF.setDisable(true);
				pathTF.clear();
				pathTF.setDisable(false);
			} else if (optionsBox.getSelectionModel().getSelectedItem().equals(DisplayBoardConstants.image.name())) {
				DashboardUtil.buildRequestValidator(pathTF, headerTxtTF);
				headerCBX.setDisable(false);
				headerTxtTF.setDisable(false);
				pathTF.clear();
				pathTF.setDisable(false);
			} else if (optionsBox.getSelectionModel().getSelectedItem().equals(DisplayBoardConstants.customer.name())) {
				DashboardUtil.buildRequestValidator(pathTF, headerTxtTF);
				headerCBX.setDisable(false);
				headerTxtTF.setDisable(false);
				pathTF.clear();
				pathTF.setDisable(false);
			}
		});

		okBtn.setOnAction(e -> {
			// now add the record obtained to the main table .
			// path is the main part of the app so check for it.
			if (pathTF.getText().length() > 0 && optionsBox.getSelectionModel().getSelectedItem() != null) {
				String fileName = pathTF.getText();
				boolean isHeaderReq = headerCBX.getSelectionModel().getSelectedItem() == null ? Boolean.FALSE
						: stringToBoolean(headerCBX.getSelectionModel().getSelectedItem());
				String isHedeaderReqTxt = booleanToString(isHeaderReq);
				String headerText = headerTxtTF.getText();
				String type = optionsBox.getSelectionModel().getSelectedItem();

				members.add(constructTableMemberBoundary(type, fileName, isHedeaderReqTxt, headerText));
				// fire the table away with the changes.
				tableView.fireEvent(e);

				// hide the popup
				alert.hideWithAnimation();
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
			if (e.getCode().ordinal() == KeyCode.ENTER.ordinal()) {
				okBtn.fire();
			}
		});

		cancelBtn.setOnKeyPressed(e -> {
			if (e.getCode().ordinal() == KeyCode.ENTER.ordinal()) {
				cancelBtn.fire();
			}
		});

		JFXButton searchBtn = new JFXButton("Search Option");
		searchBtn.setOnAction(e -> {
			try {
				// run the dialog only after a valid selection is made.
				if (optionsBox.getSelectionModel().getSelectedItem() != null)
					buildSearchOptionDialog(optionsBox.getSelectionModel().getSelectedItem(), pathTF, headerTxtTF);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});

		layout.setActions(searchBtn, okBtn, cancelBtn);
		alert.setContent(layout);
		alert.show();

	}

	private void buildSearchOptionDialog(String typeSelected, JFXTextField pathTF, JFXTextField headerTF)
			throws IOException {

		ObservableList<SearchOptionTableBoundary> searchOptionMembers = FXCollections.observableArrayList();

		if (typeSelected.equals(DisplayBoardConstants.dashboard.name())) {
			// just refer that the search is not applicable to it . it just needs to be
			// added lyk wise.

			popOutAlert("The Searh Option is Not Applicable to dashboard type. \n "
					+ "Just select this option and add this to the table , the application will run the dashboard items in sequence added. ",
					"App Sequencer", AlertType.INFORMATION);

		} else if (typeSelected.equals(DisplayBoardConstants.chart.name())) {
			Properties chartManagerProps = new Properties();
			String type = DisplayBoardConstants.chart.name();
			String propsPath = FilesUtil.PROPS_DIR_PATH;
			// load the properties from the manager file
			chartManagerProps.load(FileUtils.openInputStream(new File(FilesUtil.MANAGER_PROPS_PATH)));

			// loop on the properties applicable.
			chartManagerProps.forEach((key, value) -> {
				searchOptionMembers.add(
						constructSearchOptionTabBoundary(type, propsPath, String.valueOf(key), String.valueOf(value)));

				// call the dialog with table view.
				displaySearchOptionTableDialog(buildSearchOptionTableView(searchOptionMembers), pathTF, headerTF);

				// disable the pathTf as well so no manual edits are done.
				pathTF.setDisable(true);
			});
		} else if (typeSelected.equals(DisplayBoardConstants.image.name())) {
			// just display all the entries in the image folder that are available
			String type = DisplayBoardConstants.chart.name();
			String propsPath = FilesUtil.IMAGES_DIR_PATH;
			// String[] imageFormats = { "JPEG", "PNG", "BMP", "WBMP", "GIF" };
			// a predicate with suppoted image formats for filtering.
			Predicate<File> filePredicate = e -> {
				String fileName = e.getName().toLowerCase();
				return (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png")
						|| fileName.endsWith(".bmp") || fileName.endsWith(".wbmp") || fileName.endsWith(".gif"));
			};
			List<File> filesList = new ArrayList<>();
			File imagesDir = new File(FilesUtil.IMAGES_DIR_PATH);
			// juzz to make sure it is a directory --> obvio its a directory
			if (imagesDir.isDirectory()) {
				File[] files = imagesDir.listFiles();
				filesList = Arrays.stream(files).filter(filePredicate).collect(Collectors.toList());
				filesList.stream().forEach(f -> {
					searchOptionMembers
							.add(constructSearchOptionTabBoundary(type, propsPath, f.getName(), "To Be Added"));
				});

				// call the dialog with table view.
				displaySearchOptionTableDialog(buildSearchOptionTableView(searchOptionMembers), pathTF, headerTF);

				// don't give the user an option to edit name here .
				pathTF.setDisable(true);
			}
		} else if (typeSelected.equals(DisplayBoardConstants.customer.name())) {
			// this is yet to be implemented ..Coding in Process.
			popOutAlert("Improvement In Progress .For Now this Feature is Not Available", "MPS Charts",
					AlertType.INFORMATION);
		}

	}

	private void displaySearchOptionTableDialog(VBox tableBox, JFXTextField pathTF, JFXTextField headerTF) {

		JFXAlert<String> alert = new JFXAlert<>();
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.setOverlayClose(false);
		JFXDialogLayout layout = new JFXDialogLayout();
		layout.setHeading(new Text("Search Option View"));
		layout.setMinSize(800, 800);

		// add the table view to the body.
		layout.setBody(tableBox);

		JFXButton okBtn = new JFXButton("Okay!!");
		okBtn.getStyleClass().add("dialog-accept");

		okBtn.setOnAction(e -> {
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
			if (e.getCode().ordinal() == KeyCode.ENTER.ordinal()) {
				okBtn.fire();
			}
		});

		cancelBtn.setOnKeyPressed(e -> {
			if (e.getCode().ordinal() == KeyCode.ENTER.ordinal()) {
				okBtn.fire();
			}
		});

		JFXButton addSelectedBtn = new JFXButton("Add Selected Item");
		addSelectedBtn.setOnAction(e -> {
			if (searchOptionTabView.getSelectionModel().getSelectedItem() != null) {
				pathTF.setText(searchOptionTabView.getSelectionModel().getSelectedItem().getValue().fileName.get());
				headerTF.setText(searchOptionTabView.getSelectionModel().getSelectedItem().getValue().headerText.get());
				alert.hideWithAnimation();
			}
		});

		// for now ok button does the same as addselection
		// TODO in future remove this or make some other use of it.
		okBtn.setOnAction(e -> {
			addSelectedBtn.fire();
		});

		layout.setActions(addSelectedBtn, okBtn, cancelBtn);
		alert.setContent(layout);
		alert.show();
	}

	private VBox buildSearchOptionTableView(ObservableList<SearchOptionTableBoundary> searchOptionMembers) {

		VBox searchVBox = new VBox(10);
		// initialize the table view for options show dialog

		// build tree
		TreeItem<SearchOptionTableBoundary> searchOptionRoot = new RecursiveTreeItem<>(searchOptionMembers,
				RecursiveTreeObject::getChildren);

		searchOptionTabView = new JFXTreeTableView<>(searchOptionRoot);

		// prior settings
		searchOptionTabView.setMinSize(600, 600);
		searchOptionTabView.setPrefSize(600, 600);
		searchOptionTabView.setShowRoot(false);
		searchOptionTabView.setEditable(true);
		searchOptionTabView.setPadding(new Insets(15));

		JFXTreeTableColumn<SearchOptionTableBoundary, String> componentType = new JFXTreeTableColumn<>("Type");
		componentType.setPrefWidth(50);
		componentType
				.setCellValueFactory((TreeTableColumn.CellDataFeatures<SearchOptionTableBoundary, String> param) -> {
					if (componentType.validateValue(param)) {
						return param.getValue().getValue().getType();
					} else {
						return componentType.getComputedValue(param);
					}
				});

		JFXTreeTableColumn<SearchOptionTableBoundary, String> componentRefPath = new JFXTreeTableColumn<>("File Name");
		componentRefPath.setPrefWidth(150);
		componentRefPath
				.setCellValueFactory((TreeTableColumn.CellDataFeatures<SearchOptionTableBoundary, String> param) -> {
					if (componentRefPath.validateValue(param)) {
						return param.getValue().getValue().getFileName();
					} else {
						return componentRefPath.getComputedValue(param);
					}
				});

		JFXTreeTableColumn<SearchOptionTableBoundary, String> isHdrApplicable = new JFXTreeTableColumn<>("Folder Name");
		isHdrApplicable.setPrefWidth(250);
		isHdrApplicable
				.setCellValueFactory((TreeTableColumn.CellDataFeatures<SearchOptionTableBoundary, String> param) -> {
					if (isHdrApplicable.validateValue(param)) {
						return param.getValue().getValue().getFolderName();
					} else {
						return isHdrApplicable.getComputedValue(param);
					}
				});

		JFXTreeTableColumn<SearchOptionTableBoundary, String> hdrTxt = new JFXTreeTableColumn<>(
				"Header Text If Applicable");
		hdrTxt.setPrefWidth(250);
		hdrTxt.setCellValueFactory((TreeTableColumn.CellDataFeatures<SearchOptionTableBoundary, String> param) -> {
			if (hdrTxt.validateValue(param)) {
				return param.getValue().getValue().getHeaderText();
			} else {
				return hdrTxt.getComputedValue(param);
			}
		});

		// allow single row selection here for the table as we need to capture them in
		// the dialog for further analysis.
		searchOptionTabView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		// set colums to table
		searchOptionTabView.getColumns().addAll(componentType, componentRefPath, isHdrApplicable, hdrTxt);

		HBox box = new HBox(20);
		Label sizeLbl = new Label("Table Size: ");
		Label size = new Label();
		JFXTextField searchTF = new JFXTextField();
		searchTF.setPromptText("Search Text Here!!!");
		size.textProperty()
				.bind(Bindings.createStringBinding(() -> String.valueOf(searchOptionTabView.getCurrentItemsCount()),
						searchOptionTabView.currentItemsCountProperty()));

		searchTF.textProperty().addListener((o, oldVal, newVal) -> {
			searchOptionTabView.setPredicate(userProp -> {
				final SearchOptionTableBoundary user = userProp.getValue();
				return user.type.get().toLowerCase().contains(newVal.toLowerCase())
						|| user.fileName.get().contains(newVal) || user.folderName.get().contains(newVal)
						|| user.headerText.get().contains(newVal);
			});
		});
		searchTF.setAlignment(Pos.CENTER_LEFT);
		searchTF.setMinSize(450, 15);
		box.getChildren().addAll(searchTF, sizeLbl, size);
		box.setPrefSize(600, 20);
		box.setPadding(new Insets(15));

		// add the components to search box
		searchVBox.getChildren().addAll(box, searchOptionTabView);

		return searchVBox;
	}

	private void addTableDragDropListener() {

		tableView.setRowFactory(tv -> {
			TreeTableRow<RunJSonTableBoundary> row = new TreeTableRow<RunJSonTableBoundary>();

			row.setOnDragDetected(event -> {
				if (!row.isEmpty()) {
					Integer index = row.getIndex();
					Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
					db.setDragView(row.snapshot(null, null));
					ClipboardContent cc = new ClipboardContent();
					cc.put(SERIALIZED_MIME_TYPE, index);
					db.setContent(cc);
					event.consume();
				}
			});

			row.setOnDragOver(event -> {
				Dragboard db = event.getDragboard();
				if (db.hasContent(SERIALIZED_MIME_TYPE)) {
					if (row.getIndex() != ((Integer) db.getContent(SERIALIZED_MIME_TYPE)).intValue()) {
						event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
						event.consume();
					}
				}
			});

			row.setOnDragDropped(event -> {
				Dragboard db = event.getDragboard();
				if (db.hasContent(SERIALIZED_MIME_TYPE)) {
					int draggedIndex = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
					RunJSonTableBoundary draggedPerson = members.remove(draggedIndex);

					int dropIndex;

					if (row.isEmpty()) {
						dropIndex = members.size();
					} else {
						dropIndex = row.getIndex();
					}

					members.add(dropIndex, draggedPerson);

					event.setDropCompleted(true);
					tableView.getSelectionModel().select(dropIndex);
					// fire the table change
					tableView.fireEvent(event);
					event.consume();
				}
			});

			return row;
		});

	}

	private HBox initTableView() {

		JFXTreeTableColumn<RunJSonTableBoundary, String> componentType = new JFXTreeTableColumn<>("Type");
		componentType.setPrefWidth(200);
		componentType.setCellValueFactory((TreeTableColumn.CellDataFeatures<RunJSonTableBoundary, String> param) -> {
			if (componentType.validateValue(param)) {
				return param.getValue().getValue().getType();
			} else {
				return componentType.getComputedValue(param);
			}
		});

		JFXTreeTableColumn<RunJSonTableBoundary, String> componentRefPath = new JFXTreeTableColumn<>("Path");
		componentRefPath.setPrefWidth(300);
		componentRefPath.setCellValueFactory((TreeTableColumn.CellDataFeatures<RunJSonTableBoundary, String> param) -> {
			if (componentRefPath.validateValue(param)) {
				return param.getValue().getValue().getPath();
			} else {
				return componentRefPath.getComputedValue(param);
			}
		});

		JFXTreeTableColumn<RunJSonTableBoundary, String> isHdrApplicable = new JFXTreeTableColumn<>(
				"Is Header Applicable");
		isHdrApplicable.setPrefWidth(350);
		isHdrApplicable.setCellValueFactory((TreeTableColumn.CellDataFeatures<RunJSonTableBoundary, String> param) -> {
			if (isHdrApplicable.validateValue(param)) {
				return param.getValue().getValue().getIsHeaderApplicable();
			} else {
				return isHdrApplicable.getComputedValue(param);
			}
		});

		JFXTreeTableColumn<RunJSonTableBoundary, String> hdrTxt = new JFXTreeTableColumn<>("Header Text");
		hdrTxt.setPrefWidth(450);
		hdrTxt.setCellValueFactory((TreeTableColumn.CellDataFeatures<RunJSonTableBoundary, String> param) -> {
			if (hdrTxt.validateValue(param)) {
				return param.getValue().getValue().getDisplayTxt();
			} else {
				return hdrTxt.getComputedValue(param);
			}
		});

		// set colums to table
		tableView.getColumns().addAll(componentType, componentRefPath, isHdrApplicable, hdrTxt);

		HBox box = new HBox(20);
		Label sizeLbl = new Label("Table Size: ");
		Label size = new Label();
		JFXTextField searchTF = new JFXTextField();
		searchTF.setPromptText("Search Text Here!!!");
		size.textProperty().bind(Bindings.createStringBinding(() -> String.valueOf(tableView.getCurrentItemsCount()),
				tableView.currentItemsCountProperty()));

		searchTF.textProperty().addListener((o, oldVal, newVal) -> {
			tableView.setPredicate(userProp -> {
				final RunJSonTableBoundary user = userProp.getValue();
				return user.type.get().toLowerCase().contains(newVal.toLowerCase()) || user.path.get().contains(newVal)
						|| user.isHeaderApplicable.get().contains(newVal) || user.displayTxt.get().contains(newVal);
			});
		});
		searchTF.setAlignment(Pos.CENTER_LEFT);
		searchTF.setMinSize(450, 15);
		box.getChildren().addAll(searchTF, sizeLbl, size);
		box.setPrefSize(600, 20);
		box.setPadding(new Insets(15));

		return box;
	}

	private RunJSonTableBoundary constructTableMemberBoundary(String type, String fileName, String isHeaderApplicable,
			String headerTxt) {
		RunJSonTableBoundary m = new RunJSonTableBoundary();
		// instantiate fields
		m.setType(new SimpleStringProperty());
		m.setPath(new SimpleStringProperty());
		m.setIsHeaderApplicable(new SimpleStringProperty());
		m.setDisplayTxt(new SimpleStringProperty());

		m.getType().set(type);
		m.getPath().set(fileName);
		m.getIsHeaderApplicable().set(isHeaderApplicable);
		m.getDisplayTxt().set(headerTxt);

		return m;
	}

	private SearchOptionTableBoundary constructSearchOptionTabBoundary(String type, String folderName, String fileName,
			String headerTxt) {
		SearchOptionTableBoundary m = new SearchOptionTableBoundary();
		// instantiate fields
		m.setType(new SimpleStringProperty());
		m.setFolderName(new SimpleStringProperty());
		m.setFileName(new SimpleStringProperty());
		m.setHeaderText(new SimpleStringProperty());

		m.getType().set(type);
		m.getFolderName().set(folderName);
		m.getFileName().set(fileName);
		m.getHeaderText().set(headerTxt);

		return m;
	}

	@Getter
	@Setter
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public static class RunJSonTableBoundary extends RecursiveTreeObject<RunJSonTableBoundary> {

		StringProperty type;
		StringProperty path;
		StringProperty isHeaderApplicable;
		StringProperty displayTxt;
		// TODO add score getters and calendar events.
	}

	@Getter
	@Setter
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public static class SearchOptionTableBoundary extends RecursiveTreeObject<SearchOptionTableBoundary> {

		StringProperty type;
		StringProperty folderName;
		StringProperty fileName;
		StringProperty headerText;
		// TODO add score getters and calendar events.
	}

	private static boolean stringToBoolean(String s) {
		return s == null ? Boolean.FALSE : s.length() > 0 ? s.equalsIgnoreCase("Y") : Boolean.FALSE;
	}

	private static String booleanToString(boolean b) {
		return b ? "Y" : "N";
	}

	private Alert popOutAlert(String text, String title, AlertType alertType) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setContentText(text);
		alert.showAndWait();

		return alert;
	}

}
