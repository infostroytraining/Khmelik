package analyzer.commands;

import analyzer.comparators.WordAlphabeticalComparator;
import analyzer.comparators.WordFrequencyComparator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FindTwoMostFrequentWordsCommand implements AnalyzerCommand {

    public static final String NO_WORDS_MESSAGE = "No words found.";

    @Override
    public String execute(String filePath) throws IOException {
        StringBuilder result = new StringBuilder();
        Files.lines(Paths.get(filePath))
                .map(line -> line.toLowerCase().split(WORD_DIVIDER_REGEXP))
                .flatMap(Arrays::stream)
                .filter(mapItem -> !mapItem.isEmpty())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(new WordFrequencyComparator().reversed())
                .limit(2)
                .sorted(new WordAlphabeticalComparator().reversed())
                .forEach(wordFrequencyPair -> appendWordToBuilder(wordFrequencyPair, result));
        return (result.length() != 0) ? result.deleteCharAt(result.length() - 1).toString() : NO_WORDS_MESSAGE;
    }

    private StringBuilder appendWordToBuilder(Map.Entry<String, Long> wordFrequencyPair, StringBuilder builder){
        builder.append(wordFrequencyPair.getKey()).append(" -> ")
                .append(wordFrequencyPair.getValue()).append('\n');
        return builder;
    }
}