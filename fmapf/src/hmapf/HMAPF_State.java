package hmapf;

import java.util.ArrayList;

import main.*;
import prima.PRIMA_Action;
import prima.PRIMA_Simulator;

public class HMAPF_State extends State {
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

	public HMAPF_Game game;
	public HMAPF_Board board;

	public int nextColor;
	public int lastColor;
	public int colorRegion = 8;

	public PII[] lastMove;
	public PII[] target;
	public boolean notChangable;
	private Boolean isNotTerminal = null;
	public int realDepth = 0;
	public int myNumber;
	public int allMoveDone = 0;

	class PII {
		public PII(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public Integer x;
		public Integer y;

		@Override
		public String toString() {
			return "PII [x=" + x + ", y=" + y + "]";
		}

	}

	@Override
	public void reset() {
		super.reset();
		lastColor = -1;
	}

	public HMAPF_State() {
		System.out.println("make bad state");
	}

	// just for first time
	public HMAPF_State(HMAPF_Game game, HMAPF_Board board) {
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
	public HMAPF_State(HMAPF_State st, HMAPF_Action act) {
		game = st.game;
		board = new HMAPF_Board(game);
		lastMove = new PII[game.playerNumber + 1];
		target = new PII[game.playerNumber + 1];
		for (int i = 1; i <= game.playerNumber; ++i) {
			lastMove[i] = st.lastMove[i];
			target[i] = st.target[i];
		}
		board.setBoard(st);
		if (board.table[act.x][act.y] == -act.color || board.table[act.x][act.y] == 0) {
			board.table[lastMove[act.color].x][lastMove[act.color].y] = 0;
		}
		board.updateBoard(act);
		lastMove[act.color] = pos[act.x][act.y];
		parent = st;
		lastColor = st.nextColor;
		setNextColor();
		// depth = st.depth + 1;
		if (nextColor <= lastColor)
			depth = st.depth + 1;
		else
			depth = st.depth;
		realDepth = st.realDepth;
		// TODO lolo was here =)))
		allMoveDone = st.allMoveDone + 1;
	}

	/*
	 * Deep copy constructor
	 */
	public HMAPF_State(HMAPF_State st) {
		game = st.game;
		board = new HMAPF_Board(game);
		board.setBoard(st);
		lastMove = new PII[game.playerNumber + 1];
		target = new PII[game.playerNumber + 1];
		for (int i = 1; i <= game.playerNumber; ++i) {
			lastMove[i] = st.lastMove[i];
			target[i] = st.target[i];
		}
		parent = st;
		colorRegion = st.colorRegion;
		lastColor = st.lastColor;
		nextColor = st.nextColor;
		depth = st.depth;
		realDepth = st.realDepth;
		isNotTerminal = st.isNotTerminal;
		notChangable = st.notChangable;
		allMoveDone = st.allMoveDone;
	}

	// copy for each agent

	public HMAPF_State(HMAPF_State st, int mynum) {
		game = st.game;
		board = new HMAPF_Board(game);
		board.setBoard(st);
		lastMove = new PII[game.playerNumber + 1];
		target = new PII[game.playerNumber + 1];
		for (int i = 1; i <= game.playerNumber; ++i) {
			lastMove[i] = st.lastMove[i];
			target[i] = st.target[i];
		}
		parent = st;
		colorRegion = st.colorRegion;
		lastColor = st.lastColor;
		nextColor = st.nextColor;
		depth = st.depth;
		realDepth = st.realDepth;
		isNotTerminal = st.isNotTerminal;
		notChangable = st.notChangable;

		nextColor = mynum;
		myNumber = mynum;
		allMoveDone = st.allMoveDone;
	}

	public HMAPF_State(State[] agentState, State[] gg, int myNumber) {
		HMAPF_State st = (HMAPF_State) agentState[1];
		this.myNumber = myNumber;
		game = st.game;
		board = new HMAPF_Board(game);
		board.setBoard(st);
		lastMove = new PII[game.playerNumber + 1];
		target = new PII[game.playerNumber + 1];
		for (int i = 1; i <= game.playerNumber; ++i) {
			lastMove[i] = st.lastMove[i];
			target[i] = st.target[i];
		}
		allMoveDone = st.allMoveDone;
		for (int i = 1; i <= game.playerNumber; ++i) {
			try {
				int help = board.table[((HMAPF_State) gg[i]).lastMove[i].x][((HMAPF_State) gg[i]).lastMove[i].y];

				if (((HMAPF_State) gg[i]).lastMove[i] != lastMove[i] && (help == 0 || help < 0)) {
					if (board.table[lastMove[i].x][lastMove[i].y] != -i)
						allMoveDone = allMoveDone + 1;
					board.table[lastMove[i].x][lastMove[i].y] = 0;
					board.table[((HMAPF_State) gg[i]).lastMove[i].x][((HMAPF_State) gg[i]).lastMove[i].y] = (help == 0
							? i
							: -i);
					lastMove[i] = ((HMAPF_State) gg[i]).lastMove[i];
				}
			} catch (Exception e) {
				System.out.println(i);
				System.out.println(gg[i]);
				System.out.println(((HMAPF_State) gg[i]).lastMove[i]);
			}
		}
		// table[act.y][act.x] = act.color;
		// lastMove[act.color] = new PII(act.y, act.x);
		parent = st;
		lastColor = st.nextColor;
		// localLastColor = st.localNextColor;
		setNextColor();
//		if (nextColor <= lastColor)
//			depth = st.depth + 1;
//		else
//			depth = st.depth;
		depth = 0;
		realDepth = st.realDepth + 1;
	}

	public void setNextColor() {
		for (int i = 1; i <= game.playerNumber; ++i) {
			nextColor = (lastColor + i - 1) % game.playerNumber + 1;
			int cn = childNumber();
			if (cn == 0)
				continue;
			else
				break;
		}

//		double best = 1000000;
//		int bestColor = lastColor % game.playerNumber + 1;
//		// care disbestColor == 1
//		// for (int i = 1; i <= game.playerNumber; ++i) {
//		for (int i = 0; i < game.playerNumber; ++i) {
//			nextColor = (lastColor + i - 1) % game.playerNumber + 1;
//			int cn = childNumber();
//			// int cnt = childNumberTarget();
//			if (isNear(nextColor) || cn == 0)// || cnt == 0)
//				continue;
//			if (cn == 1) {
//				bestColor = nextColor;
//				break;
//			}
////			if (cnt == 1) {
////				bestColor = nextColor;
////				PII temp = lastMove[nextColor];
////				lastMove[nextColor] = target[nextColor];
////				target[nextColor] = temp;
////				break;
////			}
//			// double gn = cn - dis(nextColor) / size * 2;
////			if (dis(bestColor) != 1 && dis(nextColor) != 1) {
////				if (dis(nextColor) < dis(bestColor)) {
////					best = cn;
////					bestColor = nextColor;
////				} else if (dis(nextColor) == dis(bestColor))
////					if (best > cn) {
////						best = cn;
////						bestColor = nextColor;
////					}
////			} else if (dis(nextColor) != 1) {
////				best = cn;
////				bestColor = nextColor;
////			}
//
////			if (best > cn) {
////				best = cn;
////				bestColor = nextColor;
////			} else if (best == cn) {
////				if (dis(nextColor) < dis(bestColor))
////					bestColor = nextColor;
////
////			}
//
//		}

		setRegion();
	}

//	private int dis(int color) {
//		return Math.abs(lastMove[color].x - target[color].x) + Math.abs(lastMove[color].y - target[color].y);
//	}

	private boolean isNear(int color) {
		// return Math.abs(lastMove[color].x - target[color].x) +
		// Math.abs(lastMove[color].y - target[color].y) == 1;
		return board.table[lastMove[color].x][lastMove[color].y] < 0;
	}

	@Override
	public boolean isNotTerminal() {
		if (notChangable && isNotTerminal != null)
			return isNotTerminal;
		boolean vres = true;
		for (int i = 1; i <= game.playerNumber; ++i) {
			if (!isNear(i)) {
				vres = false;
				break;
			}
		}
		if (vres || !hasChild() || realDepth + depth >= Game.endTime)
			return isNotTerminal = false;
		return isNotTerminal = true;
		// int res = 0;
//		for (int i = 1; i <= game.playerNumber; ++i)
//			res += isNear(i) ? 1 : 0;
//		if (res == game.playerNumber)
//			return false;
//		if (!hasChild())
//			return false;
//		return true;
	}

	@Override
	public Value getValue() {
		if (isNotTerminal())
			return null;
		double res = 0;
		double mres = 0;
		boolean m[] = new boolean[game.playerNumber + 1];
		for (int i = 1; i <= game.playerNumber; ++i) {
			res += isNear(i) ? 1 : 0;
			// res -= isNear(i) ? (double) depth / (2 * size * size) : 0;
			m[i] = isNear(i);
		}
		// res += 1 - (double) (realDepth + depth) / Game.endTime;
		res += (1 - (double) allMoveDone / (Game.endTime * game.playerNumber));
		return new HMAPF_Value(-1, res / game.playerNumber, m);
	}

	@Override
	public String toString() {
		return "HMAPF_State [board=" + board + ", allMoveDone=" + allMoveDone + "]";
	}

	@Override
	public ArrayList<State> refreshChilds() {
		ArrayList<State> childss = new ArrayList<State>();
//		if (board.table[lastMove[nextColor].x][lastMove[nextColor].y] < 0)
//			childss.add(game.simulator.simulate(this,
//					new HMAPF_Action(lastMove[nextColor].x, lastMove[nextColor].y, nextColor, true)));
//		else
		if (isNear(nextColor))
			return childss;
		for (int i = 0; i < 4; ++i)
			if (lastMove[nextColor].x + ddx[colorRegion][i] >= 0
					&& lastMove[nextColor].x + ddx[colorRegion][i] < game.width
					&& lastMove[nextColor].y + ddy[colorRegion][i] >= 0
					&& lastMove[nextColor].y + ddy[colorRegion][i] < game.height
					&& (board.table[lastMove[nextColor].x + ddx[colorRegion][i]][lastMove[nextColor].y
							+ ddy[colorRegion][i]] == 0
							|| board.table[lastMove[nextColor].x + ddx[colorRegion][i]][lastMove[nextColor].y
									+ ddy[colorRegion][i]] == -nextColor))
				childss.add(game.simulator.simulate(this, new HMAPF_Action(lastMove[nextColor].x + ddx[colorRegion][i],
						lastMove[nextColor].y + ddy[colorRegion][i], nextColor)));
		return childss;
	}

	private boolean hasChild() {
		if (isNear(nextColor))
			return false;
		for (int i = 0; i < 4; ++i)
			if (lastMove[nextColor].x + ddx[colorRegion][i] >= 0
					&& lastMove[nextColor].x + ddx[colorRegion][i] < game.width
					&& lastMove[nextColor].y + ddy[colorRegion][i] >= 0
					&& lastMove[nextColor].y + ddy[colorRegion][i] < game.height
					&& (board.table[lastMove[nextColor].x + ddx[colorRegion][i]][lastMove[nextColor].y
							+ ddy[colorRegion][i]] == 0
							|| board.table[lastMove[nextColor].x + ddx[colorRegion][i]][lastMove[nextColor].y
									+ ddy[colorRegion][i]] == -nextColor))
				return true;
		return false;
	}

	private int childNumber() {
		int ans = 0;
		if (isNear(nextColor))
			return ans;
		for (int i = 0; i < 4; ++i)
			if (lastMove[nextColor].x + ddx[colorRegion][i] >= 0
					&& lastMove[nextColor].x + ddx[colorRegion][i] < game.width
					&& lastMove[nextColor].y + ddy[colorRegion][i] >= 0
					&& lastMove[nextColor].y + ddy[colorRegion][i] < game.height
					&& (board.table[lastMove[nextColor].x + ddx[colorRegion][i]][lastMove[nextColor].y
							+ ddy[colorRegion][i]] == 0
							|| board.table[lastMove[nextColor].x + ddx[colorRegion][i]][lastMove[nextColor].y
									+ ddy[colorRegion][i]] == -nextColor))
				++ans;
		return ans;
	}

//	private int childNumberTarget() {
//		PII temp = lastMove[nextColor];
//		lastMove[nextColor] = target[nextColor];
//		target[nextColor] = temp;
//		int res = childNumber();
//		temp = lastMove[nextColor];
//		lastMove[nextColor] = target[nextColor];
//		target[nextColor] = temp;
//		return res;
//	}

	public int dist() {
		int cn = childNumber();
		// int[] ds = { 0, 1, 1, 1, 1, 5 };
		int[] ds = { 0, 1, 2, 2, 2, 5 };
		return FastMath.rand256() % ds[cn];
	}

	public void rollDown() {
		// TODO fast random
		long stt = System.currentTimeMillis();
//		Random random = new Random();
//		int v = random.nextInt(childNumber());
		// int v = FastMath.rand256() % childNumber();
		int v = dist();
		// int v = (int) (stt % childNumber());
		int ans = 0;
		HMAPF_Action nextAct = null;
		FMAPF.shitTimer[13] += System.currentTimeMillis() - stt;
		stt = System.currentTimeMillis();

		if (isNear(nextColor))
			return;
		for (int i = 0; i < 4; ++i) {
			if (lastMove[nextColor].x + ddx[colorRegion][i] >= 0
					&& lastMove[nextColor].x + ddx[colorRegion][i] < game.width
					&& lastMove[nextColor].y + ddy[colorRegion][i] >= 0
					&& lastMove[nextColor].y + ddy[colorRegion][i] < game.height
					&& (board.table[lastMove[nextColor].x + ddx[colorRegion][i]][lastMove[nextColor].y
							+ ddy[colorRegion][i]] == 0
							|| board.table[lastMove[nextColor].x + ddx[colorRegion][i]][lastMove[nextColor].y
									+ ddy[colorRegion][i]] == -nextColor)) {
				++ans;
				if (ans == v + 1) {
					nextAct = new HMAPF_Action(lastMove[nextColor].x + ddx[colorRegion][i],
							lastMove[nextColor].y + ddy[colorRegion][i], nextColor);
					break;
				}
			}
		}
		FMAPF.shitTimer[11] += System.currentTimeMillis() - stt;
		stt = System.currentTimeMillis();
		if (nextAct != null
				&& (board.table[nextAct.x][nextAct.y] == 0 || board.table[nextAct.x][nextAct.y] == -nextColor))// ||
			// nextAct.stay))
			updateDown(nextAct);
		else
			System.out.println("WTF?!0");
		FMAPF.shitTimer[12] += System.currentTimeMillis() - stt;
		stt = System.currentTimeMillis();
		// state = state.getRandomChild();
	}

	private void updateDown(HMAPF_Action act) {
		if (board.table[act.x][act.y] == -nextColor || board.table[act.x][act.y] == 0) {
			board.table[lastMove[act.color].x][lastMove[act.color].y] = 0;
			board.updateBoard(act);
			lastMove[act.color] = pos[act.x][act.y];
		}
		// parent = st;
		// parent lol :D
		lastColor = nextColor;
		// localLastColor = localNextColor;

		setNextColor();
		if (nextColor <= lastColor)
			depth = depth + 1;

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

	@Override
	public int getDepth() {
		return realDepth;
	}

	public void setRegion() {
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

	}
}
