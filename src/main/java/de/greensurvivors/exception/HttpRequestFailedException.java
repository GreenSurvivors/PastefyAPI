package de.greensurvivors.exception;

public class HttpRequestFailedException extends RuntimeException {
    private final int statusCode;

    public HttpRequestFailedException(final int statusCode) {
        super("Http status code: " + statusCode);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
