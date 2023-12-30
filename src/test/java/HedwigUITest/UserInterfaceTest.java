package HedwigUITest;

import HedwigUI.UserInterface;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.io.*;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class UserInterfaceTest {

    private final UserInterface ui = new UserInterface("");
    private String data = "testcase";
    static InputStream resetSystemIn = System.in;

    @Test
    public void testUserInput() {
        ByteArrayInputStream in = new ByteArrayInputStream(data.getBytes());
        System.setIn(in);
        String input = ui.readUserInput();
        assertEquals(data, input);
    }

    @Test
    public void testIfDoublePrimitiveGetsParsedCorrectly() {
        ByteArrayInputStream in = new ByteArrayInputStream("45.789866".getBytes());
        System.setIn(in);
        double input = Double.parseDouble(ui.readUserInput());
        System.out.println(input);
        assertEquals(45.789866, input, 0.001);
    }

    @ParameterizedTest
    @ValueSource(strings = {"yes", "Yes", "YES", "YEs"})
    public void testIfYesIsAccepted(String values) {
        ByteArrayInputStream in = new ByteArrayInputStream(values.getBytes());
        System.setIn(in);
        boolean accepted = ui.acceptInput();
        assertTrue(accepted);
    }

    @ParameterizedTest
    @ValueSource(strings = {"no", "No", "NO"})
    public void testIfNoIsAccepted(String values) {
        ByteArrayInputStream in = new ByteArrayInputStream(values.getBytes());
        System.setIn(in);
        boolean accepted = ui.acceptInput();
        assertFalse(accepted);
    }

    @AfterAll
    public static void resetSystemIn() {
        System.setIn(resetSystemIn);
    }
}