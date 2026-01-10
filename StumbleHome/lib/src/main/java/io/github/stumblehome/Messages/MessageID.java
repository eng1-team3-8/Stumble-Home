package io.github.stumblehome.Messages;

import com.badlogic.gdx.graphics.Color;

/**
 * data structure to hold the necessary information about a message
 *
 * @author Isaac M
 */
public class MessageID {
    private String message;
    private Color colour;
    private float size;
    private float time_left;

    /**
     * creates a new messageID with a default time of 0
     *
     * @param message text that the message will show
     * @param colour the color of the text of the message
     * @param size the size of the text on screen
     */
    public MessageID(String message, Color colour, float size) throws IllegalArgumentException {
        if (message == "" || message == null) {
            throw new IllegalArgumentException("Message should have some text");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("size of message must be greater than 0");
        }
        this.message = message;
        if (colour == null) {
            colour = Color.WHITE;
        }
        this.colour = colour;
        this.size = size;
        this.time_left = 0f;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof MessageID)) {
            return false;
        }
        MessageID other_msg = (MessageID) other;
        if (other_msg.message == this.message
                && other_msg.colour == this.colour
                && other_msg.size == this.size
                && other_msg.time_left == this.time_left) {
            return true;
        }
        return false;
    }

    public boolean setMessage(String message) {
        if (message == "" || message == null) {
            return false;
        } else {
            this.message = message;
            return true;
        }
    }

    public String getMessage() {
        return message;
    }

    public boolean setSize(float size) {
        if (size <= 0) {
            return false;
        } else {
            this.size = size;
            return true;
        }
    }

    public float getSize() {
        return size;
    }

    public Color getColour() {
        return colour;
    }

    public boolean setColor(Color colour) {
        if (colour == null) {
            return false;
        } else {
            this.colour = colour;
            return true;
        }
    }

    public float getTime() {
        return time_left;
    }

    public boolean setTime(float time) {
        if (time < 0) {
            return false;
        } else {
            this.time_left = time;
            return true;
        }
    }
}
