package db;

import dao.exceptions.DaoException;
import db.exceptions.TransactionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    public <T> T doTask(Transaction<T> transaction, int transactionIsolation) throws TransactionException {
        try {
            Connection connection = usersDs.getConnection();
            connection.setTransactionIsolation(transactionIsolation);
            ConnectionHolder.setConnection(connection);
            T result = transaction.execute();
            ConnectionHolder.getConnection().commit();
            return result;
        } catch (SQLException | DaoException e) {
            logger.error("Transactional exception caused by {}.", e.getMessage());
            throw new TransactionException(e);
        }
    }

}
