package main;

import java.util.ArrayList;

public abstract class Game {

	public static ArrayList<Integer> localize = new ArrayList<Integer>();
	public static int endTime;
	protected boolean centralized;
	protected State gameState;
	public Simulator simulator;

	public abstract void init();

	public abstract Value CreateZeroValue();

	public boolean notEnded() {
		return gameState.isNotTerminal();
	}

	public State getState() {
		return gameState;
	}

	public void updateState(State newState) {
		gameState = newState;
	}

}
