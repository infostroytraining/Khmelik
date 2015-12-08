package analyzer.commands;

import org.junit.Test;

import java.io.IOError;
import java.io.IOException;

import static org.junit.Assert.*;

public class FindFirstThreeDuplicatesCommandTest {

    private static final String TEST_FILE_PATH = "src\\main\\resources\\test_input.txt";
    private static final String EMPTY_FILE_PATH = "src\\main\\resources\\empty_input.txt";
    private static final String NO_FILE_PATH = "src\\main\\resources\\no_file.txt";

    private static final String TEST_FILE_DUPLICATES = "\u042f\n\u0410\u041d\n\u0423\u0416\u0415\u041b";

    private FindFirstThreeDuplicatesCommand command = new FindFirstThreeDuplicatesCommand();

    @Test
    public void testExecute() throws IOException {
        assertEquals(TEST_FILE_DUPLICATES, command.execute(TEST_FILE_PATH));
    }

    @Test
    public void testExecuteOnEmptyFile() throws IOException {
        assertEquals(FindFirstThreeDuplicatesCommand.NO_DUPLICATES_MESSAGE,
                command.execute(EMPTY_FILE_PATH));
    }

    @Test(expected = IOException.class)
    public void testExecuteOnNoFile() throws IOException {
        command.execute(NO_FILE_PATH);
    }
}