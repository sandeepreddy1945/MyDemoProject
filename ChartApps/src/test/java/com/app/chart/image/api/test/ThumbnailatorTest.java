/**
 * 
 *//*
package com.app.chart.image.api.test;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.imgscalr.Scalr;

import com.mortennobel.imagescaling.ResampleFilters;
import com.mortennobel.imagescaling.ResampleOp;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.makers.FixedSizeThumbnailMaker;
import net.coobird.thumbnailator.name.Rename;
import net.coobird.thumbnailator.resizers.DefaultResizerFactory;
import net.coobird.thumbnailator.resizers.Resizer;

*//**
 * @author Sandeep
 *         https://stackoverflow.com/questions/24745147/java-resize-image-without-losing-quality
 *         //check site for more informaiton on image escaling.
 *//*
public class ThumbnailatorTest {

	*//**
	 * 
	 *//*
	public ThumbnailatorTest() {
		// TODO Auto-generated constructor stub
	}

	*//**
	 * @param args
	 * @throws IOException
	 *//*
	public static void main(String[] args) throws IOException {
		
		 * Thumbnails.of(new File("path/to/directory").listFiles()).size(640,
		 * 480).outputFormat("jpg") .toFiles(Rename.NO_CHANGE);
		 
		BufferedImage imageToScale = ImageIO
				.read(FileUtils.openInputStream(new File("C:\\Users\\Sandeep\\Pictures\\appreciation1.png")));
		Resizer resizer = DefaultResizerFactory.getInstance()
				.getResizer(new Dimension(imageToScale.getWidth(), imageToScale.getHeight()), new Dimension(400, 310));
		BufferedImage scaledImage = new FixedSizeThumbnailMaker(400, 310, false, true).resizer(resizer)
				.make(imageToScale);
		ImageIO.write(scaledImage, "png", new File("C:\\Users\\Sandeep\\Pictures\\p2.png"));

		// using lancos

		ResampleOp resizeOp = new ResampleOp(400, 310);
		resizeOp.setFilter(ResampleFilters.getLanczos3Filter());
		BufferedImage scaledImage1 = resizeOp.filter(imageToScale, null);
		ImageIO.write(scaledImage1, "png", new File("C:\\Users\\Sandeep\\Pictures\\p3.png"));

		// using sclar
		BufferedImage scaledImage2 = Scalr.resize(imageToScale, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_EXACT,
				400, 310);
		ImageIO.write(scaledImage2, "png", new File("C:\\Users\\Sandeep\\Pictures\\p4.png"));

	}

}
*/