package swp391_sum22.happyprogramming.exception.auth;

public class UserAlreadyExistException extends Exception {
    public UserAlreadyExistException(String message) {
        super(message);
    }
}
