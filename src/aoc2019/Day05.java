package aoc2019;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day05 extends Puzzle {
	
	private static int[] readProgram(Input input) {
		input.setDelim(",");
		List<Integer> ints = new ArrayList<>();
		while (input.hasNext()) {
			ints.add(input.nextInt());
		}
		return ints.stream().mapToInt(i -> i).toArray();
	}
	
	private static int[] run(int[] program, List<Integer> inputStream) {
		List<Integer> output = new ArrayList<>();
		int ip = 0;
		while (true) {
			int opcode = program[ip] % 100;
			int modes = program[ip] / 100;
			if (opcode == 99)
				break;
			int ss = fetchParamSize(opcode);
			int[] paramModes = new int[ss - 1];
			int[] params = new int[ss - 1];
			for (int i = 0; i < ss - 1; i++) {
				paramModes[i] = (modes / (int) Math.pow(10, i)) % 10;
				params[i] = program[ip + 1 + i];
			}
			int[] ipMod = new int[] { ip };
			Integer o = execute(opcode, params, paramModes, program, inputStream, ipMod);
			if (o != null)
				output.add(o);
			if (ipMod[0] == ip)
				ip += ss;
			else
				ip = ipMod[0];
		}
		return output.stream().mapToInt(i -> i).toArray();
	}
	
	private static Integer execute(int opcode, int[] params, int[] modes, int[] memory, List<Integer> inputStream, int[] ip) {
		if (opcode == 1) {
			memory[params[2]] = (modes[0] == 0 ? memory[params[0]] : params[0]) + (modes[1] == 0 ? memory[params[1]] : params[1]);
		} else if (opcode == 2) {
			memory[params[2]] = (modes[0] == 0 ? memory[params[0]] : params[0]) * (modes[1] == 0 ? memory[params[1]] : params[1]);
		} else if (opcode == 3) {
			memory[params[0]] = inputStream.get(0);
			inputStream.remove(0);
		} else if (opcode == 4) {
			return (modes[0] == 0 ? memory[params[0]] : params[0]);
		} else if (opcode == 5) {
			if ((modes[0] == 0 ? memory[params[0]] : params[0]) != 0)
				ip[0] = (modes[1] == 0 ? memory[params[1]] : params[1]);
		} else if (opcode == 6) {
			if ((modes[0] == 0 ? memory[params[0]] : params[0]) == 0)
				ip[0] = (modes[1] == 0 ? memory[params[1]] : params[1]);
		} else if (opcode == 7) {
			if ((modes[0] == 0 ? memory[params[0]] : params[0]) < (modes[1] == 0 ? memory[params[1]] : params[1]))
				memory[params[2]] = 1;
			else
				memory[params[2]] = 0;
		} else if (opcode == 8) {
			if ((modes[0] == 0 ? memory[params[0]] : params[0]) == (modes[1] == 0 ? memory[params[1]] : params[1]))
				memory[params[2]] = 1;
			else
				memory[params[2]] = 0;
		} else {
			throw new IllegalArgumentException("Opcode: " + opcode + " is invalid! STATE: " + Arrays.toString(params) + " :: " + Arrays.toString(modes));
		}
		return null;
	}
	
	@Override
	public Object part1(Input input) {
		int[] program = readProgram(input);
		List<Integer> inputs = new ArrayList<>();
		inputs.add(1);
		int[] output = run(program, inputs);
		return output[output.length - 1];
	}
	
	private static int fetchParamSize(int o) {
		if (o == 1 || o == 2 || o == 7 || o == 8)
			return 4;
		return (o == 3 || o == 4 ? 2 : 3);
	}
	
	@Override
	public Object part2(Input input) {
		int[] program = readProgram(input);
		List<Integer> inputs = new ArrayList<>();
		inputs.add(5);
		int[] output = run(program, inputs);
		return output[output.length - 1];
	}
	
}
