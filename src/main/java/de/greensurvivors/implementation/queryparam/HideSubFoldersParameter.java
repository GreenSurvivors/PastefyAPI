package de.greensurvivors.implementation.queryparam;

import org.jetbrains.annotations.NotNull;

public class HideSubFoldersParameter extends AQueryParameter<@NotNull Boolean> {
    private HideSubFoldersParameter() {
        this(true);
    }

    private HideSubFoldersParameter(@NotNull Boolean value) {
        super("hide_children", value);
    }

    @Override
    public @NotNull String getFormData() {
        return String.valueOf(getValue());
    }
}
