package analyzer;

import analyzer.commands.CommandsTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({CommandsTestSuite.class, TaskTest.class,
        TasksConverterTest.class, AnalyzerTest.class})
public class AnalyzerTestSuite {
}
