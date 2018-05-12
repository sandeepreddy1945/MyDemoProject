/**
 * 
 */
package com.app.chart.perfomance.dashboard;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.app.chart.model.TeamMember;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.skins.BarChartItem;
import javafx.scene.control.Pagination;

/**
 * @author Sandeep Displays the team meber board in order wise of top to bottom.
 */
public class DashboardTeamMemberScoreViewer extends DashboardAbstract {

	private final List<TeamMember> teamMembers;
	private Pagination pagination;
	private static final int ITEMS_PER_PAGE = 5;
	private int pageCount;
	private int animationPageIndex = 0;
	// private Random random = new Random();

	/**
	 * 
	 */
	// TODO to add model object here for displaying the list
	public DashboardTeamMemberScoreViewer(List<TeamMember> teamMembers) {
		this(5, teamMembers);
	}

	/**
	 * @param spacing
	 */
	public DashboardTeamMemberScoreViewer(double spacing, List<TeamMember> teamMembers) {
		super(teamMembers);
		this.teamMembers = teamMembers;
	}

	public Tile createPage(int pageIndex) {

		int page = pageIndex * ITEMS_PER_PAGE;
		int size = pageIndex * ITEMS_PER_PAGE + ITEMS_PER_PAGE > teamMembers.size() ? teamMembers.size()
				: pageIndex * ITEMS_PER_PAGE + ITEMS_PER_PAGE;
		List<BarChartItem> leaderboardChartItems = new ArrayList<>();
		for (int i = page; i < size; i++) {
			String name = teamMembers.get(i).getName();
			// TODO considering 100 per month this is calculated ..recode this if values
			// changes.
			Double score = (double) ((teamMembers.get(i).getScore1() + teamMembers.get(i).getScore2()
					+ teamMembers.get(i).getScore3()) / 3);
			leaderboardChartItems.add(new BarChartItem(name, score, getRandomTileColor()));
		}

		Tile leaderBoardTile = generateBarChartTile(leaderboardChartItems);

		return leaderBoardTile;
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

	@Override
	public void initUI() {
		// set the terms for pagination
		pagination = new Pagination();
		double pageSizes = (double) teamMembers.size() / (double) ITEMS_PER_PAGE;
		pageCount = new BigDecimal(pageSizes).setScale(0, RoundingMode.UP).intValue();
		pagination = new Pagination(pageCount, 0);
		pagination.setPageFactory((Integer pageIndex) -> createPage(pageIndex));
		pagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);

		// add the style sheets to the UI / currently no required
		// getStylesheets().add(STYLESHEET_PATH);

		// add the appgination to UI
		getChildren().add(pagination);

		// add the black background.
		setBackground(DashboardUtil.blackBackGround());

	}

}
