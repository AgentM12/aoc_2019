package aoc2019;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day02 extends Puzzle {
	
	private static int[] readProgram(Input input) {
		input.setDelim(",");
		List<Integer> ints = new ArrayList<>();
		while (input.hasNext()) {
			ints.add(input.nextInt());
		}
		return ints.stream().mapToInt(i -> i).toArray();
	}
	
	private static void run(int[] program) {
		int ip = 0;
		int ss = 4;
		while (true) {
			if (program[ip] == 99)
				break;
			if (program[ip] == 1) {
				program[program[ip + 3]] = program[program[ip + 1]] + program[program[ip + 2]];
			} else if (program[ip] == 2) {
				program[program[ip + 3]] = program[program[ip + 1]] * program[program[ip + 2]];
			}
			ip += ss;
		}
	}
	
	@Override
	public Object part1(Input input) {
		int[] program = readProgram(input);
		program[1] = 12;
		program[2] = 2;
		run(program);
		return program[0];
	}
	
	@Override
	public Object part2(Input input) {
		int[] program = readProgram(input);
		int key = findKey(program, 19690720);
		program[1] = 12;
		program[2] = 2;
		run(program);
		return key;
	}
	
	private static int findKey(int[] prog, int res) {
		int[] runnable;
		int bounds = 99;
		for (int verb = 0; verb < bounds; verb++) {
			for (int noun = 0; noun < bounds; noun++) {
				runnable = Arrays.copyOf(prog, prog.length);
				runnable[1] = noun;
				runnable[2] = verb;
				run(runnable);
				if (runnable[0] == res)
					return 100 * noun + verb;
			}
		}
		return -1;
	}
	
}
