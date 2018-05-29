/**
 * 
 */
package com.app.chart.animation;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.app.chart.fx.FilesUtil;
import com.app.chart.model.ScrollTexts;
import com.app.chart.perfomance.dashboard.DashboardUtil;
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
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellEditEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
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
public class ScrollTextDataUI extends HBox {

	private ObservableList<ScrollTxtTableBoundary> members = FXCollections
			.observableArrayList(/* constructTestBoundary() */);
	// build tree
	final TreeItem<ScrollTxtTableBoundary> root = new RecursiveTreeItem<>(members, RecursiveTreeObject::getChildren);

	private JFXTreeTableView<ScrollTxtTableBoundary> tableView = new JFXTreeTableView<>(root);

	private List<ScrollTexts> scrollTexts = new ArrayList<>();

	private ObjectMapper mapper = new ObjectMapper();

	/**
	 * 
	 */
	public ScrollTextDataUI() {
		super(5);

		// load from file
		loadListFromFile();

		// initiate UI
		initUI();
	}

	private void loadListFromFile() {
		if (new File(FilesUtil.DASHBOARD_PROJECT_SCROLL_TEXT_FILE).exists()) {
			try {
				String jsonData = FileUtils.readFileToString(new File(FilesUtil.DASHBOARD_PROJECT_SCROLL_TEXT_FILE),
						Charset.defaultCharset());
				if (jsonData != null && jsonData.length() > 0) {
					mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
					scrollTexts = mapper.readValue(jsonData,
							mapper.getTypeFactory().constructCollectionType(List.class, ScrollTexts.class));

					scrollTexts.stream().forEach(p -> {
						StringProperty s = new SimpleStringProperty();
						ScrollTxtTableBoundary st = new ScrollTxtTableBoundary();
						st.setScrollText(s);
						st.getScrollText().set(p.getScrollText());
						members.add(st);
					});
				}
				// tableView.fireEvent(null);
			} catch (IOException e) {
				log.error( "loadListFromFile", e);
			}
		}

	}

	private void initUI() {
		tableView.setMinSize(900, 800);
		tableView.setPrefSize(900, 800);
		tableView.setShowRoot(false);
		tableView.setEditable(true);
		tableView.setPadding(new Insets(15));

		JFXButton addText = new JFXButton("Add Scroll Text");
		JFXButton deleteTxt = new JFXButton("Delete Scroll Text");
		JFXButton save = new JFXButton("Save");

		addText.setOnAction(this::addText);
		deleteTxt.setOnAction(this::deleteTxt);
		save.setOnAction(this::saveAction);

		VBox mainbox = new VBox(10);
		HBox bottomBx = new HBox(10);
		bottomBx.getChildren().addAll(addText, deleteTxt, save);
		bottomBx.setAlignment(Pos.BOTTOM_RIGHT);

		mainbox.getChildren().addAll(buildTableView(), tableView, bottomBx);

		setAlignment(Pos.CENTER);
		getChildren().add(mainbox);

	}

	private void addText(ActionEvent e) {
		displayDialogBox();
	}

	private void deleteTxt(ActionEvent e) {
		if (tableView.getSelectionModel().getSelectedItem() != null) {
			members.remove(tableView.getSelectionModel().getSelectedItem().getValue());
			tableView.fireEvent(e);
		}
	}

	private void saveAction(ActionEvent e) {
		String jsonData;
		try {
			scrollTexts.clear();
			members.stream().forEach(m -> {
				ScrollTexts s = new ScrollTexts();
				s.setScrollText(m.scrollText.get());
				scrollTexts.add(s);
			});

			jsonData = mapper.writeValueAsString(scrollTexts);
			// just for storage purpose.
			log.info("saveAction : \n" + jsonData);
			FileUtils.writeStringToFile(new File(FilesUtil.DASHBOARD_PROJECT_SCROLL_TEXT_FILE), jsonData,
					Charset.defaultCharset(), false);
		} catch (IOException e1) {
			log.error( "saveAction", e1);
		}
	}

	private void displayDialogBox() {
		JFXAlert<String> alert = new JFXAlert<>();
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.setOverlayClose(false);
		JFXDialogLayout layout = new JFXDialogLayout();
		layout.setHeading(new Text("Add Scroll  Text"));
		layout.setMinWidth(600);
		layout.setBackground(new Background(new BackgroundFill(Color.web("#ffffff"), CornerRadii.EMPTY, Insets.EMPTY)));

		JFXTextField scrollTxt = new JFXTextField();
		scrollTxt.setPromptText("Enter The Scroll Text");
		scrollTxt.setLabelFloat(true);
		scrollTxt.setMinWidth(500);
		DashboardUtil.buildRequestValidator(scrollTxt);

		HBox box = new HBox(20, scrollTxt);
		layout.setBody(box);

		JFXButton okBtn = new JFXButton("OK");
		okBtn.getStyleClass().add("dialog-accept");

		okBtn.setOnAction(e -> {
			if (DashboardUtil.validateTextField(scrollTxt)) {
				StringProperty s = new SimpleStringProperty();
				ScrollTxtTableBoundary st = new ScrollTxtTableBoundary();
				st.setScrollText(s);
				st.getScrollText().set(scrollTxt.getText());

				ScrollTexts sc = new ScrollTexts();
				sc.setScrollText(scrollTxt.getText());

				scrollTexts.add(sc);
				members.add(st);
				tableView.fireEvent(e);
				alert.hideWithAnimation();
			}
		});

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

	private HBox buildTableView() {
		JFXTreeTableColumn<ScrollTxtTableBoundary, String> scrollTxt = new JFXTreeTableColumn<>("Scroll Text");
		scrollTxt.setPrefWidth(700);
		scrollTxt.setCellValueFactory((TreeTableColumn.CellDataFeatures<ScrollTxtTableBoundary, String> param) -> {
			if (scrollTxt.validateValue(param)) {
				return param.getValue().getValue().getScrollText();
			} else {
				return scrollTxt.getComputedValue(param);
			}
		});

		scrollTxt.setCellFactory(
				(TreeTableColumn<ScrollTxtTableBoundary, String> param) -> new GenericEditableTreeTableCell<>(
						new TextFieldEditorBuilder()));
		scrollTxt.setOnEditCommit((CellEditEvent<ScrollTxtTableBoundary, String> t) -> t.getTreeTableView()
				.getTreeItem(t.getTreeTablePosition().getRow()).getValue().getScrollText().set(t.getNewValue()));

		// set colums to table
		tableView.getColumns().addAll(scrollTxt);

		HBox box = new HBox(20);
		Label sizeLbl = new Label("Table Size: ");
		Label size = new Label();
		JFXTextField searchTF = new JFXTextField();
		searchTF.setPromptText("Search Text Here!!!");
		size.textProperty().bind(Bindings.createStringBinding(() -> String.valueOf(tableView.getCurrentItemsCount()),
				tableView.currentItemsCountProperty()));

		searchTF.textProperty().addListener((o, oldVal, newVal) -> {
			tableView.setPredicate(userProp -> {
				final ScrollTxtTableBoundary user = userProp.getValue();
				return user.scrollText.get().toLowerCase().contains(newVal.toLowerCase());
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

	@Getter
	@Setter
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ScrollTxtTableBoundary extends RecursiveTreeObject<ScrollTxtTableBoundary> {

		StringProperty scrollText;
	}

}
