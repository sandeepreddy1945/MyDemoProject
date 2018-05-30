/**
 * 
 */
package com.app.chart.back.ziputils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import com.app.chart.fx.FilesUtil;
import com.app.chart.xl.utilities.DataToXLSConverter;

/**
 * @author Sandeep Reddy Battula
 * 
 *         Used for backing up the data of the main folder ..Juzz in case for
 *         safety measures.
 *
 */
public class BackupManager {

	public static final String BCK_FOLDER = "backup";
	public static final String BCK_PATH = DataToXLSConverter.MAIN_PATH + FilesUtil.SLASH + BCK_FOLDER;

	/**
	 * @param args
	 * @throws CompressorException
	 * @throws IOException
	 */
	public static void main(String[] args) throws CompressorException, IOException {

		// create main files
		FilesUtil.checkAndCreateDir(DataToXLSConverter.MAIN_PATH, BCK_PATH);

		FileOutputStream fOut = null;
		BufferedOutputStream bOut = null;
		GzipCompressorOutputStream gzOut = null;
		TarArchiveOutputStream tOut = null;
		try {
			System.out.println(new File(".").getAbsolutePath());
			String dirPath = FilesUtil.MAIN_APP_PATH;
			String fileSuffix = new SimpleDateFormat("yyyyMMdd").format(new Date());
			String tarGzPath = BCK_PATH + FilesUtil.SLASH + "mpscharts-" + fileSuffix + ".tar.gz";
			fOut = new FileOutputStream(new File(tarGzPath));
			bOut = new BufferedOutputStream(fOut);
			gzOut = new GzipCompressorOutputStream(bOut);
			tOut = new TarArchiveOutputStream(gzOut);
			addFileToTarGz(tOut, dirPath, "");
		} finally {
			tOut.finish();
			tOut.close();
			gzOut.close();
			bOut.close();
			fOut.close();
		}

	}

	private static void addFileToTarGz(TarArchiveOutputStream tOut, String path, String base) throws IOException {
		File f = new File(path);
		System.out.println(f.exists());
		String entryName = base + f.getName();
		TarArchiveEntry tarEntry = new TarArchiveEntry(f, entryName);
		tOut.putArchiveEntry(tarEntry);

		if (f.isFile()) {
			IOUtils.copy(new FileInputStream(f), tOut);
			tOut.closeArchiveEntry();
		} else {
			tOut.closeArchiveEntry();
			File[] children = f.listFiles();
			if (children != null) {
				for (File child : children) {
					System.out.println(child.getName());
					addFileToTarGz(tOut, child.getAbsolutePath(), entryName + "/");
				}
			}
		}

	}
}
