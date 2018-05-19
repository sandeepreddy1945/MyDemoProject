/**
 * 
 */
package com.app.chart.dashboard.ui;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.app.chart.model.CustomColor;
import com.app.chart.model.SunburstBoundary;
import com.app.chart.model.SunburstBoundary.ReleaseBoundary;
import com.app.chart.model.SunburstBoundary.ReleaseBoundary.ReleaseAttrBoundary;
import com.app.chart.perfomance.dashboard.DashboardUtil;
import com.app.charts.utilities.JFXNumberField;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeView;
import com.jfoenix.controls.JFXTreeViewPath;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.Tile.SkinType;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.chart.ChartData;
import eu.hansolo.tilesfx.chart.SunburstChart.TextOrientation;
import eu.hansolo.tilesfx.tools.TreeNode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TreeItem;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
	private JFXTreeView<String> treeView = new JFXTreeView<String>();
	private JFXComboBox<String> pickBox = new JFXComboBox<>();
	private ObservableList<String> comboBoxList = FXCollections.observableArrayList(SUNBURN_CHART, RADIAL_CHART,
			DONUT_CHART);
	private Tile sunburstTile;
	private static final String EM1 = "1em";
	private static final String ERROR = "error";

	private List<ChartData> radialChart = new LinkedList<>();
	private List<ChartData> donutData = new LinkedList<>();

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
			} else if (pickBox.getSelectionModel().getSelectedIndex() == 1) {
				editBox = radialChartEditor();
			} else if (pickBox.getSelectionModel().getSelectedIndex() == 2) {
				editBox = donutChartEditor();
			}
		});

		editBox.setMinWidth(600);
		setMinSize(1200, 800);
		vbox = new VBox(20);
		vbox.getChildren().addAll(pickBox, new JFXTreeViewPath(treeView));
		getChildren().addAll(treeView, vbox);
	}

	private HBox sunBurnChartEditor() {
		HBox box = new HBox(5);

		HBox previewBox = new HBox(5);

		JFXButton addRoot = new JFXButton("Add Root (+)");
		addRoot.setOnAction(e -> {
			JFXTextField field = new JFXTextField();
			field.setPromptText("Enter Root Node Name");
			this.displayDialog("Root Node", 1, 1, null, field);
		});

		JFXButton addRelease = new JFXButton("Add Release (+)");
		addRelease.setOnAction(e -> {
			JFXTextField field = new JFXTextField();
			field.setPromptText("Enter Release Name");

			JFXNumberField field2 = new JFXNumberField();
			field2.setPromptText("Enter Release Points");

			JFXColorPicker colorPicker = new JFXColorPicker();

			this.displayDialog("Release Node", 1, 2, colorPicker, field, field2);
		});

		JFXButton addFeature = new JFXButton("Add Feature (+) ");
		addFeature.setOnAction(e -> {
			JFXTextField field = new JFXTextField();
			field.setPromptText("Enter Feature Name");

			JFXNumberField field2 = new JFXNumberField();
			field2.setPromptText("Enter Feature Points");

			JFXColorPicker colorPicker = new JFXColorPicker();

			this.displayDialog("Feature Node", 1, 3, colorPicker, field, field2);
		});
		JFXButton previewBtn = new JFXButton("Preview");
		previewBtn.setOnAction(e -> {
			if (previewBox.getChildren().size() > 0)
				previewBox.getChildren().remove(0, previewBox.getChildren().size());
			// code to rebuilf the chart from reterived data.
			sunBurstMap.values().stream().forEach(b -> {
				TreeNode rootNode = new TreeNode(new ChartData(b.getRootName()));
				b.getSubBoundaries().stream().forEach(bs -> {
					TreeNode node = new TreeNode(
							new ChartData(bs.getFieldName(), bs.getScores(), bs.getColor().fecthColor()), rootNode);
					bs.getAttrBoundaries().stream().forEach(bsl -> {
						TreeNode subNode = new TreeNode(
								new ChartData(bsl.getFieldName(), bsl.getScores(), bsl.getColor().fecthColor()), node);
					});
				});
				previewBox.getChildren().add(buildSunBurnTile(rootNode));
			});
		});

		JFXButton removeNode = new JFXButton("Remove Node (-)");
		removeNode.setOnAction(e -> {

			if (treeView.getSelectionModel().getSelectedItem() == null
					|| treeView.getSelectionModel().getSelectedItem().getParent() == null) {
				popOutAlert("Please select a valid node to Remove !!");
			} else {
				String selectedItem = treeView.getSelectionModel().getSelectedItem().getValue();

				// i.e the list select is the parent root item.
				if (treeView.getSelectionModel().getSelectedItem().getParent().getParent() == null) {
					sunBurstMap.remove(selectedItem);
					treeView.getSelectionModel().getSelectedItem().getParent().getChildren()
							.remove(treeView.getSelectionModel().getSelectedItem());

				}
				// check on the parents parent as hierarchy is treeview root -> chart root->
				// release child -> feature childre.
				else if (treeView.getSelectionModel().getSelectedItem().getParent().getParent().getParent() == null) {
					sunBurstMap.values().parallelStream().forEach(b -> {
						b.getSubBoundaries().removeIf(r -> r.getFieldName().equals(selectedItem));
						treeView.getSelectionModel().getSelectedItem().getParent().getChildren()
								.remove(treeView.getSelectionModel().getSelectedItem());
					});
				}
				// it is the last child i.e feature
				else {
					sunBurstMap.values().parallelStream().forEach(b -> {
						b.getSubBoundaries().parallelStream().forEach(g -> {
							g.getAttrBoundaries().removeIf(r -> r.getFieldName().equals(selectedItem));
							treeView.getSelectionModel().getSelectedItem().getParent().getChildren()
									.remove(treeView.getSelectionModel().getSelectedItem());
						});
					});
				}

			}
		});

		JFXButton saveBtn = new JFXButton("Save ");
		saveBtn.setOnAction(e -> {
			sunBurstMap.values().stream().forEach(b -> {
				try {

					/*
					 * mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
					 * mapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
					 */
					String str = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(b);
					System.out.println(str);
					mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
					mapper.reader().forType(SunburstBoundary.class).readValue(str);
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

		box.getChildren().addAll(addRoot, addRelease, addFeature, removeNode, previewBtn, saveBtn);
		// add it dynamically to the root
		vbox.getChildren().addAll(box, previewBox);
		// buildSunBurnTile(null);
		return box;

	}

	private HBox radialChartEditor() {

		HBox previewBox = new HBox(5);

		JFXButton addRelease = new JFXButton("Add Chart Details (+)");
		addRelease.setOnAction(e -> {
			JFXTextField field = new JFXTextField();
			field.setPromptText("Enter Release Name");

			JFXTextField field2 = new JFXTextField();
			field2.setPromptText("Enter Release Points");

			JFXColorPicker colorPicker = new JFXColorPicker();

			this.displayDialog("Radial / Donut Builder", 2, 2, colorPicker, field, field2);
		});

		JFXButton previewButton = new JFXButton("Preview");
		previewButton.setOnAction(e -> {
			if (previewBox.getChildren().size() > 0)
				previewBox.getChildren().remove(0, previewBox.getChildren().size());
			previewBox.getChildren().add(radialChartBuilder());
		});

		JFXButton removeBtn = new JFXButton("Remove Node");
		removeBtn.setOnAction(e -> {
			if (treeView.getSelectionModel().getSelectedItem() == null
					|| treeView.getSelectionModel().getSelectedItem().getParent() == null) {
				popOutAlert("Please select a valid node to Remove !!");
			} else {
				String selectItemName = treeView.getSelectionModel().getSelectedItem().getValue();

				// remove the element from chart list.
				radialChart.removeIf(s -> s.getName().equals(selectItemName));

				TreeItem<String> mainParent = treeView.getSelectionModel().getSelectedItem().getParent();
				mainParent.getChildren().remove(treeView.getSelectionModel().getSelectedItem());
			}
		});

		HBox box = new HBox(10);
		box.getChildren().addAll(addRelease, previewButton, removeBtn);
		vbox.getChildren().addAll(box, previewBox);

		return new HBox();

	}

	private Tile radialChartBuilder() {
		return TileBuilder.create().skinType(SkinType.RADIAL_CHART).prefSize(500, 500).title("RadialChart")
				.text("Some text").textVisible(false).chartData(radialChart).build();
	}

	private HBox donutChartEditor() {

		HBox previewBox = new HBox(5);

		JFXButton addRelease = new JFXButton("Add Chart Details (+)");
		addRelease.setOnAction(e -> {
			JFXTextField field = new JFXTextField();
			field.setPromptText("Enter Release Name");

			JFXTextField field2 = new JFXTextField();
			field2.setPromptText("Enter Release Points");

			JFXColorPicker colorPicker = new JFXColorPicker();

			this.displayDialog("Radial / Donut Builder", 2, 2, colorPicker, field, field2);
		});

		JFXButton previewButton = new JFXButton("Preview");
		previewButton.setOnAction(e -> {
			if (previewBox.getChildren().size() > 0)
				previewBox.getChildren().remove(0, previewBox.getChildren().size());
			previewBox.getChildren().add(donutChartBuilder());
		});

		JFXButton removeBtn = new JFXButton("Remove Node");
		removeBtn.setOnAction(e -> {
			if (treeView.getSelectionModel().getSelectedItem() == null
					|| treeView.getSelectionModel().getSelectedItem().getParent() == null) {
				popOutAlert("Please select a valid node to Remove !!");
			} else {
				String selectItemName = treeView.getSelectionModel().getSelectedItem().getValue();

				// remove the element from chart list.
				donutData.removeIf(s -> s.getName().equals(selectItemName));

				TreeItem<String> mainParent = treeView.getSelectionModel().getSelectedItem().getParent();
				mainParent.getChildren().remove(treeView.getSelectionModel().getSelectedItem());
			}
		});

		HBox box = new HBox(10);
		box.getChildren().addAll(addRelease, previewButton, removeBtn);
		vbox.getChildren().addAll(box, previewBox);

		return new HBox();

	}

	private Node donutChartBuilder() {
		return TileBuilder.create().skinType(SkinType.DONUT_CHART).prefSize(500, 500).title("DonutChart")
				.text("Some text").textVisible(false).chartData(donutData).build();

	}

	private void displayDialog(String dialogHeading, int chartType, int action, JFXColorPicker colorPicker,
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
		if (colorPicker != null) {
			box.getChildren().add(colorPicker);
		}
		layout.setBody(box);

		JFXButton okBtn = new JFXButton("OK");
		okBtn.getStyleClass().add("dialog-accept");
		okBtn.setOnAction(event -> {
			// if all fields are validated then only hide the dialog.
			if (DashboardUtil.validateTextField(fields)) {
				if (chartType == 1) {
					if (action == 1) {
						if (sunBurstMap.containsKey(fields[0].getText())) {
							// means already there is a entry for it. So ask them to add an other Name.
							popOutAlert("This Root Name is already selected!!");
							return;
						} else if (treeView.getSelectionModel().getSelectedItem() == null
								|| treeView.getSelectionModel().getSelectedItem().getParent() != null) {
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
							ReleaseBoundary boundary = /* snb. */new ReleaseBoundary(fields[0].getText(),
									Double.valueOf(fields[1].getText()), new CustomColor(colorPicker.getValue()),
									new LinkedList<>());
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
							ReleaseBoundary rb = boundaries.stream().filter(s -> s.getFieldName().equals(str))
									.findFirst().get();
							if (rb.getAttrBoundaries() == null)
								rb.setAttrBoundaries(new LinkedList<>());
							ReleaseAttrBoundary rab = /* rb. */new ReleaseAttrBoundary(fields[0].getText(),
									Double.valueOf(fields[1].getText()), new CustomColor(colorPicker.getValue()));
							rb.getAttrBoundaries().add(rab);
							treeView.getSelectionModel().getSelectedItem().getChildren()
									.add(new TreeItem<String>(rab.getFieldName()));
							treeView.getSelectionModel().getSelectedItem().setExpanded(true);
						} else {
							popOutAlert("Please Select a valid Sub Node (Release Node) to Add the Feature Details!!!");
							return;
						}
					}
				} else if (chartType == 2) {
					if (treeView.getSelectionModel().getSelectedItem() != null) {
						radialChart.add(new ChartData(fields[0].getText(), Double.valueOf(fields[1].getText()),
								colorPicker.getValue()));
						treeView.getSelectionModel().getSelectedItem().getChildren()
								.add(new TreeItem<String>(fields[0].getText()));
						treeView.getSelectionModel().getSelectedItem().setExpanded(true);
					} else {
						popOutAlert("No Root Node Selected. Please select a Root Node!!!");
					}
				} else if (chartType == 3) {
					if (treeView.getSelectionModel().getSelectedItem() != null) {
						donutData.add(new ChartData(fields[0].getText(), Double.valueOf(fields[1].getText()),
								colorPicker.getValue()));
						treeView.getSelectionModel().getSelectedItem().getChildren()
								.add(new TreeItem<String>(fields[0].getText()));
						treeView.getSelectionModel().getSelectedItem().setExpanded(true);
					} else {
						popOutAlert("No Root Node Selected. Please select a Root Node!!!");
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

	private Tile buildSunBurnTile(TreeNode tree) {
		sunburstTile = TileBuilder.create().skinType(SkinType.SUNBURST).prefSize(500, 500).title("Release Highlights")
				.textVisible(false).sunburstTree(tree).sunburstBackgroundColor(Tile.BACKGROUND)
				.sunburstTextColor(Tile.BACKGROUND).sunburstUseColorFromParent(true)
				.sunburstTextOrientation(TextOrientation.ORTHOGONAL).sunburstAutoTextColor(true)
				.sunburstUseChartDataTextColor(true).sunburstInteractive(true).build();

		return sunburstTile;
	}

	private void popOutAlert(String text) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Sunburst Chart");
		alert.setContentText(text);
		alert.showAndWait();
	}

}