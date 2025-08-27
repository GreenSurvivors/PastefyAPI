package de.greensurvivors.queryparam;

import de.greensurvivors.implementation.queryparam.PageLimitParameterImpl;
import org.jetbrains.annotations.NotNull;

public sealed interface PageLimitParameter extends QueryParameter<@NotNull Integer> permits PageLimitParameterImpl {
}
