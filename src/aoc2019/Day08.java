package aoc2019;

public class Day08 extends Puzzle {
	
	@Override
	public Object part1(Input input) {
		int width = 25;
		int height = 6;
		
		String str = input.nextLine();
		int imgSize = (width * height);
		int imgs[] = new int[str.length()];
		int zeroIndex = -1, zeroCount = Integer.MAX_VALUE, bestCount = Integer.MAX_VALUE;
		for (int i = 0; i < str.length(); i++) {
			if (i % imgSize == 0) {
				if (bestCount > zeroCount) {
					zeroIndex = (i / imgSize) - 1;
					bestCount = zeroCount;
				}
				zeroCount = 0;
			}
			imgs[i] = str.charAt(i) - 48;
			if (imgs[i] == 0) {
				zeroCount++;
			}
		}
		if (bestCount > zeroCount) {
			zeroIndex = (str.length() / imgSize) - 1;
			bestCount = zeroCount;
		}
		
		int onesCount = 0, twosCount = 0;
		for (int i = zeroIndex * imgSize; i < (zeroIndex + 1) * imgSize; i++) {
			if (imgs[i] == 1)
				onesCount++;
			else if (imgs[i] == 2)
				twosCount++;
		}
		return onesCount * twosCount;
	}
	
	@Override
	public Object part2(Input input) {
		int width = 25;
		int height = 6;
		String str = input.nextLine();
		int imgSize = (width * height);
		int result[] = new int[imgSize];
		for (int i = 0; i < imgSize; i++) {
			int x = str.charAt(i) - 48;
			int k = 0;
			while (x == 2) {
				k++;
				x = (str.charAt(k * imgSize + i) - 48);
			}
			result[i] = x;
		}
		return print(result, width, height);
	}
	
	private static String print(int[] img, int w, int h) {
		StringBuilder sb = new StringBuilder().append("CYKBY\n");
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				sb.append(img[y * w + x] == 1 ? "#" : " ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
}
