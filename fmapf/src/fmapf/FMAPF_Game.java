package fmapf;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import main.*;

public class FMAPF_Game extends Game {

	public State[] agentState;

	public int height;// y
	public int width;// x
	public int playerNumber;
	public int goalNumber;

	private String testCase;
	private FMAPF_Board firstBoard;
	private FMAPF_State firstState;

	public FMAPF_Game(boolean centralized, String testCase) {
		this.centralized = centralized;
		this.testCase = testCase;
	}

	@Override
	public void init() {
		getInfoFromInput();
		setFirstState();
	}

	private void getInfoFromInput() {
		File file;
		// if (FMAPF.unix)
		file = new File("input/testcase/fmapf/" + testCase);
		try {
			Scanner sc;
			if (Main.inputMode == 1)
				sc = new Scanner(System.in);
			else
				sc = new Scanner(file);
			width = sc.nextInt();
			height = sc.nextInt();
			playerNumber = sc.nextInt();
			goalNumber = sc.nextInt();
			firstBoard = new FMAPF_Board(this);
			firstState = new FMAPF_State(this, firstBoard);
			Game.endTime = 8;
			// Game.endTime = (width + height) + 2;
			// TODO
			for (int j = 0; j < height; ++j)
				for (int i = 0; i < width; ++i) {
					firstBoard.table[i][j] = sc.nextInt();
					if (firstBoard.table[i][j] > 0) {
						if (firstBoard.table[i][j] <= playerNumber) {
							if (firstState.lastMove[firstBoard.table[i][j]] == null)
								firstState.lastMove[firstBoard.table[i][j]] = FMAPF_State.pos[i][j];
						} else {
							// divar
						}
					} else if (firstBoard.table[i][j] < 0) {
						if (firstState.target[-firstBoard.table[i][j]] == null)
							firstState.target[-firstBoard.table[i][j]] = FMAPF_State.pos[i][j];
					}
				}
			firstState.lastColor = playerNumber;
			// localLastColor = playerNumber - 1;
			firstState.setNextColor();
			firstState.parent = null;
			// localLastColor = -1;
			firstState.lastColor = -1;
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		agentState = new State[playerNumber + 1];
		agentState[1] = firstState;
		firstState.myNumber = 1;
		firstState.nextColor = 1;
		for (int i = 2; i <= playerNumber; ++i) {
			agentState[i] = new FMAPF_State(firstState, i);
		}
	}

	private void setFirstState() {
		gameState = firstState;
	}

	@Override
	public Value CreateZeroValue() {
		return new FMAPF_Value(0, 0);
	}

}
