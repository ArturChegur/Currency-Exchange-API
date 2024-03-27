package exceptions;

public class DatabaseException extends RuntimeException {
    public DatabaseException(String messageError) {
        super(messageError);
    }
}