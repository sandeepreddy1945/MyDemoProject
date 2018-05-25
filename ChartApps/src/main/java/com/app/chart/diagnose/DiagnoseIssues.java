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
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import com.app.chart.fx.FilesUtil;
import com.app.chart.model.ChartBoardBoundary;
import com.app.chart.model.ManagerDetailBoundary;
import com.app.chart.model.PerfomanceBoardBoundary;
import com.app.chart.model.RunJsonBoundary;
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

	// instantiate so in order to skip null pointers.
	private List<PerfomanceBoardBoundary> perfomanceBoardBoundaries = new ArrayList<>();
	private List<RunJsonBoundary> runJsonBoundaries = new ArrayList<>();
	private List<ChartBoardBoundary> chartBoardBoundaries = new ArrayList<>();

	public static Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
	public static double WIDTH = visualBounds.getWidth() - 100;
	public static double HEIGHT = visualBounds.getHeight() - 50;

	VBox mainBox = new VBox(10);
	private ObjectMapper mapper = new ObjectMapper();
	public static final String APP_JSON = "app.json";

	private boolean isPerfomanceListTampered = false;
	private boolean isOrgChartTampered = false;
	private boolean isRunOrderTampered = false;

	/**
	 * 
	 * @param perfomanceBoardBoundaries
	 */
	public DiagnoseIssues(List<PerfomanceBoardBoundary> perfomanceBoardBoundaries) {
		super(10);
		this.perfomanceBoardBoundaries = perfomanceBoardBoundaries;

		initUI();
	}

	public DiagnoseIssues() {
		super(10);
		diagnoseAllTheIssuesPresent();
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

	/**
	 * Diagnoses the issues specified from the perfomance file
	 * 
	 * @param dataFile
	 */
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
			isPerfomanceListTampered = true;
			e.printStackTrace();
		}

	}

	/**
	 * By Default this method diagnoses all the issues present in the Application.
	 */
	private void diagnoseAllTheIssuesPresent() {
		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		// perfomance file
		File perfomanceFile = new File(FilesUtil.DASHBOARD_CONTENT_DATA);
		try {
			if (perfomanceFile != null && perfomanceFile.exists()
					&& FileUtils.readFileToString(perfomanceFile, Charset.defaultCharset()).length() > 0) {
				String fileData = FileUtils.readFileToString(perfomanceFile, Charset.defaultCharset());
				System.out.println(fileData);
				mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);

				List<PerfomanceBoardBoundary> dataList = mapper.readValue(fileData,
						mapper.getTypeFactory().constructCollectionType(List.class, PerfomanceBoardBoundary.class));
				// set the list with details
				this.perfomanceBoardBoundaries = dataList;

			} else {
				// instntiate the perfomance list as not data is present.
				perfomanceBoardBoundaries = new ArrayList<>();
			}
		} catch (IOException e) {
			isPerfomanceListTampered = true;
			e.printStackTrace();
		}

		// next runjson files and images present.
		File runJsonFile = new File(FilesUtil.RUN_PROPS_PATH);
		try {
			String jsonStr = FileUtils.readFileToString(runJsonFile, Charset.defaultCharset());
			if (jsonStr != null && jsonStr.length() > 0) {
				List<RunJsonBoundary> dataList = mapper.readValue(jsonStr,
						mapper.getTypeFactory().constructCollectionType(List.class, RunJsonBoundary.class));
				runJsonBoundaries = dataList;
			}
		} catch (Exception ex) {
			isRunOrderTampered = true;
			ex.printStackTrace();
		}

		// read all the employees present in the chart.
		try {
			Properties properties = new Properties();
			properties.load(FileUtils.openInputStream(new File(FilesUtil.MANAGER_PROPS_PATH)));

			properties.keySet().stream().forEach(o -> {
				// loop on the folders for the org charts
				try {
					String jsonStr = FileUtils.readFileToString(new File(
							FilesUtil.MAIN_APP_PATH + FilesUtil.SLASH + o.toString() + FilesUtil.SLASH + APP_JSON),
							Charset.defaultCharset());
					ChartBoardBoundary chartBoardBoundary = mapper.readValue(jsonStr, ChartBoardBoundary.class);
					chartBoardBoundaries.add(chartBoardBoundary);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});

		} catch (Exception ex) {
			isOrgChartTampered = true;
			ex.printStackTrace();
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
		diagnose.setPadding(new Insets(20));

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

		if (!new File(FilesUtil.MAIN_APP_PATH).exists()) {
			members.add(constructTableMemberBoundary("Main App No Created", "App not yet created",
					"fatal Error : Application will not be launched", "Start Creating the App and its data"));
		}

		if (isPerfomanceListTampered) {
			members.add(constructTableMemberBoundary("Perfomance List", "Peformance Data List File is tampered.",
					"The Perfomance List Data has been modified manually.	",
					"Either re-add the perfomance chart and save else verify logs."));
		}

		if (isRunOrderTampered) {
			members.add(constructTableMemberBoundary("Ordering List", "Ordering  Data List File is tampered.",
					"Ordering List Data has been modified manually.	",
					"Either re-add the Ordering List and save else verify logs."));
		}

		if (isOrgChartTampered) {
			members.add(constructTableMemberBoundary("Org Chart List", "Org Chart  Data List File is tampered.",
					"Org Chart List Data has been modified manually.	",
					"Either re-add the Or Chart List and save else verify logs."));
		}

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

			// then managers.
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

		}
		// now with the run json list
		runJsonBoundaries.stream().forEach(p -> {
			Path path = Paths.get(FilesUtil.IMAGES_DIR_PATH + FilesUtil.SLASH + p.getPath());
			if (!Files.exists(path)) {
				members.add(constructTableMemberBoundary(p.getPath(), "Image -> " + p.getPath() + "  " + "Missing!!",
						"Image Doesnot Exist In Req Folder",
						"Copy The PNG Image to folder: " + FilesUtil.IMAGES_DIR_PATH));
			}
		});

		// now check for all the images on the org chart folder.
		chartBoardBoundaries.stream().forEach(c -> {
			Path path = Paths.get(FilesUtil.MAIN_APP_PATH + FilesUtil.SLASH + c.getFolderName());
			if (!Files.exists(path)) {
				members.add(constructTableMemberBoundary(c.getFolderName(),
						"Folder -> " + c.getFolderName() + "  " + "Missing!!", "Folder Doesnot Exist In Req Main Path",
						"Please Re-Add The Details for the OrgChart: " + c.getHeaderTxt()));
			}

			c.getEmployeeDetails().stream().forEach(emp -> {
				Path path1 = Paths.get(FilesUtil.IMAGES_DIR_PATH + FilesUtil.SLASH + emp.getPortalId() + ".png");
				if (!Files.exists(path1)) {
					System.out
							.println("Image Doesnot exist for Portal Id: " + emp.getPortalId() + " - " + emp.getName());
					members.add(constructTableMemberBoundary(emp.getPortalId() + " - " + emp.getName(),
							"Image -> " + emp.getPortalId() + ".png" + "  " + "Missing!!",
							"Image Doesnot Exist In Req Folder",
							"Copy The PNG Image to folder: " + FilesUtil.IMAGES_DIR_PATH));
				}

			});
		});

		// fire table with changes.
		tableView.fireEvent(e);

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
