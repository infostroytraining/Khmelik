package analyzer.commands;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({FindTwoMostFrequentWordsCommandTest.class,
        FindFirstThreeLongestCommandTest.class, FindFirstThreeDuplicatesCommandTest.class})
public class CommandsTestSuite {
}
