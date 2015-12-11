package web.servlet;

import dto.UserDTO;
import entity.User;
import exceptions.FieldError;
import exceptions.ValidationException;
import exceptions.DuplicateInsertException;
import org.apache.log4j.Logger;
import service.UserService;
import service.validators.UserField;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class RegistrationServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(RegistrationServlet.class);

    //TODO make this property field
    private static final String TOMCAT_WEBAPPS_FOLDER_RELATIVE_PATH = "../webapps";

    UserService userService;

    @Override
    public void init() throws ServletException {
        userService = (UserService) getServletContext().getAttribute("userService");
        if (userService == null) {
            LOGGER.error("Could not get services from application context");
            throw new UnavailableException(
                    "Could not get user service.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("registrationJSP").forward(req, resp);
    }

    //TODO refactor
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

        String filePath = null;
        if (imagePart != null) {
            filePath = imagePart.getSubmittedFileName();
        }

        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setSurname(surname);
        user.setPassword(password);
        user.setImage(filePath);

        HttpSession session = req.getSession();
        try {
            User registeredUser = userService.register(user);
            session.setAttribute("user", registeredUser);
            if (imagePart != null) saveImage(imagePart, registeredUser.getImage());
            LOGGER.debug("User " + registeredUser.getEmail() + "has just successfully registered.");
            resp.sendRedirect("welcome");
        } catch (ValidationException e) {
            UserDTO userDTO = new UserDTO(email, name, surname);
            if (!password.equals(confirmedPassword)) {
                e.getFieldExceptions().add(new FieldError(UserField.PASSWORD));
            }
            session.setAttribute("userDTO", userDTO);
            session.setAttribute("fieldExceptions", e.getFieldExceptions());
            resp.sendRedirect("registration");
        } catch (DuplicateInsertException e) {
            UserDTO userDTO = new UserDTO(email, name, surname);
            session.setAttribute("userDTO", userDTO);
            session.setAttribute("userAlreadyExistsException", e);
            resp.sendRedirect("registration");
        }
    }

    private boolean isMultipartFormat(HttpServletRequest req) {
        String contentType = req.getContentType();
        return contentType.contains("multipart/form-data");
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