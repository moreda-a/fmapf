package cogplus;

import java.util.ArrayList;

import main.*;

public class COGP_State extends State {
//
//	static int[] dx = { 1, 0, -1, 0 };
//	static int[] dy = { 0, 1, 0, -1 };

	// ENSW, NEWS, NWES, WNSE, WSNE, SWEN, SEWN, ESNW
	// { ENSW},{ NEWS},{ NWES},{ WNSE},{ WSNE},{ SWEN},{ SEWN},{ ESNW}
	// { ENSW},{ NEWS},{ NWES},{ WNSE},{ WSNE},{ SWEN},{ SEWN},{ ESNW}
	// { 1,0,0,-1},{ 0,1,-1,0},{ 0,-1,1,0},{ -1,0,0,1},{ -1,0,0,1},{ 0,-1,1,0},{
	// 0,1,-1,0},{ 1,0,0,-1}
	// { 0,-1,1,0},{ -1,0,0,1},{ -1,0,0,1},{ 0,-1,1,0},{ 0,1,-1,0},{ 1,0,0,-1},{
	// 1,0,0,-1},{ 0,1,-1,0}
	static int[][] ddx = { { 1, 0, 0, -1 }, { 0, 1, -1, 0 }, { 0, -1, 1, 0 }, { -1, 0, 0, 1 }, { -1, 0, 0, 1 },
			{ 0, -1, 1, 0 }, { 0, 1, -1, 0 }, { 1, 0, 0, -1 }, { 1, 0, -1, 0 } };
	static int[][] ddy = { { 0, -1, 1, 0 }, { -1, 0, 0, 1 }, { -1, 0, 0, 1 }, { 0, -1, 1, 0 }, { 0, 1, -1, 0 },
			{ 1, 0, 0, -1 }, { 1, 0, 0, -1 }, { 0, 1, -1, 0 }, { 0, 1, 0, -1 } };

//	static int[] dx = { -1, 0, 0, 1 };
//	static int[] dy = { 0, -1, 1, 0 };

	static PII pos[][];

	public COGP_Game game;
	public COGP_Board board;

	public int nextColor;
	public int lastColor;
	public int colorRegion;

	public PII[] lastMove;
	public PII[] target;
	public boolean notChangable;

	class PII {
		public PII(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public Integer x;
		public Integer y;

		@Override
		public String toString() {
			return "PII [first=" + x + ", second=" + y + "]";
		}

	}

	@Override
	public void reset() {
		super.reset();
		lastColor = -1;
	}

	public COGP_State() {
		System.out.println("make bad state");
	}

	// just for first time
	public COGP_State(COGP_Game game, COGP_Board board) {
		this.board = board;
		this.game = game;
		lastMove = new PII[game.playerNumber + 1];
		target = new PII[game.playerNumber + 1];
		for (int i = 1; i <= game.playerNumber; ++i) {
			lastMove[i] = null;
			target[i] = null;
		}
		pos = new PII[game.width][game.height];
		for (int i = 0; i < game.width; ++i)
			for (int j = 0; j < game.height; ++j)
				pos[i][j] = new PII(i, j);

	}

	// nextState
	public COGP_State(COGP_State st, COGP_Action act) {
		game = st.game;
		board = new COGP_Board(game);
		board.setBoard(st);
		board.updateBoard(act);
		lastMove = new PII[game.playerNumber + 1];
		target = new PII[game.playerNumber + 1];
		for (int i = 1; i <= game.playerNumber; ++i) {
			lastMove[i] = st.lastMove[i];
			target[i] = st.target[i];
		}
		lastMove[act.color] = new PII(act.x, act.y);
		parent = st;
		lastColor = st.nextColor;
		setNextColor();
		depth = st.depth + 1;
	}

	/*
	 * Deep copy constructor
	 */
	public COGP_State(COGP_State st) {
		game = st.game;
		board = new COGP_Board(game);
		board.setBoard(st);
		lastMove = new PII[game.playerNumber + 1];
		target = new PII[game.playerNumber + 1];
		for (int i = 1; i <= game.playerNumber; ++i) {
			lastMove[i] = st.lastMove[i];
			target[i] = st.target[i];
		}
		parent = st;
		lastColor = st.lastColor;
		nextColor = st.nextColor;
		depth = st.depth;
	}

