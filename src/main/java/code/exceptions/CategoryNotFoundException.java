package code.exceptions;

public class CategoryNotFoundException extends Exception {
    @Override
    public String getMessage() {
        return "category not found";
    }
}
