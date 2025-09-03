package de.greensurvivors.admin.queryparam;

import de.greensurvivors.AccountStaus;
import de.greensurvivors.implementation.queryparam.filter.AdminFilterBuilderImpl;
import de.greensurvivors.queryparam.FilterBuilder;
import org.jetbrains.annotations.NotNull;

public sealed interface AdminFilterBuilder extends FilterBuilder permits AdminFilterBuilderImpl {
    @NotNull AdminFilterBuilder accountStatus(final @NotNull AccountStaus accountStaus);

    @NotNull AdminFilterBuilder authProvider(final @NotNull AuthenticationProvider authProvider);

    @NotNull AdminFilterBuilder authId(final @NotNull String authId);

    @NotNull AdminFilterBuilder eMailAddress(final @NotNull String eMailAddress);

    @NotNull AdminFilterBuilder uniqueName(final @NotNull String uniqueName);
}
