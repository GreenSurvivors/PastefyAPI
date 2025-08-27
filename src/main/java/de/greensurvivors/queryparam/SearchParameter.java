package de.greensurvivors.queryparam;

import de.greensurvivors.implementation.queryparam.SearchParameterImpl;
import org.jetbrains.annotations.NotNull;

public sealed interface SearchParameter extends QueryParameter<@NotNull String> permits SearchParameterImpl {
}
