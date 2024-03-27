package exceptions;

public class DataExistsException extends RuntimeException {
    public DataExistsException(String messageError) {
        super(messageError);
    }
}