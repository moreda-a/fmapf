package g2048;

import main.Board;
import main.FastMath;

public class G2048_Board extends Board {
	static int[] dx = { 1, 0, -1, 0 };
	static int[] dy = { 0, -1, 0, 1 };

	static int[][] ddx = { { 3, 2, 1, 0 }, { 0, 1, 2, 3 }, { 0, 1, 2, 3 }, { 0, 1, 2, 3 } };
	static int[][] ddy = { { 0, 1, 2, 3 }, { 0, 1, 2, 3 }, { 0, 1, 2, 3 }, { 3, 2, 1, 0 } };

	G2048_Game game;
	public int[][] table;
	public int unsavedScore = 0;

	public G2048_Board(G2048_Game game) {
		this.game = game;
		table = new int[game.width][game.height];
	}

	private void setBoard(G2048_Board board) {
		for (int i = 0; i < game.width; ++i)
			for (int j = 0; j < game.height; ++j)
				table[i][j] = board.table[i][j];
	}

	public void setBoard(G2048_State st) {
		setBoard(st.board);
	}

	public void updateBoard(G2048_Action act) {
		int m = act.move;
		if (m == 0 || m == 2) {
			for (int i = 0; i < 4; ++i) {
				for (int j = 0; j < 4; ++j) {
					if (table[ddx[m][j]][ddy[m][i]] == 0) {
						for (int k = 0; k < 4 - j - 1; ++k)
							table[ddx[m][j + k]][ddy[m][i]] = table[ddx[m][j + k + 1]][ddy[m][i]];
						table[ddx[m][3]][ddy[m][i]] = 0;
					}
					if (j != 3 && table[ddx[m][j]][ddy[m][i]] == table[ddx[m][j + 1]][ddy[m][i]]) {
						unsavedScore += table[ddx[m][j]][ddy[m][i]];
						table[ddx[m][j]][ddy[m][i]] = table[ddx[m][j]][ddy[m][i]] * 2;
						table[ddx[m][j + 1]][ddy[m][i]] = 0;
					}

				}
			}
		}
		if (m == 1 || m == 3) {
			for (int j = 0; j < 4; ++j) {
				for (int i = 0; i < 4; ++i) {
					if (table[ddx[m][j]][ddy[m][i]] == 0) {
						for (int k = 0; k < 4 - i - 1; ++k)
							table[ddx[m][j]][ddy[m][i + k]] = table[ddx[m][j]][ddy[m][i + k + 1]];
						table[ddx[m][j]][ddy[m][3]] = 0;
					}
					if (i != 3 && table[ddx[m][j]][ddy[m][i]] == table[ddx[m][j]][ddy[m][i + 1]]) {
						unsavedScore += table[ddx[m][j]][ddy[m][i]];
						table[ddx[m][j]][ddy[m][i]] = table[ddx[m][j]][ddy[m][i]] * 2;
						table[ddx[m][j]][ddy[m][i + 1]] = 0;
					}

				}
			}
		}
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
		return "G2048_Board [table=" + s + "]";
	}

	public void addOnBoard() {
		int ans = 0;
		for (int i = 0; i < 4; ++i)
			for (int j = 0; j < 4; ++j)
				if (table[i][j] == 0)
					++ans;
		int ra = FastMath.rand256() % ans;
		for (int i = 0; i < 4; ++i)
			for (int j = 0; j < 4; ++j)
				if (table[i][j] == 0) {
					if (ra == 0)
						table[i][j] = 2 * (FastMath.rand256() % 2) + 2;
					--ra;
				}

	}

	public int updateScore() {
		int temp = unsavedScore;
		unsavedScore = 0;
		return temp;
	}

	public boolean canMove(int m) {
		if (m == 0 || m == 2) {
			for (int i = 0; i < 4; ++i) {
				for (int j = 0; j < 4; ++j) {
					if (table[ddx[m][j]][ddy[m][i]] == 0) {
						for (int k = 0; k < 4 - j - 1; ++k)
							if (table[ddx[m][j + k]][ddy[m][i]] != table[ddx[m][j + k + 1]][ddy[m][i]])
								return true;
						if (table[ddx[m][3]][ddy[m][i]] != 0)
							return true;
					}
					if (j != 3 && table[ddx[m][j]][ddy[m][i]] == table[ddx[m][j + 1]][ddy[m][i]]) {
						if (table[ddx[m][j]][ddy[m][i]] != 0)
							return true;
					}

				}
			}
		}
		if (m == 1 || m == 3) {
			for (int j = 0; j < 4; ++j) {
				for (int i = 0; i < 4; ++i) {
					if (table[ddx[m][j]][ddy[m][i]] == 0) {
						for (int k = 0; k < 4 - i - 1; ++k)
							if (table[ddx[m][j]][ddy[m][i + k]] != table[ddx[m][j]][ddy[m][i + k + 1]])
								return true;
						if (table[ddx[m][i]][ddy[m][3]] != 0)
							return true;
					}
					if (i != 3 && table[ddx[m][j]][ddy[m][i]] == table[ddx[m][j]][ddy[m][i + 1]]) {
						if (table[ddx[m][j]][ddy[m][i]] != 0)
							return true;
					}
				}
			}
		}
		return false;
	}
}