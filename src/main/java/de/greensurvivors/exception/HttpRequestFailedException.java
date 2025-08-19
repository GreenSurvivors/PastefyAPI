package de.greensurvivors.exception;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HttpRequestFailedException extends RuntimeException {
    private final int statusCode;
    private final @Nullable String exceptionThrownName;

    public HttpRequestFailedException(final int statusCode) {
        super("Http status code: " + statusCode);
        this.statusCode = statusCode;
        this.exceptionThrownName = null;
    }

    public HttpRequestFailedException(final int statusCode, final @NotNull String exceptionThrownName) {
        super("Http request failed with exception: '" + exceptionThrownName + "', and status code: " + statusCode);
        this.statusCode = statusCode;
        this.exceptionThrownName = exceptionThrownName;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public @Nullable String getExceptionThrownName() {
        return exceptionThrownName;
    }
}
