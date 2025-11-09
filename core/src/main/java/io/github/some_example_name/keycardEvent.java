package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


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
     * Checks for collision between the player and the keycard.
     * If a collision occurs, marks the keycard as collected and prints a message.
     *
     * @param playerX the player's x position
     * @param playerY the player's y position
     * @param playerSize the player's collision box size
     * @return true if collision occurred, false otherwise
     */
    public boolean checkCollision(float playerX, float playerY, float playerSize) {
        if (collected) {
            return false;
        }

        // basic rectangle collision
        boolean colliding = playerX < keycardX + keycardSize &&
                           playerX + playerSize > keycardX &&
                           playerY < keycardY + keycardSize &&
                           playerY + playerSize > keycardY;

        if (colliding) {
            collected = true;
            System.out.println("Keycard collected! You can now exit.");
        }

        return colliding;
    }

    /**
     * Returns whether the keycard has been collected by the player.
     *
     * @return true if collected, false otherwise
     */
    public boolean isCollected() {
        return collected;
    }
}
