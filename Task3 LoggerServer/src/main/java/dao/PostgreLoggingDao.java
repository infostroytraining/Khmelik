package dao;

import dao.exceptions.DaoException;
import db.ConnectionHolder;
import entity.Log;
import entity.Type;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//TODO dao
public class PostgreLoggingDao implements LoggingDao {

    private static final Logger logger = LogManager.getLogger();
    public static final String INSERT_LOG_QUERY =
            "INSERT INTO log(name, message, type, date) VALUES (?, ?, ?, ?)";
    public static final String SELECT_ALL_LOGS_QUERY = "SELECT * FROM log";

    @Override
    public Log create(Log entity) throws DaoException {
        logger.entry(entity);
        Connection connection = ConnectionHolder.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_LOG_QUERY,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getMessage());
            preparedStatement.setString(3, entity.getType().name().toLowerCase());
            preparedStatement.setTimestamp(4, new Timestamp(entity.getDate().getTime()));
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if(resultSet.next()){
                entity.setIdLog(resultSet.getInt(1));
            }
        } catch (SQLException e) {
                logger.error("Cannot insert log into database.", e);
                throw new DaoException(e);
            }
            logger.exit();
        return null;
    }

    @Override
    public Log get(int idEntity) throws DaoException {
        return null;
    }

    @Override
    public List<Log> getAll() throws DaoException {
        logger.entry();
        List<Log> logs = new ArrayList<>();
        Connection connection = ConnectionHolder.getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_ALL_LOGS_QUERY);
            while(resultSet.next()){
                Log tempLog = new Log();
                tempLog.setIdLog(resultSet.getInt(1));
                tempLog.setName(resultSet.getString(2));
                tempLog.setType(Type.valueOf(resultSet.getString(3)));
                tempLog.setDate(new Date(resultSet.getTimestamp(4).getTime()));
                tempLog.setMessage(resultSet.getString(5));
                logs.add(tempLog);
            }
        } catch (SQLException e) {
            logger.error("Cannot read all logs from database.", e);
            throw new DaoException(e);
        }
        logger.exit(logs);
        return logs;
    }

    @Override
    public Log update(Log entity) throws DaoException {
        return null;
    }

    @Override
    public boolean delete(int idEntity) throws DaoException {
        return false;
    }
}
