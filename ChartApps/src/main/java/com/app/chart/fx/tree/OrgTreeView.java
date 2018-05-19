/**
 * 
 */
package com.app.chart.fx.tree;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

import com.app.chart.fx.AddressBook;
import com.app.chart.fx.BuildJavaScript;
import com.app.chart.fx.FilesUtil;
import com.app.chart.model.EmployeeDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeView;
import com.jfoenix.controls.JFXTreeViewPath;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeItem.TreeModificationEvent;
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
 *            For Parent Node escape "" literals as the javascript cannot find
 *            the parent if it is in quotes .<br>
 *            So skip the quotes by just pasing the template without quotes
 *            .<br>
 *            This nature to skip quotes is applicable only to the parent
 *            element in all aspects as per the java script conditionality .<br>
 */
public class OrgTreeView<T> extends Application {

	/**
	 * Re-Load Page using the window object on the javascript to reload the page.
	 */
	public static final String WINDOW_LOCATION_RELOAD = " window.location.reload();";

	public static final String RELOAD_JS_SCRIPT = " function reloadPage() {\r\n" + "    window.location.reload();\r\n"
			+ "}";

	public static final String RELOAD_PAGE = "reloadPage()";

	private List<EmployeeDetails> employeeList;

	private JFXTreeView<EmployeeDetails> treeView = new JFXTreeView<EmployeeDetails>();

	private List<EmployeeDetails> previewList = new LinkedList<EmployeeDetails>();

	private boolean isTemphtmlLoaded = false;

	private WebView view = null;
	private WebEngine webEngine = null;

	private JFXComboBox<EmployeeDetails> comboBox;

	private JFXTextField itemTF;

	public static final String HEADER_MEMBER = "1";

	private static final String COMMA = ", \n";

	private final File appDir;

	private final Stage stage;

	private Random random = new Random();

	public OrgTreeView(List<EmployeeDetails> employeeList, Stage stage, File appDir) {
		this.employeeList = employeeList;
		comboBox = new JFXComboBox<>();
		// remove header from the list and the add the list
		comboBox.getItems().addAll(
				employeeList.stream().filter(e -> !e.getParent().equals(HEADER_MEMBER)).collect(Collectors.toList()));
		this.appDir = appDir;
		this.stage = stage;
		// comboBox.setCellFactory(c -> addCellFcatoryForComboBox());
		initUI(stage);
	}

	public OrgTreeView(List<EmployeeDetails> employeeList, Dialog dialog, File appDir) {
		this.employeeList = employeeList;
		comboBox = new JFXComboBox<>();
		comboBox.getItems().addAll(
				employeeList.stream().filter(e -> !e.getParent().equals(HEADER_MEMBER)).collect(Collectors.toList()));
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
		treeView.setEditable(true);

		// add the header element to list as he is the first element of the tree.
		previewList.add(headerEmployee);

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
		// set the javascript mode enabled to true in order to js function.
		webEngine.setJavaScriptEnabled(true);
		webEngine.setUserDataDirectory(FileUtils.getTempDirectory());

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
		mainBox.getChildren().addAll(editableBox, editableBox1, new JFXTreeViewPath(treeView), view, bottomBox);

		return mainBox;
	}

