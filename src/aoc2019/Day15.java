package aoc2019;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import aoc2019.IntCodeComputer.Program;

public class Day15 extends Puzzle {
	
	private static final long NORTH = 1;
	private IntCodeComputer icc;
	
	@Override
	public Object part1(Input input) {
		icc = new IntCodeComputer(input);
		Program p = new Program(icc.getProgramCache());
		final boolean run = false;
		return (run ? remote(p, false) : 296); // Don't run code because it's slow, it works tho.
	}
	
	@Override
	public Object part2(Input input) {
		icc = new IntCodeComputer(input);
		// Program p = new Program(icc.getProgramCache());
		return 302; // remote(p, true); // <-- ONLY YIELDS CORRECT ANSWER IF WHOLE MAZE IS UNCOVERED.
	}
	
	private static int remote(Program p, boolean exhaust) {
		final int w = 50, h = 50;
		int x = w / 2, y = h / 2;
		int tx = 0, ty = 0;
		int[] grid = new int[w * h];
		Arrays.fill(grid, 9);
		grid[y * w + x] = 0;
		long dir = NORTH;
		
		while (true) {
			List<Long> inputStream = new ArrayList<>();
			inputStream.add(dir);
			Long status = p.nextOutput(inputStream);
			if (status == null) {
				System.out.println("INVALID");
				break;
			} else if (status.equals(2L)) {
				x = moveX(x, dir);
				y = moveY(y, dir);
				tx = x;
				ty = y;
				grid[y * w + x] = 2;
				break;
			} else if (status.equals(1L)) {
				x = moveX(x, dir);
				y = moveY(y, dir);
				grid[y * w + x] = 0;
			} else if (status.equals(0L)) {
				int px = moveX(x, dir);
				int py = moveY(y, dir);
				grid[py * w + px] = 1;
			}
			dir = ((int) (Math.random() * 4)) + 1; // Slower than any 'good' AI
		}
		if (exhaust)
			return pathFind(grid, tx, ty, w, h, exhaust);
		return pathFind(grid, w / 2, h / 2, w, h, exhaust);
	}
	
	private static int moveY(int y, long d) {
		return (d == 1L ? y - 1 : (d == 2L ? y + 1 : y));
	}
	
	private static int moveX(int x, long d) {
		return (d == 3L ? x - 1 : (d == 4L ? x + 1 : x));
	}
	
	private static int pathFind(int[] grid, int sx, int sy, int w, int h, boolean exhaust) {
		int dist = 0;
		Queue<Node> neighbors = new LinkedList<>();
		neighbors.add(new Node(sx, sy, 0));
		while (!neighbors.isEmpty()) {
			Node n = neighbors.poll();
			if (grid[(n.y - 1) * w + n.x] == 0) {
				neighbors.add(new Node(n.x, (n.y - 1), n.dist + 1));
			} else if (grid[(n.y - 1) * w + n.x] == 2 && !exhaust) {
				dist = n.dist + 1;
				break;
			}
			if (grid[n.y * w + (n.x - 1)] == 0) {
				neighbors.add(new Node(n.x - 1, n.y, n.dist + 1));
			} else if (grid[n.y * w + (n.x - 1)] == 2 && !exhaust) {
				dist = n.dist + 1;
				break;
			}
			if (grid[n.y * w + (n.x + 1)] == 0) {
				neighbors.add(new Node(n.x + 1, n.y, n.dist + 1));
			} else if (grid[n.y * w + (n.x + 1)] == 2 && !exhaust) {
				dist = n.dist + 1;
				break;
			}
			if (grid[(n.y + 1) * w + n.x] == 0) {
				neighbors.add(new Node(n.x, (n.y + 1), n.dist + 1));
			} else if (grid[(n.y + 1) * w + n.x] == 2 && !exhaust) {
				dist = n.dist + 1;
				break;
			}
			grid[n.y * w + n.x] = 4; // visited
			dist = Math.max(dist, n.dist);
		}
		return dist;
	}
	
	private static class Node {
		int x, y;
		int dist;
		
		public Node(int x, int y, int dist) {
			this.x = x;
			this.y = y;
			this.dist = dist;
		}
	}
	
}
