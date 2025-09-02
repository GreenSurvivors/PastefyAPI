package de.greensurvivors.implementation;

import com.google.gson.annotations.SerializedName;
import de.greensurvivors.FolderBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FolderBuilderImpl implements FolderBuilder {
    private @NotNull String name;
    @SerializedName("parent")
    private @Nullable String parentId = null;

    public FolderBuilderImpl(final @NotNull String name) {
        this.name = name;
    }

    @Override
    public @NotNull FolderBuilder setName(final @NotNull String newName) {
        name = newName;
        return this;
    }

    @Override
    public @NotNull FolderBuilder setParent(final @Nullable String parentId) {
        this.parentId = parentId;
        return this;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @Nullable String getParentId() {
        return parentId;
    }
}
