package de.greensurvivors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Set;

public interface FolderReply extends Folder {
    @NotNull String getId();

    @Nullable String getUserId();

    @Nullable Set<? extends @NotNull FolderReply> getSubFolders ();

    @Nullable Set<? extends @NotNull PasteReply> getPastes ();

    @NotNull Instant getCreatedAt();

    boolean exists ();
}
