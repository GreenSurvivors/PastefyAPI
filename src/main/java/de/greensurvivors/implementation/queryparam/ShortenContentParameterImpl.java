package de.greensurvivors.implementation.queryparam;

import de.greensurvivors.queryparam.ShortenContentParameter;
import org.jetbrains.annotations.NotNull;

public non-sealed class ShortenContentParameterImpl extends AQueryParameter<@NotNull Boolean> implements ShortenContentParameter {

    public ShortenContentParameterImpl() {
        this(true);
    }

    public ShortenContentParameterImpl(final boolean value) {
        super("shorten_content", value);
    }

    @Override
    public @NotNull String getFormData() {
        return String.valueOf(getValue());
    }
}
