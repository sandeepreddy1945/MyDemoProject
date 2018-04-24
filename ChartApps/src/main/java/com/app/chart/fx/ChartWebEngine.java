/**
 * 
 */
package com.app.chart.fx;

import java.net.URL;

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
		URL url = ClassLoader.getSystemResource("com/app/chart/html/index.html");
		
		webEngine.load(url.toExternalForm());
		webEngine.setOnAlert(event -> showAlert(event.getData()));
		animationTimer = new AnimationTimer() {
		Long lastNanoTime = new Long(System.nanoTime());

			@Override
			public void handle(long now) {
				if (now > lastNanoTime + 3_000_000_000l) {
					String str = "Sandeep Reddy45645646546";
					webEngine.executeScript("executetest()");
					//webEngine.reload();
					//webEngine.load(url.toExternalForm());
					lastNanoTime = now;
				}
			}
		};
		return this;
	}

	private void showAlert(String message) {
		System.out.println(message);
	}

	public void loadPage() {
		webEngine.executeScript("pageDisplayData('Sandeep Reddy')");
	}

}
