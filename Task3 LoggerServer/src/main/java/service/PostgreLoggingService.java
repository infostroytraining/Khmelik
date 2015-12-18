package service;

import dao.LoggingDao;
import db.TransactionManager;
import db.exceptions.TransactionException;
import entity.Log;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.util.List;

public class PostgreLoggingService implements LoggingService {

    private static final Logger logger = LogManager.getLogger();

    private LoggingDao loggingDao;
    private TransactionManager transactionManager;

    public PostgreLoggingService(LoggingDao loggingDao, TransactionManager transactionManager) {
        this.loggingDao = loggingDao;
        this.transactionManager = transactionManager;
        logger.info("Postgre LoggingService Initialized");
    }

    @Override
    public Log saveLog(Log log) throws TransactionException {
        logger.entry(log);
        return transactionManager.doTask(() -> loggingDao.create(log), Connection.TRANSACTION_READ_UNCOMMITTED);
    }

    @Override
    public List<Log> getLogs() throws TransactionException {
        logger.entry();
        return transactionManager.doTask(loggingDao::getAll, Connection.TRANSACTION_READ_UNCOMMITTED);
    }
}
