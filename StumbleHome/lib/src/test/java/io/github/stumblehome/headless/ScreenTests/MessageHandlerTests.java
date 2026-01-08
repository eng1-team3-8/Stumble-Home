package io.github.stumblehome.headless.ScreenTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
                test_handler.getMessages().size(),
                0,
                "Message handler should not have any messages initially");
        assertEquals(
                test_handler.getShown_queue().size(), 0, "Shown queue should be empty initially");
    }
}
