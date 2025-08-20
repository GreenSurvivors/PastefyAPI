package de.greensurvivors.implementation;

import com.google.gson.annotations.SerializedName;
import de.greensurvivors.AccountStaus;
import de.greensurvivors.admin.UserEditBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UserEditBuilderImpl implements UserEditBuilder {
    @SerializedName("name")
    private @Nullable String newName = null;
    @SerializedName("unique_name") // maybe??
    private @Nullable String newUniqueName = null;
    private @Nullable AccountStaus newAccountStatus = null;

    @Override
    public @NotNull UserEditBuilder setName(final @Nullable String newName) {
        this.newName = newName;
        return this;
    }

    @Override
    public @Nullable String getNewName() {
        return newName;
    }

    @Override
    public @NotNull UserEditBuilder setUniqueName(final @Nullable String newUniqueName) {
        this.newUniqueName = newUniqueName;
        return this;
    }

    @Override
    public @Nullable String getNewUniqueName() {
        return newUniqueName;
    }

    @Override
    public @NotNull UserEditBuilder setNewAccountStatus(final @Nullable AccountStaus newAccountStatus) {
        this.newAccountStatus = newAccountStatus;
        return this;
    }

    @Override
    public @Nullable AccountStaus getNewAccountStatus() {
        return newAccountStatus;
    }
}
