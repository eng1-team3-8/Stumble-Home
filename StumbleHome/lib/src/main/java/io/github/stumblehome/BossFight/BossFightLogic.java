package io.github.stumblehome.BossFight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.stumblehome.Entities.BossFightEntity;

public class BossFightLogic {

    private boolean isStopped = false;
    private boolean[] cableStatus = {true, true, true, true};
    private final Texture cutCable;
    private boolean finalStage = false;
    private int playerHealth = 100;
    private int mikeHealth = 100;
    private boolean playerWon = false;
    private BossFightStatesManager statesFSA;

    public BossFightLogic(Texture cutCable, BossFightStatesManager statesFSA) {
        this.cutCable = cutCable;
        this.statesFSA = statesFSA;
    }

    private void checkIfStopped(BossFightEntity[] cables, Scissors scissors)
            throws InterruptedException {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !isStopped) {
            isStopped = true;
            System.out.println("It is being checked");
            this.checkOverlap(cables, scissors);
            this.isStopped = false;
            this.statesFSA.moveStates(1);
            scissors.setX(105f);
            this.mikeAttacks();
            System.out.println("Player health: " + this.playerHealth);
        }

        // Below is code for testing
        //                else if  (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && isStopped) {
        //                  isStopped = false;
        //                }
    }

    public void moveScissors(
            Viewport viewport, SpriteBatch batch, Scissors scissors, BossFightEntity[] cables)
            throws InterruptedException {
        this.checkIfStopped(cables, scissors);
        if (!isStopped) {
            scissors.move(viewport);
        }

        scissors.draw(batch);
    }

    public void drawCables(BossFightEntity[] cables, SpriteBatch batch) {
        for (int i = 0; i < cables.length; i++) {
            cables[i].draw(batch);
            //            System.out.println("Cable " + i + "X coordinate = " + cables[i].getX());
        }
    }

    public void checkOverlap(BossFightEntity[] cables, Scissors scissors)
            throws InterruptedException {
        System.out.println("Overlap is being checked");
        for (int i = 0; i < cables.length; i++) {
            System.out.println("Cable: " + i);
            if (scissors.checkColliding(cables[i].getX(), cables[i].getY())) {
                System.out.println("Overlaps!");
                if (this.cableStatus[i]) {
                    updateSprite(cables[i], i);
                }
            }
        }
    }

    private void updateSprite(BossFightEntity cable, int cableNo) throws InterruptedException {
        this.cableStatus[cableNo] = false;
        cable.setTexture(this.cutCable);
        Thread.sleep(500);
        this.checkIfAllWiresCut();
    }

    private void checkIfAllWiresCut() {
        for (int i = 0; i < cableStatus.length; i++) {
            if (cableStatus[i]) {
                return;
            }
        }
        this.finalStage = true;
    }

    public boolean isFinalStage() {
        return this.finalStage;
    }

    public void attackMike() {
        double damage = Math.random();
        damage = damage * 40;

        this.mikeHealth -= (int) damage;

        if (this.mikeHealth <= 0) {
            this.mikeHealth = 0;
            this.statesFSA.moveStates(1);
        }
    }

    public int getMikeHealth() {
        return this.mikeHealth;
    }

    public int getPlayerHealth() {
        return this.playerHealth;
    }

    public void mikeAttacks() {
        double damage = Math.random();
        damage = damage * 10;

        if (!this.playerWon) {
            this.playerHealth -= (int) damage;
        }

        if (this.playerHealth <= 0) {
            this.playerHealth = 0;
            this.statesFSA.moveStates(0);
        }
    }
}
