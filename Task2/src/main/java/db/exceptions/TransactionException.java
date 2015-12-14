package db.exceptions;

public class TransactionException extends Throwable {

    public TransactionException(Throwable e) {
        super(e);
    }
}
