package web.servlet;

import com.google.gson.Gson;
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

    private static final Logger logger = LogManager.getLogger();

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = (UserService) getServletContext().getAttribute("userService");
        if (userService == null) {
            logger.fatal("Could not initialize servlet from application context");
            throw new UnavailableException(
                    "Could not get user service or google captcha validator.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.entry();
   req.setCharacterEncoding("UTF-8");
        String login = req.getParameter("email");
        String password = req.getParameter("password");

        try {
            User user = userService.loadUserByUsername(login);
            if(user != null && user.getPassword().equals(password)){
                req.getSession().setAttribute("user", user);
                logger.info("User {} have been successfully logged in.", login);
                sendAjaxJsonAnswer(resp, HttpServletResponse.SC_OK, user);
            } else {
                logger.info("User {} credentials are wrong.", login);
                String loginError = "Wrong credentials. Please try again.";
                sendAjaxJsonAnswer(resp, HttpServletResponse.SC_BAD_REQUEST, loginError);
            }
        } catch (TransactionException | ValidationException e) {
            logger.error("Transactional | Validation exception in login servlet.", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void sendAjaxJsonAnswer(HttpServletResponse response, int httpResponseCode, Object objectToJson) throws IOException {
        response.setStatus(httpResponseCode);
        response.setHeader("Content-Type", "application/json");
        response.getWriter().write(new Gson().toJson(objectToJson));
    }
}