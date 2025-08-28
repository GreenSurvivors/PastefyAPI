package de.greensurvivors.exception;

import org.jetbrains.annotations.NotNull;

public class NestedFilterException extends Exception {
    public NestedFilterException(final @NotNull String message) {
        super(message);
    }
}
