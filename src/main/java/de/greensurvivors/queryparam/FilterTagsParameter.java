package de.greensurvivors.queryparam;

import de.greensurvivors.implementation.queryparam.FilterTagsParameterImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public sealed interface FilterTagsParameter extends QueryParameter<@NotNull Set<@NotNull String>> permits FilterTagsParameterImpl {
}
