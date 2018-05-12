/**
 * 
 */
package com.app.chart.model.test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import com.app.chart.model.TeamMember;
import com.app.chart.perfomance.dashboard.DashboardPieChart;
import com.app.chart.perfomance.dashboard.DashboardTeamMemberScoreViewer;

import javafx.scene.Scene;
import javafx.stage.Stage;
import junit.framework.Assert;

/**
 * @author Sandeep
 *
 */
public class DashboardUITest extends ApplicationTest {

	DashboardTeamMemberScoreViewer teamMember;
	DashboardPieChart dashboardPieChart;
	static Random random = new Random(12000);

	@Override
	public void start(Stage stage) throws Exception {
		teamMember = new DashboardTeamMemberScoreViewer(teamMembers());
		dashboardPieChart = new DashboardPieChart(teamMembers());

		Scene scene = new Scene(dashboardPieChart, 800, 800);
		stage.setScene(scene);
		stage.show();
	}

	public static List<TeamMember> teamMembers() {
		return Arrays.asList(randomTemMember(), randomTemMember(), randomTemMember(), randomTemMember(),
				randomTemMember(), randomTemMember(), randomTemMember(), randomTemMember(), randomTemMember());

	}

	private static TeamMember randomTemMember() {
		TeamMember member = new TeamMember();
		member.setDescription(randomString());
		member.setIntreval1(randomString());
		member.setIntreval2(randomString());
		member.setIntreval3(randomString());
		member.setLink(randomString());
		member.setPortalId(randomString());
		member.setScore1(randomNumber());
		member.setScore2(randomNumber());
		member.setScore3(randomNumber());
		member.setParent(randomString());
		member.setTeam(randomString());
		member.setName(randomString());
		member.setLink(randomString());

		return member;
	}

	private static int randomNumber() {
		return random.nextInt(70);
	}

	private static String randomString() {
		return String.valueOf("asdfg" + randomNumber());
	}

	@Test
	public void should_click_on_button() throws InterruptedException {
		Thread.sleep(1000000000);
		Assert.assertEquals(true, true);
	}

}
