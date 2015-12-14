package service;

import db.exceptions.TransactionException;
import entity.Log;

import java.util.List;

public interface LoggingService {

    Log saveLog(Log log) throws TransactionException;

    List<Log> getLogs() throws TransactionException;

}
