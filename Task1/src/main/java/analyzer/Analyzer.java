package analyzer;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

import java.io.IOException;

public class Analyzer {

    private static final String EXCEPTION_MESSAGE = "\nFor detail operation descriptions use --help command";

    @Parameter(names = {"-i", "--input"}, required = true, description = "Required. Usage: -i||--input [parsing file full name]")
    private String textFilePath;

    @Parameter(names = {"-t", "--task"}, required = true, description = "Required. Usage: -t||-task [task name]", converter = TasksConverter.class)
    private Task task;

    @Parameter(names = "--help", help = true, description = "Prints the description of all available commands.")
    private boolean help;

    public static void main(String[] args) {
        Analyzer analyzer = new Analyzer();
        JCommander cmd = new JCommander(analyzer);
        String analyzerResult;
        long start = System.currentTimeMillis();
        try {
            cmd.parse(args);
            if (analyzer.help) cmd.usage();
            analyzerResult = executeTask(analyzer);
        } catch (ParameterException | IOException ex) {
            analyzerResult = (ex.toString() + EXCEPTION_MESSAGE);
        }
        long end = System.currentTimeMillis() - start;
        System.out.println(analyzerResult);
        System.out.println("elapsed time: " + end + " millis");
    }

    private static String executeTask(Analyzer analyzer) throws IOException {
        return analyzer.task.getCommand().execute(analyzer.textFilePath);
    }
}
