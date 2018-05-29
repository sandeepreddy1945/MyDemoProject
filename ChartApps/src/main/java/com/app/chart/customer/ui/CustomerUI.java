/**
 * 
 */
package com.app.chart.customer.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.app.chart.fx.ChartWebEngine;
import com.app.chart.fx.FilesUtil;
import com.app.chart.model.CustomerFileBoundary;
import com.app.chart.perfomance.dashboard.ui.DashboardUI;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
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
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellEditEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Sandeep Reddy Battula
 *
 */
@Slf4j
public class CustomerUI extends HBox {

	/**
	 * Visual Bounds of the Screen.
	 */
	public static Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
	public static double WIDTH = visualBounds.getWidth() - 100;
	public static double HEIGHT = visualBounds.getHeight() - 100;

	private ObservableList<CustomerUITableBoundary> members = FXCollections
			.observableArrayList(/* constructTestBoundary() */);
	// build tree
	final TreeItem<CustomerUITableBoundary> root = new RecursiveTreeItem<>(members, RecursiveTreeObject::getChildren);

	private JFXTreeTableView<CustomerUITableBoundary> tableView = new JFXTreeTableView<>(root);

	private List<CustomerFileBoundary> customerFileList = new ArrayList<>();

	private ObjectMapper mapper = new ObjectMapper();

	private static final String EMPTY_STR = "";

	private static final String customer_description_txt = "customer_description_txt";
	private static final String images_file_name_url = "images_file_name_url";

	public CustomerUI() {
		super(5);

		// initialize data from file
		initializeDataFromFile();

		initUI();
	}

	private void initializeDataFromFile() {
		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);

