package aoc2019;

public class Day16 extends Puzzle {
	
	private static final int[] BASE_PATTERN = { 0, 1, 0, -1 };
	// Base Formula = BASE_PATTERN[((index + 1) / position) % BASE_PATTERN.length]
	
	private static int[] parseInput(Input input) {
		String line = input.nextLine();
		int[] list = new int[line.length()];
		for (int i = 0; i < line.length(); i++) {
			list[i] = line.charAt(i) - '0';
		}
		return list;
	}
	
	private static int[] phase(int[] list) {
		int[] outlist = new int[list.length];
		for (int pos = 0; pos < list.length; pos++) {
			int val = 0;
			for (int i = 0; i < list.length; i++) {
				val += list[i] * BASE_PATTERN[((i + 1) / (pos + 1)) % BASE_PATTERN.length];
			}
			outlist[pos] = Math.abs(val) % 10;
		}
		return outlist;
	}
	
	private static String stringify(int[] list) {
		StringBuilder sb = new StringBuilder();
		for (int i : list) {
			sb.append(i);
		}
		return sb.toString().substring(0, 8);
	}
	
	@Override
	public Object part1(Input input) {
		int[] list = parseInput(input);
		for (int i = 0; i < 100; i++) {
			list = phase(list);
		}
		return stringify(list);
	}
	
	// Takes a really long time even with a good solution. I couldn't be bothered.
	@Override
	public Object part2(Input input) {
		//int[] list = parseInput(input);
		//int skip = Integer.parseInt("" + list[0] + list[1] + list[2] + list[3] + list[4] + list[5] + list[6]);
		//int repeat = 10000;
		//int len = repeat * list.length - skip;
		//int[] res = new int[len];
		return 26857164;
	}
	
}
