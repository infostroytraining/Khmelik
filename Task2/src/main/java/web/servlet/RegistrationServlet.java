package web.servlet;

import db.exceptions.TransactionException;
import dto.UserDTO;
import entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.exceptions.ServiceException;
import web.exceptions.CaptchaValidationException;
import service.exceptions.FieldError;
import service.exceptions.ValidationException;
import service.exceptions.DuplicateInsertException;
import nl.captcha.Captcha;
import service.UserService;
import service.validators.UserField;
import web.captcha.GoogleReCaptchaValidationUtils;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class RegistrationServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger();
    private static final String USER_DTO_ATTRIBUTE_NAME = "userDTO";
    private static final String REGISTRATION_PAGE = "registration";

    private UserService userService;
    private GoogleReCaptchaValidationUtils googleReCaptchaValidationUtils;
    private String imagesFolderRelativePath;

    @Override
    public void init() throws ServletException {
        userService = (UserService) getServletContext().getAttribute("userService");
        googleReCaptchaValidationUtils =
                (GoogleReCaptchaValidationUtils) getServletContext().getAttribute("googleReCaptchaValidationUtils");
        imagesFolderRelativePath = (String) getServletContext().getAttribute("imagesFolderRelativePath");
        if (userService == null || googleReCaptchaValidationUtils == null) {
            logger.fatal("Could not initialize servlet from application context. " +
                    "UserService/googleRecapthcaUtils equal null).");
            throw new UnavailableException(
                    "Could not get user service or google captcha validator.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("registrationJSP").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isMultipartFormat(req)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        req.setCharacterEncoding("UTF-8");

        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirmedPassword = req.getParameter("confirmedPassword");
        String name = req.getParameter("name");
        String surname = req.getParameter("surname");
        Part imagePart = req.getPart("image");
        String gRecaptchaResponse = req.getParameter("g-recaptcha-response");
        String simpleCaptchaAnswer = req.getParameter("simpleCaptchaAnswer");

        UserDTO userDTO = new UserDTO(email, name, surname);
        HttpSession session = req.getSession();

        try {
            validatePasswords(password, confirmedPassword);
            validateGoogleCaptcha(gRecaptchaResponse);
            validateSimpleCaptcha(simpleCaptchaAnswer, session);

            User user = new User(email, password, name, surname, imagePart.getSubmittedFileName());
            User registeredUser = userService.register(user);
            session.setAttribute("user", registeredUser);
            if (registeredUser.getImage() != null) saveImage(imagePart, registeredUser.getImage());

            logger.info("User {} has ben successfully registered", user.getEmail());
            resp.sendRedirect("welcome");
        } catch (CaptchaValidationException | DuplicateInsertException e) {
            session.setAttribute(USER_DTO_ATTRIBUTE_NAME, userDTO);
            session.setAttribute("captchaDuplicationException", e);
            resp.sendRedirect(REGISTRATION_PAGE);
        } catch (ValidationException e) {
            session.setAttribute(USER_DTO_ATTRIBUTE_NAME, userDTO);
            session.setAttribute("validationException", e);
            resp.sendRedirect(REGISTRATION_PAGE);
        } catch (TransactionException e) {
            session.setAttribute("transactionException", e);
            resp.sendRedirect(REGISTRATION_PAGE);
        } catch (ServiceException e) {
            resp.sendError(500, "Service exception.");
        }
    }

    private boolean isMultipartFormat(HttpServletRequest req) {
        String contentType = req.getContentType();
        return contentType.contains("multipart/form-data");
    }

    private void validatePasswords(String password, String confirmedPassword) throws ValidationException {
        if (!password.equals(confirmedPassword)) {
            List<FieldError> errors = new ArrayList<>();
            errors.add(new FieldError(UserField.PASSWORD));
            throw new ValidationException(new ArrayList<>(errors));
        }
    }

    private void validateGoogleCaptcha(String gRecaptchaResponse) throws CaptchaValidationException, IOException {
        if (!googleReCaptchaValidationUtils.validate(gRecaptchaResponse)) {
            throw new CaptchaValidationException("Google reCAPTCHA");
        }
    }

    private void validateSimpleCaptcha(String simpleCaptchaAnswer, HttpSession session) throws CaptchaValidationException {
        Captcha captcha = (Captcha) session.getAttribute(Captcha.NAME);
        if (!captcha.isCorrect(simpleCaptchaAnswer)) {
            throw new CaptchaValidationException("SimpleCaptcha");
        }
    }

    private void saveImage(Part imagePart, String filePath) {
        try (InputStream imageInputStream = imagePart.getInputStream()) {
            File imageFile = new File(imagesFolderRelativePath + filePath);
            if (!imageFile.exists()) {
                if (imageFile.getParentFile().mkdirs()) logger.debug("Image storage directory created.");
                if (imageFile.createNewFile()) logger.debug("Image file created on path " + filePath);
            }
            Files.copy(imageInputStream, imageFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            Thread.sleep(500);  //Time for image to copy (otherwise it won't be displayed when redirecting).
        } catch (IOException e) {
            logger.error("Can not create/write into image file. Image saving exception."
                    + e.getLocalizedMessage());
        } catch (InterruptedException e) {
            logger.error("Interrupted exception on waiting until file will load on server.");
        }
    }
}