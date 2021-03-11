package aoc2019;

// Ugly but it works
public class Day04 extends Puzzle {
	
	@Override
	public Object part1(Input input) {
		String[] range = input.nextLine().trim().split("\\s*-\\s*");
		int from = Integer.parseInt(range[0]);
		int to = Integer.parseInt(range[1]);
		return checkAll(from, to, false);
	}
	
	@Override
	public Object part2(Input input) {
		String[] range = input.nextLine().trim().split("\\s*-\\s*");
		int from = Integer.parseInt(range[0]);
		int to = Integer.parseInt(range[1]);
		return checkAll(from, to, true);
	}
	
	private static int checkAll(int from, int to, boolean ignoreRepeats) {
		int count = 0;
		search: for (int i = from; i <= to; i++) {
			char[] num = ("" + i).toCharArray();
			boolean adj = false;
			boolean endTrue = false;
			int repCount = 0;
			char lastRepeat = '-';
			for (int j = 0; j < num.length - 1; j++) {
				if (ignoreRepeats) {
					endTrue = false;
					if (num[j] != lastRepeat && repCount == 1) {
						adj = true;
					} else if (num[j] == num[j + 1]) {
						if (num[j] == lastRepeat) {
							repCount = 2;
						} else {
							repCount = 1;
							endTrue = true;
						}
						lastRepeat = num[j];
					}
					if (num[j] > num[j + 1]) {
						continue search;
					}
				} else {
					if (num[j] == num[j + 1]) {
						adj = true;
					} else if (num[j] > num[j + 1]) {
						continue search;
					}
				}
			}
			if (adj || repCount == 1 || endTrue) {
				count++;
			}
			
		}
		return count;
	}
	
}
