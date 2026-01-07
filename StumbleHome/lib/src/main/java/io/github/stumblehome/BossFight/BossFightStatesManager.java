package io.github.stumblehome.BossFight;

/**
 * Class controlling the states of the bossfight.
 * This is done by implementing an FSA, using and enum to hold all the states,
 * and using a switch statement to control to transitions.
 *
 * @author Lenny
 */
public class BossFightStatesManager {

    private BossFightStates currentState;

    public BossFightStatesManager() {
        this.currentState = BossFightStates.OPTIONS;
    }

    public BossFightStates returnState() {
        return this.currentState;
    }

    public void moveStates(int move) {
        // Validity checking, states can only move one state at a time - the automata can only move
        // from its state to the next
        // or to the previous state
        if (move > 1 || move < -1) {
            return;
        }

        // Switch statement for all the transitions of the FSA
        switch (this.currentState) {
            case INFO:
                this.currentState = BossFightStates.OPTIONS;
                break;
            case OPTIONS:
                if (move == 1) {
                    this.currentState = BossFightStates.ATTACK;
                } else if (move == -1) {
                    this.currentState = BossFightStates.INFO;
                } else {
                    this.currentState = BossFightStates.FINALATTACK;
                }
                break;
            case ATTACK:
                this.currentState = BossFightStates.OPTIONS;
                break;
        }
    }
}
