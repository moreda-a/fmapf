package main;

import java.util.*;

import g2048.G2048_State;
import hmapf.HMAPF_Game;
import hmapf.HMAPF_State;

public class MonteCarloTreeSearch extends TreeSolver {

	public MonteCarloTreeSearch(Game game, Simulator simulator) {
		super(game, simulator);
	}

	@Override
	public State getBestNextState(State root) {
		if (game.centralized)
			return getBestNextStateSingle(root);
		else
			return getBestNextStateMulti(root);

	}

	public State getBestNextStateMulti(State root) {// O(pI^2 + pIT + pIn^2)
		// TODO State vs Action
		HMAPF_State st = (HMAPF_State) root;
		HMAPF_Game ga = (HMAPF_Game) game;
		State[] gg = new State[ga.playerNumber + 1];
		State[] ng = new State[ga.playerNumber + 1];
		for (int i = 1; i <= ga.playerNumber; ++i) {// O(pI^2 + pIT + pIn^2)
			long stt = System.currentTimeMillis();
			((HMAPF_State) ga.agentState[i]).nextColor = i;
			((HMAPF_State) ga.agentState[i]).setRegion();
			gg[i] = getBestNextStateSingle(ga.agentState[i]);// O(I^2 + IT + In^2)
			FMAPF.timer[i] += System.currentTimeMillis() - stt;
		}
		for (int i = 1; i <= ga.playerNumber; ++i) {// O(p*n^2)
			ng[i] = new HMAPF_State(ga.agentState, gg, i);
		}
		ga.agentState = ng;
		return ng[1];
	}

	public State getBestNextStateSingle(State root) {// O(I^2 + IT + In^2)
		root.reset(game);
		int time = 1000;
		while (time-- > 0) {
			if (FMAPF.garbageCollectorMode)
				System.gc();
			long stt = System.currentTimeMillis();
			State leaf = selection(root);// O(I)
			FMAPF.shitTimer[0] += System.currentTimeMillis() - stt;
			stt = System.currentTimeMillis();
			State expandedLeaf = expansion(leaf);// O(n^2)
			FMAPF.shitTimer[1] += System.currentTimeMillis() - stt;
			stt = System.currentTimeMillis();
			Value simulationResult = rollout(expandedLeaf);// O(T+n^2)
			FMAPF.shitTimer[2] += System.currentTimeMillis() - stt;
			stt = System.currentTimeMillis();
			backpropagation(simulationResult, expandedLeaf);// O(I)
			FMAPF.shitTimer[3] += System.currentTimeMillis() - stt;
		}
		if (root.isNotTerminal())
			return bestChild(root);
		else
			return root;
	}

	private State selection(State state) {// Done
		State st = state;
		long stt = System.currentTimeMillis();
		while (st.isInTree && st.isNotTerminal()) {
			FMAPF.shitTimer[4] += System.currentTimeMillis() - stt;
			stt = System.currentTimeMillis();
			st = best_uct(st);
			FMAPF.shitTimer[5] += System.currentTimeMillis() - stt;
			stt = System.currentTimeMillis();
		}
		return st;
	}

	private State expansion(State state) {
		// expand just random child here ?
		// expand all child
		// expand some child
		State st = state;
		if (!st.isInTree) {
			st.isInTree = true;
			st.value = game.CreateZeroValue();
		} else if (state.isNotTerminal()) {
			System.out.println("---WTF---");
			// state = simulator.randomChild(state);
			// states.put(state, new Value(0, 0));
		}
		return st;
	}

	private State best_uct(State state) { // DONE
		// Value vx = states.get(state);
		long stt = System.currentTimeMillis();
		Value vx = state.value;
		ArrayList<State> childs = state.getChilds();

		State ans = null;
		Value vbest = null;
		FMAPF.shitTimer[6] += System.currentTimeMillis() - stt;
		stt = System.currentTimeMillis();
		for (State st : childs) {
			if (!st.isInTree)
				return st;
			Value vv = st.value;
			if (vbest == null || vbest.compareTo_UCT(vv, vx.num) < 0) {
				vbest = vv;
				ans = st;
			}
		}
		FMAPF.shitTimer[7] += System.currentTimeMillis() - stt;
		return ans;
	}

	private Value rollout(State state) {
		if (FMAPF.fastRollout)
			return fastRollout(state);
		while (state.isNotTerminal())
			state = state.getRandomChild();
		return state.getValue();
	}

	private Value fastRollout(State state) {
		// oop??
		FastMath.initRand(new Random().nextInt(), new Random().nextInt());
		long stt = System.currentTimeMillis();
		State st = new G2048_State((G2048_State) state);
		((G2048_State) st).notChangable = false;
		FMAPF.shitTimer[8] += System.currentTimeMillis() - stt;
		stt = System.currentTimeMillis();
		while (st.isNotTerminal()) {
			FMAPF.shitTimer[9] += System.currentTimeMillis() - stt;
			stt = System.currentTimeMillis();
			st.rollDown();
			FMAPF.shitTimer[10] += System.currentTimeMillis() - stt;
			stt = System.currentTimeMillis();
		}
		return st.getValue();
	}

	private void backpropagation(Value simulation_result, State state) {
		while (state != null) {
			if (state.isInTree)
				state.value = state.value.update(state, simulation_result);
			state = state.parent;
		}
	}

	private State bestChild(State state) {
		ArrayList<State> childs = state.getChilds();
		State ans = null;
		Value vbest = null;
		for (State ch : childs) {
			Value vv = ch.value;
			if (FMAPF.debugMode)
				System.out.println("CH:\n" + ch + vv);
			if (vbest == null || vbest.compareTo(vv) < 0) {
				vbest = vv;
				ans = ch;
			}
		}
		return ans;
	}

}
