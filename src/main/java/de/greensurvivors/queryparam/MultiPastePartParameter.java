package de.greensurvivors.queryparam;

import de.greensurvivors.implementation.queryparam.MultiPastePartParameterImpl;
import org.jetbrains.annotations.NotNull;

public sealed interface MultiPastePartParameter extends QueryParameter<@NotNull String> permits MultiPastePartParameterImpl {
}
