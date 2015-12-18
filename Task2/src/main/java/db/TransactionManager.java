package db;

import dao.exceptions.DaoException;
import db.exceptions.TransactionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.exceptions.DuplicateInsertException;
import service.exceptions.ValidationException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


public class TransactionManager {

    private static final Logger logger = LogManager.getLogger();

    private DataSource usersDs;

    public TransactionManager(DataSource dataSource) {
        this.usersDs = dataSource;
        logger.info("Transaction manager initialized.");
    }

    public <T> T doTask(Transaction<T> transaction, int transactionIsolation) throws TransactionException, DuplicateInsertException, ValidationException {
        Connection connection = null;
        try {
            connection = usersDs.getConnection();
            connection.setTransactionIsolation(transactionIsolation);
            ConnectionHolder.setConnection(connection);
            T result = transaction.execute();
            connection.commit();
            return result;
        } catch (SQLException | DaoException e) {
            rollback(connection);
            logger.error("Transactional exception caused by {}.", e.getMessage());
            throw new TransactionException(e);
        } finally {
            close(connection);
        }
    }

    private void close(Connection connection) {
        if(connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error("Error while closing SQL connection.", e);
            }
        }
    }

    private void rollback(Connection connection) {
        if(connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                logger.error("Error while trying to rollback SQL connection.", e);
            }
        }
    }
}
