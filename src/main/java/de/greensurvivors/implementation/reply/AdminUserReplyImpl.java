package de.greensurvivors.implementation.reply;

import com.google.gson.annotations.SerializedName;
import de.greensurvivors.AccountStaus;
import de.greensurvivors.admin.AdminUserReply;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Instant;
import java.util.Objects;

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

    private AdminUserReplyImpl(final @NotNull String id,
                               final @NotNull String name,
                               final @NotNull String avatarURL,
                               final @NotNull String displayName,
                               final @NotNull String authenticationProviderName,
                               final @NotNull AccountStaus accountStaus,
                               final @NotNull Instant createdAt,
                               final @NotNull Instant lastUpdatedAt) {
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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (AdminUserReplyImpl) obj;
        return Objects.equals(this.id, that.id) &&
            Objects.equals(this.name, that.name) &&
            Objects.equals(this.avatarURL, that.avatarURL) &&
            Objects.equals(this.displayName, that.displayName) &&
            Objects.equals(this.authenticationProviderName, that.authenticationProviderName) &&
            Objects.equals(this.accountStaus, that.accountStaus) &&
            Objects.equals(this.createdAt, that.createdAt) &&
            Objects.equals(this.lastUpdatedAt, that.lastUpdatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, avatarURL, displayName, authenticationProviderName, accountStaus, createdAt, lastUpdatedAt);
    }

    @Override
    public String toString() {
        return "AdminUserReplyImpl[" +
            "id=" + id + ", " +
            "name=" + name + ", " +
            "avatarURL=" + avatarURL + ", " +
            "displayName=" + displayName + ", " +
            "authenticationProviderName=" + authenticationProviderName + ", " +
            "accountStaus=" + accountStaus + ", " +
            "createdAt=" + createdAt + ", " +
            "lastUpdatedAt=" + lastUpdatedAt + ']';
    }
}
