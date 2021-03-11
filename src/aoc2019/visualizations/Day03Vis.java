package aoc2019.visualizations;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;

import aoc2019.Input;

public class Day03Vis {
	
	final static String TEST_INPUT1 = "R75,D30,R83,U83,L12,D49,R71,U7,L72\r\n" + "U62,R66,U55,R34,D71,R55,D58,R83";
	final static int COUNT1 = 159;
	final static String TEST_INPUT2 = "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51\r\n" + "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7";
	final static int COUNT2 = 135;
	static Canvas canvas = new Canvas(0, 0, 0, 0);
	
	// Required args:
	// -Xms1024m -Xmx16384m
	
	public static void main(String[] args) throws FileNotFoundException {
		Input input = new Input("day03");
		visualize(input);
	}
	
	private static List<Point> getLines(String line) {
		List<Point> lines = new ArrayList<>();
		int x = 0, y = 0;
		int xMin = 0, yMin = 0, xMax = 0, yMax = 0;
		try (Scanner sc = new Scanner(line)) {
			sc.useDelimiter(",");
			while (sc.hasNext()) {
				String dir = sc.next();
				switch (dir.charAt(0)) {
					case 'R':
						x += Integer.parseInt(dir.substring(1));
						break;
					case 'U':
						y += Integer.parseInt(dir.substring(1));
						break;
					case 'D':
						y -= Integer.parseInt(dir.substring(1));
						break;
					case 'L':
						x -= Integer.parseInt(dir.substring(1));
						break;
					default:
						break;
				}
				lines.add(new Point(x, y));
				if (x < xMin)
					xMin = x;
				if (x > xMax)
					xMax = x;
				if (y < yMin)
					yMin = y;
				if (y > yMax)
					yMax = y;
			}
		}
		canvas.expand(xMin, yMin, xMax, yMax);
		return lines;
	}
	
	private static void visualize(Input input) {
		List<Point> lines1 = getLines(input.nextLine());
		List<Point> lines2 = getLines(input.nextLine());
		
		Point dim = new Point(canvas.xMax - canvas.xMin + 1, canvas.yMax - canvas.yMin + 1);
		BufferedImage img = new BufferedImage(dim.x, dim.y, BufferedImage.TYPE_INT_RGB);
		
		Graphics2D g2d = img.createGraphics();
		g2d.setBackground(Color.WHITE);
		g2d.fillRect(0, 0, dim.x, dim.y);
		
		draw(lines1, g2d, Color.BLUE);
		draw(lines2, g2d, Color.RED);
		g2d.setColor(Color.BLACK);
		g2d.drawLine(-canvas.xMin, -canvas.yMin, -canvas.xMin, -canvas.yMin);
		
		try {
			File file = new File("data/day03_t.png");
			file.getParentFile().mkdirs();
			System.out.println("Writing out image...");
			ImageIO.write(img, "png", file);
		} catch (IOException e) {
		}
	}
	
	private static void draw(List<Point> lines, Graphics2D g2d, Color color) {
		g2d.setColor(color);
		BasicStroke bs = new BasicStroke(1);
		g2d.setStroke(bs);
		int xOrigin = -canvas.xMin, yOrigin = -canvas.yMin;
		int xPrev = xOrigin, yPrev = yOrigin;
		for (Point point : lines) {
			g2d.drawLine(xPrev, yPrev, xOrigin + point.x, yOrigin + point.y);
			xPrev = xOrigin + point.x;
			yPrev = yOrigin + point.y;
		}
	}
	
	private static class Canvas {
		public int xMin;
		public int xMax;
		public int yMin;
		public int yMax;
		
		public Canvas(int xMin, int yMin, int xMax, int yMax) {
			this.xMin = xMin;
			this.xMax = xMax;
			this.yMin = yMin;
			this.yMax = yMax;
		}
		
		public void expand(int xMin2, int yMin2, int xMax2, int yMax2) {
			xMin = Math.min(xMin, xMin2);
			xMax = Math.max(xMax, xMax2);
			yMin = Math.min(yMin, yMin2);
			yMax = Math.max(yMax, yMax2);
		}
		
		@Override
		public String toString() {
			return "[" + xMin + ", " + yMin + " :: " + xMax + ", " + yMax + "]";
		}
	}
	
	private static class Point {
		public int x;
		public int y;
		
		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		@Override
		public String toString() {
			return "(" + x + ", " + y + ")";
		}
	}
	
}
