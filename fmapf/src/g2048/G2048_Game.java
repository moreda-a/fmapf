package g2048;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import hmapf.HMAPF_State;
import main.*;
import prima.PRIMA_State;

public class G2048_Game extends Game {

	public State[] agentState;

	public int height;// y
	public int width;// x
	public int playerNumber;
	public int goalNumber;

	private String testCase;
	private G2048_Board firstBoard;
	private G2048_State firstState;

	public G2048_Game(boolean centralized, String testCase) {
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
		file = new File("input/testcase/g2048/" + testCase);
		try {
			Scanner sc;
			if (FMAPF.systemInput)
				sc = new Scanner(System.in);
			else
				sc = new Scanner(file);
			width = sc.nextInt();
			height = sc.nextInt();
			firstBoard = new G2048_Board(this);
			firstState = new G2048_State(this, firstBoard);
			for (int j = 0; j < height; ++j)
				for (int i = 0; i < width; ++i) {
					firstBoard.table[i][j] = sc.nextInt();
				}
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
		return new G2048_Value(0, 0);
	}

}
