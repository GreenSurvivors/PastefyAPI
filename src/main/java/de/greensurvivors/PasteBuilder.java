package de.greensurvivors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Collection;

// sorry for the sealed, but I can't take encryption lightly
public sealed interface PasteBuilder<T> extends Paste<T> permits de.greensurvivors.implementation.PasteBuilderImpl {
    @NotNull PasteBuilder<T> setTitle(final @NotNull String title);

    @NotNull PasteBuilder<T> setContent(final @NotNull PasteContent<T> content);

    @NotNull PasteContent<T> getPackagedContent();

    @NotNull PasteBuilder<T> setVisibility(final @NotNull PasteVisibility visibility);

    @NotNull PasteBuilder<T> encryptWhenSending(final byte @Nullable [] password) throws NoSuchAlgorithmException;

    @NotNull PasteBuilder<T> setExpirationTime(final @Nullable Instant expirationTime); // note: I have no Idea what timezone they use. Maybe german?? Maybe GMT???

    @NotNull PasteBuilder<T> setTags(final @NotNull Collection<@NotNull String> tags);

    @NotNull PasteBuilder<T> addTag(final @NotNull String tag);

    @NotNull PasteBuilder<T> setFolderId(final @Nullable String folderId);

    @NotNull PasteBuilder<T> setPasteIdForkedFrom(final @Nullable String pasteIdForkedFrom);

    @NotNull PasteBuilder<T> useAI(final boolean useAI);

    boolean doesUseAI();
}
