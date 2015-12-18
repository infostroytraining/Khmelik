package analyzer.commands;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class FindTwoMostFrequentWordsCommandTest {

    private static final String TEST_FILE_PATH = "src\\main\\resources\\test_input.txt";
    private static final String EMPTY_FILE_PATH = "src\\main\\resources\\empty_input.txt";
    private static final String NO_FILE_PATH = "src\\main\\resources\\no_file.txt";

    private static final String TEST_FILE_MOST_FREQUENT_WORDS = "\u043d\u0430 -> 3\n\u043b\u0435\u0436\u0443 -> 3";

    private FindTwoMostFrequentWordsCommand command = new FindTwoMostFrequentWordsCommand();

    @Test
    public void testExecute() throws IOException {
        assertEquals(TEST_FILE_MOST_FREQUENT_WORDS, command.execute(TEST_FILE_PATH, true));
    }

    @Test
    public void testExecuteOnEmptyFile() throws IOException {
        assertEquals(FindTwoMostFrequentWordsCommand.NO_WORDS_MESSAGE,
                command.execute(EMPTY_FILE_PATH, true));
    }

    @Test(expected = IOException.class)
    public void testExecuteOnNoFile() throws IOException {
        command.execute(NO_FILE_PATH, true);
    }
}