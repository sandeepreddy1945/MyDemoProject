/**
 * 
 */
package com.app.chart.perfomance.dashboard;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * @author Sandeep
 *
 */
public class DashboardHeader extends HBox {

	final File logo1;
	final File logo2;
	final String dashboardLbl;

	/**
	 * @throws IOException
	 * 
	 */
	public DashboardHeader(File f1, File f2, String dashboardLbl) throws IOException {
		this(5, f1, f2, dashboardLbl);

	}

	/**
	 * @param spacing
	 * @throws IOException
	 */
	public DashboardHeader(double spacing, File f1, File f2, String dashboardLbl) throws IOException {
		super(spacing);
		this.logo1 = f1;
		this.logo2 = f2;
		this.dashboardLbl = dashboardLbl;
		init();

	}

	private void init() throws IOException {
		ImageView imageView1 = new ImageView(new Image(FileUtils.openInputStream(logo1)));
		ImageView imageView2 = new ImageView(new Image(FileUtils.openInputStream(logo2)));
		HBox box = new HBox();
		Label dashBoardLbl = new Label(dashboardLbl);
		dashBoardLbl.setStyle("-fx-font: 36 arial;");
		box.setBackground(DashboardUtil.LIGHT_BLUE_BACKGROUND);
		box.getChildren().add(dashBoardLbl);
		HBox.setHgrow(dashBoardLbl, Priority.ALWAYS);
		box.setAlignment(Pos.CENTER);

		// dimesnsions for images
		imageView1.setFitWidth(200);
		imageView1.setFitHeight(200);

		imageView2.setFitWidth(300);
		imageView2.setFitHeight(200);

		imageView1.setPreserveRatio(true);
		imageView2.setPreserveRatio(true);

		getChildren().addAll(imageView1, box, imageView2);

		setHgrow(box, Priority.ALWAYS);
		setAlignment(Pos.TOP_CENTER);

	}

}
