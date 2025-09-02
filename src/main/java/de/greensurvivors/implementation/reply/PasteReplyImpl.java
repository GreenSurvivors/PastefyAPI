package de.greensurvivors.implementation.reply;

import com.google.gson.annotations.SerializedName;
import de.greensurvivors.Paste;
import de.greensurvivors.PasteBuilder;
import de.greensurvivors.PasteContent;
import de.greensurvivors.implementation.EncryptionHelper;
import de.greensurvivors.implementation.PasteBuilderImpl;
import de.greensurvivors.implementation.content.SimpleStringContentImpl;
import de.greensurvivors.reply.PasteReply;
import de.greensurvivors.reply.PublicUserReply;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.time.Instant;
import java.util.Collection;

public class PasteReplyImpl implements PasteReply {
    protected @Nullable String title;
    protected @NotNull String content;
    private final @NotNull PasteType type;
    private final @NotNull PasteVisibility visibility;
    @SerializedName("encrypted")
    private final boolean isEncrypted;
    @SerializedName("expire_at")
    private final @Nullable Instant expirationTime;
    private final @NotNull Collection<@NotNull String> tags;
    @SerializedName("folder")
    private final @Nullable String folderId;
    @SerializedName("forkedFrom")
    private final @Nullable String pasteIdForkedFrom;

    private final @NotNull String id;
    private final boolean exists; // I really have no idea why the api has this field, if the api answers with status code 404 if a paste doesn't exist...
    @SerializedName("raw_url")
    private final @NotNull URI rawURL;
    @SerializedName("created_at")
    private final @NotNull Instant createdAt;
    private final @Nullable PublicUserReplyImpl user;
    private final @Nullable Boolean isStarred;

    // unused, this class gets instanced by gson!
    private PasteReplyImpl(
        @Nullable String title, @NotNull String content, @NotNull PasteType type,
        @NotNull PasteVisibility visibility,
        boolean isEncrypted,
        @Nullable Instant expirationTime,
        @NotNull Collection<@NotNull String> tags,
        @Nullable String folderId,
        @Nullable String pasteIdForkedFrom,
        @NotNull String id,
        boolean exists,
        @NotNull URI rawURL, @NotNull Instant createdAt,
        @Nullable PublicUserReplyImpl user, @Nullable Boolean isStarred) {
        this.title = title;
        this.content = content;
        this.type = type;
        this.visibility = visibility;
        this.isEncrypted = isEncrypted;
        this.expirationTime = expirationTime;
        this.tags = tags;
        this.folderId = folderId;
        this.pasteIdForkedFrom = pasteIdForkedFrom;
        this.id = id;
        this.exists = exists;
        this.rawURL = rawURL;
        this.createdAt = createdAt;
        this.user = user;
        this.isStarred = isStarred;
    }

    @Override
    public @NotNull PasteType getType() {
        return type;
    }

    @Override
    public @Nullable String getTitle() {
        return title;
    }

    @Override
    public @NotNull String getContent() {
        return content;
    }

    @Override
    public @NotNull PasteVisibility getVisibility() {
        return visibility;
    }

    @Override
    public boolean isEncrypted() {
        return isEncrypted;
    }

    @Override
    public @Nullable Instant getExpirationTime() {
        return expirationTime;
    }

    @Override
    public @NotNull Collection<@NotNull String> getTags() {
        return tags;
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
    public @NotNull String getId() {
        return id;
    }

    @Override
    public @NotNull Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public @Nullable PublicUserReply getUser() {
        return user;
    }

    @Override
    public @NotNull URI getRawURL() {
        return rawURL;
    }

    @Override
    public @Nullable Boolean isStarred() {
        return isStarred;
    }

    @Override
    public void decrypt(final byte @NotNull [] password) throws InvalidCipherTextException {
        if (this.getTitle() != null && !this.getTitle().isBlank()) {
            this.title = EncryptionHelper.decrypt(this.getTitle(), password);
        }
        this.content = EncryptionHelper.decrypt(this.getContent(), password);

        EncryptionHelper.clearArray(password);

    }

    @Override
    public @NotNull PasteBuilder<String> toPasteBuilder() {
        final SimpleStringContentImpl pasteContent = (SimpleStringContentImpl) PasteContent.fromString(content);
        pasteContent.setPasteType(type);

        return ((PasteBuilderImpl<String>) Paste.newBuilder(pasteContent)).
            setEncryptionOverwrite(isEncrypted).
            setTitle(title).
            setVisibility(visibility).
            setExpirationTime(expirationTime).
            setTags(tags).
            setFolderId(folderId).
            setPasteIdForkedFrom(pasteIdForkedFrom);
    }

    @Override
    public @NotNull String toString() {
        return "PasteReplyImpl[" +
            "title=" + title + ", " +
            "content=" + content + ", " +
            "visibility=" + visibility + ", " +
            "isEncrypted=" + isEncrypted + ", " +
            "expirationTime=" + expirationTime + ", " +
            "tags=" + tags + ", " +
            "folderId=" + folderId + ", " +
            "pasteIdForkedFrom=" + pasteIdForkedFrom + ", " +
            "id=" + id + ", " +
            "rawURL=" + rawURL + ", " +
            "createdAt=" + createdAt + ", " +
            "user=" + user + ", " +
            "folderId=" + folderId + ", " +
            "isStarred=" + isStarred + ']';
    }
}
