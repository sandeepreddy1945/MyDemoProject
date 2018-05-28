/**
 * 
 */
package com.app.chart.image.utilities;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

/**
 * @author Sandeep Reddy Battula
 *
 */
public class ImageConverter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Image Converter.
		String path = "D:\\sharedfolder\\images";
		String copyPath = "D:\\sharedfolder\\images-png";

		Predicate<File> filePredicate = e -> {
			String fileName = e.getName().toLowerCase();
			return (fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".jpeg")
					|| fileName.toLowerCase().endsWith(".png"));

		};
		List<File> filesList = new ArrayList<>();
		File imgesDir = new File(path);
		File[] files = imgesDir.listFiles();
		filesList = Arrays.stream(files).filter(filePredicate).collect(Collectors.toList());
		filesList.stream().forEach(f -> {
			try {
				BufferedImage bufferedImage = ImageIO.read(f);
				// create a blank, RGB, same width and height, and a white background
				BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),
						BufferedImage.TYPE_INT_RGB);
				newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
				// write to jpeg file
				String fileName = f.getName().substring(0, f.getName().lastIndexOf("."));
				ImageIO.write(newBufferedImage, "png", new File(copyPath + "/" + fileName + ".png"));

				System.out.println("Done");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
	}
}
