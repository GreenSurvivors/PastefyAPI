package de.greensurvivors;

import de.greensurvivors.implementation.PasteBuilderImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Collection;

// sorry for the sealed, but I can't take encryption lightly
public sealed interface PasteBuilder<T> extends Paste<T> permits PasteBuilderImpl {
    @NotNull PasteBuilder<T> setTitle(final @Nullable String title);

    @NotNull PasteBuilder<T> setContent(final @NotNull PasteContent<T> content);

    @NotNull PasteContent<T> getPackagedContent();

    @NotNull PasteBuilder<T> setVisibility(final @NotNull PasteVisibility visibility);

    @NotNull PasteBuilder<T> encryptWhenSending(final byte @Nullable [] password) throws NoSuchAlgorithmException;

    @NotNull PasteBuilder<T> setExpirationTime(final @Nullable Instant expirationTime); // note: I have no Idea what timezone they use. Maybe german?? Maybe GMT???

    /// Note: When editing a paste you only can ADD tags, not remove them.
    /// There currently isn't any way via the api to remove tags without also removing the paste itself!
    @NotNull PasteBuilder<T> setTags(final @NotNull Collection<@NotNull String> tags);

    /// Note: When editing a paste you only can ADD tags, not remove them.
    /// There currently isn't any way via the api to remove tags without also removing the paste itself!
    @NotNull PasteBuilder<T> addTag(final @NotNull String tag);

    @NotNull PasteBuilder<T> setFolderId(final @Nullable String folderId);

    @NotNull PasteBuilder<T> setPasteIdForkedFrom(final @Nullable String pasteIdForkedFrom);

    /// If enabled server side and if this paste is not encrypted, may add tags (up to 6), filename / file extension ( if not multi paste) if missing.
    /// Note: does nothing when editing an already existing post.
    @NotNull PasteBuilder<T> useAI(final boolean useAI);

    boolean doesUseAI();
}
