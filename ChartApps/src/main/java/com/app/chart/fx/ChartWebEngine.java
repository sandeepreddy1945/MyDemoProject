/**
 * 
 */
package com.app.chart.fx;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import javafx.animation.AnimationTimer;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * @author ezibcef
 *
 */
public class ChartWebEngine {

	WebView webView;
	WebEngine webEngine;
	private AnimationTimer animationTimer;

	/**
	 * Constructor to Initialize WebEngine and WebView.
	 */
	public ChartWebEngine() {
		webView = new WebView();
		webView.setVisible(true);
		webEngine = webView.getEngine();
		webEngine.setJavaScriptEnabled(true);
		webEngine.setUserDataDirectory(FileUtils.getTempDirectory());
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

	/**
	 * 
	 * @return the animationTimer
	 */
	public AnimationTimer getAnimationTimer() {
		return animationTimer;
	}

	public ChartWebEngine initialize() {
		// URL url = ClassLoader.getSystemResource("com/app/chart/html/index.html");

		// webEngine.load(url.toExternalForm());
		webEngine.setOnAlert(event -> showAlert(event.getData()));
		// TODO to change in mere future just for display purpose for now.

		animationTimer = new AnimationTimer() {
			Long lastNanoTime = new Long(System.nanoTime());

			@Override
			public void handle(long now) {
				if (now > lastNanoTime + 3_000_000_000l) {

					lastNanoTime = now;
				}
			}
		};
		return this;
	}

	public void displayData() {
		webEngine.load(firstContentUIPath());
	}

	private void showAlert(String message) {
		System.out.println(message);
	}

	public void loadPage() {
		webEngine.executeScript("pageDisplayData('Sandeep Reddy')");
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// take the first entry for display
		String fileName = properties.keySet().stream().toArray(String[]::new)[0];
		String filePath = FilesUtil.MAIN_APP_PATH + FilesUtil.SLASH + fileName + FilesUtil.SLASH + "index.html";
		String path = null;
		try {
			path = new File(filePath).toURI().toURL().toExternalForm();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return path;

	}

}
