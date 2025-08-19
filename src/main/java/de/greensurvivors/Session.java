package de.greensurvivors;

import de.greensurvivors.implementation.SessionImpl;
import org.bouncycastle.crypto.CryptoException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public interface Session {

    static Session newSession() {
        return new SessionImpl();
    }

    static Session newSession(final @Nullable String apiKey) {
        return new SessionImpl(apiKey);
    }

    static Session newSession (final @NotNull String serverAddress, final @Nullable String apiKey) {
        return new SessionImpl(serverAddress, apiKey);
    }


    <T> @NotNull CompletableFuture<@Nullable PasteReply> createPaste(final @NotNull PasteBuilder<T> builder) throws IOException, CryptoException;

    @NotNull CompletableFuture<@Nullable PasteReply> getPaste(final @NotNull String pasteID);

    @NotNull CompletableFuture<@Nullable FolderReply> createFolder (final @NotNull FolderBuilder builder);

    @NotNull CompletableFuture<@Nullable FolderReply> getFolder(final @NotNull String folderId);

    @NotNull CompletableFuture<@NotNull Boolean> deletePaste(final @NotNull String pasteID);

    @NotNull CompletableFuture<@NotNull Boolean> deleteFolder(final @NotNull String folderID);
}
