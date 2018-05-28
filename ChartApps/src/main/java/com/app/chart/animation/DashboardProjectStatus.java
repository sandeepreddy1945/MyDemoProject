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
import org.slf4j.Marker;

import com.app.chart.fx.FilesUtil;
import com.app.chart.model.ProjectStatusBoundary;
import com.app.chart.perfomance.dashboard.DashboardUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
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
public class DashboardProjectStatus extends HBox {

	private static final String RED = "RED";
	private static final String ORANGE = "ORANGE";
	private static final String GREEN = "GREEN";
	private ObservableList<StatusTableBoundary> members = FXCollections
			.observableArrayList(/* constructTestBoundary() */);
	// build tree
	final TreeItem<StatusTableBoundary> root = new RecursiveTreeItem<>(members, RecursiveTreeObject::getChildren);

	private JFXTreeTableView<StatusTableBoundary> tableView = new JFXTreeTableView<>(root);
	private ObservableList<String> colorCombo = FXCollections.observableArrayList(GREEN, ORANGE, RED);
	private JFXComboBox<String> pickBox = new JFXComboBox<>(colorCombo);

	private VBox mainBox = new VBox(15);
	private List<ProjectStatusBoundary> projectStatusBoundaries = new ArrayList<>();

	private ObjectMapper mapper = new ObjectMapper();

	public DashboardProjectStatus() {
		super(10);
		loadListFromFile();
		initUI();
	}

