package de.greensurvivors.queryparam;

import de.greensurvivors.implementation.queryparam.ShortenContentParameterImpl;
import org.jetbrains.annotations.NotNull;

public sealed interface ShortenContentParameter extends QueryParameter<@NotNull Boolean> permits ShortenContentParameterImpl {
}
