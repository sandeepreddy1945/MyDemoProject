/**
 * 
 */
package com.app.chart.perfomance.dashboard;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.app.chart.model.TeamMember;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.Tile.SkinType;
import eu.hansolo.tilesfx.TileBuilder;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * @author Sandeep Used for the displaying the leader board images in the
 *         dashboard.
 */
public class DashboardImageViewer extends HBox {
	ImageView img1;
	ImageView img2;

	final File img1Path;
	final File img2Path;

	private final List<TeamMember> teamMembers;

	private String name1;
	private String name2;

	/**
	 * @deprecated
	 * 
	 * @param img1Path
	 * @param img2Path
	 * @throws IOException
	 */
	public DashboardImageViewer(File img1Path, File img2Path, List<TeamMember> teamMembers) throws IOException {
		super(5);
		this.img1Path = img1Path;
		this.img2Path = img2Path;
		this.teamMembers = teamMembers;
		this.name1 = teamMembers.get(0).getName();
		this.name2 = teamMembers.get(1).getName();
		init();
	}

	/**
	 * 
	 * @param img1Path
	 * @param img2Path
	 * @throws IOException
	 */
	public DashboardImageViewer(File img1Path, File img2Path, List<TeamMember> teamMembers, String name1, String name2)
			throws IOException {
		super(5);
		this.img1Path = img1Path;
		this.img2Path = img2Path;
		this.teamMembers = teamMembers;
		this.name1 = name1;
		this.name2 = name2;
		init();
	}

	/**
	 * @throws IOException
	 * 
	 */
	public DashboardImageViewer(String img1PathUrl, String img2PathUrl, List<TeamMember> teamMembers)
			throws IOException {
		super(5);
		this.img1Path = new File(img1PathUrl);
		this.img2Path = new File(img2PathUrl);
		this.teamMembers = teamMembers;
		init();
	}

	private void init() throws IOException {
		// consider only two images for leader board.
		img1 = new ImageView();
		img2 = new ImageView();

		// setting dimensions
		img1.setFitHeight(250);
		img1.setFitWidth(200);

		img2.setFitHeight(250);
		img2.setFitWidth(200);

		img1.setPreserveRatio(false);
		img2.setPreserveRatio(false);

		// No Glow Effect let the image be as it is.
		/*
		 * Glow glow = new Glow(); glow.setLevel(0.4);
		 * 
		 * img1.setEffect(glow); img2.setEffect(glow);
		 */

		// set the image paths
		img1.setImage(new Image(FileUtils.openInputStream(img1Path)));
		img2.setImage(new Image(FileUtils.openInputStream(img2Path)));

		Tile tile1 = TileBuilder.create().skinType(SkinType.CUSTOM).prefSize(200, 250).title("Performer of Quarter")
				.text(name1).graphic(img1).roundedCorners(true).build();

		Tile tile2 = TileBuilder.create().skinType(SkinType.CUSTOM).prefSize(200, 250).title("Performer of Month")
				.text(name2).graphic(img2).roundedCorners(true).build();

		// setBlendMode(BlendMode.COLOR_BURN);

		// set black background
		setBackground(DashboardUtil.blackBackGround());

		getChildren().addAll(Arrays.asList(tile1, tile2));

	}

	// TODO calculate the 1st and 2nd toppers and pass their title and name score
	// and other details to the images in leader board.

}
