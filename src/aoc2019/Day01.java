package aoc2019;

public class Day01 extends Puzzle {
	
	@Override
	public Object part1(Input input) {
		int fuelSum = 0;
		while (input.hasNext()) {
			int modMass = Integer.parseInt(input.nextLine());
			fuelSum += (modMass / 3) - 2;
		}
		return fuelSum;
	}
	
	@Override
	public Object part2(Input input) {
		int fuelSum = 0;
		while (input.hasNext()) {
			int modMass = Integer.parseInt(input.nextLine());
			
			fuelSum += fuelCalc(modMass);
		}
		return fuelSum;
	}
	
	private int fuelCalc(int mass) {
		int fuelReq = (mass / 3) - 2;
		if (fuelReq <= 0) {
			return 0;
		}
		return fuelReq + fuelCalc(fuelReq);
	}
	
}
