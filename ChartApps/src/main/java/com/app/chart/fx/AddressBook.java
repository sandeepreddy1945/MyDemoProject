package com.app.chart.fx;

import java.util.stream.Collectors;

import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class AddressBook extends Application {

	private TableView<Person> table = new TableView<Person>();
	private final ObservableList<Person> data = FXCollections.observableArrayList(
			new Person("Jacob", "Smith", "jacob.smith@example.com"),
			new Person("Isabella", "Johnson", "isabella.johnson@example.com"),
			new Person("Ethan", "Williams", "ethan.williams@example.com"),
			new Person("Emma", "Jones", "emma.jones@example.com"),
			new Person("Michael", "Brown", "michael.brown@example.com"));
	final HBox hb = new HBox();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		Scene scene = new Scene(new Group());
		stage.setTitle("Add Members Here");
		stage.setWidth(800);
		stage.setHeight(800);

		final Label label = new Label("Address Book");
		label.setFont(new Font("Arial", 20));

		JFXComboBox<String> teamCombo = new JFXComboBox<>();
		teamCombo.setMinSize(300, 20);
		teamCombo.setPromptText("Select Manager/Manager's Team");
		data.stream().forEach(p -> teamCombo.getItems().add(p.getName()));

		table.setEditable(true);
		Callback<TableColumn, TableCell> cellFactory = new Callback<TableColumn, TableCell>() {
			public TableCell call(TableColumn p) {
				return new EditingCell();
			}
		};

		table.setPrefWidth(stage.getWidth() - 50);
		table.setPrefHeight(stage.getHeight() - 200);

		TableColumn firstNameCol = new TableColumn("Name");
		firstNameCol.setMinWidth(230);
		firstNameCol.setCellValueFactory(new PropertyValueFactory<Person, String>("name"));
		firstNameCol.setCellFactory(cellFactory);
		firstNameCol.setOnEditCommit(new EventHandler<CellEditEvent<Person, String>>() {
			@Override
			public void handle(CellEditEvent<Person, String> t) {
				((Person) t.getTableView().getItems().get(t.getTablePosition().getRow())).setName(t.getNewValue());
			}
		});

		TableColumn lastNameCol = new TableColumn("Designation");
		lastNameCol.setMinWidth(230);
		lastNameCol.setCellValueFactory(new PropertyValueFactory<Person, String>("designation"));
		lastNameCol.setCellFactory(cellFactory);
		lastNameCol.setOnEditCommit(new EventHandler<CellEditEvent<Person, String>>() {
			@Override
			public void handle(CellEditEvent<Person, String> t) {
				((Person) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setDesignation(t.getNewValue());
			}
		});

		TableColumn emailCol = new TableColumn("Team");
		emailCol.setMinWidth(240);
		emailCol.setCellValueFactory(new PropertyValueFactory<Person, String>("Team"));
		emailCol.setCellFactory(cellFactory);
		emailCol.setOnEditCommit(new EventHandler<CellEditEvent<Person, String>>() {
			@Override
			public void handle(CellEditEvent<Person, String> t) {
				((Person) t.getTableView().getItems().get(t.getTablePosition().getRow())).setTeam(t.getNewValue());
			}
		});

		table.setItems(data);
		table.getColumns().addAll(firstNameCol, lastNameCol, emailCol);

		final JFXTextField addFirstName = new JFXTextField();
		addFirstName.setPromptText("Name");
		addFirstName.setMaxWidth(firstNameCol.getPrefWidth());
		final JFXTextField addLastName = new JFXTextField();
		addLastName.setMaxWidth(lastNameCol.getPrefWidth());
		addLastName.setPromptText("Designation");
		final JFXTextField addEmail = new JFXTextField();
		addEmail.setMaxWidth(emailCol.getPrefWidth());
		addEmail.setPromptText("Team");

		final JFXButton addButton = new JFXButton("Add");
		addButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (addFirstName.getText().length() > 0 && addLastName.getText().length() > 0
						&& addEmail.getText().length() > 0) {
					data.add(new Person(addFirstName.getText(), addLastName.getText(), addEmail.getText()));
					addFirstName.clear();
					addLastName.clear();
					addEmail.clear();
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
		});

		final JFXButton delButton = new JFXButton("Delete");
		delButton.setOnAction(e -> {
			data.remove(table.getSelectionModel().getFocusedIndex());
			table.fireEvent(e);
		});

		final JFXButton saveBtn = new JFXButton("Save");
		saveBtn.setOnAction(e -> {
			// save action
			if (teamCombo.getSelectionModel().getSelectedItem().equalsIgnoreCase("manager")) {
				FilesUtil.checkAndCreateFile(data.stream().map(Person::getName).distinct().collect(Collectors.toList())
						.toArray(new String[data.size()]));
			}
		});
		addFirstName.setPrefWidth(200);
		addLastName.setPrefWidth(200);
		addEmail.setPrefWidth(200);

		addFirstName.setMinWidth(180);
		addLastName.setMinWidth(180);
		addEmail.setMinWidth(180);

		hb.getChildren().addAll(addFirstName, addLastName, addEmail, addButton, delButton, saveBtn);
		hb.setSpacing(3);

		final VBox vbox = new VBox();
		vbox.setSpacing(5);
		vbox.setPadding(new Insets(10, 0, 0, 10));
		vbox.getChildren().addAll(label, teamCombo, table, hb);

		((Group) scene.getRoot()).getChildren().addAll(vbox);

		stage.setScene(scene);
		stage.show();
	}

	@Getter
	@Setter
	@AllArgsConstructor
	public static class Person {

		private String name;
		private String designation;
		private String team;
	}

	class EditingCell extends TableCell<Person, String> {

		private TextField textField;

		public EditingCell() {
		}

		@Override
		public void startEdit() {
			if (!isEmpty()) {
				super.startEdit();
				createTextField();
				setText(null);
				setGraphic(textField);
				textField.selectAll();
			}
		}

		@Override
		public void cancelEdit() {
			super.cancelEdit();

			setText((String) getItem());
			setGraphic(null);
		}

		@Override
		public void updateItem(String item, boolean empty) {
			super.updateItem(item, empty);

			if (empty) {
				setText(null);
				setGraphic(null);
			} else {
				if (isEditing()) {
					if (textField != null) {
						textField.setText(getString());
					}
					setText(null);
					setGraphic(textField);
				} else {
					setText(getString());
					setGraphic(null);
				}
			}
		}

		private void createTextField() {
			textField = new TextField(getString());
			textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
			textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
					if (!arg2) {
						commitEdit(textField.getText());
					}
				}
			});
		}

		private String getString() {
			return getItem() == null ? "" : getItem().toString();
		}
	}
}