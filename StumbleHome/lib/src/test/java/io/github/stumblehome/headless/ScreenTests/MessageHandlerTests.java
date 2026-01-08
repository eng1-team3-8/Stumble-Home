package io.github.stumblehome.headless.ScreenTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.badlogic.gdx.graphics.Color;
import io.github.stumblehome.Messages.*;
import io.github.stumblehome.StumbleHome;
import io.github.stumblehome.headless.AbstractHeadlessGdxTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class MessageHandlerTests extends AbstractHeadlessGdxTest {
    MessageHandler test_handler;

    @BeforeAll
    public void setup() {
        StumbleHome test_game = new StumbleHome();
        test_handler = new MessageHandler(test_game);
    }

    @Test
    public void testMessageHandlerInitialization() {
        assertNotNull(test_handler, "Message Handler failed to initialize");
        assertEquals(
                test_handler.getShown_queue().size(), 0, "Shown queue should be empty initially");
        assertEquals(
                test_handler.getMessages().size(),
                Messages.values().length,
                "All messages should be initialised into MessageHandler");
        for (Messages msg : test_handler.getMessages().keySet()) {
            assertEquals(
                    test_handler.getMessages().get(msg),
                    null,
                    "All messages should be initialised to null");
        }
    }

    @Test
    public void testInitiliseCorrectMessageID() {
        MessageID test_message = new MessageID("test", Color.WHITE, 1f);
        assertNotNull(test_message, "MessageID should be initilised");
        assertEquals(
                test_message.getColour(),
                Color.WHITE,
                "MessageID should be initilised with correct color");
        assertEquals(
                test_message.getMessage(),
                "test",
                "MessageID should be initilised with correct message");
        assertEquals(
                test_message.getSize(), 1f, "MessageID should be initilised with correct size");
        assertEquals(test_message.getTime(), 0, "MessageID should have initial time of 0");

        MessageID test_message_null_Color = new MessageID("test", null, 1f);
        assertNotNull(test_message_null_Color, "MessageID should be initilised with null colour");
        assertEquals(
                test_message_null_Color.getColour(),
                Color.WHITE,
                "MessageID should have default color White");
    }

    @Test
    public void testInitiliseIncorrectMessageID() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new MessageID("", Color.WHITE, 1f),
                "MessageID should not be able to be initilised with no message");
        assertThrows(
                IllegalArgumentException.class,
                () -> new MessageID(null, Color.WHITE, 1f),
                "MessageID should not be able to be initilised with null message");
        assertThrows(
                IllegalArgumentException.class,
                () -> new MessageID("test", Color.WHITE, -1f),
                "MessageID should not be able to be initilised with negative size");
        assertThrows(
                IllegalArgumentException.class,
                () -> new MessageID("test", Color.WHITE, 0),
                "MessageID should not be able to be initilised with size of 0");
    }

    @Test
    public void testMessageIDEquals() {
        MessageID test_message = new MessageID("test", Color.WHITE, 1f);
        MessageID test_message_2 = new MessageID("test", Color.WHITE, 1f);
        assertTrue(test_message.equals(test_message_2), "equals should have returned true");
        test_message_2.setMessage("test2");
        assertFalse(
                test_message.equals(test_message_2),
                "equals should have returned false due to message");
        test_message_2.setMessage("test");
        test_message_2.setColor(Color.BLACK);
        assertFalse(
                test_message.equals(test_message_2),
                "equals should have returned false due to colour");
        test_message_2.setColor(Color.WHITE);
        test_message_2.setSize(2f);
        assertFalse(
                test_message.equals(test_message_2),
                "equals should have returned false due to size");
        test_message_2.setSize(1f);
        test_message_2.setTime(2f);
        assertFalse(
                test_message.equals(test_message_2),
                "equals should have returned false due to time_remaining");
    }

    @Test
    public void testAddCorrectMessage() {
        boolean message_add = test_handler.addMessage(Messages.PAUSED, "test", Color.WHITE, 12.0f);
        assertTrue(message_add, "Message was not able to be added correctly");
        assertNotNull(
                test_handler.getMessages().get(Messages.PAUSED),
                "message should be added to MessageHandler");
        assertEquals(
                test_handler.getMessages().get(Messages.PAUSED).getColour(),
                Color.WHITE,
                "Message colour was not initialised correctly");
        assertEquals(
                test_handler.getMessages().get(Messages.PAUSED).getMessage(),
                "test",
                "Message text was not initialised correctly");
        assertEquals(
                test_handler.getMessages().get(Messages.PAUSED).getSize(),
                12.0f,
                "Message size was not initialised correctly");
        assertEquals(
                test_handler.getMessages().get(Messages.PAUSED).getTime(),
                0,
                "Message should be initlised with 0 time running");
    }

    @Test
    public void testAddIncorrectMessage() {
        boolean message_add_no_msg = test_handler.addMessage(Messages.PAUSED, "", Color.WHITE, 1f);
        assertFalse(message_add_no_msg, "messageHandler should report failure upon no text");
        assertNull(
                test_handler.getMessages().get(Messages.PAUSED),
                "message should not be added when text is empty");

        boolean message_add_size = test_handler.addMessage(Messages.BOTHSQUISHED, "test", null, 0);
        assertNull(
                test_handler.getMessages().get(Messages.BOTHSQUISHED),
                "message should not be added when size is 0");
        assertFalse(message_add_size, "messageHandler should report failure upon size of 0");

        boolean message_add_neg_size =
                test_handler.addMessage(Messages.CHAINSAWPICKEDUP, "test", Color.WHITE, -1);
        assertNull(
                test_handler.getMessages().get(Messages.CHAINSAWPICKEDUP),
                "message should not be added when size is negative");
        assertFalse(message_add_neg_size, "messageHandler should report failure upon size of 0");
    }

    @Test
    public void testAddNullMessage() {
        int size = test_handler.getMessages().size();
        boolean message_add = test_handler.addMessage(null, "test", Color.WHITE, 12.0f);
        assertFalse(message_add, "Adding a null message should return false");
        assertEquals(
                test_handler.getMessages().size(),
                size,
                "Message map size should not change when adding a null message");
    }

    @Test
    public void testChangeExistingMessage() {
        test_handler.addMessage(Messages.PAUSED, "test", Color.WHITE, 12.0f);
        boolean message_add = test_handler.addMessage(Messages.PAUSED, "test2", Color.RED, 1.0f);
        assertTrue(message_add, "Message was not able to be overridden correctly");
        assertNotNull(
                test_handler.getMessages().get(Messages.PAUSED),
                "message should be overridden in MessageHandler");
        assertEquals(
                test_handler.getMessages().get(Messages.PAUSED).getColour(),
                Color.RED,
                "Message colour was not changed correctly");
        assertEquals(
                test_handler.getMessages().get(Messages.PAUSED).getMessage(),
                "test2",
                "Message text was not changed correctly");
        assertEquals(
                test_handler.getMessages().get(Messages.PAUSED).getSize(),
                1.0f,
                "Message size was not changed correctly");
        assertEquals(
                test_handler.getMessages().get(Messages.PAUSED).getTime(),
                0,
                "MEssage time should remain at 0 when changed");
    }

    @Test
    public void testAddMultipleMessages() {
        boolean message_add1 = test_handler.addMessage(Messages.PAUSED, "test", Color.WHITE, 1.0f);
        boolean message_add2 =
                test_handler.addMessage(Messages.NOKEYCARD, "test2", Color.RED, 2.0f);
        assertTrue(message_add1, "First message was not able to be added correctly");
        assertTrue(message_add2, "Second message was not able to be added correctly");

        assertNotNull(
                test_handler.getMessages().get(Messages.PAUSED),
                "first message should be added to MessageHandler");
        assertEquals(
                test_handler.getMessages().get(Messages.PAUSED).getColour(),
                Color.WHITE,
                "Message 1 colour was not changed correctly");
        assertEquals(
                test_handler.getMessages().get(Messages.PAUSED).getMessage(),
                "test",
                "Message 1 text was not changed correctly");
        assertEquals(
                test_handler.getMessages().get(Messages.PAUSED).getSize(),
                1.0f,
                "Message 1 size was not changed correctly");

        assertNotNull(
                test_handler.getMessages().get(Messages.NOKEYCARD),
                "second message should be added to MessageHandler");
        assertEquals(
                test_handler.getMessages().get(Messages.PAUSED).getColour(),
                Color.RED,
                "Message 2 colour was not changed correctly");
        assertEquals(
                test_handler.getMessages().get(Messages.PAUSED).getMessage(),
                "test2",
                "Message 2 text was not changed correctly");
        assertEquals(
                test_handler.getMessages().get(Messages.PAUSED).getSize(),
                2.0f,
                "Message 2 size was not changed correctly");
    }

    public void testSetTimeExistingMessage() {
        test_handler.addMessage(Messages.PAUSED, "test", Color.WHITE, 1.0f);
        boolean time_change = test_handler.setTime(Messages.PAUSED, 5);
        assertEquals(
                test_handler.getMessages().get(Messages.PAUSED).getTime(),
                5f,
                "Time should have been set to 5");
        assertTrue(time_change, "MessageHandler should report success");
    }

    public void testSetTimeNullMessage() {
        boolean time_change = test_handler.setTime(Messages.PAUSED, 5);
        assertFalse(time_change, "MessageHandler should not change time of null message");
        assertEquals(
                test_handler.getMessages().get(Messages.PAUSED),
                null,
                "Message should not be initilised by time_change");
    }
}
