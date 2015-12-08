package analyzer.commands;

import analyzer.comparators.StringLengthComparator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

public class FindFirstThreeLongestCommand implements AnalyzerCommand {

    public static final String NO_WORDS_MESSAGE = "No words found.";

    @Override
    public String execute(String filePath) throws IOException {
        StringBuilder result = new StringBuilder();
        Files.lines(Paths.get(filePath))
                .map(line -> line.toLowerCase().split(WORD_DIVIDER_REGEXP))
                .flatMap(Arrays::stream)
                .filter(mapItem -> !mapItem.isEmpty())
                .collect(Collectors.toList())
                .stream()
                .distinct()
                .sorted(new StringLengthComparator().reversed())
                .limit(3)
                .forEach(word -> result.append(word).append(" -> ").append(word.length()).append('\n'));
        return (result.length() != 0) ? result.toString() : NO_WORDS_MESSAGE;
    }
}