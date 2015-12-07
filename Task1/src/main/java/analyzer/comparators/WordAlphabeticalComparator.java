package analyzer.comparators;

import java.util.Comparator;
import java.util.Map;

public class WordAlphabeticalComparator implements Comparator<Map.Entry<String, Long>> {

    @Override
    public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {
        return o1.getKey().compareTo(o2.getKey());
    }
}
