package cogplus;

import java.util.ArrayList;

import main.*;

public class COGP_Simulator extends Simulator {

	static int[][] ddx = { { 1, 0, 0, -1 }, { 0, 1, -1, 0 }, { 0, -1, 1, 0 }, { -1, 0, 0, 1 }, { -1, 0, 0, 1 },
			{ 0, -1, 1, 0 }, { 0, 1, -1, 0 }, { 1, 0, 0, -1 } ,{ 1, 0, -1, 0 }};
	static int[][] ddy = { { 0, -1, 1, 0 }, { -1, 0, 0, 1 }, { -1, 0, 0, 1 }, { 0, -1, 1, 0 }, { 0, 1, -1, 0 },
			{ 1, 0, 0, -1 }, { 1, 0, 0, -1 }, { 0, 1, -1, 0 } ,{ 0, 1, 0, -1 }};
//	static int[] dx = { 1, 0, -1, 0 };
//	static int[] dy = { 0, 1, 0, -1 };
//
//	static int[] dx = { -1, 0, 0, 1 };
//	static int[] dy = { 0, -1, 1, 0 };

	@Override
	public State simulate(State state, Action action) {
		COGP_Action act = (COGP_Action) action;
		COGP_State st = (COGP_State) state;
		COGP_State res = null;
		if (st.board.table[act.x][act.y] == 0)
			res = new COGP_State(st, act);
		return res;
	}

	public static State simulateX(State state, Action action) {
		COGP_Action act = (COGP_Action) action;
		COGP_State st = (COGP_State) state;
		COGP_State res = null;
		if (st.board.table[act.x][act.y] == 0)
			res = new COGP_State(st, act);
		return res;
	}

	public static ArrayList<Action> getActionsxt(State state) {
		COGP_State st = (COGP_State) state;
		ArrayList<Action> acs = new ArrayList<Action>();
		for (int i = 0; i < 4; ++i)
			if (st.lastMove[st.nextColor].x + ddx[st.colorRegion][i] >= 0
					&& st.lastMove[st.nextColor].x + ddx[st.colorRegion][i] < st.game.width
					&& st.lastMove[st.nextColor].y + ddy[st.colorRegion][i] >= 0
					&& st.lastMove[st.nextColor].y + ddy[st.colorRegion][i] < st.game.height
					&& st.board.table[st.lastMove[st.nextColor].x + ddx[st.colorRegion][i]][st.lastMove[st.nextColor].y
							+ ddy[st.colorRegion][i]] == 0)
				acs.add(new COGP_Action(st.lastMove[st.nextColor].x + ddx[st.colorRegion][i],
						st.lastMove[st.nextColor].y + ddy[st.colorRegion][i], st.nextColor));
		return acs;
	}

	public ArrayList<Action> getActionst(State state) {
		COGP_State st = (COGP_State) state;
		ArrayList<Action> acs = new ArrayList<Action>();
		for (int i = 0; i < 4; ++i)
			if (st.lastMove[st.nextColor].x + ddx[st.colorRegion][i] >= 0
					&& st.lastMove[st.nextColor].x + ddx[st.colorRegion][i] < st.game.width
					&& st.lastMove[st.nextColor].y + ddy[st.colorRegion][i] >= 0
					&& st.lastMove[st.nextColor].y + ddy[st.colorRegion][i] < st.game.height
					&& st.board.table[st.lastMove[st.nextColor].x + ddx[st.colorRegion][i]][st.lastMove[st.nextColor].y
							+ ddy[st.colorRegion][i]] == 0)
				acs.add(new COGP_Action(st.lastMove[st.nextColor].x + ddx[st.colorRegion][i],
						st.lastMove[st.nextColor].y + ddy[st.colorRegion][i], st.nextColor));
		return acs;
	}

}
