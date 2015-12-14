package db;

import dao.exceptions.DaoException;
import db.exceptions.TransactionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.exceptions.DuplicateInsertException;
import service.exceptions.ValidationException;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


public class TransactionManager {

    private static final Logger LOGGER = LogManager.getLogger();

    private DataSource usersDs;

    public TransactionManager(DataSource dataSource) throws NamingException {
        this.usersDs = dataSource;
    }

    public <T> T doTask(Transaction<T> transaction, int transactionIsolation) throws TransactionException, DuplicateInsertException, ValidationException {
        try {
            Connection connection = usersDs.getConnection();
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(transactionIsolation);
            ConnectionHolder.setConnection(connection);
            T result = transaction.execute();
            ConnectionHolder.getConnection().commit();
            return result;
        } catch (SQLException | DaoException e) {
            LOGGER.error("Transactional exception (may be resource-handling exception " + e);
            throw new TransactionException(e);
        }
    }

}
