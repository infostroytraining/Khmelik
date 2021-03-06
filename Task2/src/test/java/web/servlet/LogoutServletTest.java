package web.servlet;

import entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LogoutServletTest {

    private static final String USER_ATTRIBUTE_NAME = "user";
    private static final String START_PAGE_URL = "welcomeJSP";
    private LogoutServlet logoutServlet = new LogoutServlet();

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    @Mock
    private User loggedUser;

    @Before
    public void setUp() throws Exception {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(USER_ATTRIBUTE_NAME)).thenReturn(loggedUser);
        when(loggedUser.getEmail()).thenReturn("logged_user_email");
    }

    @Test
    public void testDoGet() throws Exception {
        logoutServlet.doGet(request, response);
        verify(session).invalidate();
        verify(response).sendRedirect(request.getContextPath() + '/');
    }
}