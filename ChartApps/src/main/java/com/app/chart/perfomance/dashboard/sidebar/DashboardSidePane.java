/**
 * 
 */
package com.app.chart.perfomance.dashboard.sidebar;

import java.io.IOException;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXDrawer.DrawerDirection;
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
 * @author Sandeep Currently there is a complicationin the usage of the side
 *         drawer. This needs to be reviewd architecture wise. Currently this
 *         will be used for viewing the project status if required. This can be
 *         used to accomplish that for now.
 */
public class DashboardSidePane {

	private JFXDrawer drawer;
	private final Node node;
	private HBox mainBox;

	/**
	 * @throws IOException
	 * 
	 */
	public DashboardSidePane(Node node, HBox mainBox) throws IOException {
		super();
		this.node = node;
		this.mainBox = mainBox;
		init();
	}

	private void init() throws IOException {
		drawer = new JFXDrawer();
		drawer.setAlignment(Pos.TOP_LEFT);
		drawer.setDefaultDrawerSize(250);
		drawer.setDirection(DrawerDirection.LEFT);
		drawer.getStyleClass().add("jfx-drawer");
		drawer.setOverLayVisible(true);
		drawer.setResizableOnDrag(false);
		// drawer.setMouseTransparent(true);
		VBox toolbar = FXMLLoader
				.load(getClass().getResource("/com/app/chart/perfomance/dashboard/sidebar/toolbar.fxml"));
		drawer.setSidePane(toolbar);
		drawer.setOverLayVisible(false);
		HamburgerSlideCloseTransition task = new HamburgerSlideCloseTransition();
		task.setRate(-1);
		node.addEventHandler(MouseEvent.MOUSE_CLICKED, (Event event) -> {
			task.setRate(task.getRate() * -1);
			task.play();
			if (drawer.isClosed()) {
				Platform.runLater(() -> {
					drawer.open();
					mainBox.getChildren().add(0, drawer);
				});
			} else {
				drawer.close();
				mainBox.getChildren().remove(0);

			}
		});

		// getChildren().add(drawer);
	}

	/**
	 * @return the drawer
	 */
	public JFXDrawer getDrawer() {
		return drawer;
	}
}
