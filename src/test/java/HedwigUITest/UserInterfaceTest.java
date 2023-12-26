package HedwigUITest;

import HedwigUI.UserInterface;
import org.junit.After;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.*;


import static org.junit.Assert.*;

public class UserInterfaceTest {

    private final UserInterface ui = new UserInterface("");
    private String data = "testcase";
    InputStream resetSystemIn = System.in;

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
    @ValueSource(strings = {"yes", "no", "boo", "3434"})
    public void testIfInputDataIsAccepted(String values) {
        ByteArrayInputStream in = new ByteArrayInputStream(values.getBytes());
        System.setIn(in);
        boolean accepted = ui.acceptInput();
        assertTrue(accepted);

    }

    @Test
    public void testIfInputDataIsNotAccepted() {
        String yes = "no";
        ByteArrayInputStream in = new ByteArrayInputStream(yes.getBytes());
        System.setIn(in);
        assertFalse(ui.acceptInput());
    }

    @After
    public void resetSystemIn() {
        System.setIn(resetSystemIn);
    }
}