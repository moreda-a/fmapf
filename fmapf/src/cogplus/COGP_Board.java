package cogplus;

import main.Board;

public class COGP_Board extends Board {
	COGP_Game game;
	public int[][] table;

	public COGP_Board(COGP_Game game) {
		this.game = game;
		table = new int[game.width][game.height];
	}

	public void setBoard(COGP_State st) {
		for (int i = 0; i < game.width; ++i)
			for (int j = 0; j < game.height; ++j)
				table[i][j] = st.board.table[i][j];

	}

	public void updateBoard(COGP_Action act) {
		table[act.x][act.y] = act.color;
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
		return "COGP_Board [table=" + s + "]";
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
