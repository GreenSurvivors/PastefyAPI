package de.greensurvivors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public interface Session {
     <T> @NotNull CompletableFuture<@Nullable PasteReply<T>> createPaste(final @NotNull PasteBuilder<T> builder);

     <T> @NotNull CompletableFuture<@Nullable PasteReply<T>> getPaste (final @NotNull String pasteID);
}
