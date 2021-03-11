package aoc2019;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import aoc2019.IntCodeComputer.Program;

public class Day13 extends Puzzle {
	private static final int BLOCK = 2;
	private static final int PADDLE = 3;
	private static final int BALL = 4;
	
	private IntCodeComputer icc;
	
	@Override
	public Object part1(Input input) {
		icc = new IntCodeComputer(input);
		return count(render(new Program(icc.getProgramCache())), BLOCK);
	}
	
	@Override
	public Object part2(Input input) {
		icc = new IntCodeComputer(input);
		long[] p = icc.getProgramCache();
		p[0] = 2;
		return renderScore(new Program(p));
	}
	
	private static int count(int[] grid, int blockId) {
		int count = 0;
		for (int i : grid) {
			if (i == blockId)
				count++;
		}
		return count;
	}
	
	private static class Tile {
		int x, y;
		
		public Tile(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		@Override
		public int hashCode() {
			return 31 * (31 + x) + y;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null || getClass() != obj.getClass())
				return false;
			Tile other = (Tile) obj;
			return (x == other.x && y == other.y);
		}
		
	}
	
	private static List<Long> l(int j) {
		List<Long> input = new ArrayList<>();
		input.add((long) j);
		return input;
	}
	
	private static int renderScore(Program program) {
		int score = 0;
		int w = 35, h = 21; // can be derived from part 1.
		int[] grid = new int[w * h];
		int px = -1, bx = -1;
		int joy = 0;
		while (!program.isTerminated()) {
			Long lx = program.nextOutput(l(joy));
			if (lx == null)
				break;
			
			int x = (int) (long) lx;
			int y = (int) (long) program.nextOutput(l(joy));
			int id = (int) (long) program.nextOutput(l(joy));
			if (x == -1 && y == 0) {
				score = id;
			} else {
				grid[y * w + x] = id;
			}
			// AI
			if (id == PADDLE) {
				px = x;
			} else if (id == BALL) {
				bx = x;
			}
			
			if (px > bx) {
				joy = -1;
			} else if (px < bx) {
				joy = 1;
			} else {
				joy = 0;
			}
		}
		return score;
	}
	
	private static int[] render(Program program) {
		Map<Tile, Integer> tiles = new HashMap<>();
		int w = 0, h = 0;
		while (!program.isTerminated()) {
			Long lx = program.nextOutput(null);
			if (lx == null)
				break;
			
			int x = (int) (long) lx;
			int y = (int) (long) program.nextOutput(null);
			int id = (int) (long) program.nextOutput(null);
			w = Math.max(w, x);
			h = Math.max(h, y);
			tiles.put(new Tile(x, y), id);
			
		}
		w++;
		h++;
		int[] grid = new int[w * h];
		for (Entry<Tile, Integer> e : tiles.entrySet()) {
			Tile t = e.getKey();
			grid[t.y * w + t.x] = e.getValue();
		}
		return grid;
	}
}
