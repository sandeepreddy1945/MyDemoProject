/**
 * 
 */
package com.app.chart.fx;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

/**
 * @author Sandeep
 *
 */
public class FilesUtil {

	public static final String DOT = ".";
	public static final String SLASH = "/";
	public static final String MPS_CHARTS_PATH = "MPS-Charts";
	public static final String MAIN_APP_PATH = FileUtils.getUserDirectory().getAbsolutePath() + SLASH + MPS_CHARTS_PATH;
	public static final String IMAGES_DIR_PATH = FileUtils.getUserDirectory().getAbsolutePath() + SLASH
			+ MPS_CHARTS_PATH + SLASH + "images";
	public static final String HTML_DIR_PATH = FileUtils.getUserDirectory().getAbsolutePath() + SLASH + MPS_CHARTS_PATH
			+ SLASH + "html";
	public static final String PROPS_DIR_PATH = FileUtils.getUserDirectory().getAbsolutePath() + SLASH + MPS_CHARTS_PATH
			+ SLASH + "properties";
	public static final String JSON_MSGS_PATH = FileUtils.getUserDirectory().getAbsolutePath() + SLASH + MPS_CHARTS_PATH
			+ SLASH + "jsons";
	public static final String JS_DIR_PATH = FileUtils.getUserDirectory().getAbsolutePath() + SLASH + MPS_CHARTS_PATH
			+ SLASH + "js";
	public static final String CSS_DIR_PATH = FileUtils.getUserDirectory().getAbsolutePath() + SLASH + MPS_CHARTS_PATH
			+ SLASH + "css";
	public static final String RUN_PROPS_PATH = PROPS_DIR_PATH + SLASH + "run.json";
	public static final String MANAGER_PROPS_PATH = PROPS_DIR_PATH + SLASH + "manager.properties";
	public static final String DASHBOARD_PROPS_PATH = PROPS_DIR_PATH + SLASH + "dashboard.json";
	public static final String DASHBOARD_CONTENT_PATH = MAIN_APP_PATH + SLASH + "dashboard";
	public static final String DASHBOARD_CONTENT_DATA_FILE = "dashboardContent.json";
	public static final String DASHBOARD_CONTENT_DATA = DASHBOARD_CONTENT_PATH + SLASH + DASHBOARD_CONTENT_DATA_FILE;
	public static final String DASHBOARD_PROPS_PATH_BCK = PROPS_DIR_PATH + SLASH + "managerpropsbck";
	public static final String DASHBOARD_CONTENT_PATH_BCK = DASHBOARD_CONTENT_PATH + SLASH + "dashboardbck";
	public static final String DASHBOARD_PROJECT_STATUS_FILE = DASHBOARD_CONTENT_PATH + SLASH + "projectstatus.json";

	/**
	 * 
	 * @param props
	 */
	public static void createFiles(Properties props) {

		props.keySet().stream().filter(k -> !new File(PROPS_DIR_PATH + SLASH + k).exists()).forEach(k -> {
			try {
				new File(PROPS_DIR_PATH + SLASH + k).createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Initialize the Dirs ..In the User Directory for App to Fucntion
	 * 
	 * @throws IOException
	 */
	public static void initializeFileSettings() throws IOException {
		checkAndCreateDir(MAIN_APP_PATH, IMAGES_DIR_PATH, HTML_DIR_PATH, PROPS_DIR_PATH, JSON_MSGS_PATH, JS_DIR_PATH,
				CSS_DIR_PATH, DASHBOARD_CONTENT_PATH, DASHBOARD_PROPS_PATH_BCK, DASHBOARD_CONTENT_PATH_BCK);

		copyJsAndCssFilesToFolders();

		checkAndCreateFile(RUN_PROPS_PATH, MANAGER_PROPS_PATH, DASHBOARD_PROPS_PATH, DASHBOARD_CONTENT_DATA,
				DASHBOARD_PROJECT_STATUS_FILE);

		copyRequiredJsAndCssFiles();
	}

	public static void copyJsAndCssFilesToFolders() {

	}

	/**
	 * Make Dir Based On The Paths
	 * 
	 * @param files
	 */
	public static void checkAndCreateDir(String... files) {

		Arrays.stream(files).filter(f -> !new File(f).exists()).forEach(f -> new File(f).mkdir());
	}

	public static void checkAndCreateFile(String... files) {
		Arrays.stream(files).filter(f -> !new File(f).exists()).forEach(f -> {
			try {
				new File(f).createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	public static void checkAndCreateFile(FileExtensionTypes fileExtension, String... files) {
		Arrays.stream(files).filter(f -> !new File(f + DOT + fileExtension.name()).exists()).forEach(f -> {
			try {
				new File(f + DOT + fileExtension.name()).createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	public static Properties readMainPropertiesFile() throws IOException {
		return readFromPropertiesFile(MANAGER_PROPS_PATH);
	}

	public static Properties readFromPropertiesFile(String url) throws IOException {
		Properties props = new Properties();
		props.load(FileUtils.openInputStream(new File(url)));

		return props;
	}

	public enum FileExtensionTypes {
		json, properties, txt
	}

	public static void copyRequiredJsAndCssFiles() throws IOException {
		// fileCopierForJSAndCss(JS_DIR_PATH, "js", "bootstrap.min.js"); // no more
		// requirement for this
		fileCopierForJSAndCss(JS_DIR_PATH, "js", "jquery-ui.min.js");
		fileCopierForJSAndCss(JS_DIR_PATH, "js", "jquery.easing.js");
		fileCopierForJSAndCss(JS_DIR_PATH, "js", "jquery.min.js");
		fileCopierForJSAndCss(JS_DIR_PATH, "js", "jquery.mousewheel.js");
		fileCopierForJSAndCss(JS_DIR_PATH, "js", "jquery.transition.min.js");
		fileCopierForJSAndCss(JS_DIR_PATH, "js", "raphael.js");
		fileCopierForJSAndCss(JS_DIR_PATH, "js", "Treant.js");

		// create dir for perfect scroll bar scripts
		checkAndCreateDir(JS_DIR_PATH + SLASH + "perfect-scrollbar");

		fileCopierForJSAndCss(JS_DIR_PATH, "js", "perfect-scrollbar/perfect-scrollbar.js");
		fileCopierForJSAndCss(JS_DIR_PATH, "js", "perfect-scrollbar/perfect-scrollbar.css");
		// css files
		// fileCopierForJSAndCss(CSS_DIR_PATH, "css", "bootstrap-grid.min.css"); // no
		// more requirement for this
		// fileCopierForJSAndCss(CSS_DIR_PATH, "css", "bootstrap.min.css"); // no more
		// requirement for this
		fileCopierForJSAndCss(CSS_DIR_PATH, "css", "custom-colored.css");
		fileCopierForJSAndCss(CSS_DIR_PATH, "css", "Treant.css");
	}

	private static void fileCopierForJSAndCss(String dir, String fileType, String fileName) throws IOException {
		checkAndCreateFile(dir + SLASH + fileName);
		FileUtils.copyToFile(
				FilesUtil.class.getClassLoader().getResourceAsStream("com/app/chart/" + fileType + SLASH + fileName),
				new File(dir + SLASH + fileName));
	}

}
