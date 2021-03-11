package aoc2019;

import java.util.Calendar;

public class Runner {
	
	private static final int THIS_YEAR = 2019;
	
	public static void main(String[] args) throws ReflectiveOperationException {
		System.out.println("======= ADVENT OF CODE - " + THIS_YEAR + " =======\n");
		
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		if (month < 11 && year <= 2019) {
			System.out.println("The event has not started yet!");
			return;
		}
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		if (year > THIS_YEAR || dayOfMonth > 25) {
			dayOfMonth = 25;
		}
		long now = 0;
		for (int i = 1; i <= dayOfMonth; i++) {
			Class<?> c = Class.forName("aoc" + THIS_YEAR + ".Day" + String.format("%02d", i));
			Puzzle p = (Puzzle) c.newInstance();
			if (now == 0)
				now = System.currentTimeMillis();
			p.run();
		}
		long then = System.currentTimeMillis();
		System.out.println("Time taken: " + (then - now) / 1000d + " seconds!");
	}
	
}
