package uk.co.blackpepper.neuroevolution.demo.pong;

import java.awt.Dimension;
import java.awt.image.ImageProducer;
import java.awt.image.MemoryImageSource;
import java.util.Arrays;

public class Screen {
	
	private static final int BACKGROUND = 0xFF000000;
	
	private static final int FOREGROUND = 0xFFFFFFFF;
	
	private final Dimension size;
	
	private final int[] pixels;
	
	private final ImageProducer imageSource;
	
	public Screen(Dimension size) {
		this.size = size;
		
		pixels = new int[size.width * size.height];
		imageSource = new MemoryImageSource(size.width, size.height, pixels, 0, size.width);
		
		clear();
	}
	
	public Dimension getSize() {
		return size;
	}
	
	public int getWidth() {
		return size.width;
	}
	
	public int getHeight() {
		return size.height;
	}
	
	public void clear() {
		Arrays.fill(pixels, BACKGROUND);
	}
	
	public void plotPixel(int x, int y) {
		pixels[y * size.width + x] = FOREGROUND;
	}
	
	public ImageProducer getImageSource() {
		return imageSource;
	}
}
