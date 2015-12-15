package entity;

import org.joda.time.DateTime;

public class Log {

    private int idLog;
    private String name;
    private String message;
    private DateTime dateTime;
    private Type type;

    public int getIdLog() {
        return idLog;
    }

    public void setIdLog(int idLog) {
        this.idLog = idLog;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Log{" +
                "idLog=" + idLog +
                ", name='" + name + '\'' +
                ", message='" + message + '\'' +
                ", date=" + dateTime +
                ", type=" + type +
                '}';
    }
}
