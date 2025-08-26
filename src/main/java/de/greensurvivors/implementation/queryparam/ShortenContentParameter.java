package de.greensurvivors.implementation.queryparam;

import org.jetbrains.annotations.NotNull;

public class ShortenContentParameter extends AQueryParameter<@NotNull Boolean>{

    public ShortenContentParameter() {
        this(true);
    }

    public ShortenContentParameter(final boolean value) {
        super("shorten_content", value);
    }

    @Override
    public @NotNull String getFormData() {
        return String.valueOf(getValue());
    }
}
