/**
 * 
 */
package com.app.chart.fx;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import javax.swing.event.HyperlinkEvent;

import org.apache.commons.io.FileUtils;
import org.codefx.libfx.control.webview.WebViewHyperlinkListener;
import org.codefx.libfx.control.webview.WebViews;

import com.app.chart.perfomance.dashboard.ui.DashboardUI;
import com.jfoenix.animation.alert.JFXAlertAnimation;
import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;

import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ezibcef
 *
 */
@Slf4j
public class ChartWebEngine {

	WebView webView;
	WebEngine webEngine;
	private Stage stage;

	/**
	 * Used to reload the page . As the webEngine.reload has a bug for this.
	 * Currently this is skipped by javascript reload.
	 */
	public static final String WINDOW_LOCATION_RELOAD = " window.location.reload();";

	/**
	 * Constructor to Initialize WebEngine and WebView.
	 */
	public ChartWebEngine() {
		webView = new WebView();
		webView.setVisible(true);
		webEngine = webView.getEngine();
		webEngine.setJavaScriptEnabled(true);
		webEngine.setUserDataDirectory(FileUtils.getTempDirectory());

		// init the webview listeners
		addWebHyperLinkListeners();
	}

	/**
	 * @return the webView
	 */
	public WebView getWebView() {
		return webView;
	}

	/**
	 * @return the webEngine
	 */
	public WebEngine getWebEngine() {
		return webEngine;
	}

	public ChartWebEngine initialize() {
		webView.setPrefSize(DashboardUI.WIDTH, DashboardUI.HEIGHT - 60);
		webView.setMinSize(DashboardUI.WIDTH - 160, DashboardUI.HEIGHT - 110);
		webView.setTranslateZ(10);
		webEngine.setOnAlert(event -> showAlert(event.getData()));
		return this;
	}

	public void displayData() {
		webEngine.load(firstContentUIPath());
	}

	public void displayData(String url) {
		if (url != null && url.length() > 0) {
			webEngine.load(url);
			// Re-Load the page via javascript as there is a java bug out there.
			reLoadPage();
		}
	}

	private void showAlert(String message) {
		System.out.println(message);
		/*
		 * Dialog<String> dialog = new Dialog<>();
		 * dialog.setHeaderText("Member Information");
		 * dialog.setContentText("Sample String"); JFXButton button = new
		 * JFXButton("Okay!!"); button.setOnMouseClicked(e -> { dialog.hide(); });
		 * dialog.getDialogPane().getChildren().add(button);
		 * dialog.setTitle("Employee Information"); dialog.setOnCloseRequest(e -> {
		 * ((Dialog) e.getSource()).hide(); }); dialog.show();
		 */
	}

	public void reLoadPage() {
		webEngine.executeScript(WINDOW_LOCATION_RELOAD);
	}

	/**
	 * This method initiates the Hyperlink Listeners to the webengine.<br>
	 * Currently CodeFX-org/LibFX library is used to accomplish this purpose.<br>
	 */
	protected void addWebHyperLinkListeners() {
		// juzz for test
		WebViewHyperlinkListener eventPrintingListener = event -> {
			System.out.println(WebViews.hyperlinkEventToString(event));
			try {
				onHyperLinkClicked(event);
			} catch (IOException e) {
				log.error( "addWebHyperLinkListeners", e);
			}
			return true;
		};
		// add the listener to webview
		WebViews.addHyperlinkListener(webView, eventPrintingListener, HyperlinkEvent.EventType.ACTIVATED);
	}

	private void onHyperLinkClicked(HyperlinkEvent event) throws IOException {
		URL url = event.getURL();
		URLConnection connection = url.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String inputLine;
		StringBuilder builder = new StringBuilder();
		while ((inputLine = in.readLine()) != null)
			builder.append(inputLine);
		in.close();

		JFXAlert<String> alert = new JFXAlert<>();
		alert.initOwner(stage);
		// set some Blur Effect to the main window of display.
		stage.getScene().getRoot().setEffect(new BoxBlur());
		alert.initModality(Modality.WINDOW_MODAL);
		alert.setOverlayClose(true);
		alert.setAnimation(JFXAlertAnimation.TOP_ANIMATION);
		JFXDialogLayout layout = new JFXDialogLayout();
		layout.setHeading(new Label("Member Detailed Information"));
		Text text = new Text(builder.toString());
		text.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		layout.setBody(text);
		JFXButton closeButton = new JFXButton("Close!!");
		closeButton.getStyleClass().add("dialog-accept");
		closeButton.setOnAction(e -> {
			alert.hideWithAnimation();
			// remove the side effect set once the dialog window is closed.
			stage.getScene().getRoot().setEffect(null);
		});
		layout.setStyle("-fx-background-color: white;\r\n" + "    -fx-background-radius: 5.0;\r\n"
				+ "    -fx-background-insets: 0.0 5.0 0.0 5.0;\r\n" + "    -fx-padding: 10;\r\n"
				+ "    -fx-hgap: 10;\r\n" + "    -fx-vgap: 10;" + " -fx-border-color: #2e8b57;\r\n"
				+ "    -fx-border-width: 2px;\r\n" + "    -fx-padding: 10;\r\n" + "    -fx-spacing: 8;");
		layout.setActions(closeButton);
		alert.setContent(layout);
		alert.show();

		// System.out.println(builder.toString());
	}

	public void setParenStage(Stage stage) {
		this.stage = stage;
	}

	/**
	 * This returns the first record from Manager.properties file and the finds it
	 * location for the Html file. <br>
	 * Donot Use this this is just for testing purpose .Will be removed in mere
	 * future.
	 * 
	 * @return
	 */
	// TODO to remove this once the demo is done
	@Deprecated
	private String firstContentUIPath() {
		Properties properties = new Properties();
		try {
			// this retunrs the details contained in the props file
			properties.load(FileUtils.openInputStream(new File(FilesUtil.MANAGER_PROPS_PATH)));
		} catch (IOException e) {
			log.error( "firstContentUIPath", e);
		}
		// take the first entry for display
		String fileName = properties.keySet().stream().toArray(String[]::new)[0];
		String filePath = FilesUtil.MAIN_APP_PATH + FilesUtil.SLASH + fileName + FilesUtil.SLASH + "index.html";
		String path = null;
		try {
			path = new File(filePath).toURI().toURL().toExternalForm();
		} catch (MalformedURLException e) {
			log.error( "firstContentUIPath", e);
		}
		return path;

	}

}
