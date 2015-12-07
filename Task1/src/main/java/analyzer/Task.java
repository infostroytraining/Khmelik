package analyzer;

import analyzer.commands.AnalyzerCommand;
import analyzer.commands.FindFirstThreeDuplicatesCommand;
import analyzer.commands.FindFirstThreeLongestCommand;
import analyzer.commands.FindTwoMostFrequentWordsCommand;

public enum Task {

    FREQUENCY(new FindTwoMostFrequentWordsCommand()),
    LENGTH(new FindFirstThreeLongestCommand()),
    DUPLICATES(new FindFirstThreeDuplicatesCommand());

    private AnalyzerCommand command;

    private Task(AnalyzerCommand command) {
        this.command = command;
    }

    public AnalyzerCommand getCommand() {
        return command;
    }

    public static Task fromString(String value) {
        for (Task task : Task.values()) {
            if (task.toString().equalsIgnoreCase(value)) {
                return task;
            }
        }
        return null;
    }
}
