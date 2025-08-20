package de.greensurvivors.reply;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.MalformedURLException;
import java.net.URL;

public interface TagReply {
    @NotNull String getTag();
    @Nullable String getDisplayName();
    /// with no (non-admin-direct-database-input) way to input this, all descriptions are AI generated. Some even have the wrong format. But don't worry, nobody but us knows.
    @Nullable String getDescription();
    /// with no (non-admin-direct-database-input) way to input this, only two tags have an image associated.
    @Nullable URL getImageUrl() throws MalformedURLException;
    /// allways empty or null.
    @Nullable String getWebsite();
    /// with no (non-admin-direct-database-input) way to input this, only two tags have an icon associated.
    @Nullable String getIcon();
    int getPasteCount();
}
