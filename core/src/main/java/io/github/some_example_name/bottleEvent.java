package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class bottleEvent extends Sprite {

    // Bottle animation
    public Texture bottleSheet;

    private Animation<TextureRegion> currentAnimation;
    private Animation<TextureRegion> previousAnimation;
    private float stateTime;

    public float bottleX;
    public float bottleY;
    public final float bottleSize = 1.5f;

    private boolean collected = false;

    public bottleEvent(Sprite sprite) {
        super(sprite);

        //Initialize bottle animations
        initializeAnimations();
    }

    private void initializeAnimations() {
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

    public void draw(SpriteBatch batch) {
        if (!collected) {
            // Determine which frame to draw
            TextureRegion frameToDraw;
            frameToDraw = currentAnimation.getKeyFrame(stateTime, true);

            // Draw the character with proper aspect ratio
            batch.draw(frameToDraw, bottleX, bottleY, bottleSize, bottleSize);
        }
    }

    public boolean checkCollision(float playerCentreX, float playerCentreY) {
        if (collected) {
            return false;
        }

        boolean colliding;
        //calculate distance between player centre and bottle centre
        double distance = sqrt(pow(playerCentreX - getCentreX(), 2) + pow(playerCentreY - getCentreY(), 2));

        // set nearing to true if player colliding (within 0.8 units of it)
        colliding = distance < 1;

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
