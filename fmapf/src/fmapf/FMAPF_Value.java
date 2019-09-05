package fmapf;

import main.*;

public class FMAPF_Value extends Value {

	public FMAPF_Value(int num, double value) {
		this.num = num;
		this.value = value;
		bestValue = value;
	}

	public FMAPF_Value(int i, double d, int[] m) {
		this.num = i;
		this.value = d;
		bestValue = value;
		this.mark = m;
	}

	public static FMAPF_Value win() {
		return new FMAPF_Value(-1, 1);
	}

	public static FMAPF_Value lose() {
		return new FMAPF_Value(-1, -1);
	}

	public static FMAPF_Value draw() {
		return new FMAPF_Value(-1, 0);
	}

//	public int compareTo_UCT(COGP_Value vv, int total_number) {
//		double u1 = value + Math.sqrt(1.8 * Math.log(total_number) / num);
//		double u2 = vv.value + Math.sqrt(1.8 * Math.log(total_number) / vv.num);
//		if (u1 < u2)
//			return -1;
//		else
//			return 1;
//	}

	@Override
	public FMAPF_Value update(State state, Value simulationResult) {
		FMAPF_State st = (FMAPF_State) state;
		FMAPF_Value simulation_result = (FMAPF_Value) simulationResult;
		++num;
//		bestValue = Math.max(value,
//				simulation_result.value
//						- (st.lastColor != -1
//								? simulation_result.mark[st.lastColor]
//										? (double) (1.5 - (double) (((modelNumber - 1) % 3) + 1) / 2)
//												* (1 / st.game.playerNumber)
//										: 0
//								: 0));
		switch (modelNumber) {
		case 1:
		case 2:
		case 3:
//			value = (value * (num - 1) + simulation_result.value
//					- (st.lastColor != -1
//							? simulation_result.mark[st.lastColor]
//									? (double) (1.5 - (double) (modelNumber / 2)) * (1 / st.game.playerNumber)
//									: 0
//							: 0))
//					/ num;
//			break;
		case 4:
		case 5:
		case 6:
			value = bestValue;
			break;
		case 7:
//			value = (value * (num - 1) + simulation_result.value
//					+ (st.lastColor != -1
//							? simulation_result.mark[st.lastColor]
//									? (double) (2 / st.game.playerNumber)
//									: 0
//							: 0))
//					/ num;
			value = (value * (num - 1)
					+ (st.lastColor != -1
							? (simulation_result.mark[st.lastColor] != -1
									? 1d - ((double) simulation_result.mark[st.lastColor] / Game.endTime)
									: 0)
							: 0))/num;
			break;
		default:
			break;
		}
		return this;
	}

	@Override
	public String toString() {
		return "COGP_Value [num=" + num + ", value=" + value + ", bestValue=" + bestValue + "]";
	}

	@Override
	public int compareTo_UCT(Value vv, int total_number) {
		double u1 = value + Math.sqrt(0.5 * Math.log(total_number) / num);
		double u2 = vv.value + Math.sqrt(0.5 * Math.log(total_number) / vv.num);
		if (u1 < u2)
			return -1;
		else
			return 1;
	}
}
