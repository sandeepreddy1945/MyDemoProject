/**
 * 
 */
package com.app.chart.fx.tree;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.io.FileUtils;

import com.app.chart.fx.AddressBook;
import com.app.chart.fx.FilesUtil;
import com.app.chart.model.EmployeeDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.skins.JFXComboBoxListViewSkin;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeItem.TreeModificationEvent;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * @author Sandeep
 * @param <T>
 *
 */
public class OrgTreeView<T> extends Application {

	private List<EmployeeDetails> employeeList;

	private TreeView<EmployeeDetails> treeView = new TreeView<EmployeeDetails>();

	private Map<String, List<EmployeeDetails>> jsMap = new LinkedHashMap<String, List<EmployeeDetails>>();

	private WebView view = null;
	private WebEngine webEngine = null;

	private JFXComboBox<EmployeeDetails> comboBox;

	private JFXTextField itemTF;

	public static final String HEADER_MEMBER = "1";

	private final File appDir;

	private final Stage stage;

	private Random random = new Random();

	public OrgTreeView(List<EmployeeDetails> employeeList, Stage stage, File appDir) {
		this.employeeList = employeeList;
		comboBox = new JFXComboBox<>();
		comboBox.getItems().addAll(employeeList);
		this.appDir = appDir;
		this.stage = stage;
		// comboBox.setCellFactory(c -> addCellFcatoryForComboBox());
		initUI(stage);
	}

	public OrgTreeView(List<EmployeeDetails> employeeList, Dialog dialog, File appDir) {
		this.employeeList = employeeList;
		comboBox = new JFXComboBox<>();
		comboBox.getItems().addAll(employeeList);
		this.appDir = appDir;

		Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();

		this.stage = stage;

		// set onclose request for the dialog.
		Window window = dialog.getDialogPane().getScene().getWindow();
		window.setOnCloseRequest(event -> window.hide());
		// comboBox.setCellFactory(c -> addCellFcatoryForComboBox());
		initUI(stage);

	}

	/*
	 * private ListCell<EmployeeDetails> addCellFcatoryForComboBox() { final
	 * ListCell<EmployeeDetails> cell = new ListCell<EmployeeDetails>() {
	 * 
	 * @Override protected void updateItem(EmployeeDetails t, boolean bln) {
	 * super.updateItem(t, bln);
	 * 
	 * if (t != null) { setText(t.getName() + " - " + t.getPortalId()); } else {
	 * setText(null); } }
	 * 
	 * };
	 * 
	 * return cell; }
	 */

	@Override
	public void start(Stage stage) throws Exception {

		initUI(stage);

	}

	private void initUI(Stage stage) {
		treeView.setEditable(true);
		// treeView.setCellFactory(TextFieldTreeCell.forTreeView());
		// Select the root node
		treeView.getSelectionModel().selectFirst();
		// Create the root node and adds event handler to it
		// name - portal id
		EmployeeDetails headerEmployee = fetchHeaderEmployee();
		TreeItem<EmployeeDetails> rootItem = new TreeItem<>(headerEmployee);
		treeView.setRoot(rootItem);

		// add the header element to map with empty list under him.
		jsMap.put(headerEmployee.getPortalId(), new ArrayList<>());

		treeView.setOnEditStart(e -> {

		});

		treeView.setOnEditCommit(e -> {

		});

		treeView.setOnEditCancel(e -> {

		});

		// Set tree modification related event handlers (branchExpandedEvent)
		rootItem.addEventHandler(TreeItem.<String>branchExpandedEvent(),
				new EventHandler<TreeItem.TreeModificationEvent<String>>() {
					@Override
					public void handle(TreeModificationEvent<String> event) {

					}
				});

		// Set tree modification related event handlers (branchCollapsedEvent)
		rootItem.addEventHandler(TreeItem.<String>branchCollapsedEvent(),
				new EventHandler<TreeItem.TreeModificationEvent<String>>() {
					@Override
					public void handle(TreeModificationEvent<String> event) {

					}
				});

		// Set tree modification related event handlers (childrenModificationEvent)
		rootItem.addEventHandler(TreeItem.<String>childrenModificationEvent(),
				new EventHandler<TreeItem.TreeModificationEvent<String>>() {
					@Override
					public void handle(TreeModificationEvent<String> event) {

					}
				});

		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		stage.setX(primaryScreenBounds.getMinX());
		stage.setY(primaryScreenBounds.getMinY());
		stage.setWidth(primaryScreenBounds.getWidth());
		stage.setHeight(primaryScreenBounds.getHeight());

		// Create the VBox
		VBox rightPane = getRightPane(primaryScreenBounds);

		treeView.setMinWidth(400);
		// Create the HBox
		HBox root = new HBox();
		// Set the horizontal space between each child in the HBox
		root.setSpacing(20);
		// Add the TreeView to the HBox
		root.getChildren().addAll(treeView, rightPane);

		// Create the Scene
		Scene scene = new Scene(root, stage.getWidth(), stage.getHeight());
		// Add the Scene to the Stage
		stage.setScene(scene);
		// Set the Title for the Scene
		stage.setTitle("Org Chart Tree Editor");
		// Display the stage using the shwo method on it.
		// stage.show();
	}

