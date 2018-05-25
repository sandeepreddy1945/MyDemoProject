/**
 * 
 */
package com.app.chart.perfomance.dashboard;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;

import javafx.geometry.Pos;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * @author Sandeep
 *
 */
public class DashboardHeader extends HBox {

	final File logo1;
	final File logo2;
	final String dashboardLbl;
	private ImageView imageView1;
	private ImageView imageView2;

	/**
	 * @throws IOException
	 * 
	 */
	public DashboardHeader(File f1, File f2, String dashboardLbl) throws IOException {
		this(0, f1, f2, dashboardLbl);

	}
	
	/**
	 * @throws IOException
	 * 
	 */
	public DashboardHeader(InputStream f1, InputStream f2, String dashboardLbl) throws IOException {
		this(0, f1, f2, dashboardLbl);

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
		imageView1 = new ImageView(new Image(FileUtils.openInputStream(logo1)));
		imageView2 = new ImageView(new Image(FileUtils.openInputStream(logo2)));
		init();
	}
	
	/**
	 * @param spacing
	 * @throws IOException
	 */
	public DashboardHeader(double spacing, InputStream f1, InputStream f2, String dashboardLbl) throws IOException {
		super(spacing);
		this.logo1 = null;
		this.logo2 = null;
		this.dashboardLbl = dashboardLbl;
		imageView1 = new ImageView(new Image(f1));
		imageView2 = new ImageView(new Image(f2));
		init();
	}

	private void init() throws IOException {
		
		HBox box = new HBox();
		Text dashBoardLbl = new Text(dashboardLbl);
		dashBoardLbl.setFont(Font.font("Verdana", 38));
		dashBoardLbl.setStyle("-fx-font-weight: bold");

		// reflection styling
		Reflection r = new Reflection();
		r.setFraction(0.7f);

		dashBoardLbl.setEffect(r);

		// dashBoardLbl.setStyle("-fx-font: 36 arial;");
		box.setBackground(DashboardUtil.LIGHT_BLUE_BACKGROUND);
		box.getChildren().add(dashBoardLbl);
		HBox.setHgrow(dashBoardLbl, Priority.ALWAYS);
		box.setAlignment(Pos.CENTER);
		// size for box
		box.setMinHeight(100);
		box.setMaxHeight(100);
		box.setPrefHeight(100);

		// dimesnsions for images
		imageView1.setFitWidth(250);
		imageView1.setFitHeight(100);

		imageView2.setFitWidth(400);
		imageView2.setFitHeight(100);

		imageView1.setPreserveRatio(true);
		imageView2.setPreserveRatio(true);

		getChildren().addAll(imageView1, box, imageView2);

		setHgrow(box, Priority.ALWAYS);
		setAlignment(Pos.TOP_CENTER);

	}

	/**
	 * @return the imageView1
	 */
	public ImageView getImageView1() {
		return imageView1;
	}

	/**
	 * @return the imageView2
	 */
	public ImageView getImageView2() {
		return imageView2;
	}
}
