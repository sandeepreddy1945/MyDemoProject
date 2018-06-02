/**
 * 
 */
package com.app.chart.perfomance.dashboard;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.app.chart.model.TeamMember;

import javafx.scene.control.Pagination;
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

	private static final double ITEMS_PER_PAGE = 1;
	private Pagination pagination;
	private int pageCount;
	private List<File> imageFileList = new ArrayList<>();
	private int animationPageIndex;

	public DashboardAppreciationImageViewer(List<TeamMember> teamMembers) {
		super(teamMembers);
	}

	@Override
	public void initUI() {

		if (imageFileList.size() > 0) {
			// set the terms for pagination
			pagination = new Pagination();
			double pageSizes = (double) imageFileList.size() - 1 / (double) ITEMS_PER_PAGE;
			pageCount = new BigDecimal(pageSizes).setScale(0, RoundingMode.UP).intValue();
			pagination = new Pagination(pageCount, 0);
			pagination.setPageFactory((Integer pageIndex) -> createPage(pageIndex));
			pagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);

			// add the style sheets to the UI / currently no required
			// getStylesheets().add(STYLESHEET_PATH);

			// add the appgination to UI using Tile
			// TODO change the naming here if wanted
			getChildren()
					.add(generateCustomTile(pagination, "Acheivements & Appreciations", 450, 500, "Appreciations"));

			// add the black background.
			setBackground(DashboardUtil.blackBackGround());

		}
	}

	private StackPane createPage(Integer pageIndex) {
		ImageView imageView = null;
		StackPane pane = new StackPane();
		try {
			imageView = new ImageView(new Image(FileUtils.openInputStream(imageFileList.get(pageIndex))));
			pane.getChildren().add(imageView);
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
