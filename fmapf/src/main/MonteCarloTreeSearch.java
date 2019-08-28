package main;

import java.util.*;
import fmapf.*;

public class MonteCarloTreeSearch extends Solver {

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
		FMAPF_State st = (FMAPF_State) root;
		FMAPF_Game ga = (FMAPF_Game) game;
		State[] gg = new State[ga.playerNumber + 1];
		State[] ng = new State[ga.playerNumber + 1];
		for (int i = 1; i <= ga.playerNumber; ++i) {// O(pI^2 + pIT + pIn^2)
			Timer.resume(-i);
			((FMAPF_State) ga.agentState[i]).nextColor = i;
			((FMAPF_State) ga.agentState[i]).setRegion();
			gg[i] = getBestNextStateSingle(ga.agentState[i]);// O(I^2 + IT + In^2)
			Timer.pause(-i);
		}
		for (int i = 1; i <= ga.playerNumber; ++i) {// O(p*n^2)
			ng[i] = new FMAPF_State(ga.agentState, gg, i);
		}
		ga.agentState = ng;
		return ng[1];
	}

	public State getBestNextStateSingle(State root) {
		root.reset(game);
		int time = 1000;
		while (time-- > 0) {
			if (Main.isGarbageCollectorOn)
				System.gc();
			Timer.resume(2);
			State leaf = selection(root);
			Timer.pause(2);
			Timer.resume(3);
			State expandedLeaf = expansion(leaf);
			Timer.pause(3);
			Timer.resume(4);
			Value simulationResult = rollout(expandedLeaf);
			Timer.pause(4);
			Timer.resume(5);
			backpropagation(simulationResult, expandedLeaf);
			Timer.pause(5);
		}
		if (root.isNotTerminal())
			return bestChild(root);
		else
			return root;
	}

	private State selection(State state) {// Done
		State st = state;
		Timer.resume(6);
		while (st.isInTree && st.isNotTerminal()) {
			Timer.pause(6);
			Timer.resume(7);
			st = best_uct(st);
			Timer.pause(7);
			Timer.resume(6);
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
		Timer.resume(8);
		Value vx = state.value;
		ArrayList<State> childs = state.getChilds();

		State ans = null;
		Value vbest = null;
		Timer.pause(8);
		Timer.resume(9);
		for (State st : childs) {
			if (!st.isInTree)
				return st;
			Value vv = st.value;
			if (vbest == null || vbest.compareTo_UCT(vv, vx.num) < 0) {
				vbest = vv;
				ans = st;
			}
		}
		Timer.pause(9);
		return ans;
	}

	private Value rollout(State state) {
		return fastRollout(state);
	}

	private Value fastRollout(State state) {
		// oop??
		FastMath.initRand(new Random().nextInt(), new Random().nextInt());
		Timer.resume(8);
		State st = new FMAPF_State((FMAPF_State) state);
		((FMAPF_State) st).notChangable = false;
		Timer.pause(8);
		Timer.resume(9);
		while (st.isNotTerminal()) {
			Timer.pause(9);
			Timer.resume(10);
			st.rollDown();
			Timer.pause(10);
			Timer.resume(9);
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
			if (Main.debugMode[1] == '1')
				System.out.println("CH:\n" + ch + vv);
			if (vbest == null || vbest.compareTo(vv) < 0) {
				vbest = vv;
				ans = ch;
			}
		}
		return ans;
	}

}
