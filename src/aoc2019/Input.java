package aoc2019;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Input {
	
	private static final String INPUT_LOCATION = "res/input/";
	
	private Scanner sc;
	private boolean closed;
	
	public Input(String file) throws FileNotFoundException {
		sc = new Scanner(new File(INPUT_LOCATION + file + ".txt"));
		closed = false;
	}
	
	private Input() {
		closed = false;
	}
	
	public String readAllLines() {
		if (closed)
			return null;
		StringBuilder sb = new StringBuilder();
		while (sc.hasNext()) {
			sb.append(sc.nextLine() + "\n");
		}
		close();
		return sb.toString();
	}
	
	public boolean hasNext() {
		return !closed;
	}
	
	public static Input test(String input) {
		Input p = new Input();
		p.sc = new Scanner(input);
		return p;
	}
	
	public String nextLine() {
		if (!closed && sc.hasNext()) {
			String next = sc.nextLine();
			if (!sc.hasNext())
				close();
			return next;
		}
		return null;
	}
	
	public void setDelim(String pat) {
		sc.useDelimiter(pat);
	}
	
	public long nextLong() {
		if (!closed && sc.hasNext()) {
			long n = sc.nextLong();
			if (!sc.hasNext())
				close();
			return n;
		}
		throw new NoSuchElementException("Can't get next long!");
	}
	
	public int nextInt() {
		if (!closed && sc.hasNext()) {
			int n = sc.nextInt();
			if (!sc.hasNext())
				close();
			return n;
		}
		throw new NoSuchElementException("Can't get next int!");
	}
	
	private void close() {
		sc.close();
		closed = true;
	}
	
}
