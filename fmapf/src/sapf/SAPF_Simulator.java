package sapf;

import java.util.ArrayList;

import main.*;
import prima.PRIMA_State;

public class SAPF_Simulator extends Simulator {

	static int[][] ddx = { { 1, 0, 0, -1 }, { 0, 1, -1, 0 }, { 0, -1, 1, 0 }, { -1, 0, 0, 1 }, { -1, 0, 0, 1 },
			{ 0, -1, 1, 0 }, { 0, 1, -1, 0 }, { 1, 0, 0, -1 }, { 1, 0, -1, 0 } };
	static int[][] ddy = { { 0, -1, 1, 0 }, { -1, 0, 0, 1 }, { -1, 0, 0, 1 }, { 0, -1, 1, 0 }, { 0, 1, -1, 0 },
			{ 1, 0, 0, -1 }, { 1, 0, 0, -1 }, { 0, 1, -1, 0 }, { 0, 1, 0, -1 } };
//	static int[] dx = { 1, 0, -1, 0 };
//	static int[] dy = { 0, 1, 0, -1 };
//
//	static int[] dx = { -1, 0, 0, 1 };
//	static int[] dy = { 0, -1, 1, 0 };

	@Override
	public State simulate(State state, Action action) {
		SAPF_Action act = (SAPF_Action) action;
		SAPF_State st = (SAPF_State) state;
		SAPF_State res = null;
		if (st.board.table[act.x][act.y] == 0 || st.board.table[act.x][act.y] == -1)
			res = new SAPF_State(st, act);
		return res;
	}

//	public static State simulateX(State state, Action action) {
//		SAPF_Action act = (SAPF_Action) action;
//		SAPF_State st = (SAPF_State) state;
//		SAPF_State res = null;
//		if (st.board.table[act.x][act.y] == 0 || st.board.table[act.x][act.y] == -1)
//			res = new SAPF_State(st, act);
//		return res;
//	}

//	public static ArrayList<Action> getActionsxt(State state) {
//		SAPF_State st = (SAPF_State) state;
//		ArrayList<Action> acs = new ArrayList<Action>();
//		for (int i = 0; i < 4; ++i)
//			if (st.lastMove[st.nextColor].x + ddx[st.colorRegion][i] >= 0
//					&& st.lastMove[st.nextColor].x + ddx[st.colorRegion][i] < st.game.width
//					&& st.lastMove[st.nextColor].y + ddy[st.colorRegion][i] >= 0
//					&& st.lastMove[st.nextColor].y + ddy[st.colorRegion][i] < st.game.height
//					&& (st.board.table[st.lastMove[st.nextColor].x + ddx[st.colorRegion][i]][st.lastMove[st.nextColor].y
//							+ ddy[st.colorRegion][i]] == 0
//							|| st.board.table[st.lastMove[st.nextColor].x
//									+ ddx[st.colorRegion][i]][st.lastMove[st.nextColor].y
//											+ ddy[st.colorRegion][i]] == -1))
//				acs.add(new SAPF_Action(st.lastMove[st.nextColor].x + ddx[st.colorRegion][i],
//						st.lastMove[st.nextColor].y + ddy[st.colorRegion][i], st.nextColor));
//		return acs;
//	}

//	public ArrayList<Action> getActionst(State state) {
//		SAPF_State st = (SAPF_State) state;
//		ArrayList<Action> acs = new ArrayList<Action>();
//		for (int i = 0; i < 4; ++i)
//			if (st.lastMove[st.nextColor].x + ddx[st.colorRegion][i] >= 0
//					&& st.lastMove[st.nextColor].x + ddx[st.colorRegion][i] < st.game.width
//					&& st.lastMove[st.nextColor].y + ddy[st.colorRegion][i] >= 0
//					&& st.lastMove[st.nextColor].y + ddy[st.colorRegion][i] < st.game.height
//					&& (st.board.table[st.lastMove[st.nextColor].x + ddx[st.colorRegion][i]][st.lastMove[st.nextColor].y
//							+ ddy[st.colorRegion][i]] == 0
//							|| st.board.table[st.lastMove[st.nextColor].x
//									+ ddx[st.colorRegion][i]][st.lastMove[st.nextColor].y
//											+ ddy[st.colorRegion][i]] == -1))
//				acs.add(new SAPF_Action(st.lastMove[st.nextColor].x + ddx[st.colorRegion][i],
//						st.lastMove[st.nextColor].y + ddy[st.colorRegion][i], st.nextColor));
//		return acs;
//	}

}
