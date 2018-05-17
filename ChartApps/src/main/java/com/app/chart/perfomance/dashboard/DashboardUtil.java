/**
 * 
 */
package com.app.chart.perfomance.dashboard;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.app.chart.model.TeamMember;
import com.app.chart.perfomance.dashboard.sidebar.DashboardSidePane;
import com.app.chart.perfomance.dashboard.ui.DashboardUI;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;

import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * @author Sandeep
 *
 */
public class DashboardUtil {

	public static final Background BLACK_BACKGROUND = new Background(
			new BackgroundFill(Color.web("#101214"), CornerRadii.EMPTY, Insets.EMPTY));
	public static final Background LIGHT_BLUE_BACKGROUND = new Background(
			new BackgroundFill(Color.web("#ccd9ff"), CornerRadii.EMPTY, Insets.EMPTY));
	public static final String EM1 = "1em";
	public static final String ERROR = "error";
	public static final int TILE_WIDTH = 250;
	public static final int TILE_HEIGHT = 250;

	/**
	 * Black BackGround
	 * 
	 * @return
	 */
	public static Background blackBackGround() {
		return BLACK_BACKGROUND;
	}

	/**
	 * Header Initialization.
	 */
	public static HBox HeaderSegment(HBox hbox) {

		DashboardHeader dashboardHeader = null;
		try {
			URL url1 = ClassLoader.getSystemResource("com/app/chart/images/nttlogo.png");
			URL url2 = ClassLoader.getSystemResource("com/app/chart/images/ntt-data.png");
			File logo1 = new File(url1.toURI().getPath());
			File logo2 = new File(url2.toURI().getPath());

			dashboardHeader = new DashboardHeader(logo1, logo2, "Sandeep Reddy Battula");
			dashboardHeader.setMinSize(DashboardUI.WIDTH - 160, 90);
			dashboardHeader.setPrefSize(DashboardUI.WIDTH, 90);

			// initalize the side panel by passing the main panel.
			// side pane explictly uses it hide and unhide the side pane.
			// TODO to check if there is any other alternative than this.
			DashboardSidePane dashboardSidePane = new DashboardSidePane(dashboardHeader.getImageView1(), hbox);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return dashboardHeader;
	}

	public static HBox FooterSegment() {
		Text creditsText = new Text("© Sandeep Reddy Battula");
		creditsText.setTextAlignment(TextAlignment.RIGHT);
		creditsText.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		creditsText.setFill(Paint.valueOf("#ffffff"));

		Text copyRights = new Text("Copy Rights Reserved.");
		copyRights.setTextAlignment(TextAlignment.LEFT);
		copyRights.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		copyRights.setFill(Paint.valueOf("#ffffff"));

		HBox copyRightsBox = new HBox(copyRights);
		copyRightsBox.setPadding(new Insets(0, 15, 0, 0));
		copyRightsBox.setMinSize(DashboardUI.WIDTH / 2 - 20, 10);
		copyRightsBox.setBackground(DashboardUtil.BLACK_BACKGROUND);
		copyRightsBox.setAlignment(Pos.BOTTOM_LEFT);

		HBox creditsBox = new HBox(creditsText);
		creditsBox.setPadding(new Insets(0, 15, 0, 0));
		creditsBox.setMinSize(DashboardUI.WIDTH / 2 - 20, 10);
		creditsBox.setBackground(DashboardUtil.BLACK_BACKGROUND);
		creditsBox.setAlignment(Pos.BOTTOM_RIGHT);
		HBox.setHgrow(copyRightsBox, Priority.ALWAYS);

		HBox footerBox = new HBox(copyRightsBox, creditsBox);
		footerBox.setAlignment(Pos.BOTTOM_CENTER);
		footerBox.setMinSize(DashboardUI.WIDTH - 10, 10);

		return footerBox;

	}

	public static void buildRequestValidator(JFXTextField... fields) {
		buildRequestValidator(Arrays.asList(fields));
	}

	public static void buildRequestValidator(List<JFXTextField> fields) {
		fields.stream().forEach(DashboardUtil::buildRequestValidator);
	}

	/**
	 * For adding a text filed as required .
	 * 
	 * @param field
	 */
	public static void buildRequestValidator(JFXTextField field) {
		buildRequestValidator(field, null);
	}

	/**
	 * 
	 * @param field
	 * @param errText
	 */
	public static void buildRequestValidator(JFXTextField field, String errText) {
		RequiredFieldValidator validator = new RequiredFieldValidator();
		validator.setMessage(errText == null || errText.length() == 0 ? "Input is Required!!" : errText);
		validator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class).glyph(FontAwesomeIcon.WARNING).size(EM1)
				.styleClass(ERROR).build());

		field.getValidators().add(validator);
		field.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal) {
				field.validate();
			}
		});
	}

	public static boolean validateTextField(JFXTextField... fields) {
		return validateTextField(Arrays.asList(fields));
	}

	public static boolean validateTextField(List<JFXTextField> fields) {
		Optional<JFXTextField> field = fields.stream().filter(f -> f.getText().length() == 0).findFirst();
		return !field.isPresent();
	}

	public static boolean validateTextField(JFXTextField field) {
		return !(field.getText().length() == 0);
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
			Integer score1 = arg0.getScore1() + arg0.getScore2() + arg0.getScore3() + arg0.getOnTime()
					+ arg0.getQuality() + arg0.getValueAdd();
			Integer score2 = arg1.getScore1() + arg1.getScore2() + arg1.getScore3() + arg1.getOnTime()
					+ arg1.getQuality() + arg1.getValueAdd();
			// based on complete scores available
			if (score1 != score2) {
				return score1.compareTo(score2);
			} else {
				// on monthly ratings
				score1 = arg0.getScore1() + arg0.getScore2() + arg0.getScore3();
				score2 = arg1.getScore1() + arg1.getScore2() + arg1.getScore3();
				if (score1 != score2) {
					return score1.compareTo(score2);
				} else {
					// on timely ratings availabe quarterly.
					score1 = arg0.getOnTime() + arg0.getQuality() + arg0.getValueAdd();
					score2 = arg1.getOnTime() + arg1.getQuality() + arg1.getValueAdd();
					return score1.compareTo(score2);
				}

			}
		}

	}
}
