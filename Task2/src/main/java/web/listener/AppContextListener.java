package web.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.UserService;
import web.captcha.GoogleReCaptchaValidationUtils;
import web.listener.factory.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class AppContextListener implements ServletContextListener {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String STORAGE_CONFIG_PARAMETER = "storage";

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        LOGGER.debug("Start to initialize servlet context.");
        ServletContext servletContext = servletContextEvent.getServletContext();
        String storageConfigParameter = servletContext.getInitParameter(STORAGE_CONFIG_PARAMETER);

        UserService userService = ServiceFactory.getUserService(storageConfigParameter);

        GoogleReCaptchaValidationUtils googleReCaptchaValidationUtils = new GoogleReCaptchaValidationUtils();

        //Put beans into context
        servletContext.setAttribute("userService", userService);
        servletContext.setAttribute("googleReCaptchaValidationUtils", googleReCaptchaValidationUtils);
        LOGGER.info("App context initialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        LOGGER.info("Servlet context destroyed.");
    }
}
