package nl.d2n.reader.wiki;

public class WikiConstructionParserException extends Exception {

    private String page;
    private WikiConstructionParserErrorType error;
    private String message;

    public WikiConstructionParserException(String page, WikiConstructionParserErrorType error, String message) {
        this.page = page;
        this.error = error;
        this.message = message;
    }

    public String getPage() {
        return this.page;
    }
    public WikiConstructionParserErrorType getError() {
        return this.error;
    }
    public String getMessage() {
        return this.message;
    }
}
