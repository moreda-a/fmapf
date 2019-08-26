package g2048;

import main.*;

public class G2048_Value extends Value {

	public G2048_Value(int num, double value) {
		this.num = num;
		this.value = value;
		bestValue = value;
	}

	public static G2048_Value win() {
		return new G2048_Value(-1, 1);
	}

	public static G2048_Value lose() {
		return new G2048_Value(-1, -1);
	}

	public static G2048_Value draw() {
		return new G2048_Value(-1, 0);
	}

	@Override
	public G2048_Value update(State state, Value simulationResult) {
		G2048_State st = (G2048_State) state;
		G2048_Value simulation_result = (G2048_Value) simulationResult;
		++num;
		bestValue = Math.max(value, simulation_result.value);
		switch (modelNumber) {
		case 1:
		case 2:
		case 3:
			value = (value * (num - 1) + simulation_result.value) / num;
			break;
		case 4:
		case 5:
		case 6:
			value = bestValue;
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
