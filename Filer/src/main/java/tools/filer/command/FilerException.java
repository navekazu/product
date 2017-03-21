package tools.filer.command;

public class FilerException extends Exception {
    public FilerException() {
        super();
    }

    public FilerException(String message) {
        super(message);
    }

    public FilerException(String message, Throwable cause) {
        super(message, cause);
    }
}
