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
	public static final String RUN_PROPS_PATH = PROPS_DIR_PATH + SLASH + "run.properties";
	public static final String MANAGER_PROPS_PATH = PROPS_DIR_PATH + SLASH + "manager.properties";

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
	 */
	public static void initializeFileSettings() {
		checkAndCreateDir(MAIN_APP_PATH, IMAGES_DIR_PATH, HTML_DIR_PATH, PROPS_DIR_PATH, JSON_MSGS_PATH);

		checkAndCreateFile(RUN_PROPS_PATH, MANAGER_PROPS_PATH);
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

	public enum FileExtensionTypes {
		json, properties, txt
	}

}
