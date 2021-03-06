package com.md.mechevo.game.ai;


import java.util.ArrayList;

import com.md.mechevo.game.Player;
import com.md.mechevo.game.State;
import com.md.mechevo.game.action.Action;
import com.md.mechevo.game.condition.Condition;


/**
 * TODO: There should also be a findPreferredPosition in case there is no target Player.
 */
public class AIEntry {

	private AIAlgorithm algorithm;
	private ArrayList<Condition> conditions;
	private ArrayList<Action> actions;


	/**
	 * Constructor.
	 * 
	 * @param aiAlgo AI Algorithm that this entry is inserted.
	 */
	public AIEntry(AIAlgorithm aiAlgo) {
		this.algorithm = aiAlgo;
		this.conditions = new ArrayList<Condition>();
		this.actions = new ArrayList<Action>();
	}

	public AIAlgorithm getAlgorithm() {
		return algorithm;
	}

	public ArrayList<Condition> getConditions() {
		return conditions;
	}

	public ArrayList<Action> getActions() {
		return actions;
	}

	public void addCondition(Condition condition) {
		conditions.add(condition);
	}

	public void addAction(Action action) {
		if (actions.size() > 0) {
			actions.get(actions.size() - 1).setNext(action);
		}
		actions.add(action);
	}

	/**
	 * Check if the current state fulfills all the conditions.
	 * 
	 * @param state Current state of the game
	 * @return True if it fulfills all the conditions
	 */
	public boolean checkRequirements(State state) {
		boolean pass = true;
		for (Condition cond : this.conditions) {
			pass = pass && cond.check(state);
			if (!pass) {
				return false;
			}
		}
		return true;
	}

	/**
	 *
	 * @param state the current state of the game
	 * @return true if at least one of the actions can be performed, false otherwise.
	 */
	public boolean canPerformAnyAction(State state) {
		for (Action action : this.actions) {
			if (action.check(state)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Since Conditions may change the target, ask them which one should be targetted. This function
	 * expects that checkRequirements to return true
	 * 
	 * @param state Current state of the game
	 * @return Player reference if there is one, else its null
	 * TODO: Optimize the preferredTarget retrieval
	 */
	public Player findPreferredTarget(State state) {
		Player preferred = null;
		for (Condition cond : this.conditions) {
			if (cond.getPreferredPlayer(state) != null) {
				preferred = cond.getPreferredPlayer(state);
			}
		}
		return preferred;
	}

	/**
	 * Retrieve the first action of the entry.
	 * 
	 * @return First action in the delay
	 */
	public Action getFirstAction() {
		return (this.actions.size() > 0) ? this.actions.get(0) : null;
	}
}
