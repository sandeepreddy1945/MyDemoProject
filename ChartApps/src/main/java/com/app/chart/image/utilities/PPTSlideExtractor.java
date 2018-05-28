/**
 * 
 */
package com.app.chart.image.utilities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;

/**
 * @author Sandeep Reddy Battula
 *
 */
public class PPTSlideExtractor {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		FileInputStream is = new FileInputStream("D:\\sharedfolder\\McKesson Display Board_Ramana (2).pptx");
		XMLSlideShow ppt = new XMLSlideShow(is);
		is.close();

		Dimension pgsize = ppt.getPageSize();

		XSLFSlide[] slide = ppt.getSlides().stream().toArray(XSLFSlide[]::new);
		for (int i = 0; i < slide.length; i++) {

			BufferedImage img = new BufferedImage(pgsize.width, pgsize.height, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics = img.createGraphics();
			// clear the drawing area
			graphics.setPaint(Color.white);
			graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));

			// render
			slide[i].draw(graphics);

			// save the output
			FileOutputStream out = new FileOutputStream("D:\\sharedfolder\\dashboardimages\\" + (i + 1) + ".png");
			javax.imageio.ImageIO.write(img, "png", out);
			out.close();

		}

		ppt.close();
	}

}
