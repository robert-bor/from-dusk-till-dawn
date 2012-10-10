package nl.d2n.model;

public class ApplicationException extends Exception {
    private D2NErrorCode error;
    private String description;

    public ApplicationException(D2NErrorCode error) {
        this.error = error;
    }
    public ApplicationException(D2NErrorCode error, String description) {
        this(error);
        this.description = description;
    }
    public D2NErrorCode getError() { return this.error; }
    public String getDescription() { return this.description; }
}
