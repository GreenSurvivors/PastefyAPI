package de.greensurvivors.implementation.queryparam;

import de.greensurvivors.queryparam.PageParameter;
import org.jetbrains.annotations.NotNull;

public non-sealed class PageParameterImpl extends AQueryParameter<@NotNull Integer> implements PageParameter {
    public PageParameterImpl(final int value) {
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
