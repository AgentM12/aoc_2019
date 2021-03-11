package aoc2019;

import java.util.ArrayList;
import java.util.List;

import aoc2019.IntCodeComputer.Program;

public class Day17 extends Puzzle {
	
	private IntCodeComputer icc;
	
	@Override
	public Object part1(Input input) {
		icc = new IntCodeComputer(input);
		Program p = new Program(icc.getProgramCache());
		List<Long> output = p.run(null);
		char[][] charray = listToCharray(output);
		return intersect(charray);
	}
	
	@Override
	public Object part2(Input input) {
		// Values found by hand, as doing it programmatically requires zip and stuff...
		final String main = "A,B,A,B,C,A,C,A,C,B\n";
		final String A = "R,12,L,8,L,4,L,4\n";
		final String B = "L,8,R,6,L,6\n";
		final String C = "L,8,L,4,R,12,L,6,L,4\n";
		final String feed = "n\n";
		final String rules = main + A + B + C + feed;
		List<Long> inputStream = longAscii(rules);
		icc = new IntCodeComputer(input);
		long[] program = icc.getProgramCache();
		program[0] = 2;
		Program p = new Program(program);
		List<Long> output = p.run(inputStream);
		return output.get(output.size() - 1);
	}
	
	private static List<Long> longAscii(String rules) {
		List<Long> list = new ArrayList<>();
		char[] cr = rules.toCharArray();
		for (char c : cr) {
			list.add((long) c);
		}
		return list;
	}
	
	private static int intersect(char[][] charray) {
		int sum = 0;
		for (int y = 1; y < charray.length - 1; y++) {
			char[] cs = charray[y];
			for (int x = 1; x < cs.length - 1; x++) {
				if (cs[x] != '#')
					continue;
				if (cs[x - 1] != '#')
					continue;
				if (cs[x + 1] != '#')
					continue;
				if (charray[y - 1][x] != '#')
					continue;
				if (charray[y + 1][x] != '#')
					continue;
				sum += (x * y);
			}
		}
		return sum;
	}
	
	private static char[][] listToCharray(List<Long> list) {
		int len = list.indexOf(10L);
		List<String> chars = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		for (Long l : list) {
			if (!l.equals(10L))
				sb.append((char) (long) l);
			else {
				chars.add(sb.toString());
				sb = new StringBuilder();
			}
		}
		char[][] charray = new char[chars.size() - 1][len];
		for (int y = 0; y < chars.size() - 1; y++) {
			charray[y] = chars.get(y).toCharArray();
		}
		return charray;
	}
}
