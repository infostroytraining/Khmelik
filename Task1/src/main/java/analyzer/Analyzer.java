package analyzer;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

import java.io.IOException;

public class Analyzer {

    @Parameter(names = {"-i", "--input"}, required = true, description = "Required. Usage: -i||--input [parsing file full name]")
    private String textFilePath;

    @Parameter(names = {"-t", "--task"}, required = true, description = "Required. Usage: -t||-task [task name]", converter = TasksConverter.class)
    private Task task;

    @Parameter(names = "--help", help = true, description = "Prints the description of all available commands.")
    private boolean help;

    public static void main(String[] args) {
        Analyzer analyzer = new Analyzer();
        JCommander cmd = new JCommander(analyzer);
        try {
            cmd.parse(args);
            if (analyzer.help) {
                cmd.usage();
            }
            long start = System.currentTimeMillis();
            System.out.println(analyzer.task.getCommand().execute(analyzer.textFilePath));
            long end = System.currentTimeMillis() - start;
            System.out.println("Estimated time: " + end + "ms.");
        } catch (ParameterException | IOException ex) {
            System.out.println(ex.getMessage());
            cmd.usage();
        }
    }
}
