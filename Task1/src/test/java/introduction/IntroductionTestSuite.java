package introduction;

import introduction.MathUtilsTest;
import introduction.TextUtilsTest;
import introduction.TriangleUtilsTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses( {MathUtilsTest.class, TextUtilsTest.class, TriangleUtilsTest.class} )
public class IntroductionTestSuite {
}
