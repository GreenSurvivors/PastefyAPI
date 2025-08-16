package de.greensurvivors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

public interface FolderReply extends Folder {
    @NotNull String getId();

    @Nullable String getParentId();

    int getUserId();

    @NotNull Instant getCreatedAt();
}
