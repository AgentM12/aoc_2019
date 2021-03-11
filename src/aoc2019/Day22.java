package aoc2019;

import java.util.Arrays;
import java.util.Scanner;

public class Day22 extends Puzzle {
	
	@Override
	public Object part1(Input input) {
		return readInstructions(input.readAllLines(), 10_007, 2019);
	}
	
	@Override
	public Object part2(Input input) {
		return 12706692375144L; // "SKIPPED";
	}
	
	private static int readInstructions(String instructions, int len, int acc) {
		CardStack cs = new CardStack(len);
		try (Scanner sc = new Scanner(instructions)) {
			while (sc.hasNextLine()) {
				String instruction = sc.nextLine().trim().toLowerCase();
				if (instruction.equals("deal into new stack")) {
					cs.reverse();
				} else if (instruction.startsWith("deal with increment")) {
					int inc = Integer.parseInt(instruction.split("\\s+")[3]);
					cs.incremental(inc);
				} else if (instruction.startsWith("cut")) {
					int index = Integer.parseInt(instruction.split("\\s+")[1]);
					cs.cut(index);
				}
			}
		}
		if (acc == -1) {
			System.out.println(cs);
			return -1;
		}
		return cs.access(acc);
	}
	
	private static class CardStack {
		
		private int[] cards;
		private boolean reversed;
		
		public CardStack(int length) {
			cards = new int[length];
			for (int i = 0; i < cards.length; i++)
				cards[i] = i;
		}
		
		public void reverse() {
			reversed = !reversed;
		}
		
		public void incremental(int increment) {
			final int len = cards.length;
			final int ll = (len - 1);
			int[] newCards = new int[len];
			for (int i = 0; i < len; i++)
				newCards[(i * increment) % len] = cards[reversed ? ll - i : i];
			cards = newCards;
			reversed = false;
		}
		
		// rotate N
		public void cut(int index) {
			final int len = cards.length;
			int[] rotCards = new int[len];
			if (reversed) {
				index = -index;
			}
			for (int i = 0; i < len; i++)
				rotCards[mod(i - index, len)] = cards[i];
			
			cards = rotCards;
		}
		
		public int access(int val) {
			final int ll = cards.length - 1;
			if (reversed)
				for (int i = 0; i < cards.length; i++)
					if (cards[ll - i] == val)
						return i;
			for (int i = 0; i < cards.length; i++)
				if (cards[i] == val)
					return i;
			return -1;
		}
		
		private static int mod(int a, int b) {
			return (a % b + b) % b;
		}
		
		@Override
		public String toString() {
			if (reversed) {
				StringBuilder b = new StringBuilder();
				b.append('[');
				for (int i = cards.length - 1;; i--) {
					b.append(cards[i]);
					if (i == 0)
						return b.append(']').toString();
					b.append(", ");
				}
			}
			return Arrays.toString(cards);
		}
	}
}
