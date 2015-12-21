package web.servlet;

import db.exceptions.TransactionException;
import dto.UserDTO;
import entity.User;
import nl.captcha.Captcha;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import service.UserService;
import service.exceptions.DuplicateInsertException;
import service.exceptions.ServiceException;
import service.exceptions.ValidationException;
import web.captcha.GoogleReCaptchaValidationUtils;
import web.exceptions.CaptchaValidationException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Captcha.class})
public class RegistrationServletTest {

    private static final String USER_SERVICE_CONTEXT_ATTRIBUTE = "userService";
    private static final String GOOGLE_RECAPTCHA_UTILS_ATTRIBUTE_NAME = "googleReCaptchaValidationUtils";
    private static final String GOOD_CAPTCHA = "good captcha";
    private static final String WRONG_CAPTCHA = "wrong captcha";
    private static final String REGISTRATION_PAGE_URL = "registration";
    public static final String TEXT_PLAIN_CONTENT_TYPE = "text/plain";
    public static final String MULTIPART_CONTENT_TYPE = "multipart/form-data";
    public static final String CAPTCHA_DUPLICATION_EXCEPTION_ATTRIBUTE_NAME = "captchaDuplicationException";
    public static final String USER_DTO_ATTRIBUTE_NAME = "userDTO";

    private RegistrationServlet servlet = new RegistrationServlet();

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
    private GoogleReCaptchaValidationUtils googleReCaptchaValidationUtils;
    @Mock
    private Part part;

    private Captcha simpleCaptcha = PowerMockito.mock(Captcha.class);

    private static User testUser;
    private static UserDTO testUserDTO;

    @BeforeClass
    public static void beforeClassInit() {
        testUser = new User("goodsiremail@gmail.com",
                "goodpassword",
                "goodname",
                "goodsurname",
                "test_image_file_name.jpg");
        testUserDTO = new UserDTO("goodsiremail@gmail.com", "goodname",
                "goodsurname");
    }

    @Before
    public void setUp() throws Exception, ServiceException {
        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute(USER_SERVICE_CONTEXT_ATTRIBUTE)).thenReturn(userService);
        when(servletContext.getAttribute(GOOGLE_RECAPTCHA_UTILS_ATTRIBUTE_NAME)).thenReturn(googleReCaptchaValidationUtils);

