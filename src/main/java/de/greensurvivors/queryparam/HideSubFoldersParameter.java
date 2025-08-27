package de.greensurvivors.queryparam;

import de.greensurvivors.implementation.queryparam.HideSubFoldersParameterImpl;
import org.jetbrains.annotations.NotNull;

public sealed interface HideSubFoldersParameter extends QueryParameter<@NotNull Boolean> permits HideSubFoldersParameterImpl {
}
