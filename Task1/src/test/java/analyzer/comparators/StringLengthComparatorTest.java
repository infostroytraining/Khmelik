package analyzer.comparators;

import org.junit.Test;

import static org.junit.Assert.*;

public class StringLengthComparatorTest {

    public static final String STRING_A = "a";
    public static final String STRING_B = "b";
    public static final String STRING_C = "abc";

    private StringLengthComparator comparator = new StringLengthComparator();

    @Test
    public void testCompare() throws Exception {
        assertEquals(0, comparator.compare(STRING_A, STRING_B));
        assertTrue(comparator.compare(STRING_B, STRING_C) < 0);
        assertTrue(comparator.compare(STRING_C, STRING_A) > 0);
    }
}