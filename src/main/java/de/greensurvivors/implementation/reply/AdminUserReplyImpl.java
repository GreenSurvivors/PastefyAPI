package de.greensurvivors.implementation.reply;

import com.google.gson.annotations.SerializedName;
import de.greensurvivors.AccountStaus;
import de.greensurvivors.admin.AdminUserReply;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Instant;

@SuppressWarnings("ClassCanBeRecord") // don't want to expose constructor
public class AdminUserReplyImpl implements AdminUserReply {
    private final @NotNull String id;
    @SerializedName("uniqueName") // maybe??
    private final @NotNull String name;
    @SerializedName("avatar") // maybe??
    private final @NotNull String avatarURL;
    @SerializedName("name") // maybe??
    private final @NotNull String displayName;
    @SerializedName("auth_id") // maybe??
    private final @NotNull String authenticationProviderName;
    @SerializedName("type")
    private final @NotNull AccountStaus accountStaus;
    @SerializedName("created_at")
    private final @NotNull Instant createdAt;
    @SerializedName("updated_at")
    private final @NotNull Instant lastUpdatedAt;

    private AdminUserReplyImpl(@NotNull String id, @NotNull String name, @NotNull String avatarURL, @NotNull String displayName, @NotNull String authenticationProviderName, @NotNull AccountStaus accountStaus, @NotNull Instant createdAt, @NotNull Instant lastUpdatedAt) {
        this.id = id;
        this.name = name;
        this.avatarURL = avatarURL;
        this.displayName = displayName;
        this.authenticationProviderName = authenticationProviderName;
        this.accountStaus = accountStaus;
        this.createdAt = createdAt;
        this.lastUpdatedAt = lastUpdatedAt;
    }

    @Override
    public @NotNull String AuthenticationProviderName() {
        return authenticationProviderName;
    }

    @Override
    public @NotNull AccountStaus getStatus() {
        return accountStaus;
    }

    @Override
    public @NotNull Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public @NotNull Instant getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    @Override
    public @NotNull String getId() {
        return id;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull URL getAvatarURL() throws MalformedURLException {
        return URI.create(avatarURL).toURL();
    }

    @Override
    public @NotNull String getDisplayName() {
        return displayName;
    }
}
