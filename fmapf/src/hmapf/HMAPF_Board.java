package hmapf;

import main.Board;

public class HMAPF_Board extends Board {
	HMAPF_Game game;
	public int[][] table;

	public HMAPF_Board(HMAPF_Game game) {
		this.game = game;
		table = new int[game.width][game.height];
	}

	private void setBoard(HMAPF_Board board) {
		for (int i = 0; i < game.width; ++i)
			for (int j = 0; j < game.height; ++j)
				table[i][j] = board.table[i][j];
	}

	public void setBoard(HMAPF_State st) {
		setBoard(st.board);
	}

	public void updateBoard(HMAPF_Action act) {
		table[act.x][act.y] = (table[act.x][act.y] < 0 ? -act.color : act.color);
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
		return "HMAPF_Board [table=" + s + "]";
	}
}