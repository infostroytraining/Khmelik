package web.servlet;

import db.exceptions.TransactionException;
import entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.UserService;
import service.exceptions.DuplicateInsertException;
import service.exceptions.ValidationException;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet{

    private static final Logger LOGGER = LogManager.getLogger();

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = (UserService) getServletContext().getAttribute("userService");
        if (userService == null) {
            LOGGER.fatal("Could not initialize servlet from application context");
            throw new UnavailableException(
                    "Could not get user service or google captcha validator.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("loginJSP").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("email");
        String password = req.getParameter("password");

        try {
            User user = userService.loadUserByUsername(login);
            if(user != null && user.getPassword().equals(password)){
                req.getSession().setAttribute("user", user);
                resp.sendRedirect("welcome");
            } else {
                req.getSession().setAttribute("loginError", "Wrong credentials. Please try again.");
                req.getSession().setAttribute("loginDTO", login);
                resp.sendRedirect("login");
            }
        } catch (TransactionException | DuplicateInsertException | ValidationException e) {
            LOGGER.error("Transactional | Duplicate | Validation exception in login servlet.");
            resp.sendError(500);
        }
    }
}
