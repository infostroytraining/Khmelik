package web.listener;

import dao.UserDao;
import dao.mock_impl.UserDaoMock;
import entity.User;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import service.UserService;
import service.mock_impl.UserServiceImpl;
import service.validators.UserValidator;
import service.validators.Validator;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class AppContextListener implements ServletContextListener {

    public static final Logger LOGGER = Logger
            .getLogger(AppContextListener.class);

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();

        initLog4J(servletContext);

        //DAO
        UserDao userDao = new UserDaoMock();


        //Validators
        Validator<User> userValidator = new UserValidator();


        //Service
        UserService userService = new UserServiceImpl(userDao, userValidator);


        //Put beans into context
        servletContext.setAttribute("userService", userService);

        LOGGER.info("App context initialized");
    }

    private void initLog4J(ServletContext servletContext) {
        log("Log4J initialization started");
        try {
            PropertyConfigurator.configure(servletContext
                    .getRealPath("WEB-INF/classes/log4j.properties"));
        } catch (Exception ex) {
            LOGGER.error("Cannot configure Log4j", ex);
        }
        log("Log4J initialization finished");
        LOGGER.debug("Log4j has been initialized");
    }

    private void log(String msg) {
        System.out.println("[AppContextListener] " + msg);
    }
}
