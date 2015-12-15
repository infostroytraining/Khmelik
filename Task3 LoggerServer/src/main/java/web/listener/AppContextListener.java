package web.listener;

import dao.LoggingDao;
import dao.PostgreLoggingDao;
import db.TransactionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.LoggingService;
import service.PostgreLoggingService;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

public class AppContextListener implements ServletContextListener {

    private static final String DB_CONNECTION_CONFIG = "java:comp/env/jdbc/logs";
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("AppContext began initializing.");
        ServletContext context = sce.getServletContext();

        //DAO
        LoggingDao loggingDao = new PostgreLoggingDao();

        //Creating dataSource
        InitialContext initContext;
        DataSource logDbDataSource = null;
        try {
            initContext = new InitialContext();
            logDbDataSource = (DataSource) initContext.lookup(DB_CONNECTION_CONFIG);
        } catch (NamingException e) {
            logger.fatal("Cannot connect to db.", e);
        }

        //Services and transaction manager
        TransactionManager transactionManager = new TransactionManager(logDbDataSource);
        LoggingService loggingService = new PostgreLoggingService(loggingDao, transactionManager);

        //Put beans into context
        context.setAttribute("loggingService", loggingService);
        logger.info("AppContext initialized.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Application context destroyed.");
    }
}
