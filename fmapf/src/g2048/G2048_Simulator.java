package g2048;

import main.*;

public class G2048_Simulator extends Simulator {

	@Override
	public State simulate(State state, Action action) {
		G2048_Action act = (G2048_Action) action;
		G2048_State st = (G2048_State) state;
		G2048_State res = null;
		// do we really need this ?
		// if (st.board.table[act.x][act.y] == 0 || st.board.table[act.x][act.y] ==
		// -act.color)
		res = new G2048_State(st, act);
		return res;
	}

}
