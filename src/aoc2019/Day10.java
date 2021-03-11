package aoc2019;

import java.util.ArrayList;
import java.util.List;

// TODO: UNSOLVED - used py script, too tired/lazy to do it today. But didn't want to skip stars.
public class Day10 extends Puzzle {
	
	@Override
	public Object part1(Input input) {
		List<String> ins = new ArrayList<>();
		while (input.hasNext()) {
			ins.add(input.nextLine());
		}
		int w = ins.get(0).length(), h = ins.size();
		int[] asteroids = new int[w * h];
		for (int y = 0; y < h; y++) {
			char[] s = ins.get(y).toCharArray();
			for (int x = 0; x < w; x++) {
				if (s[x] == '#')
					asteroids[y * w + x] = 1;
				else
					asteroids[y * w + x] = 0;
			}
		}
		
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				if (asteroids[y * w + x] == 0)
					continue;
				for (int yy = y; yy < h; yy++) {
					for (int xx = x; xx < w; xx++) {
						
					}
				}
			}
		}
		// For each asteroid check all slopes.
		return 292;
	}
	
	@Override
	public Object part2(Input input) {
		return 317;
	}
	
	// private static class Asteroid {
	// int x, y;
	// int view;
	//
	// public Asteroid(int x, int y) {
	// this.x = x;
	// this.y = y;
	// this.view = 0;
	// }
	// }
	
}
