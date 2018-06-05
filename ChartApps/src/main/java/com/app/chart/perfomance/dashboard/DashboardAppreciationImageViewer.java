/**
 * 
 */
package com.app.chart.perfomance.dashboard;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.app.chart.model.AppreciationImageBoundary;
import com.app.chart.model.TeamMember;

import eu.hansolo.tilesfx.Tile;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Sandeep
 *
 */
@Slf4j
public class DashboardAppreciationImageViewer extends DashboardAbstract {

	private static final String ACHEIVEMENTS_APPRECIATIONS = "Acheivements & Appreciations";
	private static final String NTTDATA_UPDATES = "NTT DATA NEWS & UPDATES";
	private static final double ITEMS_PER_PAGE = 1;
	private Pagination pagination;
	private int pageCount;
	private List<File> imageFileList = new ArrayList<>();
	private int animationPageIndex;
	private Tile generateCustomTile;
	private List<AppreciationImageBoundary> appreciationImageBoundaries = new ArrayList<>();

	/**
	 * Last one is a dummy parameter.
	 * 
	 * @param teamMembers
	 * @param appreciationImageBoundaries
	 * @param imageFileList
	 */
	public DashboardAppreciationImageViewer(List<TeamMember> teamMembers,
			List<AppreciationImageBoundary> appreciationImageBoundaries, List<File> imageFileList) {
		this(teamMembers, imageFileList.stream().toArray(File[]::new));
		this.appreciationImageBoundaries = appreciationImageBoundaries;
	}

	public DashboardAppreciationImageViewer(List<TeamMember> teamMembers, List<File> imageFileList) {
		super(teamMembers, imageFileList.stream().toArray(File[]::new));
	}

	public DashboardAppreciationImageViewer(List<TeamMember> teamMembers, File... imageFileList) {
		super(teamMembers, imageFileList);
	}

	@Override
	public void initUI() {
		// not in constructor as it has no initialization in the constructor.
		this.imageFileList = Arrays.asList(files);
		if (imageFileList.size() > 0) {
			// set the terms for pagination
			pagination = new Pagination();
			double pageSizes = (double) imageFileList.size() / (double) ITEMS_PER_PAGE;
			pageCount = new BigDecimal(pageSizes).setScale(0, RoundingMode.UP).intValue();
			pagination = new Pagination(pageCount, 0);
			pagination.setPageFactory((Integer pageIndex) -> createPage(pageIndex));
			pagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);

			// add the style sheets to the UI / currently no required
			// getStylesheets().add(STYLESHEET_PATH);

			// add the appgination to UI using Tile
			// TODO change the naming here if wanted
			generateCustomTile = generateCustomTile(pagination, ACHEIVEMENTS_APPRECIATIONS, 450, 500, "Appreciations");
			getChildren().add(generateCustomTile);

			// add the black background.
			setBackground(DashboardUtil.blackBackGround());

		}
	}

	private StackPane createPage(Integer pageIndex) {
		ImageView imageView = null;
		StackPane pane = new StackPane();
		try {
			Image image = new Image(FileUtils.openInputStream(Arrays.asList(files).get(pageIndex)));
			imageView = new ImageView(image);
			imageView.setPreserveRatio(false);
			// imageView.setFitWidth(430);
			// imageView.setFitHeight(310);
			ScrollPane scrollPane = new ScrollPane(imageView);
			scrollPane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
			scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
			scrollPane.setMaxSize(430, 310);
			pane.setMaxHeight(320);
			pane.getChildren().add(scrollPane);

			// change the name of the tile according to news and appreciation image
			generateCustomTile.setTitle(appreciationImageBoundaries.get(pageIndex) != null
					&& appreciationImageBoundaries.get(pageIndex).isAppreciationImg() ? ACHEIVEMENTS_APPRECIATIONS
							: NTTDATA_UPDATES);

		} catch (IOException e) {
			log.error("createPage", e);
		}

		return pane;
	}

	@Override
	public void startTimer(long now) {
		if (animationPageIndex <= pageCount) {
			pagination.setCurrentPageIndex(animationPageIndex++);
		} else {
			animationPageIndex = 0;
			pagination.setCurrentPageIndex(animationPageIndex);
		}
	}

	public int imageListSize() {
		return imageFileList.size();
	}

}
