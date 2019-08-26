package main;

import java.util.HashMap;
import java.util.Scanner;

public class Auxiliary {
	static boolean aux = false;

	public static void run() {
		if (!aux)
			return;

		Scanner sc = new Scanner(System.in);
		int width = sc.nextInt();
		int height = sc.nextInt();
		int playerNumber = sc.nextInt();
		int goalNumber = sc.nextInt();
		// TODO
		String s = "";
		HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
		for (int j = 0; j < height; ++j) {
			for (int i = 0; i < width; ++i) {
				int u = sc.nextInt();
				if (hm.containsKey(u))
					u = -u;
				s += String.format("% 4d", u) + " ";
				hm.put(u, 1);
			}
			s += (j != height - 1 ? "\n " : "");
		}
		System.out.println(s);
		sc.close();
	}

}
