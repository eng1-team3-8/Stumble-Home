package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;


/**
 * Represents a collectible keycard item required to unlock the exit.
 * The keycard displays an animated sprite until collected by the player.
 */
public class keycardEvent extends Sprite {

    /** Spritesheet texture containing keycard animation frames. */
    public Texture keycardSheet;

    /** Current animation being displayed. */
    private Animation<TextureRegion> currentAnimation;

    /** Previously displayed animation, used to detect animation changes. */
    private Animation<TextureRegion> previousAnimation;

    /** Elapsed time for the current animation. */
    private float stateTime;

    /** X-coordinate of the keycard on the map. */
    public float keycardX;

    /** Y-coordinate of the keycard on the map. */
    public float keycardY;

    /** Size of the keycard sprite when rendered. */
    public final float keycardSize = 1.0f;

    /** Whether the player has picked up the keycard. */
    private boolean collected = false;

    /**
     * Creates a new keycard event with the specified sprite.
     *
     * @param sprite the sprite containing the keycard texture
     */
    public keycardEvent(Sprite sprite) {
        super(sprite);
        initializeAnimations();
    }

    /**
     * Extracts animation frames from the keycard spritesheet.
     * Sets up a 3-frame looping animation.
     */
    private void initializeAnimations() {
        int frameWidth = 32;
        int frameHeight = 32;

        keycardSheet = this.getTexture();

        // grab frames: top-left, middle-left, top-right
        TextureRegion[] keycardFrames = new TextureRegion[3];
        keycardFrames[0] = new TextureRegion(keycardSheet, 0, 0, frameWidth, frameHeight);
        keycardFrames[1] = new TextureRegion(keycardSheet, 0, 32, frameWidth, frameHeight);
        keycardFrames[2] = new TextureRegion(keycardSheet, 32, 0, frameWidth, frameHeight);

        currentAnimation = new Animation<>(0.3f, keycardFrames);
        stateTime = 0f;
    }

    /**
     * Updates the keycard's animation state each frame.
     * Only processes animation while the keycard hasn't been collected.
     */
    public void logic() {
        if (!collected) {
            if (currentAnimation != previousAnimation) {
                stateTime = 0f;
                previousAnimation = currentAnimation;
            }
            stateTime += Gdx.graphics.getDeltaTime();
        }
    }

    /**
     * Renders the keycard to the screen if it hasn't been collected yet.
     *
     * @param batch the sprite batch used for rendering
     */
    public void draw(SpriteBatch batch) {
        if (!collected) {
            TextureRegion frameToDraw = currentAnimation.getKeyFrame(stateTime, true);
            batch.draw(frameToDraw, keycardX, keycardY, keycardSize, keycardSize);
        }
    }

    /**
     * Checks whether the player has collided with the keycard using pythagoras theorem
     * between the centres of the keycard and player.
     *
     * @param playerCentreX the player's centre x position
     * @param playerCentreY the player's centre y position
     * @return true if collision occurred, false otherwise
     */
    public boolean checkCollision(float playerCentreX, float playerCentreY) {
        if (collected) {
            return false;
        }

        boolean colliding;
        //calculate distance between player centre and long boi centre using pythagoras
        double distance = sqrt(pow(playerCentreX - getCentreX(), 2) + pow(playerCentreY - getCentreY(), 2));

        // set nearing to true if player within (value of radius) of long boi
        colliding = distance < 1f;

        if (colliding) {
            collected = true;
        }
        return colliding;
    }

    private float getCentreX(){
        return keycardX + keycardSize / 2;
    }

    private float getCentreY(){
        return keycardY + keycardSize / 2;
    }
}
