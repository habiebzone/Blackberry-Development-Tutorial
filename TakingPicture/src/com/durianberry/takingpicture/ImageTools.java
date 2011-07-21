package com.durianberry.takingpicture;

import java.io.IOException;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import net.rim.device.api.math.Fixed32;
import net.rim.device.api.system.AccelerometerSensor;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.system.JPEGEncodedImage;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.XYRect;

public class ImageTools {
	private static int[] rescaleArray(int[] ini, int x, int y, int x2, int y2) {
		int out[] = new int[x2 * y2];
		for (int yy = 0; yy < y2; yy++) {
			int dy = yy * y / y2;
			for (int xx = 0; xx < x2; xx++) {
				int dx = xx * x / x2;
				out[(x2 * yy) + xx] = ini[(x * dy) + dx];
			}
		}
		return out;
	}

	public static EncodedImage bestFit(EncodedImage image, int maxWidth,
			int maxHeight) {
		// getting image properties
		int w = image.getWidth();
		int h = image.getHeight();

		// get the ratio
		int ratiow = 100 * maxWidth / w;
		int ratioh = 100 * maxHeight / h;

		// this is to find the best ratio to
		// resize the image without deformations
		int ratio = Math.min(ratiow, ratioh);

		// computing final desired dimensions
		int desiredWidth = w * ratio / 100;
		int desiredHeight = h * ratio / 100;

		return scaleToFactor(image, image.getWidth(), desiredWidth);
	}

	public static EncodedImage scaleImageToWidth(EncodedImage encoded,
			int newWidth) {
		return scaleToFactor(encoded, encoded.getWidth(), newWidth);
	}

	public static EncodedImage scaleImageToHeight(EncodedImage encoded,
			int newHeight) {
		return scaleToFactor(encoded, encoded.getHeight(), newHeight);
	}

	public static EncodedImage scaleImage(EncodedImage image, double ratio) {
		int newWidth = (int) (image.getWidth() * ratio);
		return scaleToFactor(image, image.getWidth(), newWidth);
	}

	public static EncodedImage stretchFit(EncodedImage image, int newWidth,
			int newHeight) {
		int numerator = Fixed32.toFP(image.getWidth());
		int denominator = Fixed32.toFP(newWidth);
		int scale = Fixed32.div(numerator, denominator);

		int numerator2 = Fixed32.toFP(image.getHeight());
		int denominator2 = Fixed32.toFP(newHeight);
		int scale2 = Fixed32.div(numerator2, denominator2);

		return image.scaleImage32(scale, scale2);
	}

	public static EncodedImage scaleToFactor(EncodedImage encoded, int curSize,
			int newSize) {

		int scale = Fixed32.ONE;

		if (curSize != newSize) {
			int numerator = Fixed32.toFP(curSize);
			int denominator = Fixed32.toFP(newSize);
			scale = Fixed32.div(numerator, denominator);
		}
		if (scale == Fixed32.ONE)
			return encoded;
		else
			return encoded.scaleImage32(scale, scale);
	}

	public static void fillWithBitmap(Graphics g, Bitmap b, int left, int top,
			int width, int height) {
		int[] xPts = new int[] { left, left + width, left + width, left };
		int[] yPts = new int[] { top, top, top + height, top + height };
		byte[] pointTypes = new byte[] { Graphics.CURVEDPATH_END_POINT,
				Graphics.CURVEDPATH_END_POINT, Graphics.CURVEDPATH_END_POINT,
				Graphics.CURVEDPATH_END_POINT };
		int dux = Fixed32.toFP(1), dvx = Fixed32.toFP(0), duy = Fixed32.toFP(0), dvy = Fixed32
				.toFP(1);

		g.drawTexturedPath(xPts, yPts, pointTypes, null, left, top, dux, dvx,
				duy, dvy, b);
	}

	public static Bitmap cropBitmap(Bitmap original, int left, int top,
			int width, int height) {
		Bitmap bmp = new Bitmap(width, height);
		int x = (original.getWidth() / 2) - (width / 2); // Center the new size
															// by width
		int y = (original.getHeight() / 2) - (height / 2); // Center the new
															// size by height
		int[] argb = new int[width * height];
		original.getARGB(argb, 0, width, x, y, width, height);
		bmp.setARGB(argb, 0, width, 0, 0, width, height);
		return bmp;
		
	}

	public static void saveToFile(Bitmap bmp, String path) throws IOException {
		System.out.println("Save to file path: " + path);
		try {
			FileConnection fconn = (FileConnection) Connector.open("file:///"
					+ path, Connector.READ_WRITE);
			if (!fconn.exists())
				fconn.create();
			fconn.setWritable(true);
			OutputStream outputStream = fconn.openOutputStream();
			JPEGEncodedImage encoder = JPEGEncodedImage.encode(bmp, 70);
			
			outputStream.write(encoder.getData());
			outputStream.close();
			fconn.close();
		} catch (IOException e) {
			// TODO: handle exception
			System.out.println("Exception when save file: " + e.getMessage());
			throw e;
		}

	}

	public static Bitmap cropBitmap(Bitmap bitmap, XYRect rect) {
		int width = Math.min(bitmap.getWidth(), rect.width);
		int height = Math.min(bitmap.getHeight(), rect.height);
		int x = (bitmap.getWidth() - width) / 2;
		int y = (bitmap.getHeight() - height) / 2;

		Bitmap result = new Bitmap(width, height);
		bitmap.scaleInto(x, y, width, height, result, 0, 0, width, height,
				Bitmap.FILTER_BOX);

		return result;
	}

	public static EncodedImage convertToEncodedImage(Bitmap bitmap, int quality) {
		return JPEGEncodedImage.encode(bitmap, quality);
	}
	
}
