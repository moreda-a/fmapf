package main;

import java.util.HashMap;

public class Timer {
	private static HashMap<Integer, Long> timers = new HashMap<Integer, Long>();
	private static HashMap<Integer, Long> resumeTimers = new HashMap<Integer, Long>();

	public static void startTimer(int id) {
		timers.remove(id);
		timers.put(id, (long) 0);
		resumeTimers.remove(id);
		resume(id);
	}

	public static void resume(int id) {
		if (!resumeTimers.containsKey(id))
			resumeTimers.put(id, System.currentTimeMillis());
	}

	public static void pause(int id) {
		if (!resumeTimers.containsKey(id)) {
			timers.compute(id, (k, v) -> k == null ? resumeTimers.get(id) - System.currentTimeMillis()
					: v + resumeTimers.get(id) - System.currentTimeMillis());
			resumeTimers.remove(id);
		}
	}

	public static Long checkTimer(int id) {
		pause(id);
		return getTimer(id);
	}

	public static Long getTimer(int id) {
		return timers.get(id);
	}

	public static void init() {
		// TODO Auto-generated method stub

	}

}
