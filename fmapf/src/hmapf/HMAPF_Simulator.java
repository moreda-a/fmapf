package hmapf;

import main.*;

public class HMAPF_Simulator extends Simulator {

	@Override
	public State simulate(State state, Action action) {
		HMAPF_Action act = (HMAPF_Action) action;
		HMAPF_State st = (HMAPF_State) state;
		HMAPF_State res = null;
		if (st.board.table[act.x][act.y] == 0 || st.board.table[act.x][act.y] == -act.color)
			res = new HMAPF_State(st, act);
		return res;
	}

}
