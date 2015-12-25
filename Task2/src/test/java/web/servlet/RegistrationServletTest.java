package web.servlet;


import db.exceptions.TransactionException;
import dto.UserDTO;
import entity.User;
import nl.captcha.Captcha;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.util.Strings;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import service.UserService;
import service.exceptions.DuplicateInsertException;
import service.exceptions.ServiceException;
import service.exceptions.ValidationException;
import web.exceptions.CaptchaValidationException;
import utils.GoogleReCaptchaValidationUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({Captcha.class})
public class RegistrationServletTest {

    private static final String USER_SERVICE_CONTEXT_ATTRIBUTE = "userService";

    private static final String GOOGLE_RECAPTCHA_UTILS_ATTRIBUTE_NAME = "googleReCaptchaValidationUtils";
    private static final String GOOD_CAPTCHA = "good captcha";
    private static final String WRONG_CAPTCHA = "wrong captcha";

    private static final String REGISTRATION_PAGE_URL = "registration";
    private static final String WELCOME_PAGE_URL = "welcome";

    private static final String TEXT_PLAIN_CONTENT_TYPE = "text/plain";
    private static final String MULTIPART_CONTENT_TYPE = "multipart/form-data";

    private static final String USER_ATTRIBUTE_NAME = "user";
    private static final String USER_DTO_ATTRIBUTE_NAME = "userDTO";
    private static final String CAPTCHA_DUPLICATION_EXCEPTION_ATTRIBUTE_NAME = "captchaDuplicationException";
    private static final String VALIDATION_EXCEPTION_ATTRIBUTE_NAME = "validationException";
    private static final String TRANSACTION_EXCEPTION_ATTRIBUTE_NAME = "transactionException";
    private static final String SERVICE_EXCEPTION_MESSAGE = "Service exception.";
    private static final String IMAGES_FOLDER_CONTEXT_ATTRIBUTE_NAME = "imagesFolderRelativePath";

    private static final String LOCAL_IMAGES_FOLDER_RELATIVE_PATH = "../webapps/test/images/";
    private static final String TEST_DATA_DIRECTORY = "../webapps/test";

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
        testUser = new User(
                "goodsiremail@gmail.com",
                "goodpassword",
                "goodname",
                "goodsurname",
                "test_image_file_name.jpg");
        testUserDTO = new UserDTO(
                "goodsiremail@gmail.com",
                "goodname",
                "goodsurname");
    }

    @Before
    public void setUp() throws Exception {
        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute(USER_SERVICE_CONTEXT_ATTRIBUTE)).thenReturn(userService);
        when(servletContext.getAttribute(GOOGLE_RECAPTCHA_UTILS_ATTRIBUTE_NAME)).thenReturn(googleReCaptchaValidationUtils);
        when(servletContext.getAttribute(IMAGES_FOLDER_CONTEXT_ATTRIBUTE_NAME)).thenReturn(LOCAL_IMAGES_FOLDER_RELATIVE_PATH);

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
        when(part.getInputStream()).thenReturn(new ByteArrayInputStream("hello".getBytes()));
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
        setCorrectInput();
        verify(userService).register(testUser);
        verify(session).setAttribute(USER_ATTRIBUTE_NAME, testUser);
        verify(response).sendRedirect(WELCOME_PAGE_URL);
    }

    @Test
    public void testDoPostOnWrongRequestContentType() throws Exception {
        when(request.getContentType()).thenReturn(TEXT_PLAIN_CONTENT_TYPE);
        servlet.init(servletConfig);
        servlet.doPost(request, response);
        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    public void testDoPostOnFailPasswordConfirmation() throws Exception {
        when(request.getParameter("password")).thenReturn(testUser.getPassword());
        when(request.getParameter("confirmedPassword")).thenReturn(Strings.EMPTY);
        servlet.init(servletConfig);
        servlet.doPost(request, response);

        verifyException(VALIDATION_EXCEPTION_ATTRIBUTE_NAME, ValidationException.class);
        verify(response).sendRedirect(REGISTRATION_PAGE_URL);
    }

    @Test
    public void testDoPostOnDuplicateInsert() throws Exception {
        when(userService.register(testUser)).thenThrow(DuplicateInsertException.class);
        setCorrectInput();
        verifyUserDTOEquality();
        verify(userService).register(testUser);
        verifyException(CAPTCHA_DUPLICATION_EXCEPTION_ATTRIBUTE_NAME ,DuplicateInsertException.class);
        verify(response).sendRedirect(REGISTRATION_PAGE_URL);
    }

    @Test
    public void testDoPostOnBadSimpleCaptcha() throws Exception {
        setUserInputIntoRequest(testUser, testUser.getPassword(), GOOD_CAPTCHA, WRONG_CAPTCHA);
        servlet.init(servletConfig);
        servlet.doPost(request, response);
        verifyException(CAPTCHA_DUPLICATION_EXCEPTION_ATTRIBUTE_NAME, CaptchaValidationException.class);
        verify(response).sendRedirect(REGISTRATION_PAGE_URL);
    }

    @Test
    public void testDoPostOnBadGoogleCaptcha() throws Exception {
        setUserInputIntoRequest(testUser, testUser.getPassword(), WRONG_CAPTCHA, GOOD_CAPTCHA);
        servlet.init(servletConfig);
        servlet.doPost(request, response);
        verifyException(CAPTCHA_DUPLICATION_EXCEPTION_ATTRIBUTE_NAME, CaptchaValidationException.class);
        verify(response).sendRedirect(REGISTRATION_PAGE_URL);
    }

    @Test
    public void testDoPostOnBadValidationCredentials() throws Exception {
        when(userService.register(testUser)).thenThrow(ValidationException.class);
        setCorrectInput();
        verifyUserDTOEquality();
        verify(userService).register(testUser);
        verifyException(VALIDATION_EXCEPTION_ATTRIBUTE_NAME ,ValidationException.class);
        verify(response).sendRedirect(REGISTRATION_PAGE_URL);
    }

    @Test
    public void testDoPostOnTransactionalExceptionThrow() throws Exception {
        when(userService.register(testUser)).thenThrow(TransactionException.class);
        setCorrectInput();
        verify(userService).register(testUser);
        verifyException(TRANSACTION_EXCEPTION_ATTRIBUTE_NAME, TransactionException.class);
        verify(response).sendRedirect(REGISTRATION_PAGE_URL);
    }

    @Test
    public void testDoPostOnServiceExceptionThrow() throws Exception {
        when(userService.register(testUser)).thenThrow(ServiceException.class);
        setCorrectInput();
        verify(userService).register(testUser);
        verify(response).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, SERVICE_EXCEPTION_MESSAGE);
    }


    @AfterClass
    public static void afterClassDestroy() throws IOException {
        testUser = null;
        testUserDTO = null;

        File logsDirectory = new File(TEST_DATA_DIRECTORY);
        FileUtils.deleteDirectory(logsDirectory);
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

    private void verifyUserDTOEquality() {
        ArgumentCaptor<UserDTO> userDTOCaptor = ArgumentCaptor.forClass(UserDTO.class);
        verify(session).setAttribute(eq(USER_DTO_ATTRIBUTE_NAME), userDTOCaptor.capture());
        assertEquals(userDTOCaptor.getValue(), testUserDTO);
    }

    private void verifyException(String attributeName, Class exceptionClass) {
        ArgumentCaptor<Throwable> exceptionCaptor = ArgumentCaptor.forClass(Throwable.class);
        verify(session).setAttribute(eq(attributeName), exceptionCaptor.capture());
        assertEquals(exceptionCaptor.getValue().getClass(), exceptionClass);
    }
}