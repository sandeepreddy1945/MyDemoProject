/**
 * 
 */
package com.app.chart.dashboard.ui;

import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

import com.app.chart.model.EmployeeDetails;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;

import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.Tile.SkinType;
import eu.hansolo.tilesfx.chart.ChartData;
import eu.hansolo.tilesfx.chart.SunburstChart.TextOrientation;
import eu.hansolo.tilesfx.tools.TreeNode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

/**
 * @author Sandeep
 *
 */
public class ReleaseBoardDetails extends HBox {

	private static final String SUNBURN_CHART = "Sunburn Chart";
	private static final String RADIAL_CHART = "Radial Chart";
	private static final String DONUT_CHART = "Donut Chart";
	private TreeView<EmployeeDetails> treeView = new TreeView<EmployeeDetails>();
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
		TreeNode tree = new TreeNode(new ChartData("ROOT"));
		JFXTextField field = new JFXTextField();
		field.setPromptText("Enter Team Name");
		JFXTextField value1 = new JFXTextField();
		value1.setPromptText("Enter Value");

		this.buildRequestValidator(field);

		JFXTextField releaseField = new JFXTextField();
		releaseField.setPromptText("Enter Release Name");
		this.buildRequestValidator(releaseField);

		JFXButton button = new JFXButton("Add Feature + ");
		button.setOnAction(e -> {

		});

		sunburstTile = TileBuilder.create().skinType(SkinType.SUNBURST).prefSize(500, 500).title("SunburstTile")
				.textVisible(false).sunburstTree(tree).sunburstBackgroundColor(Tile.BACKGROUND)
				.sunburstTextColor(Tile.BACKGROUND).sunburstUseColorFromParent(true)
				.sunburstTextOrientation(TextOrientation.TANGENT).sunburstAutoTextColor(true)
				.sunburstUseChartDataTextColor(true).sunburstInteractive(true).build();
		return null;

	}

	private void buildRequestValidator(JFXTextField field) {
		RequiredFieldValidator validator = new RequiredFieldValidator();
		validator.setMessage("Input is Required!!");
		validator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class).glyph(FontAwesomeIcon.WARNING).size(EM1)
				.styleClass(ERROR).build());

		field.getValidators().add(validator);
		field.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal) {
				field.validate();
			}
		});
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
