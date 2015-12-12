package web.servlet;

import dto.UserDTO;
import entity.User;
import exceptions.CaptchaValidationException;
import exceptions.FieldError;
import exceptions.ValidationException;
import exceptions.DuplicateInsertException;
import nl.captcha.Captcha;
import org.apache.log4j.Logger;
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

    private static final Logger LOGGER = Logger.getLogger(RegistrationServlet.class);

    //TODO make this property field
    private static final String TOMCAT_WEBAPPS_FOLDER_RELATIVE_PATH = "../webapps";

    private UserService userService;
    private GoogleReCaptchaValidationUtils googleReCaptchaValidationUtils;

    @Override
    public void init() throws ServletException {
        userService = (UserService) getServletContext().getAttribute("userService");
        googleReCaptchaValidationUtils =
                (GoogleReCaptchaValidationUtils) getServletContext().getAttribute("googleReCaptchaValidationUtils");

        if (userService == null || googleReCaptchaValidationUtils == null) {
            LOGGER.error("Could not initialize servlet from application context");
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
            resp.sendError(400);
        }

        String email = req.getParameter("email");
        String name = req.getParameter("name");
        String surname = req.getParameter("surname");
        String password = req.getParameter("password");
        String confirmedPassword = req.getParameter("confirmedPassword");
        Part imagePart = req.getPart("image");
        String gRecaptchaResponse = req.getParameter("g-recaptcha-response");
        String simpleCaptchaAnswer = req.getParameter("simpleCaptchaAnswer");

        UserDTO userDTO = new UserDTO(email, name, surname);
        HttpSession session = req.getSession();

        try {
            validatePasswords(password, confirmedPassword);
            validateGoogleCaptcha(gRecaptchaResponse);
            validateSimpleCaptcha(simpleCaptchaAnswer, session);

            User user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setSurname(surname);
            user.setPassword(password);
            if (imagePart != null) user.setImage(imagePart.getSubmittedFileName());

            User registeredUser = userService.register(user);

            session.setAttribute("user", registeredUser);
            if (imagePart != null) saveImage(imagePart, registeredUser.getImage());

            LOGGER.debug("User " + registeredUser.getEmail() + "has just successfully registered.");
            resp.sendRedirect("welcome");
        } catch (CaptchaValidationException e) {
            session.setAttribute("userDTO", userDTO);
            session.setAttribute("captchaValidationException", e);
            resp.sendRedirect("registration");
        } catch (ValidationException e) {
            session.setAttribute("userDTO", userDTO);
            session.setAttribute("fieldExceptions", e.getFieldExceptions());
            resp.sendRedirect("registration");
        } catch (DuplicateInsertException e) {
            session.setAttribute("userDTO", userDTO);
            session.setAttribute("userAlreadyExistsException", e);
            resp.sendRedirect("registration");
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
            File imageFile = new File(TOMCAT_WEBAPPS_FOLDER_RELATIVE_PATH + filePath);
            if (!imageFile.exists()) {
                if (imageFile.getParentFile().mkdirs()) LOGGER.debug("Image storage directory created.");
                if (imageFile.createNewFile()) LOGGER.debug("Image file created on path " + filePath);
            }
            Files.copy(imageInputStream, imageFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            Thread.sleep(500);  //Time for image to copy (otherwise it won't be displayed when redirecting).
        } catch (IOException e) {
            LOGGER.error("Can not create/write into image file. Image saving exception."
                    + e.getLocalizedMessage());
        } catch (InterruptedException e) {
            LOGGER.error("Interrupted exception on waiting until file will load on server.");
        }
    }
}