		try {
			String jsonData = FileUtils.readFileToString(new File(FilesUtil.DASHBOARD_PROJECT_CUSTOMER_DATA_FILE),
					Charset.defaultCharset());
			if (jsonData != null && jsonData.length() > 0) {
				customerFileList = mapper.readValue(jsonData,
						mapper.getTypeFactory().constructCollectionType(List.class, CustomerFileBoundary.class));

				customerFileList.stream().forEach(c -> {
					members.add(constructTableBoundary(c));
					// no need to fire the table as first step ..data will be added after this step.
				});
			}
		} catch (IOException e) {
			log.error("initializeDataFromFile", e);
		}

	}

	private void initUI() {
		tableView.setMinSize(900, 800);
		tableView.setPrefSize(900, 800);
		tableView.setShowRoot(false);
		tableView.setEditable(true);
		tableView.setPadding(new Insets(15));

		JFXButton addTemplate = new JFXButton("Add Background Template");
		JFXButton saveTemplate = new JFXButton("Save Template For Display");
		JFXButton deleteTemplate = new JFXButton("Delete Template");
		JFXButton previewTemplate = new JFXButton("Preview Template");

		addTemplate.setOnAction(this::addTemplate);
		saveTemplate.setOnAction(this::saveTemplate);
		deleteTemplate.setOnAction(this::deleteTemplate);
		previewTemplate.setOnAction(this::previewTemplate);

		HBox bottomBox = new HBox(10);
		bottomBox.setAlignment(Pos.BOTTOM_RIGHT);
		bottomBox.getChildren().addAll(addTemplate, deleteTemplate, saveTemplate, previewTemplate);

		VBox mainBox = new VBox(10);
		mainBox.getChildren().addAll(buildTableView(), tableView, bottomBox);

		setAlignment(Pos.CENTER);
		getChildren().add(mainBox);
	}

	private void addTemplate(ActionEvent e) {

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Add Template Image File");
		// set initial properties
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPEG", "*.jpeg"),
				new FileChooser.ExtensionFilter("JPG", "*.jpg"), new FileChooser.ExtensionFilter("PNG", "*.png"));
		// just build the one from parent scene.
		File file = fileChooser.showOpenDialog(this.getScene().getWindow());

		if (file != null) {
			// remove any spaces in the file if it contains
			String fileName = file.getName().replaceAll(" ", "");
			try {
				FileUtils.copyInputStreamToFile(new FileInputStream(file),
						new File(FilesUtil.DASHBOARD_PROJECT_CUSTOMER_IMAGES_FOLDER + FilesUtil.SLASH + fileName));
				// also create the folder for copying the edited html file.
				FilesUtil.checkAndCreateDir(FilesUtil.DASHBOARD_PROJECT_CUSTOMER_FOLDER + FilesUtil.SLASH
						+ fileName.substring(0, fileName.lastIndexOf(".")));

				// copy the flight images to the sub folder.

				// empty string for now. User will edit it directly in the table ..
				members.add(
						constructTableBoundary(fileName, fileName.substring(0, fileName.lastIndexOf(".")), EMPTY_STR));

				tableView.fireEvent(e);

			} catch (IOException e1) {
				log.error("addTemplate", e1);
			}
		}

	}

	private void saveTemplate(ActionEvent e) {
		customerFileList.clear();

		members.stream().forEach(m -> {
			// for now file name and folder name both are same.
			// TODO need to check if this can be changed or remains the same.
			customerFileList.add(new CustomerFileBoundary(m.fileName.get(), m.folderName.get(), m.scrollText.get()));
		});

		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		try {
			String jsonData = mapper.writeValueAsString(customerFileList);
			log.info("saveTemplate : \n " + jsonData);
			FileUtils.write(new File(FilesUtil.DASHBOARD_PROJECT_CUSTOMER_DATA_FILE), jsonData,
					Charset.defaultCharset(), false);

			customerFileList.stream().forEach(c -> {
				// write the contents to file.
				File tempFile;
				try {
					// copy to temp file and edit it.
					tempFile = File.createTempFile("customerTemp", ".html");
					FileUtils.copyInputStreamToFile(
							getClass().getClassLoader().getResourceAsStream("com/app/chart/customer/html/index.html"),
							tempFile);
					String fileContent = FileUtils.readFileToString(tempFile, Charset.defaultCharset());

					// encode the required detail message to prevent it from getting mishandled.
					String formattedStr = fileContent.replace(images_file_name_url, c.getFileName()).replace(
							customer_description_txt, Base64.getEncoder().encodeToString(c.getScrollTxt().getBytes()));// String.format(fileContent,
					// "..//images//"+ c.getFileName(),
					// c.getScrollTxt());
					tempFile.delete();

					// save the formatted string to html file
					FilesUtil.checkAndCreateFile(FilesUtil.DASHBOARD_PROJECT_CUSTOMER_FOLDER + FilesUtil.SLASH
							+ c.getFolderName() + FilesUtil.SLASH + "index" + ".html");
					// once created write teh content to file ..juzz for safety.
					FileUtils.write(
							new File(FilesUtil.DASHBOARD_PROJECT_CUSTOMER_FOLDER + FilesUtil.SLASH + c.getFolderName()
									+ FilesUtil.SLASH + "index" + ".html"),
							formattedStr, Charset.defaultCharset(), false);
				} catch (IOException e1) {
					log.error("saveTemplate", e1);
				}

			});

		} catch (IOException e1) {
			log.error("saveTemplate", e1);
		}
	}

	private void deleteTemplate(ActionEvent e) {
		if (tableView.getSelectionModel().getSelectedItem() != null) {
			members.remove(tableView.getSelectionModel().getSelectedItem().getValue());
			// remove from the folder system as well from the customer folder.
			FileUtils.deleteQuietly(new File(FilesUtil.DASHBOARD_PROJECT_CUSTOMER_FOLDER + FilesUtil.SLASH
					+ tableView.getSelectionModel().getSelectedItem().getValue().fileName.get()));

			tableView.fireEvent(e);
		}
	}

	private void previewTemplate(ActionEvent e) {
		if (tableView.getSelectionModel().getSelectedItem() != null) {
			// perform an auto save.
			saveTemplate(e);
			String fileUrl;
			try {
				String fileFloder = tableView.getSelectionModel().getSelectedItem().getValue().folderName.get();
				fileUrl = new File(FilesUtil.DASHBOARD_PROJECT_CUSTOMER_FOLDER + FilesUtil.SLASH + fileFloder
						+ FilesUtil.SLASH + "index.html").toURI().toURL().toExternalForm();
				buildPreviewDialog(fileUrl);
			} catch (MalformedURLException e1) {
				log.error("saveTemplate", e1);
			}

		}
	}

	private void buildPreviewDialog(String fileUrl) {
		JFXAlert<String> alert = new JFXAlert<>();
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.setOverlayClose(false);
		JFXDialogLayout layout = new JFXDialogLayout();
		layout.setHeading(new Text("Preview Customer Slide"));
		layout.setMinSize(WIDTH, HEIGHT - 400);

		VBox mainBox = new VBox(5);

		// initialize the chart webengine
		ChartWebEngine chartWebEngine = new ChartWebEngine().initialize();
		// set the primary stage object to webview for popup displays
		chartWebEngine.setParenStage((Stage) this.getScene().getWindow());
		chartWebEngine.getWebView().setPrefSize(DashboardUI.WIDTH, DashboardUI.HEIGHT - 60);
		chartWebEngine.getWebView().setMinSize(DashboardUI.WIDTH - 160, DashboardUI.HEIGHT - 110);
		chartWebEngine.getWebView().setTranslateZ(10);
		mainBox.getChildren().add(chartWebEngine.getWebView());

		layout.setBody(mainBox);

		chartWebEngine.displayData(fileUrl);

		JFXButton okBtn = new JFXButton("OK");
		okBtn.getStyleClass().add("dialog-accept");

		okBtn.setOnAction(e -> {
			// currently no work except this.
			alert.hideWithAnimation();
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
		layout.setActions(okBtn, cancelBtn);
		alert.setContent(layout);
		alert.show();
	}

	private HBox buildTableView() {

		JFXTreeTableColumn<CustomerUITableBoundary, String> fileName = new JFXTreeTableColumn<>("File Name");
		fileName.setPrefWidth(200);
		fileName.setCellValueFactory((TreeTableColumn.CellDataFeatures<CustomerUITableBoundary, String> param) -> {
			if (fileName.validateValue(param)) {
				return param.getValue().getValue().getFileName();
			} else {
				return fileName.getComputedValue(param);
			}
		});

		JFXTreeTableColumn<CustomerUITableBoundary, String> folderName = new JFXTreeTableColumn<>("Folder Name");
		folderName.setPrefWidth(200);
		folderName.setCellValueFactory((TreeTableColumn.CellDataFeatures<CustomerUITableBoundary, String> param) -> {
			if (folderName.validateValue(param)) {
				return param.getValue().getValue().getFolderName();
			} else {
				return folderName.getComputedValue(param);
			}
		});

		JFXTreeTableColumn<CustomerUITableBoundary, String> scrollTxt = new JFXTreeTableColumn<>("Scroll Text");
		scrollTxt.setPrefWidth(430);
		scrollTxt.setCellValueFactory((TreeTableColumn.CellDataFeatures<CustomerUITableBoundary, String> param) -> {
			if (scrollTxt.validateValue(param)) {
				return param.getValue().getValue().getScrollText();
			} else {
				return scrollTxt.getComputedValue(param);
			}
		});

		scrollTxt.setCellFactory(
				(TreeTableColumn<CustomerUITableBoundary, String> param) -> new GenericEditableTreeTableCell<>(
						new TextFieldEditorBuilder()));
		scrollTxt.setOnEditCommit((CellEditEvent<CustomerUITableBoundary, String> t) -> t.getTreeTableView()
				.getTreeItem(t.getTreeTablePosition().getRow()).getValue().getScrollText().set(t.getNewValue()));

		// set colums to table
		tableView.getColumns().addAll(fileName, folderName, scrollTxt);

		HBox box = new HBox(20);
		Label sizeLbl = new Label("Table Size: ");
		Label size = new Label();
		JFXTextField searchTF = new JFXTextField();
		searchTF.setPromptText("Search Text Here!!!");
		size.textProperty().bind(Bindings.createStringBinding(() -> String.valueOf(tableView.getCurrentItemsCount()),
				tableView.currentItemsCountProperty()));

		searchTF.textProperty().addListener((o, oldVal, newVal) -> {
			tableView.setPredicate(userProp -> {
				final CustomerUITableBoundary user = userProp.getValue();
				return user.scrollText.get().toLowerCase().contains(newVal.toLowerCase())
						|| user.fileName.get().toLowerCase().contains(newVal.toLowerCase());
			});
		});
		searchTF.setAlignment(Pos.CENTER_LEFT);
		searchTF.setMinSize(450, 25);
		searchTF.setLabelFloat(true);
		box.getChildren().addAll(searchTF, sizeLbl, size);
		box.setPrefSize(400, 25);
		box.setPadding(new Insets(15));

		return box;
	}

	private CustomerUITableBoundary constructTableBoundary(CustomerFileBoundary c) {
		CustomerUITableBoundary ct = new CustomerUITableBoundary();
		ct.setFileName(new SimpleStringProperty());
		ct.setFolderName(new SimpleStringProperty());
		ct.setScrollText(new SimpleStringProperty());

		ct.getFileName().set(c.getFileName());
		ct.getFolderName().set(c.getFolderName());
		ct.getScrollText().set(c.getScrollTxt());

		return ct;
	}

	private CustomerUITableBoundary constructTableBoundary(String fileName, String folderName, String scrollTxt) {
		CustomerUITableBoundary ct = new CustomerUITableBoundary();
		ct.setFileName(new SimpleStringProperty());
		ct.setFolderName(new SimpleStringProperty());
		ct.setScrollText(new SimpleStringProperty());

		ct.getFileName().set(fileName);
		ct.getFolderName().set(folderName);
		ct.getScrollText().set(scrollTxt);

		return ct;
	}

	@Getter
	@Setter
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CustomerUITableBoundary extends RecursiveTreeObject<CustomerUITableBoundary> {

		StringProperty fileName;
		StringProperty folderName;
		StringProperty scrollText;
	}

}
