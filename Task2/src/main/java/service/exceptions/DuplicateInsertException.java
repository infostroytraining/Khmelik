package service.exceptions;

public class DuplicateInsertException extends Exception {

    @Override
    public String getMessage() {
        return "User with such email or name+surname combination already exists.";
    }
}
