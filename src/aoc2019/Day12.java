package aoc2019;

import java.util.ArrayList;
import java.util.List;

// Heavily used hints from /r/aoc: Namely to figure out the characteristics of the period, used for efficiency.
public class Day12 extends Puzzle {
	
	@Override
	public Object part1(Input input) {
		List<Moon> moons = readInput(input);
		simulate(moons, 1000);
		return energy(moons);
	}
	
	@Override
	public Object part2(Input input) {
		List<Moon> moons = readInput(input);
		int[] initX = new int[moons.size()], initY = new int[moons.size()], initZ = new int[moons.size()];
		for (int i = 0; i < moons.size(); i++) {
			initX[i] = moons.get(i).x;
			initY[i] = moons.get(i).y;
			initZ[i] = moons.get(i).z;
		}
		return period(moons, initX, initY, initZ);
	}
	
	private static long period(List<Moon> moons, int[] initX, int[] initY, int[] initZ) {
		long period = 0;
		long[] periods = { 0, 0, 0 };
		
		while (periods[0] == 0 || periods[1] == 0 || periods[2] == 0) {
			period++;
			simulate(moons, 1);
			
			if (moons.get(0).x == initX[0] && moons.get(0).vx == 0 && moons.get(1).x == initX[1] && moons.get(1).vx == 0 && moons.get(2).x == initX[2]
					&& moons.get(2).vx == 0 && moons.get(3).x == initX[3] && moons.get(3).vx == 0) {
				periods[0] = period;
			}
			if (moons.get(0).y == initY[0] && moons.get(0).vy == 0 && moons.get(1).y == initY[1] && moons.get(1).vy == 0 && moons.get(2).y == initY[2]
					&& moons.get(2).vy == 0 && moons.get(3).y == initY[3] && moons.get(3).vy == 0) {
				periods[1] = period;
			}
			if (moons.get(0).z == initZ[0] && moons.get(0).vz == 0 && moons.get(1).z == initZ[1] && moons.get(1).vz == 0 && moons.get(2).z == initZ[2]
					&& moons.get(2).vz == 0 && moons.get(3).z == initZ[3] && moons.get(3).vz == 0) {
				periods[2] = period;
			}
			
		}
		return lcm(periods[0], lcm(periods[1], periods[2]));
	}
	
	private static List<Moon> readInput(Input input) {
		List<Moon> moons = new ArrayList<>();
		while (input.hasNext()) {
			String pos = input.nextLine();
			String[] out = pos.split("[=,>]");
			int x = Integer.parseInt(out[1]);
			int y = Integer.parseInt(out[3]);
			int z = Integer.parseInt(out[5]);
			moons.add(new Moon(x, y, z));
		}
		return moons;
	}
	
	private static long gcd(long a, long b) {
		if (b == 0) {
			return a;
		}
		return gcd(b, a % b);
	}
	
	private static long lcm(long a, long b) {
		return (a * b) / gcd(a, b);
	}
	
	private static int energy(List<Moon> moons) {
		int energy = 0;
		for (Moon moon : moons) {
			energy += moon.getEnergy();
		}
		return energy;
	}
	
	private static void simulate(List<Moon> moons, int steps) {
		for (int i = 0; i < steps; ++i) {
			for (Moon moon : moons) {
				moon.updateVelocity(moons);
			}
			for (Moon moon : moons) {
				moon.updatePosition();
			}
		}
	}
	
	private static class Moon {
		int x, y, z;
		int vx, vy, vz;
		
		public Moon(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
		public void updateVelocity(List<Moon> others) {
			for (Moon moon : others) {
				if (moon == this)
					continue;
				this.vx += compare(this.x, moon.x);
				this.vy += compare(this.y, moon.y);
				this.vz += compare(this.z, moon.z);
			}
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = prime + vx;
			result = prime * result + vy;
			result = prime * result + vz;
			result = prime * result + x;
			result = prime * result + y;
			return prime * result + z;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null || getClass() != obj.getClass())
				return false;
			Moon other = (Moon) obj;
			if (vx != other.vx || vy != other.vy || vz != other.vz || x != other.x || y != other.y || z != other.z)
				return false;
			return true;
		}
		
		public int getEnergy() {
			int potE = Math.abs(x) + Math.abs(y) + Math.abs(z);
			int kinE = Math.abs(vx) + Math.abs(vy) + Math.abs(vz);
			return potE * kinE;
		}
		
		public static int compare(int a, int b) {
			return Math.max(Math.min(b - a, 1), -1);
		}
		
		public void updatePosition() {
			x += vx;
			y += vy;
			z += vz;
		}
		
		@Override
		public String toString() {
			return String.format("(%d, %d, %d)", x, y, z);
		}
	}
}
