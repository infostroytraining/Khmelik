package web.servlet;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({LoginServletTest.class, LogoutServletTest.class, RegistrationServletTest.class})
public class ServletTestSuite {
}