	private VBox getRightPane(Rectangle2D primaryScreenBounds) {

		JFXButton addItemBtn = new JFXButton("Add Item To Tree");
		JFXButton addPseudoBtn = new JFXButton("Add Pseudo Node");
		JFXButton removeItemBtn = new JFXButton("Remove Selected Item");
		JFXButton refreshWebView = new JFXButton("Refresh Chart");
		JFXButton saveBtn = new JFXButton("Save");
		comboBox.setSkin(new JFXComboBoxListViewSkin<>(comboBox));
		comboBox.setPromptText("Select Item From List To Add");
		itemTF = new JFXTextField();
		itemTF.setPromptText("Select Item From ComboBox");
		view = new WebView();
		webEngine = view.getEngine();

		// set size preferences for each element.
		itemTF.setMinWidth(350);
		comboBox.setMinWidth(350);

		// add a new vertical box
		VBox mainBox = new VBox(15);

		// editabel panel cols here
		HBox editableBox = new HBox(15);
		editableBox.setMinSize(primaryScreenBounds.getWidth() - 450, 50);
		editableBox.getChildren().addAll(itemTF, addItemBtn, addPseudoBtn);
		editableBox.setPadding(new Insets(20, 0, 0, 0));

		HBox editableBox1 = new HBox(15);
		editableBox1.setMinSize(primaryScreenBounds.getWidth() - 450, 50);
		editableBox1.getChildren().addAll(comboBox, removeItemBtn, refreshWebView);

		view.setMinSize(primaryScreenBounds.getWidth() - 450, primaryScreenBounds.getHeight() - 250);

		HBox bottomBox = new HBox(15);
		// bottomBox.setMinSize(800, 50);
		bottomBox.getChildren().add(saveBtn);
		bottomBox.setAlignment(Pos.BOTTOM_RIGHT);
		saveBtn.setMinWidth(200);

		// event handlers
		addItemBtn.setOnAction(this::addItem);

		addPseudoBtn.setOnAction(this::addPseudoItem);

		removeItemBtn.setOnAction(this::removeItem);

		refreshWebView.setOnAction(this::refreshWebView);

		saveBtn.setOnAction(this::doSaveAction);

		comboBox.setOnAction(e -> {
			itemTF.setText(comboBox.getSelectionModel().getSelectedItem().getName() + " - "
					+ comboBox.getSelectionModel().getSelectedItem().getPortalId());
		});

		// add all the components here.
		mainBox.getChildren().addAll(editableBox, editableBox1, view, bottomBox);

		return mainBox;
	}

