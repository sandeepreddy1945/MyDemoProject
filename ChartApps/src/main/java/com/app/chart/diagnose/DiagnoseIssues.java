/**
 * 
 */
package com.app.chart.diagnose;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.app.chart.fx.FilesUtil;
import com.app.chart.model.ManagerDetailBoundary;
import com.app.chart.model.PerfomanceBoardBoundary;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Sandeep Reddy Battula <br>
 *
 *         Class used to disgnose the problems in application .<br>
 *         Currently it diagnoses any missing images that are required .<br>
 *         This searches the images missing both from charts as well as the
 *         perfomance graphs .
 */
public class DiagnoseIssues extends HBox {

	private ObservableList<DiagnoseIssueTableBoundary> members = FXCollections
			.observableArrayList(/* constructTestBoundary() */);
	// build tree
	final TreeItem<DiagnoseIssueTableBoundary> root = new RecursiveTreeItem<>(members,
			RecursiveTreeObject::getChildren);

	JFXTreeTableView<DiagnoseIssueTableBoundary> tableView = new JFXTreeTableView<>(root);

	private List<PerfomanceBoardBoundary> perfomanceBoardBoundaries;

	public static Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
	public static double WIDTH = visualBounds.getWidth() - 100;
	public static double HEIGHT = visualBounds.getHeight() - 50;

	VBox mainBox = new VBox(10);
	private ObjectMapper mapper = new ObjectMapper();

	/**
	 * 
	 * @param perfomanceBoardBoundaries
	 */
	public DiagnoseIssues(List<PerfomanceBoardBoundary> perfomanceBoardBoundaries) {
		super(10);
		this.perfomanceBoardBoundaries = perfomanceBoardBoundaries;

		initUI();
	}

	/**
	 * 
	 * @param perfomanceBoardBoundaries
	 */
	public DiagnoseIssues(File perfomanceADetailFile) {
		super(10);

		loadPerfomanceDetailsFromFile(perfomanceADetailFile);

		initUI();
	}

