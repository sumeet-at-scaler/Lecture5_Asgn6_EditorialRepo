import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void testClientClassExists() {
        try {
            Class.forName("Client");
        } catch (ClassNotFoundException e) {
            fail("Client class not found");
        }
    }

    @Test
    public void testTableCreatorClassExists() {
        try {
            Class.forName("TableCreator");
        } catch (ClassNotFoundException e) {
            fail("TableCreator class not found");
        }
    }

    @Test
    public void testClientMainMethod() throws ClassNotFoundException {
        Class<?> clientClass = Class.forName("Client");
        boolean hasMainMethod = false;
        for (Method method : clientClass.getDeclaredMethods()) {
            if (method.getName().equals("main")) {
                hasMainMethod = true;
                break;
            }
        }
        assertTrue(hasMainMethod);
    }

    @Test
    public void testTableCreatorRunnableInterface() throws Exception {
        assertTrue(Runnable.class.isAssignableFrom(Class.forName("TableCreator")));
    }

    @Test
    public void testTableCreatorConstructor() throws Exception {
        // Get the fields of the Adder class using reflection
        Constructor<?> tcConstructor = Class.forName("TableCreator").
                                                getDeclaredConstructor(int.class);
        assertNotNull(tcConstructor);
    }

    @Test
    public void testTableCreatorOutput() {
        try {
            // Get the fields of the Adder class using reflection
            Constructor<?> tcConstructor = Class.forName("TableCreator").
                    getDeclaredConstructor(int.class);
            Object tc = tcConstructor.newInstance(2);
            Method runMethod = tc.getClass().getDeclaredMethod("run");
            runMethod.invoke(tc);
            String[] expectedArray = {
                    "2 times 1 is 2",
                    "2 times 2 is 4",
                    "2 times 3 is 6",
                    "2 times 4 is 8",
                    "2 times 5 is 10",
                    "2 times 6 is 12",
                    "2 times 7 is 14",
                    "2 times 8 is 16",
                    "2 times 9 is 18",
                    "2 times 10 is 20"
            };
            String expected = String.join("\n", expectedArray);
            expected = expected + "\n";
            assertEquals(expected, outContent.toString());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testTableCreatorThread() throws InterruptedException {
        try {
            String input = "20\n";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
            System.setIn(inputStream);

            Method mainMethod = Class.forName("Client").getDeclaredMethod("main", String[].class);
            mainMethod.invoke(null, (Object)new String[0]);

            HashMap<String, String> map = ScalerThread.map;
            assertNotEquals(20, map.size(), "Each TableCreator object should be invoked on separate thread");
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}