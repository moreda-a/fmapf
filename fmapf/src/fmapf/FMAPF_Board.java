package fmapf;

import main.Board;

public class FMAPF_Board extends Board {
	FMAPF_Game game;
	public int[][] table;

	public FMAPF_Board(FMAPF_Game game) {
		this.game = game;
		table = new int[game.width][game.height];
	}

	private void setBoard(FMAPF_Board board) {
		for (int i = 0; i < game.width; ++i)
			for (int j = 0; j < game.height; ++j)
				table[i][j] = board.table[i][j];
	}

	public void setBoard(FMAPF_State st) {
		setBoard(st.board);
	}

	public void updateBoard(FMAPF_Action act) {
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