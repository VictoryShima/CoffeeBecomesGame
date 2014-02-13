package com.md.mechevo.game.action;

import com.md.mechevo.game.Player;
import com.md.mechevo.game.State;

/**
 * Performs a fast run to either Left or Right and stops for a while.
 */
public class Dash extends Action {
    private static final double DURATION = 0.2;
    private static final boolean CANCELABLE = false;

    /**
     * @param param the direction of the dash (LEFT or RIGHT)
     */
    public Dash(Player owner, String param) {
        super(owner, param, Dash.CANCELABLE);
    }

    /**
     * Check if the required condition for an action applies.
     *
     * @param state Current State of the game
     * @return True if the condition applies
     */
    @Override
    public boolean check(State state) {
        // TODO don't forget Player#increaseHeat
        return true;
    }

    /**
     * Begin the execution of the action. Will be called once at the start of the action.
     *
     * @param state Current state of the game
     */
    @Override
    public void begin(State state) {

    }

    /**
     * Execute the action.
     *
     * @param state Current State of the game
     * @param dtime Duration of the round
     */
    @Override
    public void update(State state, double dtime) {

    }

    /**
     * End the execution of the action, whether it is cancelled or successfully ended.
     *
     * @param state Current state of the game
     */
    @Override
    public void end(State state) {

    }
}
