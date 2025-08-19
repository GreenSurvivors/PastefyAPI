package de.greensurvivors;

import de.greensurvivors.implementation.FolderBuilderImpl;
import org.jetbrains.annotations.NotNull;

public interface Folder {
    @NotNull String getName();

    static FolderBuilder newBuilder(final @NotNull String name) {
        return new FolderBuilderImpl(name);
    }
}
