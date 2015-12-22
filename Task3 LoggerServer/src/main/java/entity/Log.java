package entity;

import org.joda.time.DateTime;

public class Log {

    private int idLog;
    private String name;
    private String message;
    private DateTime dateTime;
    private Type type;

    public Log(){}

    public Log(String name, String message, DateTime dateTime, Type type) {
        this.name = name;
        this.message = message;
        this.dateTime = dateTime;
        this.type = type;
    }

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Log log = (Log) o;

        if (idLog != log.idLog) return false;
        if (dateTime != null ? !dateTime.equals(log.dateTime) : log.dateTime != null) return false;
        if (message != null ? !message.equals(log.message) : log.message != null) return false;
        if (name != null ? !name.equals(log.name) : log.name != null) return false;
        if (type != log.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idLog;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (dateTime != null ? dateTime.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
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
