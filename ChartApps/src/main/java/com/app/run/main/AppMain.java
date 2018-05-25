/**
 * 
 */
package com.app.run.main;

import com.app.chart.fx.AddressBook;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.Tile.SkinType;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * @author Sandeep Reddy Battula
 *
 */
public class AppMain extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		stage.setMaximized(true);

		Scene scene = new Scene(new AddressBook().fetchMainDisplayBox(), 1200, 900);
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

		ImageView perfomanceBoardEditorImage = new ImageView(
				new Image(getClass().getClassLoader().getResourceAsStream("com/app/chart/images/PerfomanceBoard.png")));
		perfomanceBoardEditorImage.setFitWidth(230);
		perfomanceBoardEditorImage.setFitHeight(230);
		perfomanceBoardEditorImage.setPreserveRatio(false);
		Tile perfomanceBoardEditor = buildTileFromData("Edit Perfomance Board", "Perfomance Board Editor",
				perfomanceBoardEditorImage);
		// add the main components.
		ImageView runEditorImg = new ImageView(
				new Image(getClass().getClassLoader().getResourceAsStream("com/app/chart/images/RunUI.PNG")));
		runEditorImg.setFitWidth(230);
		runEditorImg.setFitHeight(230);
		runEditorImg.setPreserveRatio(false);
		Tile runUIEditort = buildTileFromData("Dsiplay Order Editor", "View Ordering Editor", runEditorImg);

		Tile disgnosticsTool = buildTextTileFromData("Diagnostic Tools", "Runs The App Diagnostics",
				"Run The App Diagnostics Tool.");

		gridPane.add(addressBookTile, 0, 0);
		gridPane.add(perfomanceBoardEditor, 1, 0);
		gridPane.add(runUIEditort, 2, 0);
		gridPane.add(disgnosticsTool, 3, 0);

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
