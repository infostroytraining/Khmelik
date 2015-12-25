package db.exceptions;

public class TransactionException extends Exception {

    private static final String MESSAGE = "Something bad happened while executing your transaction. Please try again.";

    public TransactionException(Throwable e) {
        super(e);
    }

    @Override
    public String getMessage() {
        return MESSAGE;
    }
}
