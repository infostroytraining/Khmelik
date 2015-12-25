package web.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.UserService;
import utils.GoogleReCaptchaValidationUtils;
import web.listener.factory.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class AppContextListener implements ServletContextListener {

    private static final Logger logger = LogManager.getLogger();

    private static final String STORAGE_CONFIG_PARAMETER = "storage";
    private static final String IMAGES_FOLDER = "imagesFolder";
    private static final String DEFAULT_IMAGES_FOLDER = "../webapps/images/";

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        logger.info("Begin to initialize servlet context.");
        ServletContext servletContext = servletContextEvent.getServletContext();

        //Get context parameters
        String storageConfigParameter = servletContext.getInitParameter(STORAGE_CONFIG_PARAMETER);
        String imagesFolderRelativePath = servletContext.getInitParameter(IMAGES_FOLDER);
        if (imagesFolderRelativePath == null) {
            imagesFolderRelativePath = DEFAULT_IMAGES_FOLDER;
        }
        logger.info("Context parameters initialized.");

        //Initialize services
        UserService userService = ServiceFactory.getUserService(storageConfigParameter);
        GoogleReCaptchaValidationUtils googleReCaptchaValidationUtils = new GoogleReCaptchaValidationUtils();
        logger.info("Services and validation utils initialized");


        //Put beans into context
        servletContext.setAttribute("imagesFolderRelativePath", imagesFolderRelativePath);
        servletContext.setAttribute("userService", userService);
        servletContext.setAttribute("googleReCaptchaValidationUtils", googleReCaptchaValidationUtils);
        logger.info("App context initialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        logger.info("Servlet context destroyed.");
    }
}