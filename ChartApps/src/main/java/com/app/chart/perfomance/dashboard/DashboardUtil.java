/**
 * 
 */
package com.app.chart.perfomance.dashboard;

import java.util.Comparator;

import com.app.chart.model.TeamMember;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

/**
 * @author Sandeep
 *
 */
public class DashboardUtil {

	/**
	 * Black BackGround
	 * 
	 * @return
	 */
	public static Background blackBackGround() {
		return new Background(new BackgroundFill(Color.web("#101214"), CornerRadii.EMPTY, Insets.EMPTY));
	}

	/**
	 * Comaparator for list of team members to comare the score for leader board.
	 * 
	 * @author Sandeep
	 *
	 */
	public static class TeamMemberSorter implements Comparator<TeamMember> {

		private static TeamMemberSorter instance = new TeamMemberSorter();

		public static TeamMemberSorter getInstance() {
			return instance;
		}

		@Override
		public int compare(TeamMember arg0, TeamMember arg1) {
			Integer score1 = arg0.getScore1() + arg0.getScore2() + arg0.getScore3();
			Integer score2 = arg1.getScore1() + arg1.getScore2() + arg1.getScore3();
			return score1.compareTo(score2);
		}

	}
}
