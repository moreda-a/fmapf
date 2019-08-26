package g2048;

import java.util.ArrayList;

import main.*;
import prima.PRIMA_Action;
import prima.PRIMA_Simulator;

public class G2048_State extends State {

	static PII pos[][];

	public G2048_Game game;
	public G2048_Board board;

	public boolean notChangable;
	private Boolean isNotTerminal = null;
	public int realDepth = 0;
	public int score = 0;

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
		// lastColor = -1;
	}

	public G2048_State() {
		System.out.println("make bad state");
	}

	// just for first time
	public G2048_State(G2048_Game game, G2048_Board board) {
		this.board = board;
		this.game = game;
		pos = new PII[game.width][game.height];
		for (int i = 0; i < game.width; ++i)
			for (int j = 0; j < game.height; ++j)
				pos[i][j] = new PII(i, j);

	}

	// nextState
	public G2048_State(G2048_State st, G2048_Action act) {
		game = st.game;
		board = new G2048_Board(game);
		board.setBoard(st);
		board.updateBoard(act);
		board.addOnBoard();
		parent = st;
		realDepth = st.realDepth + 1;
		score = st.score + board.updateScore();
	}

	/*
	 * Deep copy constructor
	 */
	public G2048_State(G2048_State st) {
		game = st.game;
		board = new G2048_Board(game);
		board.setBoard(st);
		parent = st;
		depth = st.depth;
		realDepth = st.realDepth;
		isNotTerminal = st.isNotTerminal;
		notChangable = st.notChangable;
		score = st.score;
	}

	@Override
	public boolean isNotTerminal() {
		if (notChangable && isNotTerminal != null)
			return isNotTerminal;
		if (!hasChild())
			return isNotTerminal = false;
		return isNotTerminal = true;
	}

	@Override
	public Value getValue() {
		if (isNotTerminal())
			return null;
		return new G2048_Value(-1, score);
	}

	@Override
	public String toString() {
		return "G2048_State [board=" + board + ", score=" + score + "]";
	}

	@Override
	public ArrayList<State> refreshChilds() {
		ArrayList<State> childss = new ArrayList<State>();
//		if (board.table[lastMove[nextColor].x][lastMove[nextColor].y] < 0)
//			childss.add(game.simulator.simulate(this,
//					new HMAPF_Action(lastMove[nextColor].x, lastMove[nextColor].y, nextColor, true)));
//		else
		for (int i = 0; i < 4; ++i)
			if (board.canMove(i))
				childss.add(game.simulator.simulate(this, new G2048_Action(i)));
		return childss;
	}

	private boolean hasChild() {
		for (int i = 0; i < 4; ++i)
			if (board.canMove(i))
				return true;
		return false;
	}

	private int childNumber() {
		int ans = 0;
		for (int i = 0; i < 4; ++i)
			if (board.canMove(i))
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
		int[] ds = { 0, 1, 2, 3, 4, 5 };
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
		G2048_Action nextAct = null;
		FMAPF.shitTimer[13] += System.currentTimeMillis() - stt;
		stt = System.currentTimeMillis();

		for (int i = 0; i < 4; ++i) {
			if (board.canMove(i)) {
				++ans;
				if (ans == v + 1) {
					nextAct = new G2048_Action(i);
					break;
				}
			}
		}
		FMAPF.shitTimer[11] += System.currentTimeMillis() - stt;
		stt = System.currentTimeMillis();
//		if (nextAct != null
//				&& (board.table[nextAct.x][nextAct.y] == 0 || board.table[nextAct.x][nextAct.y] == -nextColor))// ||
		// nextAct.stay))
		updateDown(nextAct);
		// else
		// System.out.println("WTF?!0");
		FMAPF.shitTimer[12] += System.currentTimeMillis() - stt;
		stt = System.currentTimeMillis();
		// state = state.getRandomChild();
	}

	private void updateDown(G2048_Action act) {
//		if (board.table[act.x][act.y] == -nextColor || board.table[act.x][act.y] == 0) {
//			board.table[lastMove[act.color].x][lastMove[act.color].y] = 0;
//			board.updateBoard(act);
//			lastMove[act.color] = pos[act.x][act.y];
//		}

		// parent = st;
		// parent lol :D
//		lastColor = nextColor;
//		// localLastColor = localNextColor;
//
//		setNextColor();
//		if (nextColor <= lastColor)
//			depth = depth + 1;

		board.updateBoard(act);
		board.addOnBoard();
		// parent = st;
		realDepth = realDepth + 1;
		score = score + board.updateScore();
	}

	@Override
	public int getDepth() {
		return realDepth;
	}

	@Override
	protected void setLocalAgents(Game game) {
		// TODO Auto-generated method stub

	}

}
