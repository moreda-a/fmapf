package sapf;

import main.Board;

public class SAPF_Board extends Board {
	SAPF_Game game;
	public int[][] table;

	public SAPF_Board(SAPF_Game game) {
		this.game = game;
		table = new int[game.width][game.height];
	}

	private void setBoard(SAPF_Board board) {
		for (int i = 0; i < game.width; ++i)
			for (int j = 0; j < game.height; ++j)
				table[i][j] = board.table[i][j];
	}

	public void setBoard(SAPF_State st) {
		setBoard(st.board);
	}

	public void updateBoard(SAPF_Action act) {
		table[act.x][act.y] = (table[act.x][act.y] == -1 ? -act.color - 1 : act.color);
	}

	@Override
	public String toString() {
		String s = "\n{";
		for (int j = 0; j < game.height; ++j) {
			for (int i = 0; i < game.width; ++i)
				s += String.format("% 4d", table[i][j]) + ", ";
			s += (j != game.height - 1 ? "\n " : "");
		}
		s += "}";
		return "SAPF_Board [table=" + s + "]";
	}
//	@Override
//	public String toString() {
//		String s = "{";
//		for (int i = 0; i < size; ++i) {
//			for (int j = 0; j < size; ++j)
//				s += table[i][j] + ", ";
//			s += (i != size - 1 ? "\n " : "");
//		}
//		return s + "}";
//	}
}