	private void loadListFromFile() {
		if (new File(FilesUtil.DASHBOARD_PROJECT_STATUS_FILE).exists()) {
			try {
				String jsonData = FileUtils.readFileToString(new File(FilesUtil.DASHBOARD_PROJECT_STATUS_FILE),
						Charset.defaultCharset());
				mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
				projectStatusBoundaries = mapper.readValue(jsonData,
						mapper.getTypeFactory().constructCollectionType(List.class, ProjectStatusBoundary.class));
				
				projectStatusBoundaries.stream().forEach(p -> {
					members.add(constructStatusTableBoundary(p));
				});
				//tableView.fireEvent(null);
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

		JFXButton addTeamDetails = new JFXButton("Add A New Team Status");
		JFXButton editTeamDetail = new JFXButton("Edit Existing Team Status");
		JFXButton saveAction = new JFXButton("Save Details");
		JFXButton deleteRow = new JFXButton("Delete Row");

		addTeamDetails.setOnAction(this::addTeamDetais);
		editTeamDetail.setOnAction(this::editTeamDetails);
		saveAction.setOnAction(this::saveAction);
		deleteRow.setOnAction(this::deletRowAction);

		HBox btmBox = new HBox(15);

		btmBox.getChildren().addAll(addTeamDetails, editTeamDetail, saveAction, deleteRow);
		btmBox.setAlignment(Pos.BOTTOM_RIGHT);

		mainBox.getChildren().addAll(buildTableView(), tableView, btmBox);

		setAlignment(Pos.CENTER);
		getChildren().add(mainBox);
	}

	private void addTeamDetais(ActionEvent e) {
		displayDialogBox(null, false);
	}

	private void editTeamDetails(ActionEvent e) {
		if (tableView.getSelectionModel().getSelectedItem() != null) {
			displayDialogBox(tableView.getSelectionModel().getSelectedItem().getValue(), true);
		}
	}
	
	private void deletRowAction(ActionEvent e) {
		if(tableView.getSelectionModel().getSelectedItem() != null) {
			members.remove(tableView.getSelectionModel().getSelectedItem().getValue());
		}
	}

	private void saveAction(ActionEvent e) {
		String jsonData;
		try {
			projectStatusBoundaries.clear();
			members.stream().forEach(m -> {
				projectStatusBoundaries.add(constructProjectStatusBoundary(m));
			});

			jsonData = mapper.writeValueAsString(projectStatusBoundaries);
			// just for storage purpose.
			log.info("saveAction :  \n" +  jsonData);
			FileUtils.writeStringToFile(new File(FilesUtil.DASHBOARD_PROJECT_STATUS_FILE), jsonData,
					Charset.defaultCharset(), false);
		} catch (IOException e1) {
			log.error( "saveAction", e1);
		}

	}

	private void displayDialogBox(StatusTableBoundary p, boolean isEdit) {
		JFXAlert<String> alert = new JFXAlert<>();
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.setOverlayClose(false);
		JFXDialogLayout layout = new JFXDialogLayout();
		layout.setHeading(new Text("Add Project Status"));
		layout.setMinWidth(700);
		layout.setBackground(new Background(new BackgroundFill(Color.web("#ffffff"), CornerRadii.EMPTY, Insets.EMPTY)));

		JFXTextField teamNameTF = new JFXTextField();
		teamNameTF.setPromptText("Enter Team Name");
		teamNameTF.setLabelFloat(true);
		teamNameTF.setMinWidth(250);
		DashboardUtil.buildRequestValidator(teamNameTF);

		HBox box = new HBox(20, teamNameTF, pickBox);
		layout.setBody(box);

		JFXButton okBtn = new JFXButton("OK");
		okBtn.getStyleClass().add("dialog-accept");

		// parameters for edit box
		if (p != null) {
			teamNameTF.setText(p.getName().get());
			pickBox.getSelectionModel().select(p.getColor().get());
		}

		okBtn.setOnAction(e -> {
			if (DashboardUtil.validateTextField(teamNameTF) && pickBox.getSelectionModel().getSelectedItem() != null) {

				if (isEdit) {
					members.remove(p);
					ProjectStatusBoundary pb = new ProjectStatusBoundary();
					pb.setTeamName(teamNameTF.getText());
					pb.setStatusColor(pickBox.getSelectionModel().getSelectedItem());
					members.add(constructStatusTableBoundary(pb));
					tableView.fireEvent(e);
				} else {
					// save to the file.
					ProjectStatusBoundary pb = new ProjectStatusBoundary();
					pb.setTeamName(teamNameTF.getText());
					pb.setStatusColor(pickBox.getSelectionModel().getSelectedItem());
					members.add(constructStatusTableBoundary(pb));
					tableView.fireEvent(e);
				}
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
		JFXTreeTableColumn<StatusTableBoundary, String> teamName = new JFXTreeTableColumn<>("Team Name");
		teamName.setPrefWidth(300);
		teamName.setCellValueFactory((TreeTableColumn.CellDataFeatures<StatusTableBoundary, String> param) -> {
			if (teamName.validateValue(param)) {
				return param.getValue().getValue().getName();
			} else {
				return teamName.getComputedValue(param);
			}
		});

		JFXTreeTableColumn<StatusTableBoundary, String> statusColor = new JFXTreeTableColumn<>("Project Status Color");
		statusColor.setPrefWidth(150);
		statusColor.setCellValueFactory((TreeTableColumn.CellDataFeatures<StatusTableBoundary, String> param) -> {
			if (statusColor.validateValue(param)) {
				return param.getValue().getValue().getColor();
			} else {
				return statusColor.getComputedValue(param);
			}
		});

		teamName.setCellFactory(
				(TreeTableColumn<StatusTableBoundary, String> param) -> new GenericEditableTreeTableCell<>(
						new TextFieldEditorBuilder()));
		teamName.setOnEditCommit((CellEditEvent<StatusTableBoundary, String> t) -> t.getTreeTableView()
				.getTreeItem(t.getTreeTablePosition().getRow()).getValue().getName().set(t.getNewValue()));

		// set colums to table
		tableView.getColumns().addAll(teamName, statusColor);

		HBox box = new HBox(20);
		Label sizeLbl = new Label("Table Size: ");
		Label size = new Label();
		JFXTextField searchTF = new JFXTextField();
		searchTF.setPromptText("Search Text Here!!!");
		size.textProperty().bind(Bindings.createStringBinding(() -> String.valueOf(tableView.getCurrentItemsCount()),
				tableView.currentItemsCountProperty()));

		searchTF.textProperty().addListener((o, oldVal, newVal) -> {
			tableView.setPredicate(userProp -> {
				final StatusTableBoundary user = userProp.getValue();
				return user.name.get().toLowerCase().contains(newVal.toLowerCase())
						|| user.color.get().toLowerCase().contains(newVal.toLowerCase());
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

	private ProjectStatusBoundary constructProjectStatusBoundary(StatusTableBoundary s) {
		ProjectStatusBoundary p = new ProjectStatusBoundary();
		p.setTeamName(s.name.get());
		p.setStatusColor(s.color.get());

		return p;
	}

	private StatusTableBoundary constructStatusTableBoundary(ProjectStatusBoundary p) {
		StatusTableBoundary s = new StatusTableBoundary();
		s.setName(new SimpleStringProperty());
		s.setColor(new SimpleStringProperty());

		s.getName().set(p.getTeamName());
		s.getColor().set(p.getStatusColor());

		return s;
	}

	@Getter
	@Setter
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public static class StatusTableBoundary extends RecursiveTreeObject<StatusTableBoundary> {

		StringProperty name;
		StringProperty color;
	}
}
