package de.greensurvivors;

import de.greensurvivors.implementation.queryparam.AQueryParameter;
import org.jetbrains.annotations.NotNull;

public sealed interface QueryParameter<T extends @NotNull Object> permits AQueryParameter {
    T getValue();
}
