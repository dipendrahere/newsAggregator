package code.exceptions;

public class DissimilarArticleException extends Exception{
    @Override
    public String getMessage() {
        return "different categories";
    }
}
