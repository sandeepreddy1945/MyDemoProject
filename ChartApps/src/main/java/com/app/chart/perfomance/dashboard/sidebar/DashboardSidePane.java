/**
 * 
 */
package com.app.chart.perfomance.dashboard.sidebar;

import java.io.IOException;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXDrawer.DrawerDirection;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * @author Sandeep
 *
 */
public class DashboardSidePane extends HBox {

	private JFXDrawer drawer;
	private final Node node;

	/**
	 * @throws IOException
	 * 
	 */
	public DashboardSidePane(Node node) throws IOException {
		super();
		this.node = node;
		init();
	}

	private void init() throws IOException {

		drawer = new JFXDrawer();
		drawer.setAlignment(Pos.BASELINE_LEFT);
		drawer.setDefaultDrawerSize(150);
		drawer.setDirection(DrawerDirection.RIGHT);
		drawer.getStyleClass().add("jfx-drawer");
		drawer.setOverLayVisible(true);
		drawer.setResizableOnDrag(false);
		VBox toolbar = FXMLLoader
				.load(getClass().getResource("/com/app/chart/perfomance/dashboard/sidebar/toolbar.fxml"));
		drawer.setSidePane(toolbar);
		HamburgerSlideCloseTransition task = new HamburgerSlideCloseTransition();
		task.setRate(-1);
		node.addEventHandler(MouseEvent.MOUSE_CLICKED, (Event event) -> {
			task.setRate(task.getRate() * -1);
			task.play();
			if (drawer.isHidden()) {
				Platform.runLater(() -> drawer.open());
				;
				drawer.setVisible(true);
			} else {
				drawer.close();
			}
		});
	}

}
