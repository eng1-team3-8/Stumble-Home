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
 * Represents a collectible bottle item that appears in the game world.
 * When collected by the player, this affects their movement controls.
 */
public class bottleEvent extends Sprite {

    // Spritesheet texture containing all animation frames for the bottle.
    public Texture bottleSheet;

    // The animation sequence currently being displayed.
    private Animation<TextureRegion> currentAnimation;

    // Previously displayed animation, used to detect when animation changes.
    private Animation<TextureRegion> previousAnimation;

    // Tracks how long the current animation has been playing.
    private float stateTime;

    // X-coordinate of the bottle's position on the map.
    public float bottleX;

    // Y-coordinate of the bottle's position on the map.
    public float bottleY;

    // Size of the bottle sprite when rendered.
    public final float bottleSize = 1.5f;

    // Whether the bottle has been picked up by the player.
    private boolean collected = false;

    /**
     * Creates a new bottle event at a specific location.
     *
     * @param sprite the base sprite containing the bottle texture
     */
    public bottleEvent(Sprite sprite) {
        super(sprite);

        //Initialize bottle animations
        initializeAnimations();
    }

    /**
     * Sets up the bottle's animation frames from the spritesheet.
     * Extracts 6 frames at 32x32 pixels each and configures the looping animation.
     */
    private void initializeAnimations() {
        // Frame size: 25x49 pixels
        int frameWidth = 32;
        int frameHeight = 32;

        bottleSheet = this.getTexture();

        // Walking LEFT (4 frames)
        TextureRegion[] bottleFrames = new TextureRegion[6];
        bottleFrames[0] = new TextureRegion(bottleSheet, 0, 0, frameWidth, frameHeight);
        bottleFrames[1] = new TextureRegion(bottleSheet, 32, 0, frameWidth, frameHeight);
        bottleFrames[2] = new TextureRegion(bottleSheet, 0, 32, frameWidth, frameHeight);
        bottleFrames[3] = new TextureRegion(bottleSheet, 32, 32, frameWidth, frameHeight);
        bottleFrames[4] = new TextureRegion(bottleSheet, 0, 64, frameWidth, frameHeight);
        bottleFrames[5] = new TextureRegion(bottleSheet, 32, 64, frameWidth, frameHeight);
        Animation<TextureRegion> animation = new Animation<>(0.5f, bottleFrames);

        stateTime = 0f;

        currentAnimation = animation;
    }

    /**
     * Updates the bottle's animation state each frame.
     * Only processes animation if the bottle hasn't been collected yet.
     */
    public void logic() {
        if (!collected) {
            // Reset animation time if animation changed
            if (currentAnimation != previousAnimation) {
                stateTime = 0f;
                previousAnimation = currentAnimation;
            }

            // Update animation time
            stateTime += Gdx.graphics.getDeltaTime();
        }
    }

    /**
     * Renders the bottle to the screen if it hasn't been collected.
     *
     * @param batch the sprite batch used for rendering
     */
    public void draw(SpriteBatch batch) {
        if (!collected) {
            // Determine which frame to draw
            TextureRegion frameToDraw;
            frameToDraw = currentAnimation.getKeyFrame(stateTime, true);

            // Draw the character with proper aspect ratio
            batch.draw(frameToDraw, bottleX, bottleY, bottleSize, bottleSize);
        }
    }

    /**
     * Checks whether the player has collided with the bottle using pythagoras theorem
     * between the centres of the bottle and player.
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
        return bottleX + bottleSize / 2;
    }

    private float getCentreY(){
        return bottleY + bottleSize / 2;
    }


}
