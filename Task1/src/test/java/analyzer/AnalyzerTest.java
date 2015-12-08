package analyzer;

import org.junit.Test;

public class AnalyzerTest {

    private static final String[] ANALYZER_CORRECT_INPUT = {
            "-i", "\"src\\main\\resources\\test_input.txt\"",
            "-t", "frequency"};
    private static final String[] ANALYZER_CORRECT_INPUT_WITH_HELP = {
            "-i", "\"src\\main\\resources\\test_input.txt\"",
            "-t", "frequency", "--help"};
    private static final String[] ANALYZER_NO_PARAMETER_INPUT = {};
    private static final String[] ANALYZER_NO_FILE_INPUT = {
            "-i", "\"src\\main\\resources\\no_file_input.txt\"",
            "-t", "frequency", "--help"};

    Analyzer analyzer = new Analyzer();

    @Test
    public void testMainCorrectInputParameters() throws Exception {
        analyzer.main(ANALYZER_CORRECT_INPUT);
    }

    @Test
    public void testMainCorrectFilePath() throws Exception {
        analyzer.main(ANALYZER_CORRECT_INPUT_WITH_HELP);
    }

    @Test
    public void testMainNoFileInputParameters() throws Exception {
        analyzer.main(ANALYZER_NO_FILE_INPUT);
    }

    @Test
    public void testMainIncorrectInputParameters() throws Exception {
        analyzer.main(ANALYZER_NO_PARAMETER_INPUT);
    }
}