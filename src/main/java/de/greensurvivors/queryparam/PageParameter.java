package de.greensurvivors.queryparam;

import de.greensurvivors.implementation.queryparam.PageParameterImpl;
import org.jetbrains.annotations.NotNull;

public sealed interface PageParameter extends QueryParameter<@NotNull Integer> permits PageParameterImpl {
}
