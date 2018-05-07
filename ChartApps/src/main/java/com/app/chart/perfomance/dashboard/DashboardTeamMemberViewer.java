/**
 * 
 */
package com.app.chart.perfomance.dashboard;

import java.util.ArrayList;
import java.util.List;

import com.app.chart.model.TeamMember;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.Tile.SkinType;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.skins.LeaderBoardItem;
import javafx.scene.control.Pagination;
import javafx.scene.layout.HBox;

/**
 * @author Sandeep Displays the team meber board in order wise of top to bottom.
 */
public class DashboardTeamMemberViewer extends HBox {

	private final List<TeamMember> teamMembers;
	private Pagination pagination;
	private static final int ITEMS_PER_PAGE = 5;
	private String STYLESHEET_PATH = ClassLoader
			.getSystemResource("com/app/chart/perfomance/dashboard/teammeberstylesheet.css").toExternalForm();

	/**
	 * 
	 */
	// TODO to add model object here for displaying the list
	public DashboardTeamMemberViewer(List<TeamMember> teamMembers) {
		super(5);
		this.teamMembers = teamMembers;
		init();
	}

	/**
	 * @param spacing
	 */
	public DashboardTeamMemberViewer(double spacing, List<TeamMember> teamMembers) {
		super(spacing);
		this.teamMembers = teamMembers;
		init();
	}

	private void init() {
		// set the terms for pagination
		pagination = new Pagination();
		pagination = new Pagination(teamMembers.size() / ITEMS_PER_PAGE, 0);
		pagination.setPageFactory((Integer pageIndex) -> createPage(pageIndex));
		pagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);

		// add the style sheets to the UI / currently no required
		// getStylesheets().add(STYLESHEET_PATH);

		// add the appgination to UI
		getChildren().add(pagination);

		// add the black background.
		setBackground(DashboardUtil.blackBackGround());
	}

	public Tile createPage(int pageIndex) {

		int page = pageIndex * ITEMS_PER_PAGE;
		int size = pageIndex * ITEMS_PER_PAGE + ITEMS_PER_PAGE > teamMembers.size() ? teamMembers.size()
				: pageIndex * ITEMS_PER_PAGE + ITEMS_PER_PAGE;
		List<LeaderBoardItem> leaderBoardItems = new ArrayList<>();
		for (int i = page; i < size; i++) {
			String name = teamMembers.get(i).getName();
			// TODO considering 100 per month this is calculated ..recode this if values
			// changes.
			Double score = (double) ((teamMembers.get(i).getScore1() + teamMembers.get(i).getScore2()
					+ teamMembers.get(i).getScore3()) / 3);
			leaderBoardItems.add(new LeaderBoardItem(name, score));
		}
		@SuppressWarnings("unchecked")
		Tile leaderBoardTile = TileBuilder.create().skinType(SkinType.LEADER_BOARD)
				.prefSize(DashboardUI.TILE_WIDTH, DashboardUI.TILE_HEIGHT).title("LeaderBoard").
				// TODO to decide a text for the leader board display.
				text("").leaderBoardItems(leaderBoardItems).build();

		return leaderBoardTile;
	}

}
