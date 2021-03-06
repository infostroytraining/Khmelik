package db.exceptions;

public class TransactionException extends Exception {

    public TransactionException(Throwable e) {
        super(e);
    }

    @Override
    public String getMessage() {
        return "Something bad happened while executing your transaction. Please try again.";
    }
}
