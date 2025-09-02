package de.greensurvivors.implementation.queryparam;

import de.greensurvivors.admin.queryParam.UserParameter;
import org.jetbrains.annotations.NotNull;

public non-sealed class UserParameterImpl extends AQueryParameter<@NotNull String> implements UserParameter {
    public UserParameterImpl(@NotNull String value) {
        super("user_id", value);
    }

    @Override
    public @NotNull String getFormData() {
        return getValue();
    }
}
