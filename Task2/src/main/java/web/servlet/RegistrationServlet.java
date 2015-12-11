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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class RegistrationServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(RegistrationServlet.class);

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
        resp.sendRedirect("/WEB-INF/jsp/registration.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        String email = req.getParameter("email");
        String name = req.getParameter("name");
        String surname = req.getParameter("surname");
        String image = req.getParameter("image");
        String password = req.getParameter("password");
        String confirmedPassword = req.getParameter("confirmedPassword");

        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setSurname(surname);
        user.setPassword(password);
        user.setImage(image);

        try {
            session.setAttribute("user", userService.register(user));
            resp.sendRedirect("welcome");
        } catch (ValidationException e) {
            UserDTO userDTO = new UserDTO(email, name, surname, image);
            if(!password.equals(confirmedPassword)){
                e.getFieldExceptions().add(new FieldError(UserField.PASSWORD));
            }
            session.setAttribute("userDTO", userDTO);
            session.setAttribute("fieldExceptions", e.getFieldExceptions());
            resp.sendRedirect("registration");
        } catch (DuplicateInsertException e) {
            UserDTO userDTO = new UserDTO(email, name, surname, image);
            session.setAttribute("userDTO", userDTO);
            session.setAttribute("userAlreadyExistsException", e);
            resp.sendRedirect("registration");
        }
    }

}
