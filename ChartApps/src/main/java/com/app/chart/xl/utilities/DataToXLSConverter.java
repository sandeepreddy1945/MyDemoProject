/**
 * 
 */
package com.app.chart.xl.utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.app.chart.fx.FilesUtil;
import com.app.chart.model.PerfomanceBoardBoundary;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Sandeep
 *
 */
@Slf4j
public class DataToXLSConverter {

	public static String MAIN_PATH = "C://MPSCHARTS";
	public static String ROOT_FOLDER = "C://MPSCHARTS//xls";
	public String PERFORMANCE_FOLDER = ROOT_FOLDER + FilesUtil.SLASH + "performance";
	public String PERFORMANCE_FILE = PERFORMANCE_FOLDER + FilesUtil.SLASH + "TeamsPerformanceData.xlsx";

	private static final String[] PERFOMANCE_SHEET_HEADER = { "PORTAL ID", "FULL NAME", "DESIGNATION", "MONTH 1 SCORE",
			"MONTH 2 SCORE", "MONTH 3 SCORE", "VALUE ADD SCORE", "QUALITY SCORE", "ON TIME SCORE", "INITIAL MONTH",
			"TEAM NAME" };

	private ObjectMapper mapper = new ObjectMapper();

	private List<PerfomanceBoardBoundary> perfomanceBoardDetails = new ArrayList<>();

	/**
	 * 
	 */
	public DataToXLSConverter() {
		// create main files
		FilesUtil.checkAndCreateDir(MAIN_PATH, ROOT_FOLDER, PERFORMANCE_FOLDER);
		FilesUtil.checkAndCreateFile(PERFORMANCE_FILE);

		initializeAndCreateXLFiles();
	}

	private void initializeAndCreateXLFiles() {
		try {
			// read the performance list from file
			readPerformanceDataFromFile();
			// write the performance data to file
			createFilesForPerformanceData();

		} catch (Exception ex) {
			log.error("initializeAndCreateXLFiles", ex);
		}

	}

	private void readPerformanceDataFromFile() {
		try {
			File dataFile = new File(FilesUtil.DASHBOARD_CONTENT_DATA);
			if (dataFile != null && dataFile.exists()
					&& FileUtils.readFileToString(dataFile, Charset.defaultCharset()).length() > 0) {
				String fileData = FileUtils.readFileToString(dataFile, Charset.defaultCharset());
				System.out.println(fileData);
				mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);

				List<PerfomanceBoardBoundary> dataList = mapper.readValue(fileData,
						mapper.getTypeFactory().constructCollectionType(List.class, PerfomanceBoardBoundary.class));

				// set the list with details
				this.perfomanceBoardDetails = dataList;

			} else {
				// instntiate the perfomance list as not data is present.
				perfomanceBoardDetails = new ArrayList<>();
			}
		} catch (IOException e) {

			log.error("readDataFromFile", e);
		}

	}

	private void createFilesForPerformanceData() {
		XSSFWorkbook workbook = new XSSFWorkbook();
		perfomanceBoardDetails.stream().forEach(p -> {

			// create a page in workbook using the managers name given
			XSSFSheet spreadsheet = workbook.createSheet(p.getManagerDetailBoundary().getName());

			// create first row with header as its increment setter.
			AtomicInteger cellCount = new AtomicInteger(-1);

			XSSFRow firstRow = spreadsheet.createRow(0);
			// set colums to row
			Arrays.stream(PERFOMANCE_SHEET_HEADER).forEach(s -> {
				// increment and get so it starts from 1
				Cell cell = firstRow.createCell(cellCount.incrementAndGet());
				cell.setCellValue(s);
			});

			AtomicInteger rowCount = new AtomicInteger(0);

			p.getTeamMembers().stream().forEach(t -> {
				XSSFRow row = spreadsheet.createRow(rowCount.incrementAndGet());
				// re-initialize it to start value
				cellCount.set(-1);
				// set the cell data with the value.
				row.createCell(cellCount.incrementAndGet()).setCellValue(t.getPortalId());
				row.createCell(cellCount.incrementAndGet()).setCellValue(t.getName());
				row.createCell(cellCount.incrementAndGet()).setCellValue(t.getDescription());
				row.createCell(cellCount.incrementAndGet()).setCellValue(t.getScore1());
				row.createCell(cellCount.incrementAndGet()).setCellValue(t.getScore2());
				row.createCell(cellCount.incrementAndGet()).setCellValue(t.getScore3());
				row.createCell(cellCount.incrementAndGet()).setCellValue(t.getValueAdd());
				row.createCell(cellCount.incrementAndGet()).setCellValue(t.getQuality());
				row.createCell(cellCount.incrementAndGet()).setCellValue(t.getOnTime());
				row.createCell(cellCount.incrementAndGet()).setCellValue(t.getIntreval1());
				row.createCell(cellCount.incrementAndGet()).setCellValue(t.getTeam());

			});

		});
		// now write things to file
		FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(PERFORMANCE_FILE);
			workbook.write(outputStream);
			workbook.close();
		} catch (IOException e) {
			log.error("createFilesForPerformanceData", e);
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new DataToXLSConverter();
	}

}
