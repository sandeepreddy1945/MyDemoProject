/**
 * 
 */
package com.app.chart.perfomance.dashboard;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;

import com.app.chart.fx.FilesUtil;
import com.app.chart.model.TeamMember;

import eu.hansolo.FunLevelGauge;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import eu.hansolo.medusa.Section;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.Tile.SkinType;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.tools.Helper;
import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * @author Sandeep
 * 
 *         Dashboard to display Image,The Next Details Tile , Individual
 *         Perfomance of scores Tile, Ontime Details Tile.
 *
 */
public class DashboardIndividualStatsViewer extends DashboardAbstract {

	private Pagination pagination;
	private static final int ITEMS_PER_PAGE = 1;
	private int pageCount;
	private int animationPageIndex = 0;
	private long lastTimerCall;
	private int page;
	private Tile memberGaugeTile;
	private FunLevelGauge memberFunGuage;
	private AnimationTimer animationTimer;
	private Gauge modernGuage;
	private Gauge thirdGuage;
	private Gauge secondGuage;
	private Tile flipTile;

	public DashboardIndividualStatsViewer(List<TeamMember> teamMembers) {
		super(teamMembers);

		// animate the internal Details
		animateInternalDetails();
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

	public HBox createPage(int pageIndex) {

		HBox indBox = new HBox(10);
		TeamMember member = teamMembers.get(pageIndex);

		// initialize page
		page = pageIndex;

		// First fetch the Image
		// using page index as we have only 1 member per page.
		ImageView memberImage = fetchMemberImage(member);

		// member details .
		Tile memberTextTile = TileBuilder.create().skinType(SkinType.TEXT).prefSize(230, 200).title(member.getName())
				.text("Team Member")
				.description("Portal Id: "+ member.getPortalId() + "\n" + member.getName() + "\n" + member.getDescription())
				.descriptionAlignment(Pos.CENTER).textVisible(true).build();

		// member perfomance score tile
		Label label = new Label("Employee Perfomance");
		label.setFont(Font.font("Verdana", 20));
		label.setTextFill(Color.WHITESMOKE);
		// TODO to apply css to the label for text.
		memberFunGuage = new FunLevelGauge();
		memberFunGuage.setPrefSize(200, 200);
		// pass the value of percentage to it i.e from 0.0 t0 0.99 for display..
		// Animate it by setting some delay on it.
		memberFunGuage.setLevel(/*
								 * Double.valueOf((member.getScore1() + member.getScore2() +
								 * member.getScore3())) / 300
								 */0);
		VBox funBox = new VBox(5);
		funBox.getChildren().addAll(label, memberFunGuage);

		// member ontime guage tile
		memberGaugeTile = TileBuilder.create().skinType(SkinType.GAUGE).prefSize(200, 200).title("Quarterly Perfomance")
				.unit("P").threshold(150).build();
		memberGaugeTile.setMinValue(0);
		memberGaugeTile.setMaxValue(300);
		memberGaugeTile.setAnimationDuration(1200);
		// Animate it by setting some delay on it.
		memberGaugeTile.setValue(/* member.getOnTime() + member.getValueAdd() + member.getQuality() */0);

		// 1st month perfomance
		modernGuage = GaugeBuilder.create().skinType(eu.hansolo.medusa.Gauge.SkinType.MODERN).prefSize(200, 200)
				.sections(new Section(85, 90, "", Color.rgb(204, 0, 0, 0.5)),
						new Section(90, 95, "", Color.rgb(204, 0, 0, 0.75)),
						new Section(95, 100, "", Color.rgb(204, 0, 0)))
				.sectionTextVisible(true).title(member.getIntreval1() == null ? "JAN" : member.getIntreval1())
				.unit("Points").threshold(85).thresholdVisible(true).animated(true).build();

		// alternative guage here for second one
		// TODO select one of them both.
		/*
		 * GaugeBuilder.create() .skinType(SkinType.SIMPLE_DIGITAL)
		 * .foregroundBaseColor(Color.rgb(0, 249, 222)) .barColor(Color.rgb(0, 249,
		 * 222)) .unit("KPH") .animated(true) .build();
		 */
		// imp to exempt from null pointers, which stop the display itself.
		secondGuage = GaugeBuilder.create().skinType(eu.hansolo.medusa.Gauge.SkinType.FLAT)
				.title(member.getIntreval2() == null ? "FEB" : member.getIntreval2()).unit("Points").prefSize(200, 200)
				.foregroundBaseColor(Color.WHITE).animated(true).build();

		thirdGuage = GaugeBuilder.create().skinType(eu.hansolo.medusa.Gauge.SkinType.SIMPLE_SECTION)
				.title(member.getIntreval3() == null ? "MAR" : member.getIntreval3()).prefSize(200, 200).unit("Points")
				.titleColor(Color.WHITE).unitColor(Color.WHITE).valueColor(Color.WHITE)
				.sections(new Section(0, 33, Color.LIME), new Section(33, 66, Color.YELLOW),
						new Section(66, 100, Color.CRIMSON))
				.build();

		flipTile = TileBuilder.create().skinType(SkinType.FLIP).prefSize(200, 200).characters(Helper.TIME_00_TO_59)
				.flipTimeInMS(500).flipText(" ").title("Timer ").build();
		
		Tile memberImageDP = TileBuilder.create().skinType(SkinType.CUSTOM).prefSize(200, 200).title("")
				.text("").graphic(memberImage).roundedCorners(true).build();

		// stop and start the animation timer once the page is reloaded.
		// This consevers the threads as well.
		if (animationTimer != null) {
			lastTimerCall = System.nanoTime();
			animationTimer.start();
		}

		// add all componets in order
		indBox.getChildren().addAll(memberImageDP, memberTextTile, funBox, memberGaugeTile, modernGuage, secondGuage,
				thirdGuage, flipTile);
		HBox.setHgrow(indBox, Priority.ALWAYS);

		return indBox;
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

	public ImageView fetchMemberImage(TeamMember member) {
		// for now hard coded for dev purpose
		// TODO to change the hard coded value to get the image by portal ID.

		File imgFile = null;
		try {
			URL url1 = new File(FilesUtil.IMAGES_DIR_PATH + FilesUtil.SLASH + member.getPortalId() + ".png").toURI()
					.toURL();
			imgFile = new File(url1.toURI().getPath());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			// image not found assign default image.
			try {
				imgFile = new File(
						getClass().getClassLoader().getResource("com/app/chart/images/default.png").toURI().toURL().getPath());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ImageView imageView = new ImageView();
		imageView.setFitHeight(190);
		imageView.setFitWidth(190);
		imageView.setPreserveRatio(false);
		try {
			imageView.setImage(new Image(FileUtils.openInputStream(imgFile)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return imageView;
	}

	private void animateInternalDetails() {
		lastTimerCall = System.nanoTime();

		animationTimer = new AnimationTimer() {
			@Override
			public void handle(long now) {

				if (now > lastTimerCall + 1_500_000_000L) {

					// call the animation timer here in for all the instances applicable.
					if (teamMembers.get(page) != null && memberFunGuage != null && memberFunGuage != null) {
						// initialize the flip tile
						if (flipTile != null)
							flipTile.setFlipText(
									Helper.TIME_0_TO_5[new Random().nextInt(Helper.TIME_0_TO_5.length - 1)]);
						TeamMember member = teamMembers.get(page);
						memberFunGuage.setLevel(
								Double.valueOf((member.getScore1() + member.getScore2() + member.getScore3())) / 300);
						memberGaugeTile.setValue(member.getOnTime() + member.getValueAdd() + member.getQuality());

						// modern guage 1st month
						modernGuage.setValue(member.getScore1());
						secondGuage.setValue(member.getScore2());
						thirdGuage.setValue(member.getScore3());

					}
					lastTimerCall = now;
					animationTimer.stop();
				}

			}

		};

		animationTimer.start();

	}

}
