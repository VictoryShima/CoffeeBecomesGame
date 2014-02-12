package com.md.mechevo.game.action;

import com.md.mechevo.game.Player;
import com.md.mechevo.game.State;


/**
 * Attack Action will shot the selected weapon, first by checking if it's cooldown is up.
 */
public class AttackAction extends Action {


	/**
	 * Duration of the action
	 * 
	 * TODO: Actual duration will depend on extra attribute
	 */
	private static final int DURATION = 1000;


	/**
	 * Whether or not the action is cancelable
	 */
	private static final boolean CANCELABLE = true;

	/**
	 * Class Constructor
	 */
	public AttackAction(Player owner) {
		super(owner, AttackAction.DURATION, AttackAction.CANCELABLE);
	}

	public AttackAction(Player owner, String param) {
		super(owner, param, AttackAction.DURATION, AttackAction.CANCELABLE);
	}

	public boolean check(State state) {
		return true; // always valid
	}


	public void begin(State state) {
		this.getOwner().setMovementState(Player.MovementState.MOVING);
		this.getOwner().setSpeed(Player.SPEED);
	}


	public void update(State state, double dtime) {
		// TODO: do the actual moving
	}


	public void end(State state) {}



}
