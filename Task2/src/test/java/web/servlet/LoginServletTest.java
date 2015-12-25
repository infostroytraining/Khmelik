package web.servlet;

import com.google.gson.Gson;
import db.exceptions.TransactionException;
import entity.User;
import org.apache.logging.log4j.util.Strings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import service.UserService;
import service.exceptions.ValidationException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.PrintWriter;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LoginServletTest {

    private static final String USER_SERVICE_CONTEXT_ATTRIBUTE = "userService";
    private static final String EMAIL_ATTRIBUTE_NAME = "email";
    private static final String PASSWORD_ATTRIBUTE_NAME = "password";
    private static final String TEST_EMAIL = "test_email@gmail.com";
    private static final String TEST_PASSWORD = "test_password";
    private static final String WRONG_EMAIL = "wrong_email@gmail.com";
    private static final String USER_ATTRIBUTE = "user";
    private static final Gson GSON = new Gson();

    private LoginServlet servlet = new LoginServlet();

    @Mock
    private ServletConfig servletConfig;
    @Mock
    private ServletContext servletContext;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private UserService userService;
    @Mock
    private PrintWriter responseWriter;

    private User testUser;

    @Before
    public void setUp() throws Exception {
        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute(USER_SERVICE_CONTEXT_ATTRIBUTE)).thenReturn(userService);

        when(request.getServletContext()).thenReturn(servletContext);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
        when(response.getWriter()).thenReturn(responseWriter);

        testUser = new User();
        testUser.setEmail(TEST_EMAIL);
        testUser.setPassword(TEST_PASSWORD);

        when(userService.loadUserByUsername(TEST_EMAIL)).thenReturn(testUser);
        when(userService.loadUserByUsername(WRONG_EMAIL)).thenReturn(null);
    }

    @Test
    public void testServletInit() throws Exception {
        servlet.init(servletConfig);
    }

    @Test(expected = UnavailableException.class)
    public void testServletInitFail() throws Exception {
        when(servletContext.getAttribute(USER_SERVICE_CONTEXT_ATTRIBUTE)).thenReturn(null);
        servlet.init(servletConfig);
    }

    @Test
    public void testDoGet() throws Exception {
        servlet.doGet(request, response);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPostOnCorrectCredentials() throws Exception {
        servlet.init(servletConfig);
        mockGetRequestParams(TEST_EMAIL, TEST_PASSWORD);
        servlet.doPost(request, response);

        verify(session).setAttribute(USER_ATTRIBUTE, testUser);
        verifyJsonAnswer(HttpServletResponse.SC_OK, testUser);
    }

    @Test
    public void testDoPostOnWrongCredentials() throws Exception {
        servlet.init(servletConfig);
        mockGetRequestParams(WRONG_EMAIL, Strings.EMPTY);
        servlet.doPost(request, response);
        String loginError = "Wrong credentials. Please try again.";
        verifyJsonAnswer(HttpServletResponse.SC_BAD_REQUEST, loginError);
    }

    @Test
    public void testDoPostTransactionException() throws Exception {
        servlet.init(servletConfig);
        when(userService.loadUserByUsername(anyString())).thenThrow(TransactionException.class);
        servlet.doPost(request, response);
        verify(response).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    public void testDoPostValidationException() throws Exception {
        servlet.init(servletConfig);
        when(userService.loadUserByUsername(anyString())).thenThrow(ValidationException.class);
        servlet.doPost(request, response);
        verify(response).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    private void mockGetRequestParams(String email, String password) {
        when(request.getParameter(EMAIL_ATTRIBUTE_NAME)).thenReturn(email);
        when(request.getParameter(PASSWORD_ATTRIBUTE_NAME)).thenReturn(password);
    }

    private void verifyJsonAnswer(int httpResponseCode, Object toJsonObject) {
        verify(response).setStatus(httpResponseCode);
        verify(response).setHeader("Content-Type", "application/json");
        verify(responseWriter).write(GSON.toJson(toJsonObject));
    }
}
