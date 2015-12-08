package analyzer.commands;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class FindFirstThreeLongestCommandTest {

    private static final String TEST_FILE_PATH = "src\\main\\resources\\test_input.txt";
    private static final String EMPTY_FILE_PATH = "src\\main\\resources\\empty_input.txt";
    private static final String NO_FILE_PATH = "src\\main\\resources\\no_file.txt";

    private static final String TEST_FILE_LONGEST = "\u0441\u043e\u043b\u043d\u044b\u0448\u043a\u0435 -> 8\n\u0441\u043e\u043b\u043d\u044b\u0448\u043a\u043e -> 8\n\u0433\u043b\u044f\u0436\u0443 -> 5\n";
    private static final String TEST_FILE_LONGEST_POSSIBLE_ANSWER = "\u0441\u043e\u043b\u043d\u044b\u0448\u043a\u043e -> 8\n\u0441\u043e\u043b\u043d\u044b\u0448\u043a\u0435 -> 8\n\u0433\u043b\u044f\u0436\u0443 -> 5\n";


    FindFirstThreeLongestCommand command = new FindFirstThreeLongestCommand();

    @Test
    public void testExecute() throws IOException {
        assertTrue(
                TEST_FILE_LONGEST.equals(command.execute(TEST_FILE_PATH)) ||
                        TEST_FILE_LONGEST_POSSIBLE_ANSWER.equals(command.execute(TEST_FILE_PATH)));
    }

    @Test
    public void testExecuteOnEmptyFile() throws IOException {
        assertEquals(FindFirstThreeLongestCommand.NO_WORDS_MESSAGE,
                command.execute(EMPTY_FILE_PATH));
    }

    @Test(expected = IOException.class)
    public void testExecuteOnNoFile() throws IOException {
        command.execute(NO_FILE_PATH);
    }
}