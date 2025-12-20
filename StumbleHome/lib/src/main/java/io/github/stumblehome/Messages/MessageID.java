package io.github.stumblehome.Messages;

import com.badlogic.gdx.graphics.Color;

/**
 * data structure to hold the necessary information about a message
 * @author Isaac M
 */
public class MessageID {
    public String message;
    public Color colour;
    public float size;
    public float time_left;

    /**
     * creates a new messageID with a default time of 0
     * @param message text that the message will show
     * @param colour the color of the text of the message
     * @param size the size of the text on screen
     */
    public MessageID(String message, Color colour, float size) {
        this.message = message;
        this.colour = colour;
        this.size = size;
        this.time_left = 0f;
    }
}
