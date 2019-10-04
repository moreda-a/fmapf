package prima;

import main.*;

public class PRIMA_Simulator extends Simulator {

	@Override
	public State simulate(State state, Action action) {
		PRIMA_Action act = (PRIMA_Action) action;
		PRIMA_State st = (PRIMA_State) state;
		PRIMA_State res = null;
		if (st.board.table[act.x][act.y] == 0 || st.board.table[act.x][act.y] == -act.color ||st.board.table[act.x][act.y] == act.color)
			res = new PRIMA_State(st, act);
		return res;
	}

}
