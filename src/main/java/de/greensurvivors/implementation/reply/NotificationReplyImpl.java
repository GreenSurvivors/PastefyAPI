package de.greensurvivors.implementation.reply;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Instant;

@SuppressWarnings("ClassCanBeRecord") // don't want to expose constructor
public class NotificationReplyImpl implements de.greensurvivors.reply.NotificationReply {
    private final int id;
    private final @NotNull String message;
    @SerializedName("user_id")
    private final @NotNull String userId;
    private final @NotNull String url;
    @SerializedName("already_read") // maybe??
    private final boolean alreadyRead;
    private final boolean received;
    @SerializedName("created_at") // maybe??
    private final @NotNull Instant createdAt;
    @SerializedName("updated_at") // maybe??
    private final @Nullable Instant updatedAt;

    private NotificationReplyImpl(int id, @NotNull String message, @NotNull String userId, @NotNull String url, boolean alreadyRead, boolean received, @NotNull Instant createdAt, @Nullable Instant updatedAt) {
        this.id = id;
        this.message = message;
        this.userId = userId;
        this.url = url;
        this.alreadyRead = alreadyRead;
        this.received = received;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public @NotNull String getMessage() {
        return message;
    }

    @Override
    public @NotNull String getUserId() {
        return userId;
    }

    @Override
    public @NotNull URL getUrl() throws MalformedURLException {
        return URI.create(url).toURL();
    }

    @Override
    public boolean wasAlreadyRead() {
        return alreadyRead;
    }

    @Override
    public boolean wasReceived() {
        return received;
    }

    @Override
    public @NotNull Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public @Nullable Instant getUpdatedAt() {
        return updatedAt;
    }
}
