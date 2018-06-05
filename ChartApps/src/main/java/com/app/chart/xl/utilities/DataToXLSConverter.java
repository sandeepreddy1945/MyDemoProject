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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.app.chart.fx.FilesUtil;
import com.app.chart.model.PerfomanceBoardBoundary;
import com.app.chart.model.SunburstBoundary;
import com.app.chart.model.SunburstBoundary.ReleaseBoundary;
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

	private static final String[] PERFOMANCE_MEMBER_SHEET_HEADER = { "PORTAL ID", "FULL NAME", "DESIGNATION",
			"MONTH 1 SCORE", "MONTH 2 SCORE", "MONTH 3 SCORE", "VALUE ADD SCORE", "QUALITY SCORE", "ON TIME SCORE",
			"INITIAL MONTH", "TEAM NAME" };
	private static final String[] PERFORMANCE_RELEASE_HEADER = { "TOTAL POINTS", "ACCEPTED POINTS", "BACKLOG POINTS" };

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
			Arrays.stream(PERFOMANCE_MEMBER_SHEET_HEADER).forEach(s -> {
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

		// now write the perfomance meter data into it
		perfomanceBoardDetails.stream().forEach(p -> {
			// check if overperfomance boundary is non null and then proceed
			if (p.getPerfomanceMeterBoundary() != null) {

				// create a page in workbook using the managers name given
				XSSFSheet spreadsheet = workbook
						.createSheet(p.getManagerDetailBoundary().getName() + " - Overall Perfomance");
				XSSFRow firstRow = spreadsheet.createRow(0);

				// create first row with header as its increment setter.
				AtomicInteger cellCount = new AtomicInteger(-1);
				// set colums to row
				Arrays.stream(PERFORMANCE_RELEASE_HEADER).forEach(s -> {
					// increment and get so it starts from 1
					Cell cell = firstRow.createCell(cellCount.incrementAndGet());
					cell.setCellValue(s);
				});

				// re-initialize it to start value
				cellCount.set(-1);
				AtomicInteger rowCount = new AtomicInteger(0);
				XSSFRow row = spreadsheet.createRow(rowCount.incrementAndGet());
				row.createCell(cellCount.incrementAndGet())
						.setCellValue(p.getPerfomanceMeterBoundary().getTotalPoints());
				row.createCell(cellCount.incrementAndGet())
						.setCellValue(p.getPerfomanceMeterBoundary().getCurrentPoints());
				row.createCell(cellCount.incrementAndGet())
						.setCellValue(p.getPerfomanceMeterBoundary().getBacklogPoints());

			}
		});

		// now write the current sprint data into it
		perfomanceBoardDetails.stream().forEach(p -> {
			// check if current spring boundary is non null and then proceed

			if (p.getCurrentSprintBoundary() != null) {

				// create a page in workbook using the managers name given
				XSSFSheet spreadsheet = workbook
						.createSheet(p.getManagerDetailBoundary().getName() + " - Current Sprint");
				XSSFRow firstRow = spreadsheet.createRow(0);

				// create first row with header as its increment setter.
				AtomicInteger cellCount = new AtomicInteger(-1);
				// set colums to row
				Arrays.stream(PERFORMANCE_RELEASE_HEADER).forEach(s -> {
					// increment and get so it starts from 1
					Cell cell = firstRow.createCell(cellCount.incrementAndGet());
					cell.setCellValue(s);
				});

				// re-initialize it to start value
				cellCount.set(-1);
				AtomicInteger rowCount = new AtomicInteger(0);
				XSSFRow row = spreadsheet.createRow(rowCount.incrementAndGet());
				row.createCell(cellCount.incrementAndGet())
						.setCellValue(p.getCurrentSprintBoundary().getTotalSprintPoints());
				row.createCell(cellCount.incrementAndGet())
						.setCellValue(p.getCurrentSprintBoundary().getCurrentSprintPoints());
				row.createCell(cellCount.incrementAndGet())
						.setCellValue(p.getCurrentSprintBoundary().getBacklogSprintPoints());

			}
		});

		// now write the release graph details
		perfomanceBoardDetails.stream().forEach(p -> {
			if (p.getSunburstBoundary() != null) {
				SunburstBoundary s = p.getSunburstBoundary();
				// create a page in workbook using the managers name given
				XSSFSheet spreadsheet = workbook
						.createSheet(p.getManagerDetailBoundary().getName() + " - " + s.getRootName());
				XSSFRow firstRow = spreadsheet.createRow(0);

				// create first row with header as its increment setter.
				AtomicInteger cellCount = new AtomicInteger(-1);

				// set the serial number as the first cell.
				Cell frstCell = firstRow.createCell(cellCount.incrementAndGet());
				frstCell.setCellValue("Release Name");
				// increment and get so it starts from 1
				// Cell cell = firstRow.createCell(cellCount.incrementAndGet());
				// cell.setCellValue("Color Assigned");

				// get the highest column count and add that many colums to header row
				// a cloned list of subboundaries
				List<ReleaseBoundary> list = s.getSubBoundaries();
				Collections.sort(list, ReleaseGraphComparator.getInstance());
				Collections.reverse(list);

				int highestCount = list.get(0).getAttrBoundaries().size();

				for (int i = 1; i <= highestCount; i++) {
					Cell cell = firstRow.createCell(cellCount.incrementAndGet());
					cell.setCellValue("Feature - " + i + " - Score");
				}

				AtomicInteger rowCount = new AtomicInteger(0);

				s.getSubBoundaries().stream().forEach(sb -> {
					// re-initialize it to start value
					cellCount.set(-1);
					XSSFRow row = spreadsheet.createRow(rowCount.incrementAndGet());
					row.createCell(cellCount.incrementAndGet()).setCellValue(sb.getFieldName());
					// row.createCell(cellCount.incrementAndGet()).setCellValue(sb.getColor().fecthColor().);
					sb.getAttrBoundaries().stream().forEach(sba -> {
						row.createCell(cellCount.incrementAndGet())
								.setCellValue(sba.getFieldName() + " - " + sba.getScores());
					});
				});
			}
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

	static class ReleaseGraphComparator implements Comparator<ReleaseBoundary> {

		private static ReleaseGraphComparator _instance = new ReleaseGraphComparator();

		public static ReleaseGraphComparator getInstance() {
			return _instance;
		}

		@Override
		public int compare(ReleaseBoundary o1, ReleaseBoundary o2) {
			if (o1 != null && o2 != null) {
				Integer i1 = o1.getAttrBoundaries().size();
				Integer i2 = o2.getAttrBoundaries().size();
				return i1.compareTo(i2);
			}
			return 0;
		}

	}

}
