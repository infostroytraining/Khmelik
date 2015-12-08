package analyzer;

import com.beust.jcommander.ParameterException;
import org.junit.Test;

import java.util.regex.PatternSyntaxException;

import static org.junit.Assert.*;

public class TasksConverterTest {

    TasksConverter converter = new TasksConverter();

    @Test
    public void testConvert() throws Exception {
        assertEquals(Task.FREQUENCY, converter.convert("frequEncy"));
        assertEquals(Task.LENGTH, converter.convert("lenGth"));
        assertEquals(Task.DUPLICATES, converter.convert("Duplicates"));
    }

    @Test(expected = ParameterException.class)
    public void testConvertOnNullValue() {
        converter.convert("not a command");
    }
}