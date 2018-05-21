/**
 * 
 */
package com.app.chart.run.ui;

import java.io.File;
import java.util.List;

import com.app.chart.fx.FilesUtil;
import com.app.chart.model.PerfomanceBoardBoundary;
import com.app.chart.model.RunJsonBoundary;
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
import javafx.scene.control.Label;
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

	private ObservableList<RunJSonTableBoundary> members = FXCollections
			.observableArrayList(/* constructTestBoundary() */);
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

		// add all the buttons to box
		box.getChildren().addAll(addEntry, editEntry, deleteRow, saveBtn);
		return box;
	}

	private void addEntryAction(ActionEvent e) {

	}

	private void editEntryAction(ActionEvent e) {

	}

	private void deleteRowEntryAction(ActionEvent e) {

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
		layout.setMinSize(500, 200);

		HBox box = new HBox(10);
		JFXNumberField pathTF = new JFXNumberField();
		pathTF.setPromptText("Path");
		JFXNumberField headerTxt = new JFXNumberField();
		headerTxt.setPromptText("Enter Header Txt");

		box.getChildren().addAll(optionsBox, pathTF, headerCBX, headerTxt);
		layout.setBody(box);

		JFXButton okBtn = new JFXButton("OK");
		okBtn.getStyleClass().add("dialog-accept");

		optionsBox.setOnAction(e -> {
			if (optionsBox.getSelectionModel().getSelectedItem().equals(DisplayBoardConstants.dashboard.name())) {
				pathTF.setText(FilesUtil.DASHBOARD_CONTENT_DATA_FILE);
				pathTF.setDisable(true);
				headerCBX.getSelectionModel().select(0);
				headerCBX.setDisable(true);
				headerTxt.setDisable(true);
			} else if (optionsBox.getSelectionModel().getSelectedItem().equals(DisplayBoardConstants.chart.name())) {
				// adding validator for all these fields
				DashboardUtil.buildRequestValidator(pathTF, headerTxt);
				headerCBX.getSelectionModel().select(0);
				headerCBX.setDisable(true);
			} else if (optionsBox.getSelectionModel().getSelectedItem().equals(DisplayBoardConstants.image.name())) {
				DashboardUtil.buildRequestValidator(pathTF, headerTxt);
			} else if (optionsBox.getSelectionModel().getSelectedItem().equals(DisplayBoardConstants.customer.name())) {
				DashboardUtil.buildRequestValidator(pathTF, headerTxt);
			}
		});

		okBtn.setOnAction(e -> {
		} /*
			 * onSprintDatAddOk(teamName, alert, totalPoints, currentPoints, backlogPoints)
			 */);

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

		JFXButton searchBtn = new JFXButton("Search Option");

		layout.setActions(searchBtn, okBtn, cancelBtn);
		alert.setContent(layout);
		alert.show();

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

	private RunJSonTableBoundary constructTableMemberBoundary(String probleWith, String problemCause,
			String probelmDesc, String probelmSoln) {
		RunJSonTableBoundary m = new RunJSonTableBoundary();
		// instantiate fields
		m.setType(new SimpleStringProperty());
		m.setPath(new SimpleStringProperty());
		m.setIsHeaderApplicable(new SimpleStringProperty());
		m.setDisplayTxt(new SimpleStringProperty());

		m.getType().set(probleWith);
		m.getPath().set(problemCause);
		m.getIsHeaderApplicable().set(probelmDesc);
		m.getDisplayTxt().set(probelmSoln);

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

	private static boolean stringToBoolean(String s) {
		return s == null ? Boolean.FALSE : s.length() > 0 ? s.equalsIgnoreCase("Y") : Boolean.FALSE;
	}

	private static String booleanToString(boolean b) {
		return b ? "Y" : "N";
	}

}
