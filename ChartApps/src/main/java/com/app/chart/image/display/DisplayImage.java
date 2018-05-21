/**
 * 
 */
package com.app.chart.image.display;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.app.chart.fx.FilesUtil;
import com.app.chart.perfomance.dashboard.DashboardUtil;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

/**
 * @author Sandeep Reddy Battula
 *
 */
public class DisplayImage extends HBox {

	/**
	 * Visual Bounds of the Screen.
	 */
	public static Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
	public static double MIN_X = visualBounds.getMinX();
	public static double MIN_Y = visualBounds.getMinY();
	public static double MAX_X = visualBounds.getMaxX();
	public static double MAX_Y = visualBounds.getMaxY();
	public static double WIDTH = visualBounds.getWidth();
	public static double HEIGHT = visualBounds.getHeight();

	private ImageView displayImage;
	private boolean isHeaderDisplay;
	private String headerTxt;
	private VBox vBox = new VBox(10);

	/**
	 * @param displayImage
	 */
	public DisplayImage(ImageView displayImage, boolean isHeaderDisplay, String headerTxt) {
		super(10);
		this.displayImage = displayImage;
		this.isHeaderDisplay = isHeaderDisplay;
		this.headerTxt = headerTxt;
		// start the display
		initDisplay();
	}

	/**
	 * @param displayImage
	 */
	public DisplayImage(File displayImageFile, boolean isHeaderDisplay, String headerTxt) {
		super(10);
		// fecth Image from file
		fecthImageFromFile(displayImageFile);
		this.isHeaderDisplay = isHeaderDisplay;
		this.headerTxt = headerTxt;
		// start the display
		initDisplay();
	}

	/**
	 * The file format needs to be specified in the run.json file included.
	 * 
	 * @param displayImageFile
	 * @param isHeaderDisplay
	 * @param headerTxt
	 */
	public DisplayImage(String displayImageFile, boolean isHeaderDisplay, String headerTxt) {
		super(10);
		// fecth Image from file
		fecthImageFromFile(new File(FilesUtil.IMAGES_DIR_PATH + FilesUtil.SLASH + displayImageFile));
		this.isHeaderDisplay = isHeaderDisplay;
		this.headerTxt = headerTxt;
		// start the display
		initDisplay();
	}

	private void fecthImageFromFile(File displayImageFile) {
		if (displayImageFile != null && displayImageFile.exists()) {
			try {
				this.displayImage = new ImageView(new Image(FileUtils.openInputStream(displayImageFile)));
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

	}

	private void initDisplay() {
		HBox hbox = new HBox(10);
		if (displayImage != null) {
			if (isHeaderDisplay) {
				HBox headerBox = DashboardUtil.HeaderSegment(hbox, headerTxt);
				StackPane pane = new StackPane();
				displayImage.setFitHeight(HEIGHT - 100);
				displayImage.setFitWidth(WIDTH - 60);
				displayImage.setPreserveRatio(false);
				pane.getChildren().add(displayImage);
				pane.setPadding(new Insets(20));
				pane.setAlignment(Pos.CENTER);

				vBox.getChildren().addAll(headerBox, pane);

			} else {
				StackPane pane = new StackPane();
				displayImage.setFitHeight(HEIGHT - 10);
				displayImage.setFitWidth(WIDTH - 40);
				pane.getChildren().add(displayImage);
				displayImage.setPreserveRatio(false);
				pane.setPadding(new Insets(20));
				pane.setAlignment(Pos.CENTER);
				vBox.getChildren().addAll(pane);
			}
		}

		hbox.getChildren().add(vBox);
		setBackground(DashboardUtil.BLACK_BACKGROUND);
		getChildren().add(hbox);
		setPrefSize(WIDTH, HEIGHT);
	}

}
