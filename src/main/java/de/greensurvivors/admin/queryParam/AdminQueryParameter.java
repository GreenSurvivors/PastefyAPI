package de.greensurvivors.admin.queryParam;

import de.greensurvivors.implementation.queryparam.UserParameterImpl;
import de.greensurvivors.queryparam.QueryParameter;
import org.jetbrains.annotations.NotNull;

public sealed interface AdminQueryParameter<T extends @NotNull Object> extends QueryParameter<T> permits UserParameter {
    /// only used for GET folders of a user - you can always make use of the user filter!
    static @NotNull UserParameter newUserParameter(final @NotNull String userId) {
        return new UserParameterImpl(userId);
    }
}
