package dao.exceptions;

import java.sql.SQLException;

public class DaoException extends Throwable {

    public DaoException(SQLException e) {
        super(e);
    }

}
