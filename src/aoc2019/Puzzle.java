package aoc2019;

import java.io.FileNotFoundException;

public abstract class Puzzle {
	
	public abstract Object part1(Input input);
	
	public abstract Object part2(Input input);
	
	public final void run() {
		System.out.println("=== Day " + getClassIdentifier().substring(3, 5) + " ===");
		Input input = loadInput();
		System.out.println("Part 1: " + part1(input));
		input = loadInput();
		System.out.println("Part 2: " + part2(input));
		System.out.println();
	}
	
	private String getClassIdentifier() {
		return getClass().getSimpleName();
	}
	
	private Input loadInput() {
		try {
			return new Input(getClassIdentifier().toLowerCase());
		} catch (FileNotFoundException e) {
			return null;
		}
	}
}
