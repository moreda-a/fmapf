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
	private String testCase2;
	public FMAPF_Board firstBoard;
	public FMAPF_State firstState;

	public FMAPF_Game(boolean centralized, String testCase, String testCase2, int num) {
		this.centralized = centralized;
		this.testCase = testCase;
		this.testCase2 = testCase2;
		this.playerNumber = num;
		this.goalNumber = num;
	}

	@Override
	public void init() {
		getInfoFromInput();
		setFirstState();
	}

	private void getInfoFromInput() {
		File file = new File("input/testcase/fmapf/" + testCase);
		File file2 = new File("input/testcase/fmapf/" + testCase2);
		try {
			Scanner sc, sc2;
			if (Main.inputMode == 1)
				sc = new Scanner(System.in);
			else
				sc = new Scanner(file);
			sc.next();
			sc.next();
			sc.next();
			height = sc.nextInt();
			sc.next();
			width = sc.nextInt();
			sc.next();

			firstBoard = new FMAPF_Board(this);
			firstState = new FMAPF_State(this, firstBoard);
			Game.endTime = 30;
			// Game.endTime = (width + height) + 2;
			// TODO

			for (int j = 0; j < height; ++j) {
				String line = sc.next();
				for (int i = 0; i < width; ++i)
					firstBoard.table[i][j] = (line.charAt(i) == '.' || line.charAt(i) == 'G' ? 0 : 9999);
			}

			sc.close();
			if (Main.inputMode == 1)
				sc2 = new Scanner(System.in);
			else
				sc2 = new Scanner(file2);
			sc2.next();
			sc2.next();
			for (int i = 1; i <= playerNumber; ++i) {
				sc2.next();
				sc2.next();
				sc2.next();
				sc2.next();

				int y = sc2.nextInt();
				int x = sc2.nextInt();
				firstBoard.table[x][y] = i;
				firstState.lastMove[i] = FMAPF_State.pos[x][y];
				y = sc2.nextInt();
				x = sc2.nextInt();
				firstBoard.table[x][y] = -i;
				firstState.target[i] = FMAPF_State.pos[x][y];
				sc2.next();
			}

			firstState.lastColor = playerNumber;
			// localLastColor = playerNumber - 1;
			firstState.setNextColor();
			firstState.parent = null;
			// localLastColor = -1;
			firstState.lastColor = -1;
			sc2.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void setFirstState() {
		if (Main.debugMode[2] == '1')
			System.out.println(firstState);
		for (int i = 1; i <= playerNumber; ++i)
			firstBoard.table[firstState.target[i].x][firstState.target[i].y] = 0;
		gameState = new FMAPF_State(firstState);
	}

	@Override
	public Value CreateZeroValue() {
		return new FMAPF_Value(0, 0);
	}

}
