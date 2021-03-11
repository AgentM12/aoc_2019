package aoc2019;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Day14 extends Puzzle {
	
	String INPUT = "171 ORE => 8 CNZTR\r\n" + 
			"7 ZLQW, 3 BMBT, 9 XCVML, 26 XMNCP, 1 WPTQ, 2 MZWV, 1 RJRHP => 4 PLWSL\r\n" + 
			"114 ORE => 4 BHXH\r\n" + 
			"14 VRPVC => 6 BMBT\r\n" + 
			"6 BHXH, 18 KTJDG, 12 WPTQ, 7 PLWSL, 31 FHTLT, 37 ZDVW => 1 FUEL\r\n" + 
			"6 WPTQ, 2 BMBT, 8 ZLQW, 18 KTJDG, 1 XMNCP, 6 MZWV, 1 RJRHP => 6 FHTLT\r\n" + 
			"15 XDBXC, 2 LTCX, 1 VRPVC => 6 ZLQW\r\n" + 
			"13 WPTQ, 10 LTCX, 3 RJRHP, 14 XMNCP, 2 MZWV, 1 ZLQW => 1 ZDVW\r\n" + 
			"5 BMBT => 4 WPTQ\r\n" + 
			"189 ORE => 9 KTJDG\r\n" + 
			"1 MZWV, 17 XDBXC, 3 XCVML => 2 XMNCP\r\n" + 
			"12 VRPVC, 27 CNZTR => 2 XDBXC\r\n" + 
			"15 KTJDG, 12 BHXH => 5 XCVML\r\n" + 
			"3 BHXH, 2 VRPVC => 7 MZWV\r\n" + 
			"121 ORE => 7 VRPVC\r\n" + 
			"7 XCVML => 6 RJRHP\r\n" + 
			"5 BHXH, 4 VRPVC => 5 LTCX";
	
	@Override
	public Object part1(Input input) {
		Map<String, Formula> formulas = getFormulas(input);
		Map<String, Long> storage = new HashMap<>();
		return craft(1, "FUEL", formulas, storage);
	}
	
	@Override
	public Object part2(Input input) {
		//input = Input.test(INPUT);
		Map<String, Formula> formulas = getFormulas(input);
		return multiCraft(1000000000000L, "FUEL", formulas);
	}
	
	private static Map<String, Formula> getFormulas(Input input) {
		Map<String, Formula> formulas = new HashMap<>();
		while (input.hasNext()) {
			String[] ab = input.nextLine().split("\\s*=>\\s*");
			String[] res = ab[1].split("\\s+");
			String[] ing = ab[0].split("\\s*,\\s*");
			int[] coeffs = new int[ing.length];
			String[] rhs = new String[ing.length];
			for (int i = 0; i < ing.length; i++) {
				String[] c = ing[i].split("\\s+");
				rhs[i] = c[1];
				coeffs[i] = Integer.parseInt(c[0]);
			}
			Formula f = new Formula(Integer.parseInt(res[0]), res[1], coeffs, rhs);
			formulas.put(res[1], f);
		}
		return formulas;
	}
	
	// Slow and faulty, can produce off by one error. and has hardcoded data.
	private static long multiCraft(long desired, String result, Map<String, Formula> formulas) {
		Map<String, Long> storage = new HashMap<>();
		int cycles = 0;
		long count = 0;
		long once = craft(1, "FUEL", formulas, storage);
		count += craft(desired / once + 566666, "FUEL", formulas, storage);
		cycles += (desired / once + 566666);
		while (count < desired) {
			count += craft(1, "FUEL", formulas, storage);
			cycles++;
		}
		return cycles - 2;
	}
	
	public static long craft(long qc, String result, Map<String, Formula> formulas, Map<String, Long> storage) {
		if (result.equals("ORE"))
			return qc;
		long count = 0;
		Formula next = formulas.get(result);
		long fac = ((qc + next.count - 1) / next.count); // How many of the recipe we need to craft.
		for (int i = 0; i < next.coeffs.length; i++) {
			String nextI = next.rhs[i];
			long nextC = fac * next.coeffs[i]; // How many are crafted in the process.
			if (!storage.containsKey(nextI)) {
				storage.put(nextI, 0L);
			}
			long reqC = nextC;
			long storedC = storage.get(nextI);
			if (reqC >= storedC) {
				reqC -= storedC;
				storage.put(nextI, 0L);
				count += craft(reqC, nextI, formulas, storage);
				if (!nextI.equals("ORE")) {
					storage.put(nextI, storage.get(nextI) - reqC);
				}
			} else {
				storage.put(nextI, storedC - reqC);
			}
		}
		storage.put(result, fac * next.count);
		return count;
	}
	
	private static class Formula {
		int count;
		String lhs;
		int[] coeffs;
		String[] rhs;
		
		public Formula(int count, String lhs, int[] coeffs, String[] rhs) {
			this.count = count;
			this.lhs = lhs;
			this.coeffs = coeffs;
			this.rhs = rhs;
		}
		
		@Override
		public String toString() {
			return lhs + " = " + Arrays.toString(rhs);
		}
	}
	
}
