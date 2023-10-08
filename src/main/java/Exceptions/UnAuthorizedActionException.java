package Exceptions;

public class UnAuthorizedActionException extends Exception{
    public UnAuthorizedActionException(String message) {
        super(message);
    }

    public UnAuthorizedActionException() {
        super("Not authorized for this action");
    }
}