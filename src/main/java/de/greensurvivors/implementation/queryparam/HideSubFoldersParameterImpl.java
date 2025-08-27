package de.greensurvivors.implementation.queryparam;

import de.greensurvivors.queryparam.HideSubFoldersParameter;
import org.jetbrains.annotations.NotNull;

public non-sealed class HideSubFoldersParameterImpl extends AQueryParameter<@NotNull Boolean> implements HideSubFoldersParameter {
    public HideSubFoldersParameterImpl(@NotNull Boolean value) {
        super("hide_children", value);
    }

    @Override
    public @NotNull String getFormData() {
        return String.valueOf(getValue());
    }
}
