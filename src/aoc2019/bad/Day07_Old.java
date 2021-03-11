package aoc2019.bad;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

import aoc2019.Input;
import aoc2019.Puzzle;

public class Day07_Old extends Puzzle {
	
	public static void main(String[] args) {
		long now = System.currentTimeMillis();
		
		new Day07_Old().run();
		
		long then = System.currentTimeMillis();
		
		System.out.println("Time taken: " + (then - now) + " ms");
	}
	
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
	
	private static int amplifier(int sett, int sign) {
		int[] program = readCache();
		List<Integer> ins = new ArrayList<>();
		ins.add(sett);
		ins.add(sign);
		return run(program, ins, null);
	}
	
	private static int tryAmp(boolean part2) {
		final int[] settingsChoice;
		if (!part2) {
			settingsChoice = new int[] { 0, 1, 2, 3, 4 };
		} else {
			settingsChoice = new int[] { 5, 6, 7, 8, 9 };
			
		}
		return permute(part2, Day07_Old::pipeline, settingsChoice, 0, settingsChoice.length - 1, -1);
	}
	
	private static int pipeline(final Integer[] settings, boolean b) {
		int signal = 0;
		if (!b) {
			signal = amplifier(settings[0], signal);
			signal = amplifier(settings[1], signal);
			signal = amplifier(settings[2], signal);
			signal = amplifier(settings[3], signal);
			signal = amplifier(settings[4], signal);
		} else {
			Amplifier[] amps = new Amplifier[5];
			for (int i = 0; i < amps.length; i++) {
				amps[i] = new Amplifier(settings[i]);
			}
			signal = loop(amps);
		}
		return signal;
	}
	
	private static List<Integer> listifier(int... ints) {
		List<Integer> inp = new ArrayList<>();
		for (Integer integer : ints) {
			inp.add(integer);
		}
		return inp;
	}
	
	private static int loop(Amplifier[] amps) {
		int sign = 0;
		sign = run(amps[0].program, listifier(amps[0].sett, sign), amps[0]);
		sign = run(amps[1].program, listifier(amps[1].sett, sign), amps[1]);
		sign = run(amps[2].program, listifier(amps[2].sett, sign), amps[2]);
		sign = run(amps[3].program, listifier(amps[3].sett, sign), amps[3]);
		sign = run(amps[4].program, listifier(amps[4].sett, sign), amps[4]);
		while (true) {
			for (int i = 0; i < 5; i++) {
				List<Integer> inp = listifier(sign);
				int a = run(amps[i].program, inp, amps[i]);
				if (a == -1)
					return sign;
				sign = a;
			}
		}
	}
	
	private static int permute(boolean p2, BiFunction<Integer[], Boolean, Integer> func, int[] str, int l, int r, int max) {
		if (l == r) {
			Integer[] stri = new Integer[str.length];
			for (int i = 0; i < str.length; i++)
				stri[i] = str[i];
			int res = func.apply(stri, p2);
			return Math.max(max, res);
		}
		for (int i = l; i <= r; i++) {
			swap(str, l, i);
			max = Math.max(permute(p2, func, str, l + 1, r, max), max);
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
	
	private static class Amplifier {
		
		int[] program;
		int sett;
		int ip;
		
		public Amplifier(int setting) {
			ip = 0;
			sett = setting;
			this.program = readCache();
		}
	}
	
	// DAY 5 stuff
	
	private static int run(int[] program, List<Integer> inputStream, Amplifier ipHolder) {
		int ip = (ipHolder != null ? ipHolder.ip : 0);
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
			if (o != null) {
				ip += ss;
				if (ipHolder != null)
					ipHolder.ip = ip;
				return o;
			}
			if (ipMod[0] == ip)
				ip += ss;
			else
				ip = ipMod[0];
		}
		if (ipHolder != null)
			ipHolder.ip = ip;
		return -1;
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
	
	private static int fetchParamSize(int o) {
		if (o == 1 || o == 2 || o == 7 || o == 8)
			return 4;
		return (o == 3 || o == 4 ? 2 : 3);
	}
	
}
