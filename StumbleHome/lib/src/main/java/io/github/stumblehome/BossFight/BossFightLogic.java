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

    public BossFightLogic(Texture cutCable) {
        this.cutCable = cutCable;
    }

    private void checkIfStopped(
            BossFightEntity[] cables, Scissors scissors, BossFightStatesManager statesFSA)
            throws InterruptedException {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !isStopped) {
            isStopped = true;
            this.checkOverlap(cables, scissors, statesFSA);
        }

        // Below is code for testing
        //        else if  (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && isStopped) {
        //          isStopped = false;
        //        }
    }

    public void moveScissors(
            Viewport viewport,
            SpriteBatch batch,
            Scissors scissors,
            BossFightEntity[] cables,
            BossFightStatesManager statesFSA)
            throws InterruptedException {
        this.checkIfStopped(cables, scissors, statesFSA);
        if (!isStopped) {
            scissors.move(viewport);
        }

        scissors.draw(batch);
    }

    public void drawCables(BossFightEntity[] cables, SpriteBatch batch) {
        for (int i = 0; i < cables.length; i++) {
            cables[i].draw(batch);
        }
    }

    public void checkOverlap(
            BossFightEntity[] cables, Scissors scissors, BossFightStatesManager statesFSA)
            throws InterruptedException {
        System.out.println("Overlap is being checked");
        for (int i = 0; i < cables.length; i++) {
            System.out.println("Cable: " + i);
            if (scissors.overlaps(cables[i])) {
                System.out.println("Overlaps!");
                if (this.cableStatus[i]) {
                    updateSprite(cables[i], statesFSA, i);
                    scissors.setX(105f);
                }
            }
        }
    }

    private void updateSprite(BossFightEntity cable, BossFightStatesManager statesFSA, int cableNo)
            throws InterruptedException {
        this.cableStatus[cableNo] = false;
        cable.setTexture(this.cutCable);
        Thread.sleep(500);
        this.isStopped = false;
        this.checkIfAllWiresCut();
        statesFSA.moveStates(-1);
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
}
