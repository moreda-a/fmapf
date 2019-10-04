package prima;

import main.Action;

public class PRIMA_Action extends Action {

	public PRIMA_Action(int x, int y, int c) {
		this.x = x;
		this.y = y;
		color = c;
		finish = false;
	}

	public PRIMA_Action(int x, int y, int c, boolean f) {
		this.x = x;
		this.y = y;
		color = c;
		finish = f;
	}

	public boolean finish;
	public int x;
	public int y;
	public int color;
}
