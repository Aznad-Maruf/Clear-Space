package Exceptions;

public class InputOfRangeException extends RuntimeException{
    @Override
    public String getMessage() {
        return "Input is not in range";
    }
}
