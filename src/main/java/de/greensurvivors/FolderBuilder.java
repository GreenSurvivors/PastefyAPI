package de.greensurvivors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface FolderBuilder extends Folder {
    @NotNull FolderBuilder setName(final @NotNull String newName);

    @NotNull FolderBuilder setParent(final @Nullable String parentId);

    @Nullable String getParentId();
}
