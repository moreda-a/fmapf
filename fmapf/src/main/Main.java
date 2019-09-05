package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import fmapf.*;

public class Main {
	private static final int MAX_RUN_MODE = 7;

	public static int inputMode;
	public static char[] debugMode;
	public static boolean isGarbageCollectorOn;
	public static char[] runMode;
	public static String gameName;
	private static String testCase;
	private static String testCase2;
	private static int num;
	public static boolean isLocalSolver = false;
	private static boolean isCenterlized;

	private static Game game = null;
	private static Simulator simulator = null;
	private static Solver solver = null;

	public static void main(String[] args) {
		Auxiliary.run();
		getConfiguration();
		switch (gameName) {
		case "FMAPF": // try to make it possible :D
			game = new FMAPF_Game(isCenterlized, testCase, testCase2, num);
			simulator = new FMAPF_Simulator();
			break;
		default:
			System.out.println("Unknown game!! : " + gameName);
			break;
		}
		solver = new MonteCarloTreeSearch(game, simulator);
		for (int i = 1; i <= runMode.length; ++i)
			if (runMode[i - 1] == '1') {
				updateRunMode(i);
				run(game, simulator, solver);
			}
	}

	private static void updateRunMode(int lvl) {
		Value.modelNumber = lvl;

	}

	private static void getConfiguration() {
		File file;
		file = new File("input/configuration.txt");
		try {
			Scanner sc = new Scanner(file);
			inputMode = sc.nextInt();
			debugMode = sc.next().toCharArray();
			isGarbageCollectorOn = sc.nextBoolean();
			runMode = sc.next().toCharArray();
			gameName = sc.next();
			testCase = sc.next();
			testCase2 = sc.next();
			num = sc.nextInt();
			isLocalSolver = sc.nextBoolean();
			isCenterlized = sc.nextBoolean();
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	private static void run(Game game, Simulator simulator, Solver solver) {
		Timer.init();
		game.setSimulator(simulator);
		game.init();
		Timer.startTimer(0);
		while (game.notEnded()) {
			State state = game.getState();
			if (Main.debugMode[0] == '1')
				System.out.println("gameState: \n" + state.toStringX());
			Timer.startTimer(1);
			State nextState = solver.getBestNextState(state);
			if (Main.debugMode[0] == '1')
				System.out.println(Timer.checkTimer(1));
			game.updateState(nextState);
		}
		System.out.println("FinalState: \n" + game.getState() + "\nModelNUmber: " + Value.modelNumber + " Time: "
				+ Timer.checkTimer(0) + " Ratio: " + game.getState().getValue() + " Depth: "
				+ game.getState().getDepth());

	}
}
