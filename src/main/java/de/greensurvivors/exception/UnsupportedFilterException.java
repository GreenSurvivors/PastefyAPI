package de.greensurvivors.exception;

import org.jetbrains.annotations.NotNull;

public class UnsupportedFilterException extends UnsupportedOperationException {
    public UnsupportedFilterException (final @NotNull String message) {
        super(message);
    }
}
