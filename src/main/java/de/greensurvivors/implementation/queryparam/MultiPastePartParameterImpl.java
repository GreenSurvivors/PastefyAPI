package de.greensurvivors.implementation.queryparam;

import de.greensurvivors.queryparam.MultiPastePartParameter;
import org.jetbrains.annotations.NotNull;

public non-sealed class MultiPastePartParameterImpl extends AQueryParameter<@NotNull String> implements MultiPastePartParameter {
    protected MultiPastePartParameterImpl(final @NotNull String multiPastePartName) {
        super("part", multiPastePartName);
    }

    @Override
    public @NotNull String getFormData() {
        return getValue();
    }
}
