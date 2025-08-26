package de.greensurvivors.implementation.queryparam;

import org.jetbrains.annotations.NotNull;

public class SearchParameter extends AQueryParameter<@NotNull String>{
    protected SearchParameter(final @NotNull String value) {
        super("search", value);
    }

    @Override
    public @NotNull String getFormData() {
        return getValue();
    }
}
