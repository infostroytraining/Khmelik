package analyzer.commands;

import analyzer.comparators.StringLengthComparator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class FindFirstThreeDuplicatesCommand implements AnalyzerCommand {

    public static final String NO_DUPLICATES_MESSAGE = "No duplicates found.";

    @Override
    public String execute(String filePath) throws IOException {
        StringBuilder result = new StringBuilder();
        Set<String> words = new HashSet<>();
        Set<String> duplicated = new TreeSet<>(new StringLengthComparator().reversed());
        Files.lines(Paths.get(filePath))
                .map(line -> line.toLowerCase().split(WORD_DIVIDER_REGEXP))
                .flatMap(Arrays::stream)
                .filter(mapItem -> !mapItem.isEmpty())
                .anyMatch(word -> {
                    if (words.contains(word) && !duplicated.contains(word)) {
                        duplicated.add(word.toUpperCase());
                        return duplicated.size() == 3;
                    } else {
                        words.add(word);
                        return false;
                    }
                });
        duplicated.stream().forEach(word -> result.append(word).append('\n'));
        return (result.length() != 0) ? result.reverse().deleteCharAt(0).toString() : NO_DUPLICATES_MESSAGE;
    }
}
