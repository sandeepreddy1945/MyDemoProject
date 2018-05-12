/**
 * 
 */
package com.app.chart.perfomance.dashboard;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.app.chart.model.TeamMember;

import eu.hansolo.FunLevelGauge;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.Tile.SkinType;
import eu.hansolo.tilesfx.TileBuilder;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
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

	private final List<TeamMember> teamMembers;
	private Pagination pagination;
	private static final int ITEMS_PER_PAGE = 1;
	private int pageCount;
	private int animationPageIndex = 0;

	public DashboardIndividualStatsViewer(List<TeamMember> teamMembers) {
		super(teamMembers);
		this.teamMembers = teamMembers;
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

		HBox indBox = new HBox(5);
		TeamMember member = teamMembers.get(pageIndex);

		// First fetch the Image
		// using page index as we have only 1 member per page.
		ImageView memberImage = fetchMemberImage(member);

		// member details .
		Tile memberTextTile = TileBuilder.create().skinType(SkinType.TEXT).prefSize(200, 200).title(member.getName())
				.text("Team Member").description(member.getDescription() + "\n" + member.getExtraDescription())
				.descriptionAlignment(Pos.CENTER).textVisible(true).build();

		// member perfomance score tile
		Label label = new Label("Employee Perfomance");
		label.setFont(Font.font("Verdana", 20));
		label.setTextFill(Color.WHITESMOKE);
		// TODO to apply css to the label for text.
		FunLevelGauge memberFunGuage = new FunLevelGauge();
		memberFunGuage.setPrefSize(200, 200);
		memberFunGuage.setLevel((member.getScore1() + member.getScore2() + member.getScore3()) / 3);
		VBox funBox = new VBox(5);
		funBox.getChildren().addAll(label, memberFunGuage);

		// member ontime guage tile
		Tile memberGaugeTile = TileBuilder.create().skinType(SkinType.GAUGE).prefSize(200, 200)
				.title("Quarterly Perfomance").unit("P").threshold(150).build();
		memberGaugeTile.setMinValue(0);
		memberGaugeTile.setMaxValue(300);
		memberGaugeTile.setValue(member.getOnTime() + member.getValueAdd() + member.getQuality());

		// add all componets in order
		indBox.getChildren().addAll(memberImage, memberTextTile, funBox, memberGaugeTile);

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
		File imgFile = new File("Default path to image folder" + "/" + member.getPortalId() + ".png");
		ImageView imageView = new ImageView();
		imageView.setFitHeight(300);
		imageView.setFitWidth(250);
		imageView.setPreserveRatio(true);
		try {
			imageView.setImage(new Image(FileUtils.openInputStream(imgFile)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return imageView;
	}

}
