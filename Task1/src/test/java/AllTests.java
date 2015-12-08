import analyzer.AnalyzerTestSuite;
import introduction.IntroductionTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({AnalyzerTestSuite.class, IntroductionTestSuite.class})
public class AllTests {
}