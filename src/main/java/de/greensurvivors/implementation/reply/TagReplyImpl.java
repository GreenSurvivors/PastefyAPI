package de.greensurvivors.implementation.reply;

import com.google.gson.annotations.SerializedName;
import de.greensurvivors.reply.TagReply;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class TagReplyImpl implements TagReply {
    @SerializedName("tag")
    private final @NotNull String tag;
    @SerializedName("display_name")
    private final @Nullable String displayName;
    private final @Nullable String description;
    @SerializedName("image_url")
    private final @Nullable String imageURL;
    private final @Nullable String icon;
    private final @Nullable String website;
    @SerializedName("paste_count")
    private final int pasteCount;

    private TagReplyImpl(@NotNull String tag, @Nullable String displayName, @Nullable String description, @Nullable String imageURL, @Nullable String icon, @Nullable String website, int pasteCount) {
        this.tag = tag;
        this.displayName = displayName;
        this.description = description;
        this.imageURL = imageURL;
        this.icon = icon;
        this.website = website;
        this.pasteCount = pasteCount;
    }

    public @NotNull String getTag() {
        return tag;
    }

    @Override
    public @Nullable String getDisplayName() {
        return displayName;
    }

    @Override
    public @Nullable String getDescription() {
        return description;
    }

    @Override
    public @Nullable URL getImageUrl() throws MalformedURLException {
        return imageURL == null ? null : URI.create(imageURL).toURL();
    }

    @Override
    public @Nullable String getWebsite() {
        return website;
    }

    @Override
    public @Nullable String getIcon() {
        return icon;
    }

    @Override
    public int getPasteCount() {
        return pasteCount;
    }
}
