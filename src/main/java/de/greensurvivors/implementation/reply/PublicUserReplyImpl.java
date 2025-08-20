package de.greensurvivors.implementation.reply;

import com.google.gson.annotations.SerializedName;
import de.greensurvivors.reply.PublicUserReply;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

@SuppressWarnings("ClassCanBeRecord") // don't want to expose constructor
public class PublicUserReplyImpl implements PublicUserReply {
    private final @NotNull String id;
    private final @NotNull String name;
    @SerializedName("avatar")
    private final @NotNull String avatarURL;
    @SerializedName("display_name")
    private final String displayName;

    private PublicUserReplyImpl(@NotNull String id, @NotNull String name, @NotNull String avatarURL, String displayName) {
        this.id = id;
        this.name = name;
        this.avatarURL = avatarURL;
        this.displayName = displayName;
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
