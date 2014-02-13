package com.md.mechevo.game;

import com.google.gson.JsonObject;

public class Simulator {
	/**
	 * Time between rounds is in seconds
	 */
	public static final float TIME_BETWEEN_ROUNDS = 0.1f;



	private Simulator() {}

	private static boolean gameHasFinished() {
		// TODO
		return false;
	}



	/**
	 * Run the game until end.
	 * 
	 * @param state The initial state to begin with
	 */
	public static Report runGame(State state) {
		int i = 0;

		// game loop
		while (!gameHasFinished() && (i++) < 5) {
			state.update(TIME_BETWEEN_ROUNDS);
		}

		return Simulator.buildFullReport(state);
	}


	/**
	 * Build the report. Get the events from the state and add some additional info to it.
	 * 
	 * @param state State of the game (usually the final state)
	 * @report Json Object that defines the full report
	 */
	private static Report buildFullReport(State state) {
		JsonObject head = new JsonObject();
		head.addProperty("totalTime", state.getTotalTime());
		head.add("events", state.buildEventReport());
		return new Report(head);
	}



}
