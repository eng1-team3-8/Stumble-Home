package io.github.stumblehome;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * Handles the Long Boi event movement, animations, and interactions with the game world.
 */
public class longBoiEvent extends Sprite {
    /**
     * Sprite sheet containing all animation frames.
     */
    public Texture longSheet;

    /**
     * Idle animation frame. (This has not been used but here in case for future use.)
     */
    private TextureRegion idleFrame;

    /** Currently active animation being played. */
    private Animation<TextureRegion> currentAnimation;

    /** Previous animation, used to reset timing when animation changes. */
    private Animation<TextureRegion> previousAnimation;

    /** Time elapsed in the current animation. */
    private float stateTime;

    /** Current x position of long boi on map. */
    public float longX;
    /** Current y position of long boi on map. */
    public float longY;
    /** Scale of long boi frames. */
    public final float longSize = 2f;

    /** Variable which is true when player is in radius of long boi.*/
    private boolean near = false;

    // the walking loop variables
    private int xDirection = 70;
    private int yDirection = 150;
    private int xDirection2 = 250;
    private int yDirection2 = 70;
    private int xDirection3 = 70;
    private int yDirection3 = 150;
    private int xDirection4 = 60;
    private int yDirection4 = 120;
    private int xLoopCounter = 0;
    private int yLoopCounter = 0;
    private int xLoopCounter2 = 0;
    private int yLoopCounter2 = 0;
    private int xLoopCounter3 = 0;
    private int yLoopCounter3 = 0;
    private int xLoopCounter4 = 0;
    private int yLoopCounter4 = 0;

    /** Variable which is true when long boi has completed the walk. */
    public boolean doneWalk = false;

    /** Variable which is true when player collided with long boi. */
    public boolean collided = false;

    /** Distance between player and long boi to set near to true. */
    public float radius = 6;

    /**
     * Constructs a new longBoiEvent with given sprite and initialises animations.
     *
     * @param sprite the sprite containing long boi texture.
     */
    public longBoiEvent(Sprite sprite) {
        super(sprite);

        // initialize animations
        initializeAnimations();
    }

    /**
     * Loads all animation frames from the long boi spritesheet.
     */
    private void initializeAnimations() {
        int frameWidth = 32;
        int frameHeight = 32;

        longSheet = this.getTexture();

        // set idle frame
        idleFrame = new TextureRegion(longSheet, 0, 32, frameWidth, frameHeight);

        // walking frames
        TextureRegion[] longFrames = new TextureRegion[2];
        longFrames[0] = new TextureRegion(longSheet, 0, 0, frameWidth, frameHeight);
        longFrames[1] = new TextureRegion(longSheet, 32, 0, frameWidth, frameHeight);
        Animation<TextureRegion> animation = new Animation<>(0.3f, longFrames);

        stateTime = 0f;

        currentAnimation = animation;
    }

    /**
     * Updates the animation state timer each frame. Resets the timer when switching between
     * different animations. Only does these if player is near long boi.
     */
    public void logic() {
        if (near) {
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
     * Renders long boi to the screen if near and not finished walking.
     *
     * @param batch the sprite batch used for rendering.
     */
    public void draw(SpriteBatch batch) {
        TextureRegion frameToDraw;
        if (near & !doneWalk) {
            frameToDraw = currentAnimation.getKeyFrame(stateTime, true);
            // draw long boi
            batch.draw(frameToDraw, longX, longY, longSize, longSize);
        }
    }

    /**
     * Checks whether player's centre is within 1.3 world units of long boi's centre. If so, variable
     * 'collided' is set to true.
     *
     * @param playerCentreX the centre of the player in x direction.
     * @param playerCentreY the centre of player in y direction.
     * @return true if player has collided with long boi
     */
    public boolean checkCollision(float playerCentreX, float playerCentreY) {
        if (collided) {
            return true;
        }

        boolean colliding;
        //calculate distance between player centre and long boi centre using pythagoras
        double distance = sqrt(pow(playerCentreX - getCentreX(), 2) + pow(playerCentreY - getCentreY(), 2));

        // set nearing to true if player within (value of radius) of long boi
        colliding = distance < 1.3f;

        if (colliding) {
            collided = true;
        }
        return colliding;
    }

    /**
     * Checks whether player's centre is within the radius of long boi's centre. If so, variable
     * 'near' is set to true.
     *
     * @param playerCentreX the centre of the player in x direction.
     * @param playerCentreY the centre of player in y direction.
     * @return true if player is near long boi in that frame, false if player is already near or not within radius.
     */
    public boolean checkNear(float playerCentreX, float playerCentreY) {
        if (near) {
            return false;
        }

        boolean nearing;
        //calculate distance between player centre and long boi centre using pythagoras
        double distance = sqrt(pow(playerCentreX - getCentreX(), 2) + pow(playerCentreY - getCentreY(), 2));

        // set nearing to true if player within (value of radius) of long boi
        nearing = distance < radius;

        if (nearing) {
            near = true;
        }
        return nearing;
    }

    private float getCentreX(){
        return longX + longSize / 2;
    }

    private float getCentreY(){
        return longY + longSize / 2;
    }

    public boolean getNear() {
        return near;
    }

    /**
     * This function is not very good. It controls the walking of long boi, each 'if' statement is
     * the different distances long boi walks in the x and y directions in order.
     * Needs to only execute once per frame hence why it is if statements, otherwise it will just teleport.
     */
    public void walkPath(){
        // this is badly written due to time
        float delta = Gdx.graphics.getDeltaTime();
        float speed = 3f;
        if (xLoopCounter < xDirection) {
            longX += delta*speed;
            xLoopCounter++;
        } else if (yLoopCounter < yDirection) {
            longY += delta*speed;
            yLoopCounter++;
        } else if (xLoopCounter2 < xDirection2) {
            longX -= delta*speed;
            xLoopCounter2++;
        } else if (yLoopCounter2 < yDirection2) {
            longY-= delta*speed;
            yLoopCounter2++;
        } else if (xLoopCounter3 < xDirection3) {
            longX -= delta*speed;
            xLoopCounter3++;
        } else if (yLoopCounter3 < yDirection3) {
            longY += delta*speed;
            yLoopCounter3++;
        } else if (xLoopCounter4 < xDirection4) {
            longX += delta*speed;
            xLoopCounter4++;
        } else if (yLoopCounter4 < yDirection4) {
            longY += delta * speed;
            yLoopCounter4++;
        } else {
            doneWalk = true;
        }
    }
}
