package aoc2019;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day06 extends Puzzle {
	
	@Override
	public Object part1(Input input) {
		Node com = readInput(input, null);
		return findPath(com, 0);
	}
	
	@Override
	public Object part2(Input input) {
		List<Node> remarkables = new ArrayList<>();
		remarkables.add(new Node("YOU"));
		remarkables.add(new Node("SAN"));
		readInput(input, remarkables);
		return intersect(remarkables.get(0), remarkables.get(1));
	}
	
	private static int intersect(Node a, Node b) {
		List<Node> aParents = new ArrayList<>();
		List<Node> bParents = new ArrayList<>();
		Node x = a;
		while (x.parent != null) {
			aParents.add(x.parent);
			x = x.parent;
		}
		x = b;
		while (x.parent != null) {
			bParents.add(x.parent);
			x = x.parent;
		}
		while (true) {
			Node s = aParents.get(aParents.size() - 1);
			Node t = bParents.get(bParents.size() - 1);
			if (s.equals(t)) {
				aParents.remove(s);
				bParents.remove(t);
				continue;
			}
			break;
		}
		return aParents.size() + bParents.size();
	}
	
	private static Node readInput(Input input, List<Node> remarkables) {
		Node com = null;
		Map<String, Node> allNodes = new HashMap<>();
		while (input.hasNext()) {
			String code = input.nextLine();
			String[] ns = code.split("[)\\n]");
			Node a = allNodes.get(ns[0]);
			Node b = allNodes.get(ns[1]);
			
			if (a == null) {
				a = new Node(ns[0]);
				allNodes.put(ns[0], a);
			}
			if (b == null) {
				b = new Node(ns[1]);
				allNodes.put(ns[1], b);
			}
			if (a.name.equals("COM"))
				com = a;
			b.parent = a;
			a.children.add(b); // a --> b
			if (remarkables != null) {
				for (Node x : remarkables) {
					if (x.name.equals(a.name)) {
						remarkables.remove(x);
						remarkables.add(a);
						break;
					}
					if (x.name.equals(b.name)) {
						remarkables.remove(x);
						remarkables.add(b);
						break;
					}
				}
			}
		}
		return com;
	}
	
	private static class Node {
		String name;
		Node parent;
		List<Node> children;
		
		public Node(String name) {
			this.name = name;
			this.children = new ArrayList<>();
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	private static int findPath(Node n, int d) {
		int distance = d;
		for (Node x : n.children) {
			distance += findPath(x, d + 1);
		}
		return distance;
	}
}
