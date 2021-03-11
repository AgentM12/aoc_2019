package aoc2019;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class Day07 extends Puzzle {
	
	private static int[] programCache;
	
	private static int[] readProgram(Input input) {
		input.setDelim(",");
		List<Integer> ints = new ArrayList<>();
		while (input.hasNext()) {
			ints.add(input.nextInt());
		}
		return ints.stream().mapToInt(i -> i).toArray();
	}
	
	static int[] readCache() {
		return Arrays.copyOf(programCache, programCache.length);
	}
	
	private static int tryAmp(boolean part2) {
		final int[] settingsChoice;
		if (!part2) {
			settingsChoice = new int[] { 0, 1, 2, 3, 4 };
		} else {
			settingsChoice = new int[] { 5, 6, 7, 8, 9 };
			
		}
		return permute(part2, settingsChoice, 0, settingsChoice.length - 1, -1);
	}
	
	private static int pipeline(final int[] settings, boolean p2) {
		Program[] programs = new Program[5];
		for (int i = 0; i < programs.length; i++) {
			programs[i] = new Program(readCache());
		}
		return loop(programs, settings, p2);
	}
	
	private static List<Integer> listifier(int... ints) {
		List<Integer> inp = new ArrayList<>();
		for (Integer integer : ints) {
			inp.add(integer);
		}
		return inp;
	}
	
	private static int loop(Program[] programs, int[] settings, boolean p2) {
		int sign = 0;
		sign = programs[0].nextOutput(listifier(settings[0], sign));
		sign = programs[1].nextOutput(listifier(settings[1], sign));
		sign = programs[2].nextOutput(listifier(settings[2], sign));
		sign = programs[3].nextOutput(listifier(settings[3], sign));
		sign = programs[4].nextOutput(listifier(settings[4], sign));
		while (p2) {
			for (int i = 0; i < 5; i++) {
				Integer o = programs[i].nextOutput(listifier(sign));
				if (o == null)
					return sign;
				sign = o;
			}
		}
		return sign;
	}
	
	private static int permute(boolean p2, int[] str, int l, int r, int max) {
		if (l == r) {
			int res = pipeline(str, p2);
			return Math.max(max, res);
		}
		for (int i = l; i <= r; i++) {
			swap(str, l, i);
			max = Math.max(permute(p2, str, l + 1, r, max), max);
			swap(str, l, i);
		}
		return max;
	}
	
	private static void swap(int[] a, int i, int j) {
		int temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}
	
	@Override
	public Object part1(Input input) {
		programCache = readProgram(input);
		
		return tryAmp(false);
	}
	
	@Override
	public Object part2(Input input) {
		programCache = readProgram(input);
		
		return tryAmp(true);
	}
	
	private static class MetaData {
		int ip;
		int[] data;
		List<Integer> inputStream;
		
		public MetaData(int ip, int[] data, List<Integer> inputStream) {
			this.ip = ip;
			this.data = data;
			this.inputStream = inputStream;
		}
	}
	
	private static class InstructionSet {
		
		public static final Map<Integer, BiFunction<Integer[], MetaData, Integer>> instructions;
		static {
			instructions = new HashMap<>();
			
			instructions.put(1, (params, metadata) -> {
				metadata.data[params[2]] = params[0] + params[1];
				return null;
			});
			
			instructions.put(2, (params, metadata) -> {
				metadata.data[params[2]] = params[0] * params[1];
				return null;
			});
			
			instructions.put(3, (params, metadata) -> {
				metadata.data[params[0]] = metadata.inputStream.get(0);
				metadata.inputStream.remove(0);
				return null;
			});
			
			instructions.put(4, (params, metadata) -> params[0]);
			
			instructions.put(5, (params, metadata) -> {
				if (params[0] != 0)
					metadata.ip = params[1];
				return null;
			});
			
			instructions.put(6, (params, metadata) -> {
				if (params[0] == 0)
					metadata.ip = params[1];
				return null;
			});
			
			instructions.put(7, (params, metadata) -> {
				if (params[0] < params[1])
					metadata.data[params[2]] = 1;
				else
					metadata.data[params[2]] = 0;
				return null;
			});
			
			instructions.put(8, (params, metadata) -> {
				if (params[0] == params[1])
					metadata.data[params[2]] = 1;
				else
					metadata.data[params[2]] = 0;
				return null;
			});
		}
		
		static Integer[] decode(int[] data, int opcode, int[] params, int[] modes) {
			Integer[] decodedParams = new Integer[params.length];
			if (opcode == 3) {
				decodedParams[0] = params[0];
				return decodedParams;
			}
			decodedParams[0] = (modes[0] == 0 ? data[params[0]] : params[0]);
			if (opcode == 4)
				return decodedParams;
			decodedParams[1] = (modes[1] == 0 ? data[params[1]] : params[1]);
			if (opcode == 5 || opcode == 6)
				return decodedParams;
			decodedParams[2] = params[2];
			return decodedParams;
		}
		
		static int fetchParamSize(int o) {
			if (o == 1 || o == 2 || o == 7 || o == 8)
				return 4;
			return (o == 3 || o == 4 ? 2 : 3);
		}
	}
	
	private static class Program {
		
		private static final int[] TP = { 1, 10, 100, 1000, 10000, 100000 };
		
		int[] data;
		int ip;
		boolean terminated;
		
		public Program(int[] data) {
			this.data = data;
			this.ip = 0;
			this.terminated = false;
		}
		
		Integer nextOutput(List<Integer> inputStream) {
			if (this.terminated) {
				System.out.println("Can't run program because it has terminated.");
				return null;
			}
			while (!this.terminated) {
				int opcode = data[ip] % 100;
				int modes = data[ip] / 100;
				if (opcode == 99) {
					this.terminated = true;
					return null;
				}
				int ss = InstructionSet.fetchParamSize(opcode);
				int[] paramModes = new int[ss - 1];
				int[] params = new int[ss - 1];
				for (int i = 0; i < ss - 1; i++) {
					paramModes[i] = (modes / TP[i]) % 10;
					params[i] = data[ip + 1 + i];
				}
				BiFunction<Integer[], MetaData, Integer> operation = InstructionSet.instructions.get(opcode);
				if (operation == null)
					throw new IllegalArgumentException("Opcode " + opcode + " is not defined!");
				
				Integer[] decodedParams = InstructionSet.decode(data, opcode, params, paramModes);
				MetaData md = new MetaData(ip, data, inputStream);
				Integer o = operation.apply(decodedParams, md);
				if (o != null) {
					ip += ss;
					return o;
				}
				if (md.ip == ip)
					ip += ss;
				else
					ip = md.ip;
			}
			return null;
		}
		
	}
	
}
