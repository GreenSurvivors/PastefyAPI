package de.greensurvivors.implementation.queryparam;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.HashSet;
import java.util.Set;

public class FilterTagsParameter extends AQueryParameter<@NotNull Set<@NotNull String>> {

    protected FilterTagsParameter(final @NotNull Set<@NotNull String> value) {
        super("filter_tags", new HashSet<>(value));
    }

    // return an unmodifiable copy
    @Override
    public @NotNull @Unmodifiable Set<@NotNull String> getValue() {
        return Set.copyOf(super.getValue());
    }

    @Override
    public @NotNull String getFormData() throws IllegalArgumentException {
        if (getValue().isEmpty()) {
            throw new IllegalArgumentException("Tags to filter by are missing!");
        }

        return String.join(",", super.getValue());
    }
}
