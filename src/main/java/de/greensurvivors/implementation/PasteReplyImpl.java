package de.greensurvivors.implementation;

import com.google.gson.annotations.SerializedName;
import de.greensurvivors.PasteReply;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.time.Instant;
import java.util.Collection;

public class PasteReplyImpl extends PasteImpl<String> implements PasteReply {
    private final @NotNull String id;
    private final boolean exists;
    @SerializedName("raw_url")
    private final @NotNull URI rawURL;
    @SerializedName("created_at")
    private final @NotNull Instant createdAt;
    @SerializedName("user_id")
    private final @Nullable String userID;

    private PasteReplyImpl(@NotNull String title, @NotNull String content, @NotNull PasteType type, @NotNull PasteVisibility visibility, boolean isEncrypted, @Nullable Instant expirationTime, @NotNull Collection<@NotNull String> tags,
                          @NotNull String id, boolean exists,
                          @NotNull URI rawURL, @NotNull Instant createdAt,
                          @Nullable String userID) {
        super(title, content, type, visibility, isEncrypted, expirationTime, tags);
        this.id = id;
        this.exists = exists;
        this.rawURL = rawURL;
        this.createdAt = createdAt;
        this.userID = userID;
    }

    @Override
    public @NotNull String getId() {
        return id;
    }

    @Override
    public @NotNull Instant getCreatedAtInstance() {
        return createdAt;
    }

    @Override
    public @Nullable String getUserId() {
        return userID;
    }

    @Override
    public @NotNull URI getRawURL() {
        return rawURL;
    }

    public boolean exists() {
        return exists;
    }
}
