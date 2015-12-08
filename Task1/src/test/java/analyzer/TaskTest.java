package analyzer;

import analyzer.commands.FindFirstThreeDuplicatesCommand;
import analyzer.commands.FindFirstThreeLongestCommand;
import analyzer.commands.FindTwoMostFrequentWordsCommand;
import org.junit.Test;

import static org.junit.Assert.*;

public class TaskTest {

    @Test
    public void testGetCommand() throws Exception {
        assertTrue(Task.FREQUENCY.getCommand() instanceof FindTwoMostFrequentWordsCommand);
        assertTrue(Task.DUPLICATES.getCommand() instanceof FindFirstThreeDuplicatesCommand);
        assertTrue(Task.LENGTH.getCommand() instanceof FindFirstThreeLongestCommand);
    }

    @Test
    public void testFromString() throws Exception {
        assertEquals(Task.FREQUENCY, Task.fromString("frequency"));
        assertEquals(Task.LENGTH, Task.fromString("length"));
        assertEquals(Task.DUPLICATES, Task.fromString("duplicates"));
        assertNull(Task.fromString("not a command"));
    }
}