package analyzer;

import analyzer.commands.CommandsTestSuite;
import analyzer.comparators.ComparatorsTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({CommandsTestSuite.class, ComparatorsTestSuite.class,
        TaskTest.class, TasksConverterTest.class, AnalyzerTest.class})
public class AnalyzerTestSuite {
}