	private void loadPerfomanceDetailsFromFile(File dataFile) {

		try {
			if (dataFile != null && dataFile.exists()
					&& FileUtils.readFileToString(dataFile, Charset.defaultCharset()).length() > 0) {
				String fileData = FileUtils.readFileToString(dataFile, Charset.defaultCharset());
				System.out.println(fileData);
				mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);

				List<PerfomanceBoardBoundary> dataList = mapper.readValue(fileData,
						mapper.getTypeFactory().constructCollectionType(List.class, PerfomanceBoardBoundary.class));
				dataList.forEach(System.out::println);

				// set the list with details
				this.perfomanceBoardBoundaries = dataList;

			} else {
				// instntiate the perfomance list as not data is present.
				perfomanceBoardBoundaries = new ArrayList<>();
			}
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	private void initUI() {

		// construct the table first.
		tableView.setMinSize(WIDTH, HEIGHT - 180);
		tableView.setPrefSize(WIDTH, HEIGHT - 180);
		tableView.setShowRoot(false);
		tableView.setEditable(true);
		tableView.setPadding(new Insets(15));

		JFXButton diagnose = new JFXButton("Diagnose Problems");
		diagnose.setAlignment(Pos.BOTTOM_RIGHT);
		diagnose.setPadding(new Insets(10));

		diagnose.setOnAction(this::diagnoseProblemsAndSolutions);

		mainBox.getChildren().addAll(initTableView(), tableView, diagnose);

		getChildren().add(mainBox);

	}

	/**
	 * Diagnoses the problems and solutins.
	 * 
	 * @param e
	 */
	// TODO implement this one for Chart Boundaries as well the required image
	// libraries.
	private void diagnoseProblemsAndSolutions(ActionEvent e) {
		// do a search for the images required on performance board.
		if (perfomanceBoardBoundaries != null && perfomanceBoardBoundaries.size() > 0) {
			perfomanceBoardBoundaries.stream().map(PerfomanceBoardBoundary::getTeamMembers).forEach(t -> {
				t.forEach(b -> {
					Path path = Paths.get(FilesUtil.IMAGES_DIR_PATH + FilesUtil.SLASH + b.getPortalId() + ".png");
					if (!Files.exists(path)) {
						System.out
								.println("Image Doesnot exist for Portal Id: " + b.getPortalId() + " - " + b.getName());
						members.add(constructTableMemberBoundary(b.getPortalId() + " - " + b.getName(),
								"Image -> " + b.getPortalId() + ".png" + "  " + "Missing!!",
								"Image Doesnot Exist In Req Folder",
								"Copy The PNG Image to folder: " + FilesUtil.IMAGES_DIR_PATH));
					}
					/*
					 * path.forEach(p -> { Files.exists(p); });
					 */
				});
			});

			perfomanceBoardBoundaries.stream().forEach(p -> {
				ManagerDetailBoundary b = p.getManagerDetailBoundary();
				Path path = Paths.get(FilesUtil.IMAGES_DIR_PATH + FilesUtil.SLASH + b.getPortalId() + ".png");
				if (!Files.exists(path)) {
					System.out.println("Image Doesnot exist for Portal Id: " + b.getPortalId() + " - " + b.getName());
					members.add(constructTableMemberBoundary(b.getPortalId() + " - " + b.getName(),
							"Image -> " + b.getPortalId() + ".png" + "  " + "Missing!!",
							"Image Doesnot Exist In Req Folder",
							"Copy The PNG Image to folder: " + FilesUtil.IMAGES_DIR_PATH));
				}
			});

			// fire table with changes.
			tableView.fireEvent(e);
		}

	}

	private HBox initTableView() {

		JFXTreeTableColumn<DiagnoseIssueTableBoundary, String> probleWithCol = new JFXTreeTableColumn<>("Problem With");
		probleWithCol.setPrefWidth(200);
		probleWithCol
				.setCellValueFactory((TreeTableColumn.CellDataFeatures<DiagnoseIssueTableBoundary, String> param) -> {
					if (probleWithCol.validateValue(param)) {
						return param.getValue().getValue().getProbleWith();
					} else {
						return probleWithCol.getComputedValue(param);
					}
				});

		JFXTreeTableColumn<DiagnoseIssueTableBoundary, String> problemCauseCol = new JFXTreeTableColumn<>(
				"Problem Cause");
		problemCauseCol.setPrefWidth(300);
		problemCauseCol
				.setCellValueFactory((TreeTableColumn.CellDataFeatures<DiagnoseIssueTableBoundary, String> param) -> {
					if (problemCauseCol.validateValue(param)) {
						return param.getValue().getValue().getProblemCause();
					} else {
						return problemCauseCol.getComputedValue(param);
					}
				});

		JFXTreeTableColumn<DiagnoseIssueTableBoundary, String> problemDescCol = new JFXTreeTableColumn<>(
				"Problem Description");
		problemDescCol.setPrefWidth(350);
		problemDescCol
				.setCellValueFactory((TreeTableColumn.CellDataFeatures<DiagnoseIssueTableBoundary, String> param) -> {
					if (problemDescCol.validateValue(param)) {
						return param.getValue().getValue().getProblemDesc();
					} else {
						return problemDescCol.getComputedValue(param);
					}
				});

		JFXTreeTableColumn<DiagnoseIssueTableBoundary, String> probelemSolutionCol = new JFXTreeTableColumn<>(
				"Problem Solution");
		probelemSolutionCol.setPrefWidth(450);
		probelemSolutionCol
				.setCellValueFactory((TreeTableColumn.CellDataFeatures<DiagnoseIssueTableBoundary, String> param) -> {
					if (probelemSolutionCol.validateValue(param)) {
						return param.getValue().getValue().getProbelemSolution();
					} else {
						return probelemSolutionCol.getComputedValue(param);
					}
				});

		// set colums to table
		tableView.getColumns().addAll(probleWithCol, problemCauseCol, problemDescCol, probelemSolutionCol);

		HBox box = new HBox(20);
		Label sizeLbl = new Label("Table Size: ");
		Label size = new Label();
		JFXTextField searchTF = new JFXTextField();
		searchTF.setPromptText("Search Text Here!!!");
		size.textProperty().bind(Bindings.createStringBinding(() -> String.valueOf(tableView.getCurrentItemsCount()),
				tableView.currentItemsCountProperty()));

		searchTF.textProperty().addListener((o, oldVal, newVal) -> {
			tableView.setPredicate(userProp -> {
				final DiagnoseIssueTableBoundary user = userProp.getValue();
				return user.probleWith.get().toLowerCase().contains(newVal.toLowerCase())
						|| user.problemCause.get().contains(newVal) || user.problemDesc.get().contains(newVal)
						|| user.probelemSolution.get().contains(newVal);
			});
		});
		searchTF.setAlignment(Pos.CENTER_LEFT);
		searchTF.setMinSize(450, 15);
		box.getChildren().addAll(searchTF, sizeLbl, size);
		box.setPrefSize(600, 20);
		box.setPadding(new Insets(15));

		return box;
	}

	private DiagnoseIssueTableBoundary constructTableMemberBoundary(String probleWith, String problemCause,
			String probelmDesc, String probelmSoln) {
		DiagnoseIssueTableBoundary m = new DiagnoseIssueTableBoundary();
		// instantiate fields
		m.setProbleWith(new SimpleStringProperty());
		m.setProblemCause(new SimpleStringProperty());
		m.setProblemDesc(new SimpleStringProperty());
		m.setProbelemSolution(new SimpleStringProperty());

		m.getProbleWith().set(probleWith);
		m.getProblemCause().set(problemCause);
		m.getProblemDesc().set(probelmDesc);
		m.getProbelemSolution().set(probelmSoln);

		return m;
	}

	@Getter
	@Setter
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public static class DiagnoseIssueTableBoundary extends RecursiveTreeObject<DiagnoseIssueTableBoundary> {

		StringProperty probleWith;
		StringProperty problemCause;
		StringProperty problemDesc;
		StringProperty probelemSolution;
		// TODO add score getters and calendar events.
	}

}