        when(request.getServletContext()).thenReturn(servletContext);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getContentType()).thenReturn(MULTIPART_CONTENT_TYPE);

        when(googleReCaptchaValidationUtils.validate(GOOD_CAPTCHA)).thenReturn(true);
        when(googleReCaptchaValidationUtils.validate(WRONG_CAPTCHA)).thenReturn(false);
        when(session.getAttribute(Captcha.NAME)).thenReturn(simpleCaptcha);

        when(simpleCaptcha.getAnswer()).thenReturn(GOOD_CAPTCHA);
        when(simpleCaptcha.isCorrect(anyString())).thenReturn(false);
        when(simpleCaptcha.isCorrect(GOOD_CAPTCHA)).thenReturn(true);

        when(userService.register(testUser)).thenReturn(testUser);
        when(part.getSubmittedFileName()).thenReturn("test_image_file_name.jpg");
        when(part.getInputStream()).thenReturn(null);
    }

    @Test
    public void testInitWithFullParameters() throws Exception {
        servlet.init(servletConfig);
    }

    @Test(expected = UnavailableException.class)
    public void testInitWithoutService() throws Exception {
        when(servletContext.getAttribute(USER_SERVICE_CONTEXT_ATTRIBUTE)).thenReturn(null);
        servlet.init(servletConfig);
    }

    @Test(expected = UnavailableException.class)
    public void testInitWithoutGoogleCaptchaUtils() throws ServletException {
        when(servletContext.getAttribute(GOOGLE_RECAPTCHA_UTILS_ATTRIBUTE_NAME)).thenReturn(null);
        servlet.init(servletConfig);
    }

    @Test
    public void testDoGet() throws Exception {
        servlet.init(servletConfig);
        servlet.doGet(request, response);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPostOnCorrectData() throws Exception {
        //TODO correct insertion (deal with photo somehow)
    }

    @Test
    public void testDoPostOnWrongRequestContentType() throws Exception {
        when(request.getContentType()).thenReturn(TEXT_PLAIN_CONTENT_TYPE);
        servlet.init(servletConfig);
        servlet.doPost(request, response);
        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    public void testDoPostOnDuplicateInsert() throws Exception, ServiceException {
        when(userService.register(testUser)).thenThrow(DuplicateInsertException.class);
        setUserInputIntoRequest(testUser, testUser.getPassword(), GOOD_CAPTCHA, GOOD_CAPTCHA);
        servlet.init(servletConfig);
        servlet.doPost(request, response);

        ArgumentCaptor<UserDTO> userDTOCaptor = ArgumentCaptor.forClass(UserDTO.class);
        verify(session).setAttribute(eq(USER_DTO_ATTRIBUTE_NAME), userDTOCaptor.capture());
        assertEquals(userDTOCaptor.getValue(), testUserDTO);

        ArgumentCaptor<Throwable> exceptionCaptor = ArgumentCaptor.forClass(Throwable.class);
        verify(session).setAttribute(eq(CAPTCHA_DUPLICATION_EXCEPTION_ATTRIBUTE_NAME),
                exceptionCaptor.capture());
        assertTrue(exceptionCaptor.getValue() instanceof DuplicateInsertException);
        verify(response).sendRedirect(REGISTRATION_PAGE_URL);
    }

    @Test
    public void testDoPostOnBadSimpleCaptcha() throws Exception {
        setUserInputIntoRequest(testUser, testUser.getPassword(), GOOD_CAPTCHA, WRONG_CAPTCHA);
        servlet.init(servletConfig);
        servlet.doPost(request, response);
        ArgumentCaptor<Throwable> exceptionCaptor = ArgumentCaptor.forClass(Throwable.class);
        verify(session).setAttribute(eq(CAPTCHA_DUPLICATION_EXCEPTION_ATTRIBUTE_NAME),
                exceptionCaptor.capture());
        assertTrue(exceptionCaptor.getValue() instanceof CaptchaValidationException);
        verify(response).sendRedirect(REGISTRATION_PAGE_URL);
    }

    @Test
    public void testDoPostOnBadGoogleCaptcha() throws Exception {
        setUserInputIntoRequest(testUser, testUser.getPassword(), WRONG_CAPTCHA, GOOD_CAPTCHA);
        servlet.init(servletConfig);
        servlet.doPost(request, response);
        ArgumentCaptor<Throwable> exceptionCaptor = ArgumentCaptor.forClass(Throwable.class);
        verify(session).setAttribute(eq(CAPTCHA_DUPLICATION_EXCEPTION_ATTRIBUTE_NAME),
                exceptionCaptor.capture());
        assertTrue(exceptionCaptor.getValue() instanceof CaptchaValidationException);
        verify(response).sendRedirect(REGISTRATION_PAGE_URL);
    }

    @Test
    public void testDoPostOnBadValidationCredentials() throws Exception, ServiceException {
        when(userService.register(testUser)).thenThrow(ValidationException.class);
        setCorrectInput();
        //TODO check redirects and data
    }

    @Test
    public void testDoPostOnTransactionalExceptionThrow() throws Exception, ServiceException {
        when(userService.register(testUser)).thenThrow(TransactionException.class);
        setCorrectInput();
        //TODO check redirects and data
    }

    @Test
    public void testDoPostOnServiceExceptionThrow() throws Exception, ServiceException {
        when(userService.register(testUser)).thenThrow(TransactionException.class);
        setCorrectInput();
        //TODO check redirects and data
    }


    @AfterClass
    public static void afterClassDestroy(){
        //TODO remove all created photos with folder and set null user objects.
    }


    private void setUserInputIntoRequest(User user, String confirmedPassword, String googleCaptchaAnswer,
                                         String simpleCaptchaAnswer) throws IOException, ServletException {
        when(request.getParameter("email")).thenReturn(user.getEmail());
        when(request.getParameter("password")).thenReturn(user.getPassword());
        when(request.getParameter("confirmedPassword")).thenReturn(confirmedPassword);
        when(request.getParameter("name")).thenReturn(user.getName());
        when(request.getParameter("surname")).thenReturn(user.getSurname());
        when(request.getPart("image")).thenReturn(part);
        when(request.getParameter("g-recaptcha-response")).thenReturn(googleCaptchaAnswer);
        when(request.getParameter("simpleCaptchaAnswer")).thenReturn(simpleCaptchaAnswer);
    }

    private void setCorrectInput() throws IOException, ServletException {
        setUserInputIntoRequest(testUser, testUser.getPassword(), GOOD_CAPTCHA, GOOD_CAPTCHA);
        servlet.init(servletConfig);
        servlet.doPost(request, response);
    }
}