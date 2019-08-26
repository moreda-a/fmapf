package sapf;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import main.*;

public class SAPF_Game extends Game {

	public int height;// y
	public int width;// x
	public int playerNumber;
	public int goalNumber;

	private String testCase;
	private SAPF_Board firstBoard;
	private SAPF_State firstState;

	public SAPF_Game(boolean centralized, String testCase) {
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
		file = new File("input/testcase/sapf/" + testCase);
		try {
			Scanner sc;
			if (FMAPF.systemInput)
				sc = new Scanner(System.in);
			else
				sc = new Scanner(file);
			width = sc.nextInt();
			height = sc.nextInt();
			playerNumber = sc.nextInt();
			goalNumber = sc.nextInt();
			firstBoard = new SAPF_Board(this);
			firstState = new SAPF_State(this, firstBoard);
			// Game.endTime = 3 * (width + height) / 2;
			Game.endTime = (width + height) + 2;
			// TODO
			for (int j = 0; j < height; ++j)
				for (int i = 0; i < width; ++i) {
					firstBoard.table[i][j] = sc.nextInt();
					if (firstBoard.table[i][j] != 0 && firstBoard.table[i][j] != -1) {
						if (firstState.lastMove[firstBoard.table[i][j]] == null)
							firstState.lastMove[firstBoard.table[i][j]] = SAPF_State.pos[i][j];
//						else
//							firstState.target[firstBoard.table[i][j]] = SAPF_State.pos[i][j];
					} else if (firstBoard.table[i][j] == -1) {
						firstState.target[-firstBoard.table[i][j]] = SAPF_State.pos[i][j];
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
	}

	private void setFirstState() {
		gameState = firstState;
	}

	@Override
	public Value CreateZeroValue() {
		return new SAPF_Value(0, 0);
	}

}
