package de.greensurvivors.implementation.reply;

import com.google.gson.annotations.SerializedName;
import de.greensurvivors.reply.PublicUserReply;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Objects;

@SuppressWarnings("ClassCanBeRecord") // don't want to expose constructor
public class PublicUserReplyImpl implements PublicUserReply {
    private final @NotNull String id;
    private final @NotNull String name;
    @SerializedName("avatar")
    private final @NotNull String avatarURL;
    @SerializedName("display_name")
    private final String displayName;

    private PublicUserReplyImpl(final @NotNull String id, final @NotNull String name,
                                final @NotNull String avatarURL,
                                final String displayName) {
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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (PublicUserReplyImpl) obj;
        return Objects.equals(this.id, that.id) &&
            Objects.equals(this.name, that.name) &&
            Objects.equals(this.avatarURL, that.avatarURL) &&
            Objects.equals(this.displayName, that.displayName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, avatarURL, displayName);
    }

    @Override
    public String toString() {
        return "PublicUserReplyImpl[" +
            "id=" + id + ", " +
            "name=" + name + ", " +
            "avatarURL=" + avatarURL + ", " +
            "displayName=" + displayName + ']';
    }
}
