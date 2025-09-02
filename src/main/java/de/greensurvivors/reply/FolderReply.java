package de.greensurvivors.reply;

import de.greensurvivors.Folder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Set;

public interface FolderReply extends Folder {
    @NotNull String getId();

    @Nullable String getUserId();

    @NotNull Set<? extends @NotNull FolderReply> getSubFolders();

    @NotNull Set<? extends @NotNull PasteReply> getPastes();

    @NotNull Instant getCreatedAt();
}
