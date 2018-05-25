/**
 * 
 */
package com.app.run.main;

import java.io.IOException;

import com.app.chart.dashboard.ui.PerfomanceBoardDetails;
import com.app.chart.diagnose.DiagnoseIssues;
import com.app.chart.fx.AddressBook;
import com.app.chart.perfomance.dashboard.DashboardUtil;
import com.app.chart.run.ui.AppSequencerUI;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.Tile.SkinType;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * @author Sandeep Reddy Battula
 *
 */
public class AppMain extends Application {

	public static Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
	public static double WIDTH = visualBounds.getWidth() - 100;
	public static double HEIGHT = visualBounds.getHeight() - 50;
	private Stage stage;

	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		// stage.setMaximized(true);

		Scene scene = new Scene(contructUIPane(), 600, 600);
		stage.setScene(scene);
		stage.show();

	}

	public GridPane contructUIPane() {
		GridPane gridPane = new GridPane();
		ImageView addrBookImage = new ImageView(
				new Image(getClass().getClassLoader().getResourceAsStream("com/app/chart/images/AppChartEditor.PNG")));
		addrBookImage.setFitWidth(230);
		addrBookImage.setFitHeight(230);
		addrBookImage.setPreserveRatio(false);
		Tile addressBookTile = buildTileFromData("Edit Organization Charts", "Org Charts Editor", addrBookImage);

		addressBookTile.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			Scene scene = null;
			try {
				scene = new Scene(new AddressBook().fetchMainDisplayBox(), WIDTH, HEIGHT);
				scene.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, w -> {
					GridPane pane = contructUIPane();
					Scene sc = new Scene(pane);
					stage.setScene(sc);
					stage.toFront();

				});
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			stage.setScene(scene);
			stage.toFront();
			stage.setMaximized(true);
			stage.setTitle("MPS Org chart Editor");
		});

		ImageView perfomanceBoardEditorImage = new ImageView(
				new Image(getClass().getClassLoader().getResourceAsStream("com/app/chart/images/PerfomanceBoard.png")));
		perfomanceBoardEditorImage.setFitWidth(230);
		perfomanceBoardEditorImage.setFitHeight(230);
		perfomanceBoardEditorImage.setPreserveRatio(false);
		Tile perfomanceBoardEditor = buildTileFromData("Edit Perfomance Board", "Perfomance Board Editor",
				perfomanceBoardEditorImage);
		perfomanceBoardEditor.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			Scene scene = new Scene(new PerfomanceBoardDetails(), WIDTH, HEIGHT);
			stage.setScene(scene);
			scene.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, w -> {
				GridPane pane = contructUIPane();
				Scene sc = new Scene(pane);
				stage.setScene(sc);
				stage.toFront();

			});
			stage.toFront();
			stage.setMaximized(true);
		});

		// add the main components.
		ImageView runEditorImg = new ImageView(
				new Image(getClass().getClassLoader().getResourceAsStream("com/app/chart/images/RunUI.PNG")));
		runEditorImg.setFitWidth(230);
		runEditorImg.setFitHeight(230);
		runEditorImg.setPreserveRatio(false);
		Tile runUIEditort = buildTileFromData("Dsiplay Order Editor", "View Ordering Editor", runEditorImg);

		runUIEditort.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			Scene scene = new Scene(new AppSequencerUI(), WIDTH, HEIGHT);
			scene.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, w -> {
				GridPane pane = contructUIPane();
				Scene sc = new Scene(pane);
				stage.setScene(sc);
				stage.toFront();

			});
			stage.setScene(scene);
			stage.toFront();
			stage.setMaximized(true);
		});

		Tile disgnosticsTool = buildTextTileFromData("Diagnostic Tools", "Runs The App Diagnostics",
				"Run The App Diagnostics Tool.");

		runUIEditort.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			Scene scene = new Scene(new DiagnoseIssues(), WIDTH, HEIGHT);
			scene.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, w -> {
				GridPane pane = contructUIPane();
				Scene sc = new Scene(pane);
				stage.setScene(sc);
				stage.toFront();

			});
			stage.setScene(scene);
			stage.toFront();
			stage.setMaximized(true);
		});

		gridPane.add(addressBookTile, 0, 0);
		gridPane.add(perfomanceBoardEditor, 1, 0);
		gridPane.add(runUIEditort, 0, 1);
		gridPane.add(disgnosticsTool, 1, 1);

		gridPane.setVgap(20);
		gridPane.setHgap(20);
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setPadding(new Insets(10));
		gridPane.setBackground(DashboardUtil.BLACK_BACKGROUND);
		return gridPane;
	}

	/**
	 * To build the Tiles for Adding data.
	 * 
	 * @param tile
	 * @param text
	 * @param imageView
	 * @return
	 */
	private Tile buildTileFromData(String tile, String text, ImageView imageView) {

		Tile dataTile = TileBuilder.create().skinType(SkinType.CUSTOM).prefSize(250, 250).title(tile).text(text)
				.graphic(imageView).roundedCorners(true).build();

		return dataTile;
	}

	/**
	 * To build a text Tile.
	 * 
	 * @param title
	 * @param text
	 * @param description
	 * @return
	 */
	private Tile buildTextTileFromData(String title, String text, String description) {
		Tile textTile = TileBuilder.create().skinType(SkinType.TEXT).prefSize(250, 250).title(title).text(text)
				.description(description).descriptionAlignment(Pos.CENTER).textVisible(true).build();

		return textTile;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);

	}

}
