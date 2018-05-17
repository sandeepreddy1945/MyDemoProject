/**
 * 
 */
package com.app.chart.dashboard.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.app.chart.model.SunburstBoundary;
import com.app.chart.model.SunburstBoundary.ReleaseBoundary;
import com.app.chart.model.SunburstBoundary.ReleaseBoundary.ReleaseAttrBoundary;
import com.app.chart.perfomance.dashboard.DashboardUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.Tile.SkinType;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.chart.ChartData;
import eu.hansolo.tilesfx.chart.SunburstChart.TextOrientation;
import eu.hansolo.tilesfx.tools.TreeNode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;

/**
 * @author Sandeep
 *
 */
public class ReleaseBoardDetails extends HBox {

	private static final String SUNBURN_CHART = "Sunburn Chart";
	private static final String RADIAL_CHART = "Radial Chart";
	private static final String DONUT_CHART = "Donut Chart";
	private TreeView<String> treeView = new TreeView<String>();
	private JFXComboBox<String> pickBox = new JFXComboBox<>();
	private ObservableList<String> comboBoxList = FXCollections.observableArrayList(SUNBURN_CHART, RADIAL_CHART,
			DONUT_CHART);
	private Tile sunburstTile;
	private Tile radialPercentageTile;
	private JFXColorPicker colorPicker = new JFXColorPicker();
	private static final String EM1 = "1em";
	private static final String ERROR = "error";

	private List<SunburstBoundary> sunburstBoundaries = new ArrayList<>();

	private Map<String, SunburstBoundary> sunBurstMap = new HashMap<>();
	private HBox editBox = new HBox();
	private VBox vbox;

	private ObjectMapper mapper = new ObjectMapper();

	public ReleaseBoardDetails() {
		super(5);

		initUI();
	}

	private void initUI() {

		treeView.setMinWidth(400);
		treeView.setRoot(new TreeItem<>("Root Node"));

		// add the comboBox Children
		pickBox.setPromptText("Select Item From List To Edit");
		pickBox.getItems().addAll(comboBoxList);
		editBox = new HBox();
		pickBox.setOnAction(e -> {
			if (pickBox.getSelectionModel().getSelectedIndex() == 0) {
				editBox = sunBurnChartEditor();
			} else if (pickBox.getSelectionModel().getSelectedIndex() == 1
					|| pickBox.getSelectionModel().getSelectedIndex() == 2) {
				editBox = radialOrDonutEditor();
			}
		});

		editBox.setMinWidth(600);
		setMinSize(1200, 800);
		vbox = new VBox(20);
		vbox.getChildren().add(pickBox);
		getChildren().addAll(treeView, vbox);
	}

