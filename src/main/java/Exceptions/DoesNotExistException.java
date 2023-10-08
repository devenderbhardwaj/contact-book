package Exceptions;

public class DoesNotExistException extends Exception{
    public DoesNotExistException() {
        super("Does not Exist");
    }

    public DoesNotExistException(String message) {
        super(message);
    }
}
