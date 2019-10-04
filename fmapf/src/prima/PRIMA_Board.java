package prima;

import main.Board;
import main.Utility;

public class PRIMA_Board extends Board {
	PRIMA_Game game;
	public int[][] table;
	private int xx = -1, yy = -1, cc = -1;
	public int xl = -1, yl = -1;

	public PRIMA_Board(PRIMA_Game game) {
		this.game = game;
		table = new int[game.width][game.height];
	}

	private void setBoard(PRIMA_Board board) {
		for (int i = 0; i < game.width; ++i)
			for (int j = 0; j < game.height; ++j)
				table[i][j] = board.table[i][j];
	}

	public void setBoard(PRIMA_State st) {
		setBoard(st.board);
	}

	public void updateBoard(PRIMA_Action act) {
		// table[act.x][act.y] = (table[act.x][act.y] < 0 ? -act.color : act.color);
		table[act.x][act.y] = (act.finish ? -act.color : act.color);
		xx = act.x;
		yy = act.y;
		cc = act.color;
	}

	@Override
	public String toString() {
		String s = "\n{";
		for (int j = 0; j < game.height; ++j) {
			for (int i = 0; i < game.width; ++i)
				s += String.format("% 6d", table[i][j]) + ", ";
			s += (j != game.height - 1 ? "\n " : "");
		}
		s += "}";
		return "HMAPF_Board [table=" + s + "]";
	}

	public String toStringX() {
		String s = "\n{";
		int xt = -1, yt = -1, tmp = -9999;
		if (cc != -1) {
			xt = game.firstState.target[cc].x;
			yt = game.firstState.target[cc].y;
			if (table[xt][yt] == 0) {
				table[xt][yt] = -cc;
				tmp = 0;
			}
		}
		for (int j = 0; j < game.height; ++j) {
			for (int i = 0; i < game.width; ++i)
				if (i == xl && j == yl)
					s += Utility.YELLOW_BOLD + String.format("% 6d", table[i][j]) + Utility.RESET + ", ";
				else if (i == xx && j == yy)
					s += Utility.GREEN_BOLD + String.format("% 6d", table[i][j]) + Utility.RESET + ", ";
				else if (i == xt && j == yt)
					s += Utility.BLUE_BOLD + String.format("% 6d", table[i][j]) + Utility.RESET + ", ";
				else
					s += String.format("% 6d", table[i][j]) + ", ";
			s += (j != game.height - 1 ? "\n " : "");
		}
		s += "}";
		if (tmp != -9999)
			table[xt][yt] = tmp;
		return "HMAPF_Board [table=" + s + "]";
	}
}