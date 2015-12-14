package web.listener.factory;

import dao.UserDao;
import dao.mock_impl.UserDaoMock;
import dao.postgre.PostgreUserDao;
import db.TransactionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.UserService;
import service.mock_impl.UserServiceImpl;
import service.postgre.PostgreUserService;
import service.validators.UserValidator;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.ServiceConfigurationError;

public class ServiceFactory {

    private static final Logger logger = LogManager.getLogger();

    private static final String MEMORY = "memory";
    private static final String DB = "db";
    private static final String DB_CONNECTION_CONFIG = "java:comp/env/jdbc/users";

    public static UserService getUserService(String type) {
        if (type == null || type.isEmpty()) {
            logger.fatal("Could initialize application. Source type is null or empty");
            throw new IllegalArgumentException();
        }
        if (type.equals(MEMORY)) {
            return initMemoryService();
        } else if (type.equals(DB)) {
            return initTransactionalService();
        } else {
            logger.fatal("Could initialize application with source type {}", type);
            throw new ServiceConfigurationError("Could not initialize application with source type [" + type + "]");
        }
    }

    private static DataSource loadPostgreConnectionPool() {
        try {
            InitialContext initContext = new InitialContext();
            return (DataSource) initContext.lookup(DB_CONNECTION_CONFIG);
        } catch (NamingException e) {
            logger.fatal("Could not load postgres connection pool from context.", e);
            throw new ServiceConfigurationError("Could not load postgres connection pool.");
        }
    }

    private static UserService initMemoryService() {
        UserDao userDao = new UserDaoMock();
        UserValidator userValidator = new UserValidator();
        return new UserServiceImpl(userDao, userValidator);
    }

    private static UserService initTransactionalService() {
        DataSource dataSource = loadPostgreConnectionPool();
        UserDao userDao = new PostgreUserDao();
        UserValidator validator = new UserValidator();
        TransactionManager transactionManager = new TransactionManager(dataSource);

        return new PostgreUserService(userDao, validator, transactionManager);
    }
}