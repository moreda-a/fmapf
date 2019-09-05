package fmapf;

import main.*;

public class FMAPF_Simulator extends Simulator {

	@Override
	public State simulate(State state, Action action) {
		FMAPF_Action act = (FMAPF_Action) action;
		FMAPF_State st = (FMAPF_State) state;
		FMAPF_State res = null;
		if (st.board.table[act.x][act.y] == 0 || st.board.table[act.x][act.y] == -act.color ||st.board.table[act.x][act.y] == act.color)
			res = new FMAPF_State(st, act);
		return res;
	}

}
