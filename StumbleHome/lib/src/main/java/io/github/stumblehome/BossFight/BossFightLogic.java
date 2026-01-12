package io.github.stumblehome.BossFight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.stumblehome.Entities.BossFightEntity;

/**
 * This class contains methods and attributes corresponding to the logic
 * needing to be executed for the boss fight.
 */
public class BossFightLogic {

    // Boolean to see if the scissors have been stopped
    private boolean isStopped = false;

    // Boolean array holding the status of the wires
    // True if active, False if cut
    private boolean[] cableStatus = {true, true, true, true};

    // Texture of the cut cable
    private final Texture cutCable;

    // Boolean holding if it is the final stage
    private boolean finalStage = false;

    // Health variables for the player and Mike
    private int playerHealth = 100;
    private int mikeHealth = 100;

    // Boolean holding if the player has won or not
    private boolean playerWon = false;

    // The FSA of the boss fight
    private BossFightStatesManager statesFSA;

    // The viewport of the bossfight
    private FitViewport viewport;

    /**
     * Constructor for the BossFightLogic object
     *
     * @param cutCable Texture: The texture of the cut cable to update the cable object to, once it has been cut
     * @param statesFSA BossFightStatesManager: The FSA of the boss fight, allowing the logic handler to update the state
     * @param viewport FitViewport: The viewport of the bossfight, allowing for the logic events to be scaled
     */
    public BossFightLogic(
            Texture cutCable, BossFightStatesManager statesFSA, FitViewport viewport) {
        this.cutCable = cutCable;
        this.statesFSA = statesFSA;
        this.viewport = viewport;
    }

    /**
     * This method checks if the scissors are stopped, and when they are checks if the
     * scissors and the cable overlaps, makes mike attack and changes the state.
     * ]
     * @param cables BossFightEntity[]: The array of cables
     * @param scissors Scissors: The scissors
     * @throws InterruptedException
     */
    private void checkIfStopped(BossFightEntity[] cables, Scissors scissors)
            throws InterruptedException {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !isStopped) {
            isStopped = true;
            this.checkOverlap(cables, scissors);
            this.isStopped = false;
            this.mikeAttacks();
            this.statesFSA.moveStates(1);
            scissors.setX(105f);
        }

        // Below is code for testing
        //                else if  (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && isStopped) {
        //                  isStopped = false;
        //                }
    }

    /**
     * This is the method that checks if the space has been pressed, and if not keeps the scissors moving
     *
     * @param viewport FitViewport: The viewport to ensure that everything scales
     * @param batch SpriteBatch: The sprite batch, allowing the scissors to be drawn
     * @param scissors Scissors: The scissors
     * @param cables BossFightEntity[]: The array of cables
     * @throws InterruptedException
     */
    public void moveScissors(
            Viewport viewport, SpriteBatch batch, Scissors scissors, BossFightEntity[] cables)
            throws InterruptedException {
        this.checkIfStopped(cables, scissors);
        if (!isStopped) {
            scissors.move(viewport);
        }

        scissors.draw(batch);
    }

    /**
     * This method draws all the cables in the cables array
     *
     * @param cables BossFightEntity[]: The array of cables
     * @param batch SpriteBatch: The sprite batch allowing the cables to be drawn
     */
    public void drawCables(BossFightEntity[] cables, SpriteBatch batch) {
        for (int i = 0; i < cables.length; i++) {
            cables[i].draw(batch);
        }
    }

    /**
     * This method checks if there is an overlap between any of the cables and the scissors
     *
     *
     * @param cables BossFightEntity[]: The array of cables
     * @param scissors Scissors: The scissors
     * @throws InterruptedException
     */
    public void checkOverlap(BossFightEntity[] cables, Scissors scissors)
            throws InterruptedException {
        for (int i = 0; i < cables.length; i++) {
            if (scissors.checkOverlap(cables[i].getX(), this.viewport)) {
                if (this.cableStatus[i]) {
                    updateSprite(cables[i], i);
                }
            }
        }
    }

    // Updates the sprite if the cable is cut
    private void updateSprite(BossFightEntity cable, int cableNo) throws InterruptedException {
        this.cableStatus[cableNo] = false;
        cable.setTexture(this.cutCable);
        Thread.sleep(500);
        this.checkIfAllWiresCut();
    }

    // Checks if all the wires are cut, by looping through the status array
    private void checkIfAllWiresCut() {
        for (int i = 0; i < cableStatus.length; i++) {
            if (cableStatus[i]) {
                return;
            }
        }
        this.finalStage = true;
    }

    /**
     * A getter to check if the bossfight is in its final stage
     * @return boolean
     */
    public boolean isFinalStage() {
        return this.finalStage;
    }

    /**
     * This method attacks Mike
     *
     * The attacks are randomly generated and multiplied.
     * Additionally, if Mike's health is at 0, the player wins
     */
    public void attackMike() {
        double damage = Math.random();
        damage = damage * 40;

        this.mikeHealth -= (int) damage;

        if (this.mikeHealth <= 0) {
            this.mikeHealth = 0;
            this.playerWon = true;
            this.statesFSA.moveStates(1);
        }
    }

    /**
     * Getter for Mike's health
     * @return integer
     */
    public int getMikeHealth() {
        return this.mikeHealth;
    }

    /**
     * Getter for player health
     * @return integer
     */
    public int getPlayerHealth() {
        return this.playerHealth;
    }

    /**
     * Method for Mike attacking the player
     */
    public void mikeAttacks() {
        double damage = Math.random();
        while (damage <= 0.1) {
            damage = Math.random();
        }
        damage = damage * 10;

        if (!this.playerWon) {
            this.playerHealth -= (int) damage;
        }

        if (this.playerHealth <= 0) {
            this.playerHealth = 0;
            this.statesFSA.moveStates(0);
        }
    }

    /**
     * Getter checking if the player has won
     * @return boolean
     */
    public boolean checkIfWon() {
        return this.playerWon;
    }
}