	void setNextColor() {
		double best = 1000000;
		int bestColor = lastColor % game.playerNumber + 1;
		// care disbestColor == 1
		// for (int i = 1; i <= game.playerNumber; ++i) {
		for (int i = 0; i < game.playerNumber; ++i) {
			nextColor = (lastColor + i - 1) % game.playerNumber + 1;
			int cn = childNumber();
			int cnt = childNumberTarget();
			if (isNear(nextColor) || cn == 0 || cnt == 0)
				continue;
			if (cn == 1) {
				bestColor = nextColor;
				break;
			}
			if (cnt == 1) {
				bestColor = nextColor;
				PII temp = lastMove[nextColor];
				lastMove[nextColor] = target[nextColor];
				target[nextColor] = temp;
				break;
			}
			// double gn = cn - dis(nextColor) / size * 2;
			if (dis(bestColor) != 1 && dis(nextColor) != 1) {
				if (dis(nextColor) < dis(bestColor)) {
					best = cn;
					bestColor = nextColor;
				} else if (dis(nextColor) == dis(bestColor))
					if (best > cn) {
						best = cn;
						bestColor = nextColor;
					}
			} else if(dis(nextColor) != 1){
					best = cn;
					bestColor = nextColor;
			}
//			if (best > cn) {
//				best = cn;
//				bestColor = nextColor;
//			} else if (best == cn) {
//				if (dis(nextColor) < dis(bestColor))
//					bestColor = nextColor;
//
//			}

		}
		nextColor = bestColor;

		int delx = (target[nextColor].x - lastMove[nextColor].x);
		int dely = (target[nextColor].y - lastMove[nextColor].y);
		if (delx >= 0 && dely <= 0) {
			if (Math.abs(delx) >= Math.abs(dely))
				colorRegion = 0;
			else
				colorRegion = 1;
		} else if (delx <= 0 && dely <= 0) {
			if (Math.abs(delx) <= Math.abs(dely))
				colorRegion = 2;
			else
				colorRegion = 3;
		} else if (delx <= 0 && dely >= 0) {
			if (Math.abs(delx) >= Math.abs(dely))
				colorRegion = 4;
			else
				colorRegion = 5;
		} else if (delx >= 0 && dely >= 0) {
			if (Math.abs(delx) <= Math.abs(dely))
				colorRegion = 6;
			else
				colorRegion = 7;
		}
		// colorRegion = 8;
	}

	private int dis(int color) {
		return Math.abs(lastMove[color].x - target[color].x) + Math.abs(lastMove[color].y - target[color].y);
	}

	private boolean isNear(int color) {
		return Math.abs(lastMove[color].x - target[color].x) + Math.abs(lastMove[color].y - target[color].y) == 1;
	}

	@Override
	public boolean isNotTerminal() {
		int res = 0;
		for (int i = 1; i <= game.playerNumber; ++i)
			res += isNear(i) ? 1 : 0;
		if (res == game.playerNumber)
			return false;
		if (!hasChild())
			return false;
		return true;
	}

	@Override
	public Value getValue() {
		if (isNotTerminal())
			return null;
		double res = 0;
		boolean m[] = new boolean[game.playerNumber + 1];
		for (int i = 1; i <= game.playerNumber; ++i) {
			res += isNear(i) ? 1 : 0;
			// res -= isNear(i) ? (double) depth / (2 * size * size) : 0;
			m[i] = isNear(i);
		}

		return new COGP_Value(-1, res / game.playerNumber, m);
	}

	@Override
	public String toString() {
		return "COGP_State [board=" + board + "]";
	}

	@Override
	public ArrayList<State> refreshChilds() {
		ArrayList<State> childss = new ArrayList<State>();
		for (int i = 0; i < 4; ++i)
			if (lastMove[nextColor].x + ddx[colorRegion][i] >= 0
					&& lastMove[nextColor].x + ddx[colorRegion][i] < game.width
					&& lastMove[nextColor].y + ddy[colorRegion][i] >= 0
					&& lastMove[nextColor].y + ddy[colorRegion][i] < game.height
					&& board.table[lastMove[nextColor].x + ddx[colorRegion][i]][lastMove[nextColor].y
							+ ddy[colorRegion][i]] == 0)
				childss.add(COGP_Simulator.simulateX(this, new COGP_Action(lastMove[nextColor].x + ddx[colorRegion][i],
						lastMove[nextColor].y + ddy[colorRegion][i], nextColor)));
		return childss;
	}

