package de.greensurvivors.implementation.queryparam;

import de.greensurvivors.queryparam.QueryParameter;
import org.jetbrains.annotations.NotNull;

// Note: The API does not need `getName` or `getFormData` exposed. That's why I used this very odd chaining of interface -> abstract Class -> implementation
public abstract non-sealed class AQueryParameter<T extends @NotNull Object> implements QueryParameter<T> {
    private final @NotNull String name;
    private final @NotNull T value;

    protected AQueryParameter(@NotNull String name, @NotNull T value) {
        this.name = name;
        this.value = value;
    }

    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull T getValue() {
        return value;
    }

    public abstract @NotNull String getFormData();
}
