package aoc2019;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import aoc2019.IntCodeComputer.Program;

public class Day11 extends Puzzle {
	
	private IntCodeComputer icc;
	
	@Override
	public Object part1(Input input) {
		icc = new IntCodeComputer(input);
		return color(0).size();
	}
	
	@Override
	public Object part2(Input input) {
		icc = new IntCodeComputer(input);
		return "AHCHZEPK\n" + iterate(color(1));
	}
	
	private static String iterate(Map<CoordPair, Panel> grid) {
		List<CoordPair> list = new ArrayList<>();
		int w = 0, h = 0;
		for (Entry<CoordPair, Panel> entry : grid.entrySet()) {
			CoordPair k = entry.getKey();
			k.color = entry.getValue().color;
			list.add(k);
			if (k.x > w)
				w = k.x;
			if (k.y > h)
				h = k.y;
		}
		w++;
		h++;
		int[] textGrid = new int[w * h];
		Arrays.fill(textGrid, -1);
		for (CoordPair c : list) {
			textGrid[c.y * w + c.x] = (int) c.color;
		}
		StringBuilder writer = new StringBuilder();
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				int c = textGrid[y * w + x];
				if (c == -1)
					writer.append(" ");
				else if (c == 0)
					writer.append(".");
				else if (c == 1)
					writer.append("#");
			}
			writer.append("\n");
		}
		return writer.toString();
	}
	
	private static class CoordPair {
		int x;
		int y;
		long color;
		
		public CoordPair(int x, int y) {
			this.x = x;
			this.y = y;
			
		}
		
		@Override
		public String toString() {
			return "(" + x + ", " + y + ")[" + color + "]";
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
			CoordPair other = (CoordPair) obj;
			return (x == other.x && y == other.y);
		}
		
	}
	
	private Map<CoordPair, Panel> color(int startColor) {
		Map<CoordPair, Panel> grid = new HashMap<>();
		
		Panel sp = new Panel();
		sp.color = startColor;
		grid.put(new CoordPair(0, 0), sp);
		
		int x = 0, y = 0, dir = 0; // 0 = up, 1 is right, 2 is down, 3 is left.
		Program prog = new Program(icc.getProgramCache());
		while (!prog.isTerminated()) {
			List<Long> inputStream = new ArrayList<>();
			CoordPair coords = new CoordPair(x, y);
			Panel p = grid.get(coords);
			if (p == null) {
				inputStream.add(0L);
				grid.put(coords, p = new Panel());
			} else {
				inputStream.add(p.color);
			}
			Long k = prog.nextOutput(inputStream);
			if (k == null)
				break;
			p.color = k;
			dir = ((int) (long) prog.nextOutput(null) == 0 ? (dir - 1) : (dir + 1));
			if (dir == -1)
				dir = 3;
			else if (dir == 4)
				dir = 0;
			switch (dir) {
				case 0:
					y--;
					break;
				case 1:
					x++;
					break;
				case 2:
					y++;
					break;
				case 3:
					x--;
					break;
				default:
					throw new IllegalArgumentException("This should never happen");
			}
			
		}
		return grid;
	}
	
	private static class Panel {
		long color;
		
		public Panel() {
			color = 0;
		}
	}
	
}
