package de.greensurvivors.implementation.queryparam;

import de.greensurvivors.queryparam.SearchParameter;
import org.jetbrains.annotations.NotNull;

public non-sealed class SearchParameterImpl extends AQueryParameter<@NotNull String> implements SearchParameter {
    public SearchParameterImpl(final @NotNull String value) {
        super("search", value);
    }

    @Override
    public @NotNull String getFormData() {
        return getValue();
    }
}
