package de.greensurvivors.implementation.reply;

import com.google.gson.annotations.SerializedName;
import de.greensurvivors.AccountStaus;
import de.greensurvivors.reply.PrivateUserReply;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Objects;
import java.util.Set;

@SuppressWarnings("ClassCanBeRecord") // no public constructor!
public class PrivateUserReplyImpl implements PrivateUserReply {
    private final @NotNull String id;
    private final @NotNull String name;
    @SerializedName("profile_picture")
    private final @NotNull String avatarURL;
    @SerializedName("display_name")
    private final String displayName;
    @SerializedName("auth_type")
    private final @NotNull String authenticationProviderName;
    @SerializedName("type")
    private final @NotNull AccountStaus accountStaus;

    // the following are pretty useless as it stands
    @SerializedName("logged_in")
    private final boolean isLoggedIn;
    @SerializedName("color")
    private final @Nullable String favoriteColor; // always #f52966 for now
    @SerializedName("auth_types")
    private final @NotNull Set<@NotNull String> availableProviderNames;

    private PrivateUserReplyImpl(@NotNull String id, @NotNull String name,
                                @NotNull String avatarURL, @NotNull String displayName,
                                @NotNull String authenticationProviderName, @NotNull AccountStaus accountStaus,
                                @NotNull Set<@NotNull String> availableProviderNames,
                                boolean isLoggedIn, @Nullable String favoriteColor) {
        this.id = id;
        this.name = name;
        this.avatarURL = avatarURL;
        this.displayName = displayName;
        this.authenticationProviderName = authenticationProviderName;
        this.accountStaus = accountStaus;
        this.isLoggedIn = isLoggedIn;
        this.favoriteColor = favoriteColor;
        this.availableProviderNames = availableProviderNames;
    }

    @Override
    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public @Nullable String getFavoriteColor() {
        return favoriteColor;
    }

    @Override
    public @NotNull String getAuthenticationProviderName() {
        return authenticationProviderName;
    }

    public @NotNull Set<@NotNull String> getAvailableProviderNames() { // Why just why is this on the user???
        return availableProviderNames;
    }

    @Override
    public @NotNull AccountStaus getStatus() {
        return accountStaus;
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
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (PrivateUserReplyImpl) obj;
        return Objects.equals(this.id, that.id) &&
            Objects.equals(this.name, that.name) &&
            Objects.equals(this.avatarURL, that.avatarURL) &&
            Objects.equals(this.displayName, that.displayName) &&
            Objects.equals(this.authenticationProviderName, that.authenticationProviderName) &&
            Objects.equals(this.accountStaus, that.accountStaus) &&
            Objects.equals(this.availableProviderNames, that.availableProviderNames) &&
            this.isLoggedIn == that.isLoggedIn &&
            Objects.equals(this.favoriteColor, that.favoriteColor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, avatarURL, displayName, authenticationProviderName, accountStaus, availableProviderNames, isLoggedIn, favoriteColor);
    }

    @Override
    public String toString() {
        return "PrivateUserReplyImpl[" +
            "id=" + id + ", " +
            "name=" + name + ", " +
            "avatarURL=" + avatarURL + ", " +
            "displayName=" + displayName + ", " +
            "authenticationProviderName=" + authenticationProviderName + ", " +
            "accountStaus=" + accountStaus + ", " +
            "availableProviderNames=" + availableProviderNames + ", " +
            "isLoggedIn=" + isLoggedIn + ", " +
            "favoriteColor=" + favoriteColor + ']';
    }
}
