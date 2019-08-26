package prima;

import main.*;

public class PRIMA_Game extends Game {

	public State[] agentState;

	private String testCase;

	public PRIMA_Game(boolean centralized, String testCase) {
		this.centralized = centralized;
		this.testCase = testCase;
	}

	@Override
	public void init() {
		centralized = false;
		gameState = new PRIMA_State(testCase, 1);
		PRIMA_State ms = (PRIMA_State) gameState;
		Game.endTime = 3 * (ms.width + ms.height) / 2;
		FMAPF.timer = new long[ms.playerNumber + 1];
		for (int i = 1; i <= ms.playerNumber; ++i)
			FMAPF.timer[i] = 0;
		// Game.endTime = 22;
		agentState = new State[ms.playerNumber + 1];
		agentState[1] = gameState;
		for (int i = 2; i <= ms.playerNumber; ++i) {
			agentState[i] = new PRIMA_State(testCase, i);
		}
	}

	@Override
	public Value CreateZeroValue() {
		return new PRIMA_Value(0, 0);
	}

}