package de.greensurvivors.implementation.queryparam;

import org.jetbrains.annotations.NotNull;

public class PageParameter extends AQueryParameter<@NotNull Integer> {
    protected PageParameter(final int value) {
        super("page", value);
    }

    @Override
    public @NotNull String getFormData()  throws IllegalArgumentException {
        if (getValue() < 0) {
            throw new IllegalArgumentException("Page number (" + getValue() + ") lower than 0: ");
        }

        return String.valueOf(getValue());
    }
}