	private void addItem(ActionEvent e) {
		EmployeeDetails selectedItem = comboBox.getSelectionModel().getSelectedItem();
		if (comboBox.getSelectionModel().getSelectedIndex() != -1 && selectedItem != null) {
			TreeItem<EmployeeDetails> parent = treeView.getSelectionModel().getSelectedItem();
			boolean isDuplicateData = false;
			if (parent == null) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Org Tree Editor");
				alert.setContentText("Please Select a Parent To Add the Employee");
				alert.showAndWait();
			} else {
				// check for duplicates
				for (TreeItem<EmployeeDetails> ti : parent.getChildren()) {
					// check if child already has the same child
					if (ti.getValue().getPortalId().equals(selectedItem.getPortalId())) {
						isDuplicateData = true;
						break;
					}
				}
				// check if the same parent element is added again
				if (selectedItem.getPortalId().equals(parent.getValue().getPortalId())) {
					isDuplicateData = true;
				}

				// check if element is already in existing in the tree
				// double looping on all the elements available.
				for (TreeItem<EmployeeDetails> ti : treeView.getRoot().getChildren()) {
					for (TreeItem<EmployeeDetails> tic : ti.getChildren()) {
						if (selectedItem.getPortalId().equals(tic.getValue().getPortalId())) {
							isDuplicateData = true;
						}
					}
				}

				if (isDuplicateData) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Org Tree Editor");
					alert.setContentText("Employee Your Trying to Add Already Exists On The Tree.");
					alert.showAndWait();
				} else {
					TreeItem<EmployeeDetails> ti = new TreeItem<EmployeeDetails>(selectedItem);
					parent.getChildren().add(ti);

					// check if the parent is present and set him a list if not
					if (jsMap.containsKey(parent.getValue().getPortalId())) {
						if (jsMap.get(parent.getValue().getPortalId()) != null) {
							jsMap.get(parent.getValue().getPortalId()).add(selectedItem);
						} else {
							jsMap.put(parent.getValue().getPortalId(), new ArrayList<>());
						}
					} else {
						jsMap.put(parent.getValue().getPortalId(), new ArrayList<>());
					}

					itemTF.clear();
					if (!parent.isExpanded()) {
						parent.setExpanded(true);
					}
				}
			}
		}
	}

	private void addPseudoItem(ActionEvent e) {
		TreeItem<EmployeeDetails> parent = treeView.getSelectionModel().getSelectedItem();
		if (parent == null) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Org Tree Editor");
			alert.setContentText("Please Select a Parent To Add the Employee");
			alert.showAndWait();
		} else {
			// add a Pseudo Node here
			EmployeeDetails details = new EmployeeDetails();
			details.setPortalId(String.valueOf(random.nextInt(999999)));
			details.setName("Pseudo *");
			details.setPseudo(true);
			// check if the parent is present and set him a list if not
			if (jsMap.containsKey(parent.getValue().getPortalId())) {
				if (jsMap.get(parent.getValue().getPortalId()) != null) {
					jsMap.get(parent.getValue().getPortalId()).add(details);
				} else {
					jsMap.put(parent.getValue().getPortalId(), new ArrayList<>());
				}
			} else {
				jsMap.put(parent.getValue().getPortalId(), new ArrayList<>());
			}
			parent.getChildren().add(new TreeItem<EmployeeDetails>(details));
		}

	}

	private void removeItem(ActionEvent e) {
		TreeItem<EmployeeDetails> item = treeView.getSelectionModel().getSelectedItem();
		if (item == null) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Org Tree Editor");
			alert.setContentText("Please Select a Parent To Add the Employee");
			alert.showAndWait();
		} else {
			TreeItem<EmployeeDetails> mainParent = item.getParent();
			if (mainParent == null) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Org Tree Editor");
				alert.setContentText("Cannot Remove Root Element");
				alert.showAndWait();
			} else {
				mainParent.getChildren().remove(item);
			}
		}

	}

	private void refreshWebView(ActionEvent e) {
		webEngine.reload();

	}

	private void doSaveAction(ActionEvent e) {
		// write the map to preview.json file
		ObjectMapper mapper = new ObjectMapper();
		try {
			FileUtils.write(new File(appDir.getAbsolutePath() + FilesUtil.SLASH + AddressBook.PREVIEW_JSON),
					mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsMap), Charset.defaultCharset(), false);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// next write the things to app.json file.
	}

	private EmployeeDetails fetchHeaderEmployee() {
		return employeeList.stream().filter(e -> e.getParent().equals(HEADER_MEMBER)).findFirst().get();
	}

	/**
	 * Used to display the Dialog / Stage on the main.
	 */
	public void show() {
		stage.show();
	}

	/**
	 * Just To Test TODO to remove in mere future
	 * 
	 * @param args
	 */
	static public void main(String[] args) {

		launch(args);
	}

}
