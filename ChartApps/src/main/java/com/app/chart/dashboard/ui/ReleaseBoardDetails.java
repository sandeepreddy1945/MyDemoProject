/**
 * 
 */
package com.app.chart.dashboard.ui;

import com.app.chart.model.EmployeeDetails;
import com.app.chart.perfomance.dashboard.DashboardUtil;
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
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
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
	private TreeView<TreeNode> treeView = new TreeView<TreeNode>();
	private JFXComboBox<String> pickBox = new JFXComboBox<>();
	private ObservableList<String> comboBoxList = FXCollections.observableArrayList(SUNBURN_CHART, RADIAL_CHART,
			DONUT_CHART);
	private Tile sunburstTile;
	private Tile radialPercentageTile;
	private JFXColorPicker colorPicker = new JFXColorPicker();
	private static final String EM1 = "1em";
	private static final String ERROR = "error";

	public ReleaseBoardDetails() {
		super(5);

		initUI();
	}

	private void initUI() {

		treeView.setMinWidth(400);
		treeView.setRoot(new TreeItem<>());

		// add the comboBox Children
		pickBox.setPromptText("Select Item From List To Edit");
		pickBox.getItems().addAll(comboBoxList);
		pickBox.setOnAction(e -> {
			if (pickBox.getSelectionModel().getSelectedIndex() == 1) {
				sunBurnChartEditor();
			} else if (pickBox.getSelectionModel().getSelectedIndex() == 2
					|| pickBox.getSelectionModel().getSelectedIndex() == 3) {
				radialOrDonutEditor();
			}
		});

	}

	private HBox sunBurnChartEditor() {
		HBox box = new HBox(5);

		JFXButton addRoot = new JFXButton("Add Root (+)");
		addRoot.setOnAction(e -> {
			JFXTextField field = new JFXTextField();
			field.setPromptText("Enter Root Node Name");
			// JFXAlert<String> alert = displayDialog("Add Roote Node", 1, field);
		});

		JFXButton addRelease = new JFXButton("Add Release (+)");
		addRelease.setOnAction(e -> {
			JFXTextField field = new JFXTextField();
			field.setPromptText("Enter Release Name");

			JFXTextField field2 = new JFXTextField();
			field2.setPromptText("Enter Release Points");

			JFXColorPicker colorPicker = new JFXColorPicker();
		});

		JFXButton addFeature = new JFXButton("Add Feature (+) ");
		addFeature.setOnAction(e -> {

		});

		buildSunBurnTile(null);
		return null;

	}

	private HBox previewBox() {
		return null;

	}

	private JFXAlert<String> displayDialog(String dialogHeading, int action, JFXColorPicker colorPicker,
			JFXTextField... fields) {
		JFXAlert<String> alert = new JFXAlert<>();
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.setOverlayClose(false);
		JFXDialogLayout layout = new JFXDialogLayout();
		layout.setHeading(new Text(dialogHeading));

		// make fields as required fields with default text.
		DashboardUtil.buildRequestValidator(fields);

		HBox box = new HBox(10);
		box.getChildren().addAll(fields);
		box.getChildren().add(colorPicker);
		layout.setBody(box);

		JFXButton closeButton = new JFXButton("OK");
		closeButton.getStyleClass().add("dialog-accept");
		closeButton.setOnAction(event -> {
			// if all fields are validated then only hide the dialog.
			if (DashboardUtil.validateTextField(fields)) {
				alert.hideWithAnimation();
			}
		});
		layout.setStyle("-fx-background-color: white;\r\n" + "    -fx-background-radius: 5.0;\r\n"
				+ "    -fx-background-insets: 0.0 5.0 0.0 5.0;\r\n" + "    -fx-padding: 10;\r\n"
				+ "    -fx-hgap: 10;\r\n" + "    -fx-vgap: 10;" + " -fx-border-color: #2e8b57;\r\n"
				+ "    -fx-border-width: 2px;\r\n" + "    -fx-padding: 10;\r\n" + "    -fx-spacing: 8;");
		layout.setActions(closeButton);
		alert.setContent(layout);
		alert.show();

		return alert;
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
		return null;

	}

}
