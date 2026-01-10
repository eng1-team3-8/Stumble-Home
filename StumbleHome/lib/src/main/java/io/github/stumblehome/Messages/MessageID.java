package io.github.stumblehome.Messages;

import com.badlogic.gdx.graphics.Color;

/**
 * data structure to hold the necessary information about a message
 *
 * @author Isaac M
 */
public class MessageID {
    // Text of the message
    private String message;
    // Color of the message, default white
    private Color colour;
    // size of the message
    private float size;
    // remaining time left that the message should be shown for 
    private float time_left;

    /**
     * creates a new messageID with a default time of 0
     *
     * @param message text that the message will show
     * @param colour the color of the text of the message, if null default to White
     * @param size the size of the text on screen
     * @throws IllegalArgumentException if the message or size are invalid or null
     */
    public MessageID(String message, Color colour, float size) throws IllegalArgumentException {
        if (!this.setMessage(message)) {
            throw new IllegalArgumentException("Message should have some text");
        }
        if (!this.setSize(size)) {
            throw new IllegalArgumentException("size of message must be greater than 0");
        }
        if (colour == null) {
            colour = Color.WHITE;
        }
        this.setColor(colour);
        this.time_left = 0f;
    }

    /**
     * checks if MessageID is equal to another MessageID
     * @param other the other object being tested
     * @return false if other is not a messsageID or if it has any different variables to this
     */
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

    /**
     * sets the text of the message ID
     * @param message to be displayed, can't be null or empty
     * @return true if text was set correctly 
     */
    public boolean setMessage(String message) {
        if (message == "" || message == null) {
            return false;
        } else {
            this.message = message;
            return true;
        }
    }

    /**
     * getter for the message text
     * @return message text
     */
    public String getMessage() {
        return message;
    }

    /**
     * sets the size of the message
     * @param size of the message, cannot be 0 or less
     * @return true if the size was updated 
     */
    public boolean setSize(float size) {
        if (size <= 0) {
            return false;
        } else {
            this.size = size;
            return true;
        }
    }

    /**
     * getter for the message size
     * @return size of the message
     */
    public float getSize() {
        return size;
    }

    /**
     * getter for the message colour
     * @return member of Color enum, color of message
     */
    public Color getColour() {
        return colour;
    }

    /**
     * sets the color of the message
     * @param colour of the message, cannot be null
     * @return true if color was updated
     */
    public boolean setColor(Color colour) {
        if (colour == null) {
            return false;
        } else {
            this.colour = colour;
            return true;
        }
    }

    /**
     * getter for the time remaining on a message
     * @return time remaining on message
     */
    public float getTime() {
        return time_left;
    }

    /**
     * setter for the time remaining on a message
     * @param time new time remaining for message, cannot be negative
     * @return true if the time_left was updated correctly
     */
    public boolean setTime(float time) {
        if (time < 0) {
            return false;
        } else {
            this.time_left = time;
            return true;
        }
    }
}