	private void addItem(ActionEvent e) {
		isItemPresent = false;
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
				// loop and completey check if item is already present.
				isDuplicateData = checkIfItemAlreadyPresentInTree(selectedItem, treeView.getRoot().getChildren()
						.toArray(new TreeItem[treeView.getRoot().getChildren().size()]));
				// check if the same parent element is added again
				if (selectedItem.getPortalId().equals(parent.getValue().getPortalId())) {
					isDuplicateData = true;
				}

				if (isDuplicateData) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Org Tree Editor");
					alert.setContentText("Employee Your Trying to Add Already Exists On The Tree.");
					alert.showAndWait();
				} else {
					TreeItem<EmployeeDetails> ti = new TreeItem<EmployeeDetails>(selectedItem);
					parent.getChildren().add(ti);

					itemTF.clear();
					if (!parent.isExpanded()) {
						parent.setExpanded(true);
					}
				}
			}
		}
	}

	private void addPseudoItem(ActionEvent e) {
		isItemPresent = false;
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
			details.setName("Pseudo");
			details.setPseudo(true);
			// check if the parent is present and set him a list if not
			parent.getChildren().add(new TreeItem<EmployeeDetails>(details));

			if (!parent.isExpanded()) {
				parent.setExpanded(true);
			}
		}

	}

	private void removeItem(ActionEvent e) {
		isItemPresent = false;
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

	/**
	 * There seems to be a JDK-8193044 bug from oracle .<br>
	 * So this doesnot allow the page refresh to work appropriately .<br>
	 * No work around for now always relaod the page manually by adding elements
	 * manually.<br>
	 * 
	 * @param e
	 */
	private void refreshWebView(ActionEvent e) {
		isItemPresent = false;
		// empty string to skip possible null pointers.
		String fileContent = "";
		try {
			fileContent = FileUtils.readFileToString(
					new File(appDir.getAbsolutePath() + FilesUtil.SLASH + AddressBook.TEMP_HTML),
					Charset.defaultCharset());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (fileContent.length() > 0) {

			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Org Tree Editor");
			alert.setContentText(
					"This would perform an Auto Save Operation . \n " + "Press OK to Auto Save and Continue. ");
			alert.showAndWait();
			if (alert.getResult() == ButtonType.OK) {
				// do an auto save action
				doSaveAction(e);
				if (isTemphtmlLoaded) {
					// reload the browser using the js function reloadPage.
					// webEngine.loadContent(fileContent);
					// webEngine.reload();
					webEngine.executeScript(WINDOW_LOCATION_RELOAD);
				} else {
					try {
						webEngine.load(new File(appDir.getAbsolutePath() + FilesUtil.SLASH + AddressBook.TEMP_HTML)
								.toURI().toURL().toExternalForm());
					} catch (MalformedURLException e1) {
						e1.printStackTrace();
					}
					isTemphtmlLoaded = true;
				}
			}

		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Org Tree Editor");
			alert.setContentText("Plese Add Organizational Tree in order to Preview / Refresh it.");
			alert.showAndWait();
		}
	}

	private void doSaveAction(ActionEvent e) {
		// set the class variable to false for refresh.
		isItemPresent = false;
		// write the map to preview.json file
		ObjectMapper mapper = new ObjectMapper();

		// clear the preview list if it had any elements in previous or so for safety.
		previewList.clear();

		// add the header item to the preview list
		previewList.add(treeView.getRoot().getValue());
		try {

			// TODO next write the things to app.js and temp.js file.
			// either use the treeview as it contains the defaut things or use the jsMap in
			// order to achieve the order.
			int stepCount = 0;
			// builder for individual chart mapping
			StringBuilder sb = new StringBuilder();
			// config builder for individual elements added.
			StringBuilder configOrderBuilder = new StringBuilder();
			// append first config as the letter as per procedure.
			configOrderBuilder.append("config");
			configOrderBuilder.append(COMMA);

			BuildJavaScript bjs = new BuildJavaScript(employeeList);
			sb.append(bjs.buildChartConfigJsString());
			sb.append(COMMA);
			// add the header member
			sb.append(bjs.buildHeadMemberJsString(treeView.getRoot().getValue()));
			sb.append(COMMA);

			// add header employee next
			configOrderBuilder.append(
					treeView.getRoot().getValue().getName() + "_" + treeView.getRoot().getValue().getPortalId());
			configOrderBuilder.append(COMMA);

			// loop on the entire view of list of tree nodes availabe in seq by order.
			loopOnTreeView(sb, configOrderBuilder, bjs, stepCount,
					treeView.getRoot().getChildren().toArray(new TreeItem[treeView.getRoot().getChildren().size()]));
			// print for debug purpose
			System.out.println("******************************************************");
			System.out.println(sb.toString());
			System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
			System.out.println(
					String.format(BuildJavaScript.jsConfigTemplate, "chart_config", configOrderBuilder.toString()));

			System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
			System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(previewList));

			// write the things back to files
			synchronized (sb) {
				FileUtils
						.write(new File(appDir.getAbsolutePath() + FilesUtil.SLASH + AddressBook.APP_JS),
								sb.toString() + " \n \n " + String.format(BuildJavaScript.jsConfigTemplate,
										" chart_config", configOrderBuilder.toString()),
								Charset.defaultCharset(), false);
				FileUtils
						.write(new File(appDir.getAbsolutePath() + FilesUtil.SLASH + AddressBook.TEMP_JS),
								sb.toString() + " \n \n " + String.format(BuildJavaScript.jsConfigTemplate,
										" chart_config", configOrderBuilder.toString()),
								Charset.defaultCharset(), false);

				FileUtils.write(new File(appDir.getAbsolutePath() + FilesUtil.SLASH + AddressBook.PREVIEW_JSON),
						mapper.writerWithDefaultPrettyPrinter().writeValueAsString(previewList),
						Charset.defaultCharset(), false);
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	int DEPTH = 20;
	int iterationCount = 0;
	int checkCount = 0;

	private void loopOnTreeView(StringBuilder sb, StringBuilder configOrderBuilder, BuildJavaScript bjs, int stepCount,
			@SuppressWarnings("unchecked") TreeItem<EmployeeDetails>... ti) throws IOException {

		if (iterationCount == DEPTH) {
			return;
		}

		for (TreeItem<EmployeeDetails> tid : ti) {
			// set the parent portal id as the parent of the child
			tid.getValue()
					.setParent(tid.getParent().getValue().getName() + "_" + tid.getParent().getValue().getPortalId());
			if (tid.getChildren().size() >= 3) {
				if (tid.getValue().isPseudo()) {
					sb.append(bjs.buildPseudoJsString(tid.getValue(), stepCount++));
					sb.append(COMMA);
					configOrderBuilder.append(fetchParentStr(tid));
					configOrderBuilder.append(COMMA);
				} else {
					sb.append(bjs.buildMemberJsString(tid.getValue(), stepCount++));
					sb.append(COMMA);
					configOrderBuilder.append(fetchParentStr(tid));
					configOrderBuilder.append(COMMA);
				}
				previewList.add(tid.getValue());
			} else {
				if (tid.getValue().isPseudo()) {
					sb.append(bjs.buildPseudoJsString(tid.getValue()));
					sb.append(COMMA);
					configOrderBuilder.append(fetchParentStr(tid));
					configOrderBuilder.append(COMMA);
				} else {
					sb.append(bjs.buildMemberJsString(tid.getValue()));
					sb.append(COMMA);
					configOrderBuilder.append(fetchParentStr(tid));
					configOrderBuilder.append(COMMA);
				}
				previewList.add(tid.getValue());
			}

			// check if the child has any more children
			if (tid.getChildren().size() > 0) {
				iterationCount = 0;
				loopOnTreeView(sb, configOrderBuilder, bjs, stepCount,
						tid.getChildren().toArray(new TreeItem[tid.getChildren().size()]));
			}
			iterationCount++;
		}

	}

	private String fetchParentStr(TreeItem<EmployeeDetails> tid) {
		return tid.getValue().getName() + "_" + tid.getValue().getPortalId();
	}

	// check if duplicate item is present;
	boolean isItemPresent = false;

	/**
	 * Loops and check if employee is already present on the tree.
	 * 
	 * @param ed
	 * @param checkRootNode
	 * @return
	 */
	private boolean checkIfItemAlreadyPresentInTree(EmployeeDetails ed, TreeItem<EmployeeDetails>... checkRootNode) {

		if (checkCount == DEPTH) {
			return false;
		}

		for (TreeItem<EmployeeDetails> tid : checkRootNode) {
			if (tid.getValue().getPortalId().equals(ed.getPortalId())) {
				isItemPresent = true;
				break;
			}

			if (tid.getChildren().size() > 0) {
				checkCount = 0;
				checkIfItemAlreadyPresentInTree(ed, tid.getChildren().toArray(new TreeItem[tid.getChildren().size()]));
			}
			checkCount++;
		}
		return isItemPresent;

	}

	private EmployeeDetails fetchHeaderEmployee() {
		return employeeList.stream().filter(e -> e.getParent().equals(HEADER_MEMBER)).findFirst().get();
	}

	/**
	 * As there is a bug in JDK we need to reload the page using js.<br>
	 * In order to do that this function is executed against the engine.<br>
	 * For now as an alternative this will reload the page.
	 * 
	 * @return
	 */
	private String buildReloadJSScript() {
		return RELOAD_JS_SCRIPT;

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
