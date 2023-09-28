package Exceptions;

public class UserDoesNotExist extends Exception{
    public UserDoesNotExist() {
        super("User Does not Exist");
    }
}
