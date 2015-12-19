package analyzer.commands;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class FindFirstThreeDuplicatesCommand implements AnalyzerCommand {

    public static final String NO_DUPLICATES_MESSAGE = "No duplicates found.";

    @Override
    public String execute(String filePath, boolean parallel) throws IOException {
        StringBuilder result = new StringBuilder();
        Set<String> words = new HashSet<>();
        Set<String> duplicates = new TreeSet<>((s1, s2) -> s2.length()-s1.length());
        Stream<String> stream = Files.lines(Paths.get(filePath));
        stream.map(line -> line.toLowerCase().split(WORD_DIVIDER_REGEXP))
                .flatMap(Arrays::stream)
                .filter(mapItem -> !mapItem.isEmpty())
                .anyMatch(word -> isDuplicatedAdded(word, words, duplicates) && (duplicates.size() == 3));
        duplicates.stream().forEach(word -> result.append(word).append('\n'));
        return (result.length() != 0) ? result.reverse().deleteCharAt(0).toString() : NO_DUPLICATES_MESSAGE;
    }

    private boolean isDuplicatedAdded(String word, Set<String> words, Set<String> duplicates) {
        if (words.contains(word) && !duplicates.contains(word)) {
            duplicates.add(word.toUpperCase());
            return true;
        } else {
            words.add(word);
            return false;
        }
    }
}