	private boolean hasChild() {
		for (int i = 0; i < 4; ++i)
			if (lastMove[nextColor].x + ddx[colorRegion][i] >= 0
					&& lastMove[nextColor].x + ddx[colorRegion][i] < game.width
					&& lastMove[nextColor].y + ddy[colorRegion][i] >= 0
					&& lastMove[nextColor].y + ddy[colorRegion][i] < game.height
					&& board.table[lastMove[nextColor].x + ddx[colorRegion][i]][lastMove[nextColor].y
							+ ddy[colorRegion][i]] == 0)
				return true;
		return false;
	}

	private int childNumber() {
		int ans = 0;
		for (int i = 0; i < 4; ++i)
			if (lastMove[nextColor].x + ddx[colorRegion][i] >= 0
					&& lastMove[nextColor].x + ddx[colorRegion][i] < game.width
					&& lastMove[nextColor].y + ddy[colorRegion][i] >= 0
					&& lastMove[nextColor].y + ddy[colorRegion][i] < game.height
					&& board.table[lastMove[nextColor].x + ddx[colorRegion][i]][lastMove[nextColor].y
							+ ddy[colorRegion][i]] == 0)
				++ans;
		return ans;
	}

	private int childNumberTarget() {
		PII temp = lastMove[nextColor];
		lastMove[nextColor] = target[nextColor];
		target[nextColor] = temp;
		int res = childNumber();
		temp = lastMove[nextColor];
		lastMove[nextColor] = target[nextColor];
		target[nextColor] = temp;
		return res;
	}

	public void rollDown() {
		// TODO fast random
		long stt = System.currentTimeMillis();
//		Random random = new Random();
//		int v = random.nextInt(childNumber());
		int v = FastMath.rand256() % childNumber();
		// int v = (int) (stt % childNumber());
		int ans = 0;
		COGP_Action nextAct = null;
		FMAPF.shitTimer[13] += System.currentTimeMillis() - stt;
		stt = System.currentTimeMillis();
		for (int i = 0; i < 4; ++i) {
			if (lastMove[nextColor].x + ddx[colorRegion][i] >= 0
					&& lastMove[nextColor].x + ddx[colorRegion][i] < game.width
					&& lastMove[nextColor].y + ddy[colorRegion][i] >= 0
					&& lastMove[nextColor].y + ddy[colorRegion][i] < game.height
					&& board.table[lastMove[nextColor].x + ddx[colorRegion][i]][lastMove[nextColor].y
							+ ddy[colorRegion][i]] == 0) {
				++ans;
				if (ans == v + 1) {
					nextAct = new COGP_Action(lastMove[nextColor].x + ddx[colorRegion][i],
							lastMove[nextColor].y + ddy[colorRegion][i], nextColor);
					break;
				}
			}
		}
		FMAPF.shitTimer[11] += System.currentTimeMillis() - stt;
		stt = System.currentTimeMillis();
		if (nextAct != null && (board.table[nextAct.x][nextAct.y] == 0))
			updateDown(nextAct);
		else
			System.out.println("WTF?!0");
		FMAPF.shitTimer[12] += System.currentTimeMillis() - stt;
		stt = System.currentTimeMillis();
		// state = state.getRandomChild();
	}

	private void updateDown(COGP_Action act) {
		// TODO lolo was here =)))
		board.updateBoard(act);
		// parent = st;
		// parent lol :D
		lastColor = nextColor;
//		localLastColor = localNextColor;
		setNextColor();
		depth = depth + 1;
//		else
//			depth = depth;
//		realDepth = realDepth;

	}

	@Override
	protected void setLocalAgents(Game gamex) {
//		Game.localize = new ArrayList<Integer>();
//		for (int i = 1; i <= game.playerNumber; ++i) {
//			if (Math.abs(lastMove[i].first - lastMove[game.myNumber].first)
//					+ Math.abs(lastMove[i].second - lastMove[myNumber].second) < 10)
//				Game.localize.add(i);
//			// PII pii = new PII(i, j);
//		}

	}

}
