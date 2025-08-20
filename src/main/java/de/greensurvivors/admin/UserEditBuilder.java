package de.greensurvivors.admin;

import de.greensurvivors.AccountStaus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface UserEditBuilder {
    @NotNull UserEditBuilder setName (final @Nullable String newName);

    @Nullable String getNewName();

    @NotNull UserEditBuilder setUniqueName (final @Nullable String newUniqueName);

    @Nullable String getNewUniqueName();

    @NotNull UserEditBuilder setNewAccountStatus(final @Nullable AccountStaus newAccountStatus);

    @Nullable AccountStaus getNewAccountStatus();
}