	private HBox sunBurnChartEditor() {
		HBox box = new HBox(5);

		JFXButton addRoot = new JFXButton("Add Root (+)");
		addRoot.setOnAction(e -> {
			JFXTextField field = new JFXTextField();
			field.setPromptText("Enter Root Node Name");
			this.displayDialog("Root Node", 1, null, field);
		});

		JFXButton addRelease = new JFXButton("Add Release (+)");
		addRelease.setOnAction(e -> {
			JFXTextField field = new JFXTextField();
			field.setPromptText("Enter Release Name");

			JFXTextField field2 = new JFXTextField();
			field2.setPromptText("Enter Release Points");

			JFXColorPicker colorPicker = new JFXColorPicker();

			this.displayDialog("Release Node", 2, colorPicker, field, field2);
		});

		JFXButton addFeature = new JFXButton("Add Feature (+) ");
		addFeature.setOnAction(e -> {
			JFXTextField field = new JFXTextField();
			field.setPromptText("Enter Release Name");

			JFXTextField field2 = new JFXTextField();
			field2.setPromptText("Enter Release Points");

			JFXColorPicker colorPicker = new JFXColorPicker();

			this.displayDialog("Feature Node", 3, colorPicker, field, field2);
		});

		JFXButton removeNode = new JFXButton("Remove Node (-)");
		removeNode.setOnAction(e -> {

		});

		JFXButton saveBtn = new JFXButton("Save ");
		saveBtn.setOnAction(e -> {
			sunBurstMap.values().stream().forEach(b -> {
				try {
					String str = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(b);
					System.out.println(str);

					SunburstBoundary boundary = mapper.readValue(str, SunburstBoundary.class);
					System.out.println(boundary.toString());
				} catch (JsonProcessingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
		});

		box.getChildren().addAll(addRoot, addRelease, addFeature, removeNode, saveBtn);
		// add it dynamically to the root
		vbox.getChildren().add(box);
		// buildSunBurnTile(null);
		return box;

	}

	private HBox previewBox() {
		return new HBox();

	}

	private void displayDialog(String dialogHeading, int action, JFXColorPicker colorPicker, JFXTextField... fields) {
		JFXAlert<String> alert = new JFXAlert<>();
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.setOverlayClose(false);
		JFXDialogLayout layout = new JFXDialogLayout();
		layout.setHeading(new Text(dialogHeading));

		// make fields as required fields with default text.
		DashboardUtil.buildRequestValidator(fields);

		HBox box = new HBox(10);
		box.getChildren().addAll(fields);
		if (colorPicker != null) {
			box.getChildren().add(colorPicker);
		}
		layout.setBody(box);

		JFXButton closeButton = new JFXButton("OK");
		closeButton.getStyleClass().add("dialog-accept");
		closeButton.setOnAction(event -> {
			// if all fields are validated then only hide the dialog.
			if (DashboardUtil.validateTextField(fields)) {
				if (action == 1) {
					if (sunBurstMap.containsKey(fields[0].getText())) {
						// means already there is a entry for it. So ask them to add an other Name.
						popOutAlert("This Root Name is already selected!!");
						return;
					} else if (treeView.getSelectionModel().getSelectedItem() == null) {
						popOutAlert("Root Node not selected ..Please select the Root Node!!");
						return;
					} else {
						sunBurstMap.put(fields[0].getText(), new SunburstBoundary());
						sunBurstMap.get(fields[0].getText()).setRootName(fields[0].getText());
						treeView.getRoot().getChildren().add(new TreeItem<String>(fields[0].getText()));
						treeView.getSelectionModel().getSelectedItem().setExpanded(true);
					}
				} else if (action == 2) {
					String str = treeView.getSelectionModel().getSelectedItem().getValue();
					if (!sunBurstMap.containsKey(str)) {
						popOutAlert("Root Node Properly Not Selected !!!. Wrong Node selected");
						return;
					} else {
						if (sunBurstMap.get(str).getSubBoundaries() == null)
							sunBurstMap.get(str).setSubBoundaries(new LinkedList<>());
						SunburstBoundary snb = sunBurstMap.get(str);
						ReleaseBoundary boundary = snb.new ReleaseBoundary(fields[0].getText(),
								Double.valueOf(fields[1].getText()), colorPicker.getValue(), new LinkedList<>());
						sunBurstMap.get(str).getSubBoundaries().add(boundary);
						treeView.getSelectionModel().getSelectedItem().getChildren()
								.add(new TreeItem<>(boundary.getFieldName()));
						treeView.getSelectionModel().getSelectedItem().setExpanded(true);
					}
				} else if (action == 3) {
					String str = treeView.getSelectionModel().getSelectedItem().getValue();
					/*
					 * Optional<List<ReleaseBoundary>> s = sunBurstMap.values().stream()
					 * .map(SunburstBoundary::getSubBoundaries).filter(b -> {
					 * Optional<ReleaseBoundary> o = b.stream().filter(r ->
					 * r.getFieldName().equals(str)) .findFirst(); return o.isPresent();
					 * }).findFirst();
					 */

					Optional<List<ReleaseBoundary>> optional = sunBurstMap.values().stream()
							.map(SunburstBoundary::getSubBoundaries)
							.filter(l -> l.stream().map(ReleaseBoundary::getFieldName).anyMatch(r -> r.equals(str)))
							.findFirst();

					if (optional.isPresent()) {
						List<ReleaseBoundary> boundaries = optional.get();
						ReleaseBoundary rb = boundaries.stream().filter(s -> s.getFieldName().equals(str)).findFirst()
								.get();
						if (rb.getAttrBoundaries() == null)
							rb.setAttrBoundaries(new LinkedList<>());
						ReleaseAttrBoundary rab = rb.new ReleaseAttrBoundary(fields[0].getText(),
								Double.valueOf(fields[1].getText()), colorPicker.getValue());
						rb.getAttrBoundaries().add(rab);
						treeView.getSelectionModel().getSelectedItem().getChildren()
								.add(new TreeItem<String>(rab.getFieldName()));
						treeView.getSelectionModel().getSelectedItem().setExpanded(true);
					} else {
						popOutAlert("Please Select a valid Sub Node (Release Node) to Add the Feature Details!!!");
						return;
					}
				}
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
		layout.setActions(closeButton, cancelBtn);
		alert.setContent(layout);
		alert.show();

	}

	private void buildSunBurnTile(TreeNode tree) {
		sunburstTile = TileBuilder.create().skinType(SkinType.SUNBURST).prefSize(500, 500).title("SunburstTile")
				.textVisible(false).sunburstTree(tree).sunburstBackgroundColor(Tile.BACKGROUND)
				.sunburstTextColor(Tile.BACKGROUND).sunburstUseColorFromParent(true)
				.sunburstTextOrientation(TextOrientation.ORTHOGONAL).sunburstAutoTextColor(true)
				.sunburstUseChartDataTextColor(true).sunburstInteractive(true).build();
	}

	private HBox radialOrDonutEditor() {
		ChartData chartData1 = new ChartData("Item 1", 24.0, Tile.GREEN);
		ChartData chartData2 = new ChartData("Item 2", 10.0, Tile.BLUE);
		ChartData chartData3 = new ChartData("Item 3", 12.0, Tile.RED);
		ChartData chartData4 = new ChartData("Item 4", 13.0, Tile.YELLOW_ORANGE);
		radialPercentageTile = TileBuilder.create().skinType(SkinType.RADIAL_PERCENTAGE).prefSize(500, 500)
				.backgroundColor(Color.web("#26262D")).maxValue(1000).title("RadialPercentageSkin")
				.description("Product 1").textVisible(false).chartData(chartData1, chartData2, chartData3)
				.animated(true).referenceValue(100).value(chartData1.getValue()).descriptionColor(Tile.GRAY)
				// .valueColor(Tile.BLUE)
				// .unitColor(Tile.BLUE)
				.barColor(Tile.BLUE).decimals(0).build();
		return new HBox();

	}

	private void popOutAlert(String text) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Sunburst Chart");
		alert.setContentText(text);
		alert.showAndWait();
	}

}
