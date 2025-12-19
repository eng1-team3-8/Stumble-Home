package io.github.stumblehome.headless;

import static org.mockito.Mockito.mock;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import org.junit.jupiter.api.BeforeAll;

public abstract class AbstractHeadlessGdxTest {
    private static HeadlessApplication application;

    @BeforeAll
    public static void init() {
        // Initialize headless application once for all tests
        if (application == null) {
            application = new HeadlessApplication(new ApplicationAdapter() {});
            Gdx.gl = Gdx.gl20 = mock(GL20.class);
        }
    }
}
