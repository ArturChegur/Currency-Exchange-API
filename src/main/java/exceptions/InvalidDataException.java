package exceptions;

public class InvalidDataException extends RuntimeException {
    public InvalidDataException(String messageError) {
        super(messageError);
    }
}