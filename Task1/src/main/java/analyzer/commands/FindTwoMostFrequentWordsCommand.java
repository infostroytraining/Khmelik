package analyzer.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FindTwoMostFrequentWordsCommand implements AnalyzerCommand {

    public static final String NO_WORDS_MESSAGE = "No words found.";

    @Override
    public String execute(String filePath, boolean parallel) throws IOException {
        StringBuilder result = new StringBuilder();
        Stream<String> linesStream = Files.lines(Paths.get(filePath));
        if(parallel) linesStream.parallel();
        linesStream
                .map(line -> line.toLowerCase().split(WORD_DIVIDER_REGEXP))
                .flatMap(Arrays::stream)
                .filter(mapItem -> !mapItem.isEmpty())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .sorted((word1, word2) -> (int) (word2.getValue() - word1.getValue()))
                .limit(2)
                .sorted((word1, word2) -> - word1.getKey().compareTo(word2.getKey()))
                .forEach(wordFrequencyPair -> appendWordToBuilder(wordFrequencyPair, result));
        return (result.length() != 0) ? result.deleteCharAt(result.length() - 1).toString() : NO_WORDS_MESSAGE;
    }

    private StringBuilder appendWordToBuilder(Map.Entry<String, Long> wordFrequencyPair, StringBuilder builder){
        builder.append(wordFrequencyPair.getKey()).append(" -> ")
                .append(wordFrequencyPair.getValue()).append('\n');
        return builder;
    }
}