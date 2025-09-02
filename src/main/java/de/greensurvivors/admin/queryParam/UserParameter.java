package de.greensurvivors.admin.queryParam;

import de.greensurvivors.implementation.queryparam.UserParameterImpl;
import org.jetbrains.annotations.NotNull;

public sealed interface UserParameter extends AdminQueryParameter<@NotNull String> permits UserParameterImpl {
}
