package de.greensurvivors.implementation;

import de.greensurvivors.Paste;
import de.greensurvivors.PasteBuilder;
import de.greensurvivors.PasteContent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

// sorry for the final, but I can't take encryption lightly.
public final class PasteBuilderImpl<T> implements PasteBuilder<T> {
    private @NotNull String title;
    private @NotNull PasteContent<T> content;
    private @NotNull PasteVisibility visibility = PasteVisibility.UNLISTED;
    private @Nullable EncryptionHelper.HashedPasskey hashedPasskey = null;
    private @Nullable Instant expirationTime = null;
    @SuppressWarnings("unchecked") // yes Java a list IS a collection and if empty there CAN'T be any generic issues.
    private @NotNull Collection<@NotNull String> tags = Collections.EMPTY_LIST;
    private @Nullable String folderId = null;
    private @Nullable String pasteIdForkedFrom = null;

    public PasteBuilderImpl (final @NotNull String title, final @NotNull PasteContent<T> content) {
        this.title = title;
        this.content = content;
    }

    @Override
    public @NotNull PasteBuilder<T> setTitle(final @NotNull String title) {
        this.title = title;
        return this;
    }

    @Override
    public @NotNull PasteBuilder<T> setContent(final @NotNull PasteContent<T> content) {
        this.content = content;
        return this;
    }

    @Override
    public @NotNull PasteContent<T> getPackagedContent() {
        return content;
    }

    @Override
    public @NotNull PasteBuilder<T> setVisibility(final @NotNull PasteVisibility visibility) {
        this.visibility = visibility;
        return this;
    }

    @Override
    public @NotNull PasteBuilder<T> encryptWhenSending(final byte @Nullable [] password) throws NoSuchAlgorithmException {
        if (password != null) {
            this.hashedPasskey = EncryptionHelper.hashPasskey(password);
        } else {
            this.hashedPasskey = null;
        }
        return this;
    }

    @Override
    public @NotNull PasteBuilder<T> setExpirationTime(final @Nullable Instant expirationTime) {
        this.expirationTime = expirationTime;
        return this;
    }

    @Override
    public @NotNull PasteBuilder<T> setTags(final @NotNull Collection<@NotNull String> tags) {
        this.tags = new LinkedHashSet<>(tags); // copy tags into new collection to prevent dumb issues
        return this;
    }

    @Override
    public @NotNull PasteBuilder<T> addTag(final @NotNull String tag) {
        if (tags == Collections.EMPTY_LIST){
            tags = new LinkedHashSet<>();
        }

        tags.add(tag);

        return this;
    }

    @Override
    public @NotNull PasteBuilder<T> setFolderId(final @Nullable String folderId) {
        this.folderId = folderId;
        return this;
    }

    @Override
    public @NotNull PasteBuilder<T> setPasteIdForkedFrom(final @Nullable String pasteIdForkedFrom) {
        this.pasteIdForkedFrom = pasteIdForkedFrom;
        return this;
    }

    @Override
    public @NotNull Paste<T> build() {
        return new PasteImpl<>(this);
    }

    @Override
    public @NotNull PasteType getType() {
        return content.getPasteType();
    }

    @Override
    public @NotNull String getTitle() {
        return title;
    }

    @Override
    public @NotNull T getContent() {
        return content.getContent();
    }

    @Override
    public @NotNull PasteVisibility getVisibility() {
        return visibility;
    }

    @Override
    public boolean isEncrypted() {
        return hashedPasskey != null;
    }

    @Override
    public @Nullable Instant getExpirationTime() {
        return expirationTime;
    }

    @Override
    public @NotNull @Unmodifiable Collection<String> getTags() {
        return List.copyOf(tags); // copy tags into new collection to prevent dumb issues
    }

    @Override
    public @Nullable String getFolderId() {
        return folderId;
    }

    @Override
    public @Nullable String getPasteIdForkedFrom() {
        return pasteIdForkedFrom;
    }

    @Override
    public @NotNull <NewT> PasteBuilder<NewT> newTypedBuilder(@NotNull PasteContent<NewT> newTypedPasteContent) {
        return new PasteBuilderImpl<>(title, newTypedPasteContent);
    }

    @Nullable EncryptionHelper.HashedPasskey getHashedPasskey() {
        return hashedPasskey;
    }
}
