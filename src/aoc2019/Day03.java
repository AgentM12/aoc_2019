package aoc2019;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day03 extends Puzzle {
	
	final static String TEST_INPUT1 = "R75,D30,R83,U83,L12,D49,R71,U7,L72\r\n" + "U62,R66,U55,R34,D71,R55,D58,R83";
	final static int COUNT1 = 159;
	final static String TEST_INPUT2 = "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51\r\n" + "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7";
	final static int COUNT2 = 135;
	
	@Override
	public Object part1(Input input) {
		List<Point> lines1 = getLines(input.nextLine());
		List<Point> lines2 = getLines(input.nextLine());
		
		List<Point> intersections = intersect(lines1, lines2);
		return closest(intersections);
	}
	
	@Override
	public Object part2(Input input) {
		List<Point> lines1 = getLines(input.nextLine());
		List<Point> lines2 = getLines(input.nextLine());
		
		List<Point> intersections = intersect(lines1, lines2);
		return shortestPath(intersections);
	}
	
	private static int shortestPath(List<Point> intersections) {
		int sd = Integer.MAX_VALUE;
		for (Point point : intersections) {
			sd = Math.min(sd, point.distance);
		}
		return sd;
	}
	
	private static List<Point> getLines(String line) {
		List<Point> lines = new ArrayList<>();
		int x = 0, y = 0, dist = 0, px = 0, py = 0;
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
				lines.add(new Point(x, y, dist));
				dist += Math.abs(x - px) + Math.abs(y - py);
				px = x;
				py = y;
			}
		}
		return lines;
	}
	
	private static int closest(List<Point> points) {
		int sum = Integer.MAX_VALUE;
		for (Point point : points) {
			sum = Math.min(sum, Math.abs(point.x) + Math.abs(point.y));
		}
		return sum;
	}
	
	private static List<Point> intersect(List<Point> lines1, List<Point> lines2) {
		int oldPointX = 0, oldPointY = 0;
		List<Point> intersects = new ArrayList<>();
		int mnx, mxx, mny, mxy, mnx2, mxx2, mny2, mxy2;
		for (Point point : lines1) {
			int oldPointX2 = 0, oldPointY2 = 0;
			mnx = (point.x > oldPointX) ? oldPointX : point.x;
			mxx = (point.x > oldPointX) ? point.x : oldPointX;
			
			mny = (point.y > oldPointY) ? oldPointY : point.y;
			mxy = (point.y > oldPointY) ? point.y : oldPointY;
			
			for (Point point2 : lines2) {
				mnx2 = (point2.x > oldPointX2) ? oldPointX2 : point2.x;
				mxx2 = (point2.x > oldPointX2) ? point2.x : oldPointX2;
				
				mny2 = (point2.y > oldPointY2) ? oldPointY2 : point2.y;
				mxy2 = (point2.y > oldPointY2) ? point2.y : oldPointY2;
				
				if (mxx2 >= mnx && mxx >= mnx2 && mxy2 >= mny && mxy >= mny2) {
					int ix = 0, iy = 0, dist = -1;
					if (mnx == mxx && mny2 == mxy2) {
						ix = mnx;
						iy = mny2;
						dist = (point.distance + Math.abs(point2.y - oldPointY)) + (point2.distance + Math.abs(point.x - oldPointX2));
					} else if (mny == mxy && mnx2 == mxx2) {
						ix = mnx2;
						iy = mny;
						dist = (point.distance + Math.abs(point2.x - oldPointX)) + (point2.distance + Math.abs(point.y - oldPointY2));
					}
					
					if (ix != 0 || iy != 0)
						intersects.add(new Point(ix, iy, dist));
				}
				oldPointX2 = point2.x;
				oldPointY2 = point2.y;
			}
			oldPointX = point.x;
			oldPointY = point.y;
		}
		return intersects;
	}
	
	private static class Point {
		public int x;
		public int y;
		public int distance;
		
		public Point(int x, int y, int distance) {
			this.x = x;
			this.y = y;
			this.distance = distance;
		}
		
		@Override
		public String toString() {
			return "(" + x + ", " + y + ")[" + distance + "]";
		}
	}
	
	// private static void tests() {
	// List<Point> lines1 = getLines(TEST_INPUT1.split("\r\n")[0]);
	// List<Point> lines2 = getLines(TEST_INPUT1.split("\r\n")[1]);
	// List<Point> intersections = intersect(lines1, lines2);
	// System.out.println(intersections);
	// int closestIntersection = closest(intersections);
	// System.out.println("Test 1: " + (closestIntersection == COUNT1 ? "succeeded!" : "failed"));
	//
	// lines1 = getLines(TEST_INPUT2.split("\r\n")[0]);
	// lines2 = getLines(TEST_INPUT2.split("\r\n")[1]);
	//
	// intersections = intersect(lines1, lines2);
	// System.out.println(intersections);
	// closestIntersection = closest(intersections);
	// System.out.println("Test 2: " + (closestIntersection == COUNT2 ? "succeeded!" : "failed"));
	// }
	//
	// private static void tests2() {
	// List<Point> lines1 = getLines(TEST_INPUT1.split("\r\n")[0]);
	// System.out.println(lines1);
	// List<Point> lines2 = getLines(TEST_INPUT1.split("\r\n")[1]);
	// System.out.println(lines2);
	// List<Point> intersections = intersect(lines1, lines2);
	// System.out.println(intersections);
	// int sp = shortestPath(intersections);
	// System.out.println(sp);
	// System.out.println("Test 1: " + (sp == 610 ? "succeeded!" : "failed"));
	//
	// lines1 = getLines(TEST_INPUT2.split("\r\n")[0]);
	// lines2 = getLines(TEST_INPUT2.split("\r\n")[1]);
	//
	// intersections = intersect(lines1, lines2);
	// System.out.println(intersections);
	// sp = shortestPath(intersections);
	// System.out.println(sp);
	// System.out.println("Test 2: " + (sp == 410 ? "succeeded!" : "failed"));
	// }
}
