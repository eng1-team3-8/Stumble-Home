package io.github.stumblehome.BossFight;

/**
 * Class controlling the states of the bossfight.
 * This is done by implementing an FSA, using and enum to hold all the states,
 * and using a switch statement to control to transitions.
 *
 * @author Lenny
 */
public class BossFightStatesManager {

    // The current state of the machine
    private BossFightStates currentState;

  /**
   * This constructor creates the machine and sets the state to OPTIONS
   */
  public BossFightStatesManager() {
        this.currentState = BossFightStates.OPTIONS;
    }

  /**
   * Getter for the current state
   * @return BossFightStates
   */
  public BossFightStates returnState() {
        return this.currentState;
    }

  /**
   * This is the method enclosing all the transitions between the states
   * @param move integer: How many places to move (0 is for states that were added later)
   */
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
                if (move == 1) {
                    this.currentState = BossFightStates.OPTIONS;
                } else {
                    this.currentState = BossFightStates.LOST;
                }
                break;
            case FINALATTACK:
                if (move == 1) {
                    this.currentState = BossFightStates.WIN;
                } else if (move == -1) {
                    this.currentState = BossFightStates.OPTIONS;
                } else {
                    this.currentState = BossFightStates.LOST;
                }
                break;
        }
    }
}
