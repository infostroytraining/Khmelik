package web.servlet;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import db.exceptions.TransactionException;
import entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.exceptions.ServiceException;
import utils.ImageSavingUtils;
import web.exceptions.CaptchaValidationException;
import service.exceptions.FieldError;
import service.exceptions.ValidationException;
import nl.captcha.Captcha;
import service.UserService;
import service.validators.UserField;
import utils.GoogleReCaptchaValidationUtils;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RegistrationServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger();
    private static final Gson GSON = new Gson();

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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isMultipartFormat(req)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Content type is not multiformat.");
            return;
        }
        req.setCharacterEncoding("UTF-8");

        String email = req.getParameter("email");
        String password = req.getParameter("registrationPassword");
        String confirmedPassword = req.getParameter("registrationConfirmed");
        String name = req.getParameter("name");
        String surname = req.getParameter("surname");
        Part imagePart = req.getPart("image");
        String gRecaptchaResponse = req.getParameter("g-recaptcha-response");
        String simpleCaptchaAnswer = req.getParameter("simpleCaptchaAnswer");

        HttpSession session = req.getSession();

        try {
            validatePasswords(password, confirmedPassword);
            validateGoogleCaptcha(gRecaptchaResponse);
            validateSimpleCaptcha(simpleCaptchaAnswer, session);

            User user = new User(email, password, name, surname, imagePart.getSubmittedFileName());
            User registeredUser = userService.register(user);
            if (registeredUser.getImage() != null)
                ImageSavingUtils.saveImage(imagePart, imagesFolderRelativePath, registeredUser.getImage());

            session.setAttribute("user", registeredUser);
            logger.info("User {} has ben successfully registered", user.getEmail());
            sendAjaxJsonAnswer(resp, HttpServletResponse.SC_OK, user);
        } catch (CaptchaValidationException | ValidationException e) {
            sendAjaxJsonAnswer(resp, HttpServletResponse.SC_BAD_REQUEST, e);
        } catch (TransactionException e) {
            sendAjaxJsonAnswer(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e);
        } catch (ServiceException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Service exception.");
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
            throw new CaptchaValidationException("recaptcha");
        }
    }

    private void validateSimpleCaptcha(String simpleCaptchaAnswer, HttpSession session) throws CaptchaValidationException {
        Captcha captcha = (Captcha) session.getAttribute(Captcha.NAME);

        if (Strings.isNullOrEmpty(simpleCaptchaAnswer) ||
                captcha == null ||
                !captcha.isCorrect(simpleCaptchaAnswer)) {
            throw new CaptchaValidationException("simplecaptcha");
        }
    }

    private void sendAjaxJsonAnswer(HttpServletResponse response, int httpResponseCode, Object objectToJson) throws IOException {
        response.setStatus(httpResponseCode);
        response.setHeader("Content-Type", "application/json");
        response.getWriter().write(GSON.toJson(objectToJson));
    }
}