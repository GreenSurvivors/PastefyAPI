package de.greensurvivors.implementation.reply;

import com.google.gson.annotations.SerializedName;
import de.greensurvivors.AccountStaus;
import de.greensurvivors.reply.PrivateUserReply;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
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
    private final Set<String> availableProviderNames;

    private PrivateUserReplyImpl(@NotNull String id, @NotNull String name, @NotNull String avatarURL, String displayName, @NotNull String authenticationProviderName, @NotNull AccountStaus accountStaus, boolean isLoggedIn, @Nullable String favoriteColor, Set<String> availableProviderNames) {
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

    @Override
    public @Nullable String getFavoriteColor() {
        return favoriteColor;
    }

    @Override
    public @NotNull String AuthenticationProviderName() {
        return authenticationProviderName;
    }

    public @NotNull Set<@NotNull String> getAvailableProviderNamess() { // Why just why is this on the user???
        return Set.of();
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
}
