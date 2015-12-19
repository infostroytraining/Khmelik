package analyzer.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FindFirstThreeLongestCommand implements AnalyzerCommand {

    public static final String NO_WORDS_MESSAGE = "No words found.";

    @Override
    public String execute(String filePath, boolean parallel) throws IOException {
        StringBuilder result = new StringBuilder();
        Stream<String> linesStream = Files.lines(Paths.get(filePath));
        if (parallel) linesStream = linesStream.parallel();
        linesStream.map(line -> line.toLowerCase().split(WORD_DIVIDER_REGEXP))
                .flatMap(Arrays::stream)
                .filter(mapItem -> !mapItem.isEmpty())
                .collect(Collectors.toList())
                .stream()
                .distinct()
                .sorted((s1, s2) -> s2.length() - s1.length())
                .limit(3)
                .forEach(word -> result.append(word).append(" -> ").append(word.length()).append('\n'));
        return (result.length() != 0) ? result.toString() : NO_WORDS_MESSAGE;
    }
}