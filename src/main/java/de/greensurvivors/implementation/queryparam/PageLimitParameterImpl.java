package de.greensurvivors.implementation.queryparam;

import de.greensurvivors.queryparam.PageLimitParameter;
import org.jetbrains.annotations.NotNull;

public non-sealed class PageLimitParameterImpl extends AQueryParameter<@NotNull Integer> implements PageLimitParameter {
    public PageLimitParameterImpl(final int value) {
        super("page_limit", value);
    }

    @Override
    public @NotNull String getFormData() throws IllegalArgumentException {
        if (getValue() < 0) {
            throw new IllegalArgumentException("Page number (" + getValue() + ") lower than 0: ");
        }

        return String.valueOf(getValue());
    }
}
