package de.greensurvivors;

import org.bouncycastle.crypto.CryptoException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public interface Session {
    <T> @NotNull CompletableFuture<@Nullable PasteReply> createPaste(final @NotNull PasteBuilder<T> builder) throws IOException, CryptoException;

    @NotNull CompletableFuture<@Nullable PasteReply> getPaste(final @NotNull String pasteID);

    @NotNull CompletableFuture<@Nullable FolderReply> createFolder (final @NotNull FolderBuilder builder);

    @NotNull CompletableFuture<@Nullable FolderReply> getFolder();

    @NotNull CompletableFuture<@NotNull Boolean> deletePaste(final @NotNull String pasteID);

    @NotNull CompletableFuture<@NotNull Integer> deleteFolder(final @NotNull String folderID);
}
