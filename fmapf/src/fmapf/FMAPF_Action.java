package fmapf;

import main.Action;

public class FMAPF_Action extends Action {

	public FMAPF_Action(int x, int y, int c) {
		this.x = x;
		this.y = y;
		color = c;
		finish = false;
	}

	public FMAPF_Action(int x, int y, int c, boolean f) {
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
