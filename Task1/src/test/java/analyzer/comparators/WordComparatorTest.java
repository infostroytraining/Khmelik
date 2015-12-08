package analyzer.comparators;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class WordComparatorTest {

    private Map<String, Long> unsortedMap;
    private List<Long> actualValuesListOrderList;
    private List<Long> expectedValuesOrderList;
    private List<String> expectedKeyOrderList;
    private List<String> actualKeyOrderList;

    private WordAlphabeticalComparator alphabeticalComparator = new WordAlphabeticalComparator();
    private WordFrequencyComparator frequencyComparator = new WordFrequencyComparator();

    @Before
    public void setUp() throws Exception {
        unsortedMap = new HashMap<String, Long>();
        unsortedMap.put("WordC", 2L);
        unsortedMap.put("WordA", 4L);
        unsortedMap.put("WordB", 1L);

        expectedValuesOrderList = new ArrayList<Long>();
        expectedValuesOrderList.add(4L);
        expectedValuesOrderList.add(1L);
        expectedValuesOrderList.add(2L);

        expectedKeyOrderList = new ArrayList<String>();
        expectedKeyOrderList.add("WordB");
        expectedKeyOrderList.add("WordC");
        expectedKeyOrderList.add("WordA");

        actualValuesListOrderList = new ArrayList<Long>();
        actualKeyOrderList = new ArrayList<String>();
    }

    @Test
    public void testCompareAlphabeticalComparator() throws Exception {
        unsortedMap.entrySet().stream()
                .sorted(alphabeticalComparator)
                .forEach(word -> actualValuesListOrderList.add(word.getValue()));
        assertEquals(expectedValuesOrderList, actualValuesListOrderList);
    }

    @Test
    public void testCompareFrequencyComparator() throws Exception {
        unsortedMap.entrySet().stream()
                .sorted(frequencyComparator)
                .forEach(word -> actualKeyOrderList.add(word.getKey()));
        assertEquals(expectedKeyOrderList, actualKeyOrderList);
    }
}